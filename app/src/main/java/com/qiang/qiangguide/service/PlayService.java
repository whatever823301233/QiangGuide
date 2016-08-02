package com.qiang.qiangguide.service;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.biz.MusicProvider;
import com.qiang.qiangguide.util.LogUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.qiang.qiangguide.service.MediaIDHelper.CATEGORY_SEPARATOR;
import static com.qiang.qiangguide.service.MediaIDHelper.MEDIA_ID_MUSICS_BY_GENRE;

public class PlayService extends MediaBrowserServiceCompat implements Playback.Callback {

    public static final String MEDIA_ID_GUIDE = "__GUIDE__";


    private static final String TAG = PlayService.class.getSimpleName();
    // Extra on MediaSession that contains the Cast device name currently connected to
    //当前连接设备的额外信息
    public static final String EXTRA_CONNECTED_CAST = "com.example.android.uamp.CAST_NAME";
    // The action of the incoming Intent indicating that it contains a command
    // to be executed (see {@link #onStartCommand})
    public static final String ACTION_CMD = "com.qiang.guide.ACTION_CMD";
    // The key in the extras of the incoming Intent indicating the command that
    // should be executed (see {@link #onStartCommand})
    public static final String CMD_NAME = "CMD_NAME";
    // A value of a CMD_NAME key in the extras of the incoming Intent that
    // indicates that the music playback should be paused (see {@link #onStartCommand})
    public static final String CMD_PAUSE = "CMD_PAUSE";
    // A value of a CMD_NAME key that indicates that the music playback should switch
    // to local playback from cast playback.
    public static final String CMD_STOP_CASTING = "CMD_STOP_CASTING";
    // Action to thumbs up a media item
    private static final String CUSTOM_ACTION_THUMBS_UP = "com.qiang.guide.THUMBS_UP";
    // Delay stopSelf by using a handler.
    private static final int STOP_DELAY = 30000;

    private MediaSessionCompat mSession;

    // Music catalog manager
    private MusicProvider mMusicProvider;


    private final DelayedStopHandler mDelayedStopHandler = new DelayedStopHandler(this);

    // "Now playing" queue:
    private List<MediaSessionCompat.QueueItem> mPlayingQueue;
    private int mCurrentIndexOnQueue;
    // Indicates whether the service was started.
    private boolean mServiceStarted;

    private Bundle mSessionExtras;

    private Playback mPlayback;

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayingQueue = new ArrayList<>();
        //mMusicProvider = new MusicProvider();
        // Start a new MediaSession
        mSession = new MediaSessionCompat(this, TAG);
        setSessionToken(mSession.getSessionToken());
        mSession.setCallback(new MediaSessionCompatCallback());
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mPlayback = new LocalPlayback(this);
        mPlayback.setState(PlaybackStateCompat.STATE_NONE);
        mPlayback.setCallback(this);
        mPlayback.start();

