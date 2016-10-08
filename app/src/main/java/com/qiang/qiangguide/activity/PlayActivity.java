package com.qiang.qiangguide.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.session.PlaybackState;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.qiang.qiangguide.AppManager;
import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.IPlayView;
import com.qiang.qiangguide.adapter.adapterImpl.LyricViewPagerAdapter;
import com.qiang.qiangguide.adapter.adapterImpl.MultiAngleImgAdapter;
import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.bean.MultiAngleImg;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.fragment.BaseFragment;
import com.qiang.qiangguide.fragment.EmptyFragment;
import com.qiang.qiangguide.fragment.LyricFragment;
import com.qiang.qiangguide.presenter.PlayShowPresenter;
import com.qiang.qiangguide.service.MediaIDHelper;
import com.qiang.qiangguide.service.PlayService;
import com.qiang.qiangguide.util.AndroidUtil;
import com.qiang.qiangguide.util.BitmapUtil;
import com.qiang.qiangguide.util.FileUtil;
import com.qiang.qiangguide.util.LogUtil;
import com.qiang.qiangguide.volley.QVolley;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 *  mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
 | View.SYSTEM_UI_FLAG_FULLSCREEN
 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
 | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
 | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
 | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
 */
public class PlayActivity extends AppCompatActivity implements IPlayView{

    private static final String TAG="PlayActivity";

    private MediaBrowserCompat mMediaBrowser;

    private ScheduledFuture<?> mScheduleFuture;
    private final ScheduledExecutorService mExecutorService =
            Executors.newSingleThreadScheduledExecutor();
    private final Handler mHandler = new Handler();

    private static final long PROGRESS_UPDATE_INTERNAL = 1000;
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 100;
    private Toolbar mToolbar;
    private ViewPager viewpager;
    private ImageView backgroundImage;
    private RecyclerView recycleMultiAngle;
    private SeekBar mSeekbar;
    private ImageView mPlayPause;
    private Drawable mPauseDrawable;
    private Drawable mPlayDrawable;
    private TextView mStart;
    private TextView mEnd;
    private ProgressBar mLoading;
    private View mControllers;
    private PlaybackStateCompat mLastPlaybackState;

