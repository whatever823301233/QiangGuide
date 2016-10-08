package com.qiang.qiangguide.biz;

import android.os.AsyncTask;
import android.support.v4.media.MediaMetadataCompat;

import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.db.handler.ExhibitHandler;
import com.qiang.qiangguide.util.FileUtil;
import com.qiang.qiangguide.util.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Qiang on 2016/8/2.
 */
public class MusicProvider {


    private static final String TAG =MusicProvider.class.getSimpleName();
    // Categorized caches for music track data:
    //private ConcurrentMap<String, List<Exhibit>> mMusicListByGenre;
    private ConcurrentMap<String, List<MediaMetadataCompat>> mMusicListByMuseumId;

    //展品 id 以及实体结合
    private final ConcurrentMap<String, MediaMetadataCompat> mMusicListById;

    private final Set<String> mFavoriteTracks;

    enum State {
        NON_INITIALIZED, INITIALIZING, INITIALIZED
    }

    private volatile State mCurrentState = State.NON_INITIALIZED;

    public interface Callback {
        void onMusicCatalogReady(boolean success);
    }

    public MusicProvider() {
        mMusicListByMuseumId = new ConcurrentHashMap<>();
        mMusicListById = new ConcurrentHashMap<>();
        mFavoriteTracks = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
    }


    /**
     * Get music tracks of the given genre
     *
     */
    public Iterable<MediaMetadataCompat> getMusicsByMuseumId(String museumId) {
        if (mCurrentState != State.INITIALIZED || !mMusicListByMuseumId.containsKey(museumId)) {
            return Collections.emptyList();
        }
        return mMusicListByMuseumId.get(museumId);
    }


    /**
     * Get music tracks of the given genre
     *

    public Iterable<Exhibit> getMusicsByGenre(String genre) {
        if (mCurrentState != State.INITIALIZED || !mMusicListByGenre.containsKey(genre)) {
            return Collections.emptyList();
        }
        return mMusicListByGenre.get(genre);
    } */

    /**
     * Very basic implementation of a search that filter music tracks with title containing
     * the given query.
     *
     */
    public Iterable<MediaMetadataCompat> searchMusicBySongTitle(String query) {
        return searchMusic(MediaMetadataCompat.METADATA_KEY_TITLE, query);
    }


    /**
     * Very basic implementation of a search that filter music tracks with album containing
     * the given query.
     *
     */
    public Iterable<MediaMetadataCompat> searchMusicByAlbum(String query) {
        return searchMusic(MediaMetadataCompat.METADATA_KEY_ALBUM, query);
    }

    /**
     * Very basic implementation of a search that filter music tracks with artist containing
     * the given query.
     *
     */
    public Iterable<MediaMetadataCompat> searchMusicByArtist(String query) {
        return searchMusic(MediaMetadataCompat.METADATA_KEY_ARTIST, query);
    }

    Iterable<MediaMetadataCompat> searchMusic(String metadataField, String query) {
        if (mCurrentState != State.INITIALIZED) {
            return Collections.emptyList();
        }
        ArrayList<MediaMetadataCompat> result = new ArrayList<>();
        query = query.toLowerCase(Locale.US);
        for (MediaMetadataCompat track : mMusicListById.values()) {
            if (track.getString(metadataField).toLowerCase(Locale.US)
                    .contains(query)) {
                result.add(track);
            }
        }
        return result;
    }

    /**
     * Return the MediaMetadata for the given musicID.
     *
     * @param musicId The unique, non-hierarchical music ID.
     */
    public MediaMetadataCompat getMusic(String musicId) {
        return mMusicListById.containsKey(musicId) ? mMusicListById.get(musicId) : null;
    }


    public synchronized void updateMusic(String musicId, MediaMetadataCompat metadata) {
        MediaMetadataCompat track = mMusicListById.get(musicId);
        if (track == null) {
            return;
        }

        String oldGenre = track.getString(MediaMetadataCompat.METADATA_KEY_GENRE);
        String newGenre = metadata.getString(MediaMetadataCompat.METADATA_KEY_GENRE);

        track = metadata;

        // if genre has changed, we need to rebuild the list by genre
        if (!oldGenre.equals(newGenre)) {
            //buildListsByGenre();
            buildListsByMuseumId();
        }
    }

    public void setFavorite(String musicId, boolean favorite) {
        if (favorite) {
            mFavoriteTracks.add(musicId);
        } else {
            mFavoriteTracks.remove(musicId);
        }
    }

    public boolean isFavorite(String musicId) {
        return mFavoriteTracks.contains(musicId);
    }

    public boolean isInitialized() {
        return mCurrentState == State.INITIALIZED;
    }

