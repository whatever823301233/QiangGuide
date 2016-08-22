package com.qiang.qiangguide.aInterface;

import android.content.Context;
import android.content.Intent;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;

/**
 * Created by Qiang on 2016/8/19.
 */
public interface IPlayView {

    void updateFromParams(Intent intent);

    void initMediaBrowser();

    void registerMediaControllerCallback();

    void unregisterMediaControllerCallback();


    void updateDuration(MediaMetadataCompat metadata);

    void onMetadataChanged(MediaMetadataCompat metadata);

    void subscribeExhibitList();

    void setToolbarTitle(String title);

    void scheduleSeekbarUpdate();

    void stopSeekbarUpdate();

    void updateProgress();

    void updatePlaybackState(PlaybackStateCompat state);

    Context getContext();


    void connectSession();

    Intent getIntent();

    MediaControllerCompat getSupportMediaController();

    String getTag();

    void setPlayTime(String time);

    void finish();

    void setSupportMediaController(MediaControllerCompat mediaController);

    void updateMediaMetadataCompat(MediaMetadataCompat description);

    void setMuseumId(String museumId);


    void showIcon(String iconUrl);

    void setLyricUrl(String lyricUrl);

    void refreshLyricContent();
}
