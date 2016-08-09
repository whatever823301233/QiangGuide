package com.qiang.qiangguide.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.activity.MainActivity;
import com.qiang.qiangguide.activity.MainGuideActivity;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.util.BitmapCache;
import com.qiang.qiangguide.util.BitmapUtil;
import com.qiang.qiangguide.util.LogUtil;
import com.qiang.qiangguide.volley.QVolley;

/**
 * Created by xq823 on 2016/8/8.
 */
public class PlaybackControlsFragment extends BaseFragment {


    private static final String TAG = LogUtil.makeLogTag(PlaybackControlsFragment.class);

    private ImageButton mPlayPause;
    private TextView mTitle;
    private TextView mSubtitle;
    private TextView mExtraInfo;
    private ImageView mAlbumArt;
    private String mArtUrl;
    // Receive callbacks from the MediaController. Here we update our state such as which queue
    // is being shown, the current title and description and the PlaybackState.
    private final MediaControllerCompat.Callback mCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
            LogUtil.d(TAG, "Received playback state change to state ", state.getState());
            PlaybackControlsFragment.this.onPlaybackStateChanged(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            if (metadata == null) {
                return;
            }
            LogUtil.d(TAG, "Received metadata state change to mediaId=",
                    metadata.getDescription().getMediaId(),
                    " song=", metadata.getDescription().getTitle());
            PlaybackControlsFragment.this.onMetadataChanged(metadata);
        }
    };

    private Activity activity;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    void initView() {
        View rootView = LayoutInflater.from(activity).inflate(R.layout.fragment_playback_controls, null);
        setContentView(rootView);
        mPlayPause = (ImageButton) rootView.findViewById(R.id.play_pause);
        mPlayPause.setEnabled(true);
        mPlayPause.setOnClickListener(mButtonListener);

        mTitle = (TextView) rootView.findViewById(R.id.title);
        mSubtitle = (TextView) rootView.findViewById(R.id.artist);
        mExtraInfo = (TextView) rootView.findViewById(R.id.extra_info);
        mAlbumArt = (ImageView) rootView.findViewById(R.id.album_art);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                MediaControllerCompat controller = getActivity().getSupportMediaController();
                MediaMetadataCompat metadata = controller.getMetadata();
                if (metadata != null) {
                    intent.putExtra(MainGuideActivity.EXTRA_CURRENT_MEDIA_DESCRIPTION,
                            metadata.getDescription());
                }
                startActivity(intent);
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d(TAG, "fragment.onStart");
        MediaControllerCompat controller =  getActivity().getSupportMediaController();
        if (controller != null) {
            onConnected();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d(TAG, "fragment.onStop");
        MediaControllerCompat controller =  getActivity().getSupportMediaController();
        if (controller != null) {
            controller.unregisterCallback(mCallback);
        }
    }

    public void onConnected() {
        if(getActivity()==null){return;}
        MediaControllerCompat controller =  getActivity().getSupportMediaController();
        LogUtil.d(TAG, "onConnected, mediaController==null? ", controller == null);
        if (controller != null) {
            onMetadataChanged(controller.getMetadata());
            onPlaybackStateChanged(controller.getPlaybackState());
            controller.registerCallback(mCallback);
        }
    }

    private void onMetadataChanged(MediaMetadataCompat metadata) {
        LogUtil.d(TAG, "onMetadataChanged ", metadata);
        if (getActivity() == null) {
            LogUtil.w(TAG, "onMetadataChanged called when getActivity null," +
                    "this should not happen if the callback was properly unregistered. Ignoring.");
            return;
        }
        if (metadata == null) {
            return;
        }
        mTitle.setText(metadata.getDescription().getTitle());
        Bundle bundle=metadata.getBundle();
        mSubtitle.setText((String) bundle.get(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST));
        String artUrl = (String) bundle.get(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI);
        if (!TextUtils.equals(artUrl, mArtUrl)) {
            mArtUrl = artUrl;
            Bitmap art = metadata.getDescription().getIconBitmap();
            if (art == null) {
                Bitmap bm = BitmapCache.getInstance().getBitmap(Constants.BASE_URL+mArtUrl);
                if(bm!=null){
                    art= BitmapUtil.scaleBitmap(bm,128,128);
                }
            }
            if (art != null) {
                mAlbumArt.setImageBitmap(art);
            } else {
                QVolley.getInstance(null).loadImageIcon(Constants.BASE_URL+mArtUrl,mAlbumArt);
            }
        }
    }

    public void setExtraInfo(String extraInfo) {
        if (extraInfo == null) {
            mExtraInfo.setVisibility(View.GONE);
        } else {
            mExtraInfo.setText(extraInfo);
            mExtraInfo.setVisibility(View.VISIBLE);
        }
    }

    private void onPlaybackStateChanged(PlaybackStateCompat state) {
        LogUtil.d(TAG, "onPlaybackStateChanged ", state);
        if (getActivity() == null) {
            LogUtil.w(TAG, "onPlaybackStateChanged called when getActivity null," +
                    "this should not happen if the callback was properly unregistered. Ignoring.");
            return;
        }
        if (state == null) {
            return;
        }
        boolean enablePlay = false;
        switch (state.getState()) {
            case PlaybackStateCompat.STATE_PAUSED:
            case PlaybackStateCompat.STATE_STOPPED:
                enablePlay = true;
                break;
            case PlaybackStateCompat.STATE_ERROR:
                LogUtil.e(TAG, "error playbackstate: ", state.getErrorMessage());
                Toast.makeText(getActivity(), state.getErrorMessage(), Toast.LENGTH_LONG).show();
                break;
            case PlaybackStateCompat.STATE_BUFFERING:
                break;
            case PlaybackStateCompat.STATE_CONNECTING:
                break;
            case PlaybackStateCompat.STATE_FAST_FORWARDING:
                break;
            case PlaybackStateCompat.STATE_NONE:
                break;
            case PlaybackStateCompat.STATE_PLAYING:
                break;
            case PlaybackStateCompat.STATE_REWINDING:
                break;
            case PlaybackStateCompat.STATE_SKIPPING_TO_NEXT:
                break;
            case PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS:
                break;
            case PlaybackStateCompat.STATE_SKIPPING_TO_QUEUE_ITEM:
                break;
        }

        if (enablePlay) {
            mPlayPause.setImageDrawable(
                    ContextCompat.getDrawable(getActivity(), R.drawable.ic_play_arrow_black_36dp));
        } else {
            mPlayPause.setImageDrawable(
                    ContextCompat.getDrawable(getActivity(), R.drawable.ic_pause_black_36dp));
        }

       /* MediaControllerCompat controller =  getActivity().getSupportMediaController();
        String extraInfo = null;
        if (controller != null && controller.getExtras() != null) {
            String castName = controller.getExtras().getString(PlayService.EXTRA_CONNECTED_CAST);
            if (castName != null) {
                extraInfo = getResources().getString(R.string.casting_to_device, castName);
            }
        }
        setExtraInfo(extraInfo);*/
    }

    private final View.OnClickListener mButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MediaControllerCompat controller =  getActivity().getSupportMediaController();
            PlaybackStateCompat stateObj = controller.getPlaybackState();
            final int state = stateObj == null ?
                    PlaybackStateCompat.STATE_NONE : stateObj.getState();
            LogUtil.d(TAG, "Button pressed, in state " + state);
            switch (v.getId()) {
                case R.id.play_pause:
                    LogUtil.d(TAG, "Play button pressed, in state " + state);
                    if (state == PlaybackStateCompat.STATE_PAUSED ||
                            state == PlaybackStateCompat.STATE_STOPPED ||
                            state == PlaybackStateCompat.STATE_NONE) {
                        playMedia();
                    } else if (state == PlaybackStateCompat.STATE_PLAYING ||
                            state == PlaybackStateCompat.STATE_BUFFERING ||
                            state == PlaybackStateCompat.STATE_CONNECTING) {
                        pauseMedia();
                    }
                    break;
            }
        }
    };

    private void playMedia() {
        MediaControllerCompat controller = getActivity().getSupportMediaController();
        if (controller != null) {
            controller.getTransportControls().play();
        }
    }

    private void pauseMedia() {
        MediaControllerCompat controller =  getActivity().getSupportMediaController();
        if (controller != null) {
            controller.getTransportControls().pause();
        }
    }

}