    /**
     * Get the list of music tracks from a server and caches the track information
     * for future reference, keying tracks by musicId and grouping by genre.
     */
    public void retrieveMediaAsync(final Callback callback) {
        LogUtil.d(TAG, "retrieveMediaAsync called");
        if (mCurrentState == State.INITIALIZED) {
            // Nothing to do, execute callback immediately
            callback.onMusicCatalogReady(true);
            return;
        }

        // Asynchronously load the music catalog in a separate thread
        new AsyncTask<Void, Void, State>() {
            @Override
            protected State doInBackground(Void... params) {
                retrieveMedia();
                return mCurrentState;
            }

            @Override
            protected void onPostExecute(State current) {
                if (callback != null) {
                    callback.onMusicCatalogReady(current == State.INITIALIZED);
                }
            }
        }.execute();
    }


    private synchronized void buildListsByMuseumId() {
        ConcurrentMap<String, List<MediaMetadataCompat>> newMusicListByMuseumId = new ConcurrentHashMap<>();

        for (MediaMetadataCompat m : mMusicListById.values()) {
            String museumId = m.getString(MediaMetadataCompat.METADATA_KEY_ALBUM);
            List<MediaMetadataCompat> list = newMusicListByMuseumId.get(museumId);
            if (list == null) {
                list = new ArrayList<>();
                newMusicListByMuseumId.put(museumId, list);
            }
            list.add(m);
        }
        mMusicListByMuseumId = newMusicListByMuseumId;
    }

    private synchronized void retrieveMedia() {
        try {
            if (mCurrentState == State.NON_INITIALIZED) {
                mCurrentState = State.INITIALIZING;
                List<Exhibit> exhibitList= ExhibitHandler.queryAllExhibitList();
                if (exhibitList != null) {
                    for (int j = 0; j < exhibitList.size(); j++) {
                        MediaMetadataCompat item = buildFromExhibit(exhibitList.get(j));
                        String musicId = item.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID);
                        mMusicListById.put(musicId, item);
                    }
                    buildListsByMuseumId();
                }
                mCurrentState = State.INITIALIZED;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e + " Could not retrieve music list");
        } finally {
            if (mCurrentState != State.INITIALIZED) {
                // Something bad happened, so we reset state to NON_INITIALIZED to allow
                // retries (eg if the network connection is temporary unavailable)
                mCurrentState = State.NON_INITIALIZED;
            }
        }
    }

    private MediaMetadataCompat buildFromExhibit(Exhibit exhibit){

        String id = exhibit.getId();
        String title = exhibit.getName();
        String museumId = exhibit.getMuseumId();
        String source = exhibit.getAudiourl();
        String iconUrl = exhibit.getIconurl();
        String beaconId = exhibit.getBeaconId();
        String content = exhibit.getContent();
        String imgs = exhibit.getImgsurl();
        String introduce = exhibit.getIntroduce();
        String texturl = exhibit.getTexturl();
        String labels = exhibit.getLabels();
        //int duration = json.getInt(JSON_DURATION) * 1000; // ms

        LogUtil.d(TAG, "Found music track: " +exhibit.toString());

        if (!source.startsWith("http")) {
            source = Constants.LOCAL_PATH +exhibit.getMuseumId()+"/"+ FileUtil.changeUrl2Name(source);
        }
       /* if (!iconUrl.startsWith("http")) {
            iconUrl = Constants.LOCAL_PATH +exhibit.getMuseumId()+"/"+ FileUtil.changeUrl2Name(iconUrl);
        }*/
        // Since we don't have a unique ID in the server, we fake one using the hashcode of
        // the music source. In a real world app, this could come from the server.

        // Adding the music source to the MediaMetadata (and consequently using it in the
        // mediaSession.setMetadata) is not a good idea for a real world music app, because
        // the session metadata can be accessed by notification listeners. This is done in this
        // sample for convenience only.
        return new MediaMetadataCompat.Builder()
                // TODO: 2016/8/3 暂定 METADATA_KEY_ART_URI 放source museumId subtitle

                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, source)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, museumId)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, labels)
                /*.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)*/
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, iconUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_COMPILATION, texturl)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, content)
                .putString(MediaMetadataCompat.METADATA_KEY_COMPOSER, imgs)
               /*
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, title)


                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, title)*/

                /*.putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, trackNumber)
                .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, totalTrackCount)*/
                .build();
    }

    /**
     * Get an iterator over the list of genres
     *
     * @return genres
     */
    public Iterable<String> getMuseumIds() {
        if (mCurrentState != State.INITIALIZED) {
            return Collections.emptyList();
        }
        return mMusicListByMuseumId.keySet();
    }

}
