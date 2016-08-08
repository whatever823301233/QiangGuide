package com.qiang.qiangguide.activity;

import android.content.ComponentName;
import android.content.Context;
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

import com.qiang.qiangguide.AppManager;
import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.MediaBrowserProvider;
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
        AppManager.getInstance( getApplicationContext() ).addActivity( this );
        mMediaBrowser = new MediaBrowserCompat(this, new ComponentName(this, PlayService.class), mConnectionCallback, null);

        mMediaBrowser.connect();

    }

    private final MediaBrowserCompat.ConnectionCallback mConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    LogUtil.d(TAG, "onConnected");
                    try {
                        connectToSession(mMediaBrowser.getSessionToken());
                    } catch (RemoteException e) {
                        LogUtil.e("",e.toString());
                    }
                }
            };

    private void connectToSession(MediaSessionCompat.Token token) throws RemoteException {
        MediaControllerCompat mediaController = new MediaControllerCompat(this, token);
        setSupportMediaController(mediaController);
        mediaController.registerCallback(mMediaControllerCallback);

      /*  if (shouldShowControls()) {
            showPlaybackControls();
        } else {
            LogUtil.d(TAG, "connectionCallback.onConnected: " +
                    "hiding controls because metadata is null");
            //hidePlaybackControls();
        }
*/
        /*if (mControlsFragment != null) {
            mControlsFragment.onConnected();
        }
*/
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
                }

                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    super.onMetadataChanged(metadata);
                }

                /* @Override
                public void onPlaybackStateChanged(@NonNull PlaybackState state) {
                   *//* if (shouldShowControls()) {
                        showPlaybackControls();
                    } else {
                        LogHelper.d(TAG, "mediaControllerCallback.onPlaybackStateChanged: " +
                                "hiding controls because state is ", state.getState());
                        hidePlaybackControls();
                    }*//*
                }

                @Override
                public void onMetadataChanged(MediaMetadata metadata) {
                   *//* if (shouldShowControls()) {
                        showPlaybackControls();
                    } else {
                        LogHelper.d(TAG, "mediaControllerCallback.onMetadataChanged: " +
                                "hiding controls because metadata is null");
                        hidePlaybackControls();
                    }*//*
                }*/
            };


    @Override
    protected void onStart() {
        super.onStart();
    }

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
        errorFreshButton=(Button)mErrorView.findViewById(R.id.click_refresh);
        errorFreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorRefresh();
            }
        });

    }

    abstract void errorRefresh();


    public void showFailedError() {
        if(mErrorView==null){
            throw new IllegalStateException("mErrorView is null ! ");
        }
        mErrorView.setVisibility(View.VISIBLE);
    }

    public void hideErrorView() {
        if(mErrorView==null){
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
