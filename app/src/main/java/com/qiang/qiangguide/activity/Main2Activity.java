package com.qiang.qiangguide.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.qiang.qiangguide.AppManager;
import com.qiang.qiangguide.R;
import com.qiang.qiangguide.service.MediaIDHelper;
import com.qiang.qiangguide.service.PlayService;
import com.qiang.qiangguide.util.LogUtil;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG="Main2Activity";

    private static final long PROGRESS_UPDATE_INTERNAL = 1000;
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 100;
    private Toolbar mToolbar;
    private ViewPager viewpager;
    private ImageView backgroundImage;
    private RecyclerView recyclerView;
    private SeekBar mSeekbar;
    private ImageView mPlayPause;
    private Drawable mPauseDrawable;
    private Drawable mPlayDrawable;
    private TextView mStart;
    private TextView mEnd;
    private ProgressBar mLoading;
    private View mControllers;


    private PlaybackStateCompat mLastPlaybackState;
    private String mMediaId;
    private String museumId;
    private MediaBrowserCompat mMediaBrowser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findView();
        addListener();

        // Only update from the intent if we are not recreating from a config change:
        if (savedInstanceState == null) {
            updateFromParams(getIntent());
        }
        mMediaBrowser = new MediaBrowserCompat(this, new ComponentName(this, PlayService.class), mConnectionCallback, null);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mMediaBrowser.connect();
        MediaControllerCompat controller = getSupportMediaController();
        if (controller != null) {
            controller.registerCallback(mCallback);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MediaControllerCompat controller = getSupportMediaController();
        if (controller != null) {
            controller.unregisterCallback(mCallback);
        }

    }


    private final MediaBrowserCompat.ConnectionCallback mConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    LogUtil.d(TAG, "onConnected");
                    connectToSession(mMediaBrowser.getSessionToken());
                }
            };



    private void connectToSession(MediaSessionCompat.Token token) {
        MediaControllerCompat mediaController = null;
        try {
            mediaController = new MediaControllerCompat(Main2Activity.this, token);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if ((mediaController != null ? mediaController.getMetadata() : null) == null) {
            finish();
            return;
        }
        setSupportMediaController(mediaController);
        mediaController.registerCallback(mCallback);
        PlaybackStateCompat state = mediaController.getPlaybackState();
        updatePlaybackState(state);
        MediaMetadataCompat metadata = mediaController.getMetadata();
        if (metadata != null) {
            updateMediaDescription(metadata.getDescription());
            updateDuration(metadata);
        }
        updateProgress();
        if (state != null && (state.getState() == PlaybackStateCompat.STATE_PLAYING ||
                state.getState() == PlaybackStateCompat.STATE_BUFFERING)) {
            scheduleSeekbarUpdate();
        }

        onConnected();
    }


    private final MediaControllerCompat.Callback mCallback = new MediaControllerCompat.Callback() {

        @Override
        public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
            LogUtil.d(TAG, "Received playback state change to state ", state.getState());
            updatePlaybackState(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            if (metadata == null) {
                return;
            }
            LogUtil.d(TAG, "Received metadata state change to mediaId=",
                    metadata.getDescription().getMediaId(),
                    " song=", metadata.getDescription().getTitle());
            Main2Activity.this.onMetadataChanged(metadata);
            updateDuration(metadata);
        }
    };


    private void updateDuration(MediaMetadataCompat metadata) {
        if (metadata == null) {
            return;
        }
        LogUtil.d(TAG, "updateDuration called ");
        //int duration = (int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        int duration= AppManager.getInstance(null).getCurrentDuration();
        mSeekbar.setMax(duration);
        mEnd.setText(DateUtils.formatElapsedTime(duration/1000));
    }


    private void onMetadataChanged(MediaMetadataCompat metadata) {

    }

    private void onConnected() {
        if (mMediaId == null) {
            mMediaId = MediaIDHelper.createBrowseCategoryMediaID(MediaIDHelper.MEDIA_ID_MUSEUM_ID,museumId);
        }

        //getMediaBrowser().unsubscribe(mMediaId);
        mMediaBrowser.unsubscribe(mMediaId);

        //getMediaBrowser().subscribe(mMediaId, mSubscriptionCallback);
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


    private void updateFromParams(Intent intent) {
        if (intent != null) {
            MediaDescriptionCompat description = intent.getParcelableExtra(
                    MainGuideActivity.EXTRA_CURRENT_MEDIA_DESCRIPTION);
            if (description != null) {
                updateMediaDescription(description);
            }
        }
    }

    private void updateMediaDescription(MediaDescriptionCompat description) {
        if (description == null) {
            return;
        }
        LogUtil.d(TAG, "updateMediaMetadataCompat called ");
        museumId=description.getSubtitle()==null?null:String.valueOf(description.getSubtitle());
        setToolbarTitle(String.valueOf(description.getTitle()));
        //mLine2.setText(description.getSubtitle());
        //fetchImageAsync(description);
    }




    private void addListener() {
        mPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaybackStateCompat state = getSupportMediaController().getPlaybackState();
                if (state != null) {
                    MediaControllerCompat.TransportControls controls =
                            getSupportMediaController().getTransportControls();
                    switch (state.getState()) {
                        case PlaybackState.STATE_PLAYING: // fall through
                        case PlaybackState.STATE_BUFFERING:
                            controls.pause();
                            stopSeekbarUpdate();
                            break;
                        case PlaybackState.STATE_PAUSED:
                        case PlaybackState.STATE_STOPPED:
                            controls.play();
                            scheduleSeekbarUpdate();
                            break;
                        default:
                            LogUtil.d(TAG, "onClick with state ", state.getState());
                    }
                }
            }
        });



        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mStart.setText(DateUtils.formatElapsedTime(progress / 1000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                stopSeekbarUpdate();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                getSupportMediaController().getTransportControls().seekTo(seekBar.getProgress());
                scheduleSeekbarUpdate();
            }
        });






    }

    private void findView() {
        setToolbar();
        viewpager=(ViewPager)findViewById(R.id.viewpager);
        backgroundImage=(ImageView)findViewById(R.id.background_image);
        recyclerView=(RecyclerView)findViewById(R.id.recycleMultiAngle);
        mSeekbar=(SeekBar)findViewById(R.id.seekBar);
        mPlayPause = (ImageView) findViewById(R.id.play_pause);
        mPauseDrawable = getResources().getDrawable(R.drawable.ic_pause_white_48dp);
        mPlayDrawable = getResources().getDrawable(R.drawable.ic_play_arrow_white_48dp);
        mStart = (TextView) findViewById(R.id.startText);
        mEnd = (TextView) findViewById(R.id.endText);
        mLoading = (ProgressBar) findViewById(R.id.progressBar1);
        mControllers = findViewById(R.id.controllers);
    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null) {
            throw new IllegalStateException("Layout is required to include a Toolbar with id 'toolbar'");
        }
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

    public void setToolbarTitle(String title) {
        if(mToolbar==null){return;}
        TextView toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        if (toolbarTitle == null) {return;}
        toolbarTitle.setText(title);
    }


    private ScheduledFuture<?> mScheduleFuture;
    private final ScheduledExecutorService mExecutorService =
            Executors.newSingleThreadScheduledExecutor();
    private final Handler mHandler = new Handler();

    private void scheduleSeekbarUpdate() {
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


    private void stopSeekbarUpdate() {
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


    private void updateProgress() {
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
    }


    private void updatePlaybackState(PlaybackStateCompat state) {
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

        /*mSkipNext.setVisibility((state.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_NEXT) == 0
                ? INVISIBLE : VISIBLE );
        mSkipPrev.setVisibility((state.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) == 0
                ? INVISIBLE : VISIBLE );*/
    }




}
