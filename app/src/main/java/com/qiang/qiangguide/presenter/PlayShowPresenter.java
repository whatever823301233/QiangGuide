package com.qiang.qiangguide.presenter;

import android.content.Intent;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.widget.SeekBar;

import com.qiang.qiangguide.aInterface.IPlayView;
import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.bean.MultiAngleImg;
import com.qiang.qiangguide.biz.IPlayShowBiz;
import com.qiang.qiangguide.biz.bizImpl.PlayShowBiz;
import com.qiang.qiangguide.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by Qiang on 2016/8/19.
 */
public class PlayShowPresenter {

    private IPlayView playView;
    private IPlayShowBiz playShowBiz;

    public PlayShowPresenter(IPlayView playView){
        this.playView=playView;
        playShowBiz=new PlayShowBiz();
    }

    public void onViewCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Intent intent=playView.getIntent();
            playView.updateFromParams(intent);
        }
        playView.initMediaBrowser();
    }

    public void onViewStart() {
        playView.connectSession();
        playView.registerMediaControllerCallback();
    }

    public void onViewStop() {
        playView.unregisterMediaControllerCallback();
    }

    public void onMediaBrowserConnected(MediaSessionCompat.Token sessionToken) {
        MediaControllerCompat mediaController = null;
        try {
            mediaController = new MediaControllerCompat(playView.getContext(), sessionToken);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if ((mediaController != null ? mediaController.getMetadata() : null) == null) {
            playView.finish();
            return;
        }
        playView.setSupportMediaController(mediaController);
        playView.registerMediaControllerCallback();
        // mediaController.registerCallback(mCallback);
        PlaybackStateCompat state = mediaController.getPlaybackState();
        playView.updatePlaybackState(state);
        MediaMetadataCompat metadata = mediaController.getMetadata();
        if (metadata != null) {
            playView.updateMediaMetadataCompat(metadata);
            playView.updateDuration(metadata);
        }
        playView.updateProgress();
        if (state != null && (state.getState() == PlaybackStateCompat.STATE_PLAYING ||
                state.getState() == PlaybackStateCompat.STATE_BUFFERING)) {
            playView.scheduleSeekbarUpdate();
        }
        //playView.connectToSession(sessionToken);
    }


    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        playView.updatePlaybackState(state);
    }

    public void onMetadataChanged(MediaMetadataCompat metadata) {
        if (metadata == null) {
            return;
        }
        playView.onMetadataChanged(metadata);
        playView.updateDuration(metadata);
    }

    public void onPauseButtonClick() {
        PlaybackStateCompat state = playView.getSupportMediaController().getPlaybackState();
        if (state == null) {return;}
        MediaControllerCompat.TransportControls controls =
                playView.getSupportMediaController().getTransportControls();
        switch (state.getState()) {
            case PlaybackState.STATE_PLAYING: // fall through
            case PlaybackState.STATE_BUFFERING:
                controls.pause();
                playView.stopSeekbarUpdate();
                break;
            case PlaybackState.STATE_PAUSED:
            case PlaybackState.STATE_STOPPED:
                controls.play();
                playView.scheduleSeekbarUpdate();
                break;
            default:
                LogUtil.d(playView.getTag(), "onClick with state ", state.getState());
        }
    }

    public void onProgressChanged(int progress) {
        String time=DateUtils.formatElapsedTime(progress / 1000);
        playView.setPlayTime(time);
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        playView.getSupportMediaController().getTransportControls().seekTo(seekBar.getProgress());
        playView.scheduleSeekbarUpdate();
    }

    public void updateMediaMetadataCompat(MediaMetadataCompat metadata) {
        if (metadata == null) {return;}
        Bundle bundle=metadata.getBundle();
        String exhibitId=bundle.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID);
        Exhibit exhibit=playShowBiz.getExhibit(exhibitId);
        if(exhibit==null){return;}
        playView.setExhibit(exhibit);
        String museumId=exhibit.getMuseumId();
        playView.setMuseumId(museumId);
        String title=exhibit.getName();
        playView.setToolbarTitle(title);
        String iconUrl=exhibit.getIconurl();
        playView.setIconUrl(iconUrl);
        playView.showIcon();
        String lyricUrl=exhibit.getTexturl();
        playView.setLyricUrl(lyricUrl);
        String imgs=exhibit.getImgsurl();
        setMultiImgs(imgs,iconUrl);
        String content=bundle.getString(MediaMetadataCompat.METADATA_KEY_ARTIST);
        playView.setExhibitContent(content);
        playView.refreshLyricContent();
    }

    private void setMultiImgs(String imgs,String iconUrl) {
        ArrayList<MultiAngleImg> multiAngleImgs=new ArrayList<>();
        ArrayList<Integer> imgsTimeList=new ArrayList<>();
        if(TextUtils.isEmpty(imgs)){
            MultiAngleImg multiAngleImg=new MultiAngleImg();
            multiAngleImg.setUrl(iconUrl);
            multiAngleImgs.add(multiAngleImg);

        }else{//获取多角度图片地址数组
            String[] img = imgs.split(",");
            for (String singleUrl : img) {
                String[] nameTime = singleUrl.split("\\*");
                MultiAngleImg multiAngleImg=new MultiAngleImg();
                int time=Integer.valueOf(nameTime[1]);
                multiAngleImg.setTime(time);
                multiAngleImg.setUrl(nameTime[0]);
                imgsTimeList.add(time);
                multiAngleImgs.add(multiAngleImg);
            }
        }
        playView.setImgsAndTimes(multiAngleImgs,imgsTimeList);

        playView.refreshMultiImgs();
    }

    public void onSwitchLyric() {
        playView.onSwitchLyric();
    }

    public void onMultiImgClick(MultiAngleImg multiAngleImg) {
        int time=multiAngleImg.getTime();
        playView.skipTo(time);
    }
}
