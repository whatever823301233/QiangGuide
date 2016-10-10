package com.qiang.qiangguide.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.qiang.qiangguide.manager.AppManager;
import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.MediaBrowserProvider;
import com.qiang.qiangguide.fragment.PlaybackControlsFragment;
import com.qiang.qiangguide.service.PlayService;
import com.qiang.qiangguide.util.LogUtil;
import com.qiang.qiangguide.volley.QVolley;

/**
 * Created by Qiang on 2016/7/12.
 *
 */
public abstract class ActivityBase extends AppCompatActivity implements MediaBrowserProvider {

    /**
     * 类唯一标记
     */
    public String TAG = getClass().getSimpleName();
    public RelativeLayout mErrorView;
    public Button errorFreshButton;
    private MediaBrowserCompat mMediaBrowser;
    protected PlaybackControlsFragment mControlsFragment;


    public ActivityBase getActivity(){
        return this;
    }
    /**
     * 获得当前activity的tag
     *
     * @return activity的tag
     */
    public String getTag() {
        return TAG;
    }

    public Context getContext(){
        return this.getActivity();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppManager.getInstance( getApplicationContext() ).addActivity( this );
        mMediaBrowser = new MediaBrowserCompat(this, new ComponentName(this, PlayService.class), mConnectionCallback, null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mControlsFragment = (PlaybackControlsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_playback_controls);

        /*if (mControlsFragment == null) {
            throw new IllegalStateException("Mising fragment with id 'controls'. Cannot continue.");
        }*/// TODO: 2016/9/22
         hidePlaybackControls();
        if(mMediaBrowser != null && ! mMediaBrowser.isConnected() ){
            mMediaBrowser.connect();
        }
    }


    /*@Override
    protected void onResume() {
        super.onResume();
        LogUtil.i("","onResume");
        if (shouldShowControls()) {
            showPlaybackControls();
        } else {
            //LogUtil.d(TAG, "mediaControllerCallback.onMetadataChanged: " + "hiding controls because metadata is null");
            hidePlaybackControls();
        }

    }*/

    protected void hidePlaybackControls() {
        if(mControlsFragment == null){return;}
        LogUtil.i("","hidePlaybackControls 隐藏fragment");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(mControlsFragment)
                            .commitAllowingStateLoss();
                    LogUtil.i("","隐藏了Fragment");
                }catch (Exception e){
                    LogUtil.e("",e);
                }
            }
        });
    }

    protected void showPlaybackControls() {
        if(mControlsFragment == null){return;}
        LogUtil.i("","showPlaybackControls 显示fragment");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(mControlsFragment)
                            .commitAllowingStateLoss();
                    LogUtil.i("","显示了Fragment");
                }catch (Exception e){
                    LogUtil.e("","显示fragment异常"+e);
                }
            }
        });

    }

    protected boolean shouldShowControls() {
        MediaControllerCompat mediaController = getSupportMediaController();
        if (mediaController == null ||
                mediaController.getMetadata() == null ||
                mediaController.getPlaybackState() == null) {
            return false;
        }
        switch (mediaController.getPlaybackState().getState()) {
            case PlaybackStateCompat.STATE_ERROR:
            case PlaybackStateCompat.STATE_NONE:
            case PlaybackStateCompat.STATE_STOPPED:
                return false;
            default:
                return true;
        }
    }



    private final MediaBrowserCompat.ConnectionCallback mConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    LogUtil.d("", "onConnected");
                    try {
                        connectToSession(mMediaBrowser.getSessionToken());
                    } catch (RemoteException e) {
                        LogUtil.e("","异常"+e.toString());
                    }
                }
            };

    private void connectToSession(MediaSessionCompat.Token token) throws RemoteException {
        MediaControllerCompat mediaController = new MediaControllerCompat(this, token);
        setSupportMediaController(mediaController);
        mediaController.registerCallback(mMediaControllerCallback);

        if (shouldShowControls()) {
            LogUtil.i("","connectToSession 显示fragment");
            showPlaybackControls();
        } else {
            //LogUtil.d("", "connectionCallback.onConnected: " + "hiding controls because metadata is null");
            LogUtil.i("","connectToSession 隐藏fragment");
            hidePlaybackControls();
        }
        if (mControlsFragment != null) {
            mControlsFragment.onConnected();
        }
        onMediaControllerConnected();
    }


    protected void onMediaControllerConnected() {
        // empty implementation, can be overridden by clients.

    }

    // Callback that ensures that we are showing the controls
    private final MediaControllerCompat.Callback mMediaControllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onPlaybackStateChanged(PlaybackStateCompat state) {
                    super.onPlaybackStateChanged(state);
                    LogUtil.i("",TAG+" onPlaybackStateChanged");
                    if (shouldShowControls()) {
                        showPlaybackControls();
                    } else {
                        //LogUtil.d(TAG, "mediaControllerCallback.onPlaybackStateChanged: " +"hiding controls because state is ", state.getState());
                        hidePlaybackControls();
                    }

                }

                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    super.onMetadataChanged(metadata);
                    LogUtil.i("",TAG+"onMetadataChanged");
                    if (shouldShowControls()) {
                        showPlaybackControls();
                    } else {
                        //LogUtil.d(TAG, "mediaControllerCallback.onMetadataChanged: " + "hiding controls because metadata is null");
                        hidePlaybackControls();
                    }
                }
            };



    @Override
    protected void onStop() {
        super.onStop();
        QVolley.getInstance(this).cancelFromRequestQueue(getTag());

        if (getSupportMediaController() != null) {
            getSupportMediaController().unregisterCallback(mMediaControllerCallback);
        }
        mMediaBrowser.disconnect();

    }

    @Override
    public MediaBrowserCompat getMediaBrowser() {
        return mMediaBrowser;
    }

    public void showToast(String content){
        Toast.makeText(getActivity(),content,Toast.LENGTH_SHORT).show();
    }

    public void initErrorView(){
        mErrorView = (RelativeLayout)findViewById(R.id.error_view);
        if(mErrorView==null){return;}
        errorFreshButton = ( Button )mErrorView.findViewById(R.id.click_refresh);
        errorFreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorRefresh();
            }
        });

    }

    abstract void errorRefresh();


    public void showFailedError() {
        if(mErrorView == null){
            throw new IllegalStateException("mErrorView is null ! ");
        }
        mErrorView.setVisibility(View.VISIBLE);
    }

    public void hideErrorView() {
        if(mErrorView == null){
            throw new IllegalStateException("mErrorView is null ! ");
        }
        mErrorView.setVisibility(View.GONE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance( getApplicationContext() ).removeActivity( this );
    }

    /**
     * 响应后退按键
     */
    public void keyBack() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean onKey = true;
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                keyBack();
                break;
            default:
                onKey = super.onKeyDown(keyCode, event);
                break;
        }
        return onKey;
    }



}