    private PlayShowPresenter presenter;
    private String mMediaId;
    private String museumId;
    private TextView toolbarTitle;
    private EmptyFragment emptyFragment;
    private LyricFragment lyricFragment;
    private String lyricUrl;
    private String exhibitContent;
    private ImageView mSwtchLyric;
    private View statusBar;
    private MultiAngleImgAdapter mulTiAngleImgAdapter;
    private List<MultiAngleImg> multiAngleImgs;
    private Exhibit currentExhibit;
    private ArrayList<Integer> imgsTimeList;
    private String currentIconUrl;
    private boolean isBlur=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        setStatusAlpha();
        presenter=new PlayShowPresenter(this);
        findView();
        addListener();
        initFragment();
        presenter.onViewCreate(savedInstanceState);
    }

    private void initFragment() {

        if(emptyFragment==null){
            emptyFragment= EmptyFragment.newInstance();
        }
        if(lyricFragment==null){
            lyricFragment=LyricFragment.newInstance();
        }
        ArrayList<BaseFragment> fragments=new ArrayList<>();
        fragments.add(lyricFragment);
        fragments.add(emptyFragment);
        LyricViewPagerAdapter viewPagerAdapter=new LyricViewPagerAdapter(getSupportFragmentManager(),fragments);
        viewpager.setAdapter(viewPagerAdapter);
    }

    private void setStatusAlpha() {
        statusBar=findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View statusBarView = mContentView != null ? mContentView.getChildAt(0) : null;
            int statuesBarHeight= AndroidUtil.getStatusBarHeight(this);
            //移除假的 View
            if (statusBarView != null && statusBarView.getLayoutParams() != null && statusBarView.getLayoutParams().height == statuesBarHeight) {
                mContentView.removeView(statusBarView);
            }
            //不预留空间
            if ((mContentView != null ? mContentView.getChildAt(0) : null) != null) {
                ViewCompat.setFitsSystemWindows(mContentView.getChildAt(0), false);
            }
            statusBar.setVisibility(VISIBLE);
        }else{
            if (statusBar != null) {
                statusBar.setVisibility(View.GONE);
            }
        }
    }

    private void addListener() {
        mPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onPauseButtonClick();
            }
        });

        mSwtchLyric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSwitchLyric();
            }
        });

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                presenter.onProgressChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                stopSeekbarUpdate();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                presenter.onStopTrackingTouch(seekBar);
            }
        });

        mulTiAngleImgAdapter.setOnItemClickListener(new MultiAngleImgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MultiAngleImg multiAngleImg=mulTiAngleImgAdapter.get(position);
                presenter.onMultiImgClick(multiAngleImg);

            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    isBlur=true;
                }else{
                    isBlur=false;
                }
                showIcon();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    private void findView() {
        setToolbar();
        viewpager=(ViewPager)findViewById(R.id.viewpager);
        backgroundImage=(ImageView)findViewById(R.id.background_image);
        mSeekbar=(SeekBar)findViewById(R.id.seekBar);
        mPlayPause = (ImageView) findViewById(R.id.play_pause);
        mSwtchLyric = (ImageView) findViewById(R.id.switch_lyric);
        mPauseDrawable = getResources().getDrawable(R.drawable.ic_pause_white_48dp);
        mPlayDrawable = getResources().getDrawable(R.drawable.ic_play_arrow_white_48dp);
        mStart = (TextView) findViewById(R.id.startText);
        mEnd = (TextView) findViewById(R.id.endText);
        mLoading = (ProgressBar) findViewById(R.id.progressBar1);
        mControllers = findViewById(R.id.controllers);
        recycleMultiAngle =(RecyclerView)findViewById(R.id.recycleMultiAngle);

        multiAngleImgs=new ArrayList<>();
        mulTiAngleImgAdapter=new MultiAngleImgAdapter(this);
        /*设置为横向*/
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleMultiAngle.setLayoutManager(linearLayoutManager);
        recycleMultiAngle.setAdapter(mulTiAngleImgAdapter);
        recycleMultiAngle.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);

    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null) {
            throw new IllegalStateException("Layout is required to include a Toolbar with id 'toolbar'");
        }
       /* int height=mToolbar.getHeight();
        int width=mToolbar.getWidth();
        RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(width,height);
        lp.topMargin=getStatusBarHeight();
        mToolbar.setLayoutParams(lp);*/
        toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setToolbarTitle("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.i("","onStart");
        presenter.onViewStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.i("","onStop");
        presenter.onViewStop();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mMediaBrowser!=null&&mMediaBrowser.isConnected()){
            mMediaBrowser.disconnect();
        }
        LogUtil.i("","onDestroy");
    }



    @Override
    public void updateFromParams(Intent intent) {
        if (intent != null) {
            MediaMetadataCompat metadata=intent.getParcelableExtra(
                    MainGuideActivity.EXTRA_CURRENT_MEDIA_DESCRIPTION);
            if(metadata!=null){
                updateMediaMetadataCompat(metadata);
            }
        }
    }


    @Override
    public void updateMediaMetadataCompat(MediaMetadataCompat metadata) {
        LogUtil.d(TAG, "updateMediaMetadataCompat called ");
        presenter.updateMediaMetadataCompat(metadata);
    }

    @Override
    public void setMuseumId(String museumId) {
        this.museumId=museumId;
        if(lyricFragment==null){
            lyricFragment=LyricFragment.newInstance();
        }
        lyricFragment.setMuseumId(museumId);
    }


    @Override
    public void showIcon() {

        if(TextUtils.isEmpty(currentIconUrl)){return;}
        String name=FileUtil.changeUrl2Name(currentIconUrl);
        if(FileUtil.checkFileExists(currentIconUrl,museumId)){
            String path=Constants.LOCAL_PATH+museumId+"/"+name;
            Bitmap bitmap=BitmapFactory.decodeFile(path);
            if(bitmap==null){return;}
            if (isBlur) {
                bitmap=BitmapUtil.blur(bitmap,this);
            }
            backgroundImage.setImageBitmap(bitmap);
        }else{
            if(isBlur){
                QVolley.getInstance(null).loadBlurImage(Constants.BASE_URL+currentIconUrl,backgroundImage,0,0);
            }else{
                QVolley.getInstance(null).loadImage(Constants.BASE_URL+currentIconUrl,backgroundImage,0,0);
            }
        }
    }



    @Override
    public void setLyricUrl(String lyricUrl) {
        this.lyricUrl=lyricUrl;
        if(lyricFragment==null){
            lyricFragment=LyricFragment.newInstance();
        }
        lyricFragment.setLyricUrl(lyricUrl);
    }

    @Override
    public void refreshLyricContent() {
        if(lyricFragment==null){
            lyricFragment=LyricFragment.newInstance();
        }
        lyricFragment.refreshLyricContent();
    }

    @Override
    public void setExhibitContent(String content) {
        this.exhibitContent=content;
        if(lyricFragment==null){
            lyricFragment=LyricFragment.newInstance();
        }
        lyricFragment.setExhibitContent(exhibitContent);
    }

    @Override
    public void onSwitchLyric() {
        if(lyricFragment==null){
            lyricFragment=LyricFragment.newInstance();
        }
        lyricFragment.onSwitchLyric();
    }

    @Override
    public void setExhibit(Exhibit exhibit) {
        this.currentExhibit=exhibit;
    }

    @Override
    public Exhibit getExhibit() {
        return currentExhibit;
    }

    @Override
    public void setImgsAndTimes(ArrayList<MultiAngleImg> multiAngleImgs, ArrayList<Integer> imgsTimeList) {
        this.multiAngleImgs=multiAngleImgs;
        this.imgsTimeList=imgsTimeList;
    }

    @Override
    public void refreshMultiImgs() {
        if(mulTiAngleImgAdapter!=null){
            mulTiAngleImgAdapter.updateData(multiAngleImgs);
        }
    }

    @Override
    public void setIconUrl(String iconUrl) {
        this.currentIconUrl=iconUrl;
    }

    @Override
    public void skipTo(int time) {
        getSupportMediaController().getTransportControls().seekTo(time);
        scheduleSeekbarUpdate();
    }

    @Override
    public boolean getIsBlur() {
        return isBlur;
    }

    @Override
    public void setIsBlur(boolean isBlur) {
        this.isBlur=isBlur;
    }


    @Override
    public void initMediaBrowser() {
        mMediaBrowser = new MediaBrowserCompat(this, new ComponentName(this, PlayService.class), mConnectionCallback, null);
    }

    private final MediaBrowserCompat.ConnectionCallback mConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    LogUtil.d(TAG, "onConnected");
                    presenter.onMediaBrowserConnected(mMediaBrowser.getSessionToken());
                }
            };


    @Override
    public void registerMediaControllerCallback() {
        MediaControllerCompat controller = getSupportMediaController();
        if (controller != null) {
            controller.registerCallback(mCallback);
        }
    }

    @Override
    public void unregisterMediaControllerCallback() {
        MediaControllerCompat controller = getSupportMediaController();
        if (controller != null) {
            controller.unregisterCallback(mCallback);
        }
    }

    private final MediaControllerCompat.Callback mCallback = new MediaControllerCompat.Callback() {

        @Override
        public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
            LogUtil.d(TAG, "Received playback state change to state ", state.getState());
            presenter.onPlaybackStateChanged(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            presenter.onMetadataChanged(metadata);
        }
    };

    @Override
    public void updateDuration(MediaMetadataCompat metadata) {
        if (metadata == null) {
            return;
        }
        LogUtil.d(TAG, "updateDuration called ");
        //int duration = (int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        int duration= AppManager.getInstance(null).getCurrentDuration();
        mSeekbar.setMax(duration);
        mEnd.setText(DateUtils.formatElapsedTime(duration/1000));
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        LogUtil.i("","onMetadataChanged");
        if(metadata!=null){
            updateMediaMetadataCompat(metadata);
        }

    }

    @Override
    public void subscribeExhibitList() {
        if (mMediaId == null) {
            mMediaId = MediaIDHelper.createBrowseCategoryMediaID(MediaIDHelper.MEDIA_ID_MUSEUM_ID,museumId);
        }
        mMediaBrowser.unsubscribe(mMediaId);
        mMediaBrowser.subscribe(mMediaId, mSubscriptionCallback);
    }

    private MediaBrowserCompat.SubscriptionCallback mSubscriptionCallback=new MediaBrowserCompat.SubscriptionCallback() {
        @Override
        public void onChildrenLoaded(@NonNull String parentId, List<MediaBrowserCompat.MediaItem> children) {
            super.onChildrenLoaded(parentId, children);
        }

        @Override
        public void onError(@NonNull String parentId) {
            super.onError(parentId);
        }

    };


    @Override
    public void setToolbarTitle(String title) {
        if (toolbarTitle == null) {return;}
        toolbarTitle.setText(title);
    }

    @Override
    public void scheduleSeekbarUpdate() {
        stopSeekbarUpdate();
        if (!mExecutorService.isShutdown()) {
            mScheduleFuture = mExecutorService.scheduleAtFixedRate(
                    new Runnable() {
                        @Override
                        public void run() {
                            mHandler.post(mUpdateProgressTask);
                        }
                    }, PROGRESS_UPDATE_INITIAL_INTERVAL,
                    PROGRESS_UPDATE_INTERNAL, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void stopSeekbarUpdate() {
        if (mScheduleFuture != null) {
            mScheduleFuture.cancel(false);
        }
    }

    private final Runnable mUpdateProgressTask = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };



    @Override
    public void updateProgress() {
        if (mLastPlaybackState == null) {
            return;
        }
        long currentPosition = mLastPlaybackState.getPosition();
        if (mLastPlaybackState.getState() != PlaybackStateCompat.STATE_PAUSED) {
            // Calculate the elapsed time between the last position update and now and unless
            // paused, we can assume (delta * speed) + current position is approximately the
            // latest position. This ensure that we do not repeatedly call the getPlaybackState()
            // on MediaController.
            long timeDelta = SystemClock.elapsedRealtime() -
                    mLastPlaybackState.getLastPositionUpdateTime();
            currentPosition += (int) timeDelta * mLastPlaybackState.getPlaybackSpeed();
        }
        mSeekbar.setProgress((int) currentPosition);
        lyricFragment.notifyTime(currentPosition);
        refreshIcon((int)currentPosition);
    }

    /**
     * 刷新icon图标
     */
    public void refreshIcon(int currentProgress){
        if (imgsTimeList==null||imgsTimeList.size() == 0) {return;}
        for (int i = 0; i < imgsTimeList.size()-1; i++) {
            int imgTime = imgsTimeList.get(i);
            int overTime= imgsTimeList.get(i+1);
            if (currentProgress > imgTime && currentProgress <= overTime) {
                if(multiAngleImgs==null||multiAngleImgs.size()==0){return;}
                for(MultiAngleImg angleImg:multiAngleImgs){
                    if(angleImg.getTime()==imgTime){
                        currentIconUrl=angleImg.getUrl();
                    }
                }
            }else if(currentProgress>overTime){
                try{
                    currentIconUrl=multiAngleImgs.get(imgsTimeList.size()-1).getUrl();
                }catch (Exception e){
                    LogUtil.e("",e);
                }
            }else if(currentProgress<imgTime){
                if(multiAngleImgs==null||multiAngleImgs.size()==0){return;}
                currentIconUrl=multiAngleImgs.get(0).getUrl();
            }
            showIcon();
        }
    }

    /**
     * 初始化icon图标
     */
    private void initIcon() {

        if(TextUtils.isEmpty(currentIconUrl)){
            LogUtil.i("","TextUtils.isEmpty(currentIconUrl)");
            return;}
        String name=FileUtil.changeUrl2Name(currentIconUrl);
        if(FileUtil.checkFileExists(currentIconUrl,museumId)){
            String path=Constants.LOCAL_PATH+museumId+"/"+name;
            Bitmap bitmap=BitmapFactory.decodeFile(path);
            if(bitmap==null){
                LogUtil.i("","文件存在，bitmap=null,path = "+path);
                return;}
            if (viewpager.getCurrentItem()==0) {
                bitmap=BitmapUtil.blur(bitmap,this);
            }
            backgroundImage.setImageBitmap(bitmap);
        }else{
            // TODO: 2016/9/18
            LogUtil.i("","文件不存在"+Constants.BASE_URL+currentIconUrl);
            QVolley.getInstance(null).loadImage(Constants.BASE_URL+currentIconUrl,backgroundImage,0,0);
        }

    }

    @Override
    public void updatePlaybackState(PlaybackStateCompat state) {
        if (state == null) {
            return;
        }
        mLastPlaybackState = state;
        switch (state.getState()) {
            case PlaybackState.STATE_PLAYING:
                mLoading.setVisibility(INVISIBLE);
                mPlayPause.setVisibility(VISIBLE);
                mPlayPause.setImageDrawable(mPauseDrawable);
                mControllers.setVisibility(VISIBLE);
                scheduleSeekbarUpdate();
                break;
            case PlaybackState.STATE_PAUSED:
                mControllers.setVisibility(VISIBLE);
                mLoading.setVisibility(INVISIBLE);
                mPlayPause.setVisibility(VISIBLE);
                mPlayPause.setImageDrawable(mPlayDrawable);
                stopSeekbarUpdate();
                break;
            case PlaybackState.STATE_NONE:
            case PlaybackState.STATE_STOPPED:
                mLoading.setVisibility(INVISIBLE);
                mPlayPause.setVisibility(VISIBLE);
                mPlayPause.setImageDrawable(mPlayDrawable);
                stopSeekbarUpdate();
                break;
            case PlaybackState.STATE_BUFFERING:
                mPlayPause.setVisibility(INVISIBLE);
                mLoading.setVisibility(VISIBLE);
                //mLine3.setText(R.string.loading);
                stopSeekbarUpdate();
                break;
            default:
                LogUtil.d(TAG, "Unhandled state ", state.getState());
        }
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void connectSession() {
        if(mMediaBrowser!=null&&!mMediaBrowser.isConnected()){
            mMediaBrowser.connect();
        }
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public void setPlayTime(String time) {
        mStart.setText(time);
    }



}