        updatePlaybackState(null);

    }

    @Override
    public int onStartCommand(Intent startIntent, int flags, int startId) {
        if (startIntent != null) {
            String action = startIntent.getAction();
            String command = startIntent.getStringExtra(CMD_NAME);
            if (ACTION_CMD.equals(action)) {
                if (CMD_PAUSE.equals(command)) {
                    if (mPlayback != null && mPlayback.isPlaying()) {
                        handlePauseRequest();
                    }
                } /*else if (CMD_STOP_CASTING.equals(command)) {
                    mCastManager.disconnect();
                }*/
            }
        }
        // Reset the delay handler to enqueue a message to stop the service if
        // nothing is playing.
        mDelayedStopHandler.removeCallbacksAndMessages(null);
        mDelayedStopHandler.sendEmptyMessageDelayed(0, STOP_DELAY);
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        LogUtil.d(TAG, "onDestroy");
        // Service is being killed, so make sure we release our resources
        handleStopRequest(null);
        mDelayedStopHandler.removeCallbacksAndMessages(null);
        // Always release the MediaSession to clean up resources
        // and notify associated MediaController(s).
        mSession.release();
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return new MediaBrowserServiceCompat.BrowserRoot(MEDIA_ID_GUIDE, null);
    }

    @Override
    public void onLoadChildren(@NonNull final String parentId, @NonNull final Result<List<MediaBrowserCompat.MediaItem>> result) {
        if (!mMusicProvider.isInitialized()) {
            // Use result.detach to allow calling result.sendResult from another thread:
            result.detach();

            mMusicProvider.retrieveMediaAsync(new MusicProvider.Callback() {
                @Override
                public void onMusicCatalogReady(boolean success) {
                    if (success) {
                        loadChildrenImpl(parentId, result);
                    } else {
                        updatePlaybackState(getString(R.string.error_no_metadata));
                        result.sendResult(Collections.<MediaBrowserCompat.MediaItem>emptyList());
                    }
                }
            });

        } else {
            // If our music catalog is already loaded/cached, load them into result immediately
            loadChildrenImpl(parentId, result);
        }
    }

    private void loadChildrenImpl(String parentMediaId, Result<List<MediaBrowserCompat.MediaItem>> result) {
        LogUtil.d(TAG, "OnLoadChildren: parentMediaId="+ parentMediaId);

        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();

        if (MEDIA_ID_GUIDE.equals(parentMediaId)) {

            String genre = MediaIDHelper.getHierarchy(parentMediaId)[1];
            LogUtil.d(TAG, "OnLoadChildren.SONGS_BY_GENRE  genre=" + genre);
            for (MediaMetadataCompat track : mMusicProvider.getMusicsByGenre(genre)) {
                // Since mediaMetadata fields are immutable, we need to create a copy, so we
                // can set a hierarchy-aware mediaID. We will need to know the media hierarchy
                // when we get a onPlayFromMusicID call, so we can create the proper queue based
                // on where the music was selected from (by artist, by genre, random, etc)
                String hierarchyAwareMediaID = MediaIDHelper.createMediaID(
                        track.getDescription().getMediaId(), MEDIA_ID_MUSICS_BY_GENRE, genre);
                MediaMetadataCompat trackCopy = new MediaMetadataCompat.Builder(track)
                        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, hierarchyAwareMediaID)
                        .build();
                MediaBrowserCompat.MediaItem bItem = new MediaBrowserCompat.MediaItem(
                        trackCopy.getDescription(),
                        MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);
                mediaItems.add(bItem);
            }

        }else {
            LogUtil.w(TAG, "Skipping unmatched parentMediaId: "+parentMediaId);
        }
        LogUtil.d(TAG, "OnLoadChildren sending "+mediaItems.size() + " results for " +parentMediaId);
        result.sendResult(mediaItems);

    }


    public static String createBrowseCategoryMediaID(String categoryType, String categoryValue) {
        return categoryType + CATEGORY_SEPARATOR + categoryValue;
    }

    private void handleStopRequest(String withError) {
        LogUtil.d(TAG, "handleStopRequest: mState=" + mPlayback.getState() + " error="+ withError);
        mPlayback.stop(true);
        // reset the delayed stop handler.
        mDelayedStopHandler.removeCallbacksAndMessages(null);
        mDelayedStopHandler.sendEmptyMessageDelayed(0, STOP_DELAY);

        updatePlaybackState(withError);

        // service is no longer necessary. Will be started again if needed.
        stopSelf();
        mServiceStarted = false;
    }


    /**
     * Handle a request to play music
     */
    private void handlePlayRequest() {
        LogUtil.d(TAG, "handlePlayRequest: mState=" + mPlayback.getState());

        mDelayedStopHandler.removeCallbacksAndMessages(null);
        if (!mServiceStarted) {
            LogUtil.v(TAG, "Starting service");
            // The MusicService needs to keep running even after the calling MediaBrowser
            // is disconnected. Call startService(Intent) and then stopSelf(..) when we no longer
            // need to play media.
            startService(new Intent(getApplicationContext(), PlayService.class));
            mServiceStarted = true;
        }

        if (!mSession.isActive()) {
            mSession.setActive(true);
        }

        if (QueueHelper.isIndexPlayable(mCurrentIndexOnQueue, mPlayingQueue)) {
            updateMetadata();
            mPlayback.play(mPlayingQueue.get(mCurrentIndexOnQueue));
        }
    }

    /**
     * Handle a request to pause music
     */
    private void handlePauseRequest() {
        LogUtil.d(TAG, "handlePauseRequest: mState=" + mPlayback.getState());
        mPlayback.pause();
        // reset the delayed stop handler.
        mDelayedStopHandler.removeCallbacksAndMessages(null);
        mDelayedStopHandler.sendEmptyMessageDelayed(0, STOP_DELAY);
    }


    /**
     * Update the current media player state, optionally showing an error message.
     *
     * @param error if not null, error message to present to the user.
     */
    private void updatePlaybackState(String error) {
        LogUtil.d(TAG, "updatePlaybackState, playback state=" + mPlayback.getState());
        long position = PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN;
        if (mPlayback != null && mPlayback.isConnected()) {
            position = mPlayback.getCurrentStreamPosition();
        }

        //long actions=getAvailableActions();
        PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder();

        long actions = PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID |
                PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH;
        if (mPlayingQueue == null || mPlayingQueue.isEmpty()) {
            stateBuilder.setActions(actions);
        }else{
            if (mPlayback.isPlaying()) {
                actions |= PlaybackStateCompat.ACTION_PAUSE;
            }
            if (mCurrentIndexOnQueue > 0) {
                actions |= PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS;
            }
            if (mCurrentIndexOnQueue < mPlayingQueue.size() - 1) {
                actions |= PlaybackStateCompat.ACTION_SKIP_TO_NEXT;
            }
            stateBuilder.setActions(actions);
        }

        setCustomAction(stateBuilder);
        int state = mPlayback.getState();

        // If there is an error message, send it to the playback state:
        if (error != null) {
            // Error states are really only supposed to be used for errors that cause playback to
            // stop unexpectedly and persist until the user takes action to fix it.
            stateBuilder.setErrorMessage(error);
            state = PlaybackStateCompat.STATE_ERROR;
        }
        switch (state){
            case PlaybackStateCompat.STATE_NONE:
                state =PlaybackStateCompat.STATE_NONE;
                break;
            case PlaybackStateCompat.STATE_STOPPED:
                state =PlaybackStateCompat.STATE_STOPPED;
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                state =PlaybackStateCompat.STATE_PAUSED;
                break;
            case PlaybackStateCompat.STATE_PLAYING:
                state =PlaybackStateCompat.STATE_PLAYING;
                break;
            case PlaybackStateCompat.STATE_FAST_FORWARDING:
                state =PlaybackStateCompat.STATE_FAST_FORWARDING;
                break;
            case PlaybackStateCompat.STATE_BUFFERING:
                state =PlaybackStateCompat.STATE_BUFFERING;
                break;
            case PlaybackStateCompat.STATE_ERROR:
                state =PlaybackStateCompat.STATE_ERROR;
                break;
            default:state =PlaybackStateCompat.STATE_NONE;
        }

        stateBuilder.setState(state, position, 1.0f, SystemClock.elapsedRealtime());

//        // Set the activeQueueItemId if the current index is valid.
//        if (QueueHelper.isIndexPlayable(mCurrentIndexOnQueue, mPlayingQueue)) {
//            MediaSessionCompat.QueueItem item = mPlayingQueue.get(mCurrentIndexOnQueue);
//            stateBuilder.setActiveQueueItemId(item.getQueueId());
//        }

        mSession.setPlaybackState(stateBuilder.build());

    }

    private void setCustomAction(PlaybackStateCompat.Builder stateBuilder) {

        MediaMetadataCompat currentMusic = getCurrentPlayingMusic();
        if (currentMusic != null) {
            // Set appropriate "Favorite" icon on Custom action:
            String musicId = currentMusic.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID);
            int favoriteIcon = R.drawable.ic_star_off;
            /*if (mMusicProvider.isFavorite(musicId)) {
                favoriteIcon = R.drawable.ic_star_on;
            }*/// TODO: 2016/8/2 喜爱标记
//            LogUtil.d(TAG, "updatePlaybackState, setting Favorite custom action of music ",
//                    musicId, " current favorite=", mMusicProvider.isFavorite(musicId));
            Bundle customActionExtras = new Bundle();
            stateBuilder.addCustomAction(new PlaybackStateCompat.CustomAction.Builder(
                    CUSTOM_ACTION_THUMBS_UP, getString(R.string.favorite), favoriteIcon)
                    .setExtras(customActionExtras)
                    .build());
        }

    }

    private void updateMetadata() {

    }

    private MediaMetadataCompat getCurrentPlayingMusic() {
        if (QueueHelper.isIndexPlayable(mCurrentIndexOnQueue, mPlayingQueue)) {
            MediaSessionCompat.QueueItem item = mPlayingQueue.get(mCurrentIndexOnQueue);
            if (item != null) {
                LogUtil.d(TAG, "getCurrentPlayingMusic for musicId="+ item.getDescription().getMediaId());
                return mMusicProvider.getMusic(
                        MediaIDHelper.extractMusicIDFromMediaID(item.getDescription().getMediaId()));
            }
        }
        return null;
    }

    private  long getAvailableActions() {
        long actions = PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID |
                PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH;
        if (mPlayingQueue == null || mPlayingQueue.isEmpty()) {
            return actions;
        }
        if (mPlayback.isPlaying()) {
            actions |= PlaybackStateCompat.ACTION_PAUSE;
        }
        if (mCurrentIndexOnQueue > 0) {
            actions |= PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS;
        }
        if (mCurrentIndexOnQueue < mPlayingQueue.size() - 1) {
            actions |= PlaybackStateCompat.ACTION_SKIP_TO_NEXT;
        }
        return actions;
    }



    @Override
    public void onCompletion() {

    }

    @Override
    public void onPlaybackStatusChanged(int state) {

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onMetadataChanged(String mediaId) {

    }





    private final class MediaSessionCompatCallback extends MediaSessionCompat.Callback{

        @Override
        public void onPlay() {

        }

        @Override
        public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
            return super.onMediaButtonEvent(mediaButtonEvent);
        }

        @Override
        public void onCommand(String command, Bundle extras, ResultReceiver cb) {
            super.onCommand(command, extras, cb);
        }

        @Override
        public void onCustomAction(String action, Bundle extras) {
            super.onCustomAction(action, extras);
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            super.onPlayFromMediaId(mediaId, extras);
        }

        @Override
        public void onFastForward() {
            super.onFastForward();
        }

        @Override
        public void onPlayFromSearch(String query, Bundle extras) {
            super.onPlayFromSearch(query, extras);
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onSkipToQueueItem(long id) {
            super.onSkipToQueueItem(id);
        }

        @Override
        public void onPlayFromUri(Uri uri, Bundle extras) {
            super.onPlayFromUri(uri, extras);
        }

        @Override
        public void onRewind() {
            super.onRewind();
        }

        @Override
        public void onSetRating(RatingCompat rating) {
            super.onSetRating(rating);
        }

    }


    /**
     * A simple handler that stops the service if playback is not active (playing)
     */
    private static class DelayedStopHandler extends Handler {
        private final WeakReference<PlayService> mWeakReference;

        private DelayedStopHandler(PlayService service) {
            mWeakReference = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            PlayService service = mWeakReference.get();
            if (service != null && service.mPlayback != null) {
                if (service.mPlayback.isPlaying()) {
                    LogUtil.d(TAG, "Ignoring delayed stop since the media player is in use.");
                    return;
                }
                LogUtil.d(TAG, "Stopping service with delay handler.");
                service.stopSelf();
                service.mServiceStarted = false;
            }
        }
    }



}
