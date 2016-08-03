package com.qiang.qiangguide.biz;

import android.os.AsyncTask;
import android.support.v4.media.MediaMetadataCompat;

import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.db.DBHandler;
import com.qiang.qiangguide.util.FileUtil;
import com.qiang.qiangguide.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

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

    public static final String CUSTOM_METADATA_TRACK_SOURCE = "__SOURCE__";

    private static final String JSON_MUSIC = "music";
    private static final String JSON_TITLE = "title";
    private static final String JSON_ALBUM = "album";
    private static final String JSON_ARTIST = "artist";
    private static final String JSON_GENRE = "genre";
    private static final String JSON_SOURCE = "source";
    private static final String JSON_IMAGE = "image";
    private static final String JSON_TRACK_NUMBER = "trackNumber";
    private static final String JSON_TOTAL_TRACK_COUNT = "totalTrackCount";
    private static final String JSON_DURATION = "duration";

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
       // mMusicListByGenre = new ConcurrentHashMap<>();
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

   /* private synchronized void buildListsByGenre() {
        ConcurrentMap<String, List<Exhibit>> newMusicListByGenre = new ConcurrentHashMap<>();

        for (Exhibit m : mMusicListById.values()) {
            String genre = m.metadata.getString(MediaMetadataCompat.METADATA_KEY_GENRE);
            List<Exhibit> list = newMusicListByGenre.get(genre);
            if (list == null) {
                list = new ArrayList<>();
                newMusicListByGenre.put(genre, list);
            }
            list.add(m);
        }
        mMusicListByGenre = newMusicListByGenre;
    }*/

    private synchronized void buildListsByMuseumId() {
        ConcurrentMap<String, List<MediaMetadataCompat>> newMusicListByMuseumId = new ConcurrentHashMap<>();

        for (MediaMetadataCompat m : mMusicListById.values()) {
            String museumId = m.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID);
            List<MediaMetadataCompat> list = newMusicListByMuseumId.get(museumId);
            if (list == null) {
                list = new ArrayList<>();
                newMusicListByMuseumId.put(museumId, list);
            }
            list.add(m);
        }
        //mMusicListByGenre = newMusicListByMuseumId;
        mMusicListByMuseumId = newMusicListByMuseumId;
    }

    private synchronized void retrieveMedia() {
        try {
            if (mCurrentState == State.NON_INITIALIZED) {
                mCurrentState = State.INITIALIZING;
                List<Exhibit> exhibitList=DBHandler.getInstance(null).queryAllExhibitList();
                if (exhibitList != null) {
                    for (int j = 0; j < exhibitList.size(); j++) {
                        MediaMetadataCompat item = buildFromExhibit(exhibitList.get(j));
                        String musicId = item.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID);
                        mMusicListById.put(musicId, exhibitList.get(j).metadata);
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


        String title = exhibit.getName();
        //String album = exhibit.getIconurl();
        //String artist = json.getString(JSON_ARTIST);
        //String genre = json.getString(JSON_GENRE);
        String source = exhibit.getAudiourl();
        String iconUrl = exhibit.getIconurl();
        //int trackNumber = json.getInt(JSON_TRACK_NUMBER);
        //int totalTrackCount = json.getInt(JSON_TOTAL_TRACK_COUNT);
        //int duration = json.getInt(JSON_DURATION) * 1000; // ms

        LogUtil.d(TAG, "Found music track: " +exhibit.toString());

        if (!source.startsWith("http")) {
            source = Constants.LOCAL_PATH +exhibit.getMuseumId()+"/"+ FileUtil.changeUrl2Name(source);
        }
        if (!iconUrl.startsWith("http")) {
            iconUrl = Constants.LOCAL_PATH +exhibit.getMuseumId()+"/"+ iconUrl;
        }
        // Since we don't have a unique ID in the server, we fake one using the hashcode of
        // the music source. In a real world app, this could come from the server.
        String id = exhibit.getId();

        // Adding the music source to the MediaMetadata (and consequently using it in the
        // mediaSession.setMetadata) is not a good idea for a real world music app, because
        // the session metadata can be accessed by notification listeners. This is done in this
        // sample for convenience only.
        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, source)// TODO: 2016/8/3 暂定 METADATA_KEY_ART_URI 放source

                //.putString(CUSTOM_METADATA_TRACK_SOURCE, source)// TODO: 2016/8/2
                /*.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, genre)*/
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, iconUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                /*.putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, trackNumber)
                .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, totalTrackCount)*/
                .build();
    }
    private MediaMetadataCompat buildFromJSON(JSONObject json, String basePath) throws JSONException {
        String title = json.getString(JSON_TITLE);
        String album = json.getString(JSON_ALBUM);
        String artist = json.getString(JSON_ARTIST);
        String genre = json.getString(JSON_GENRE);
        String source = json.getString(JSON_SOURCE);
        String iconUrl = json.getString(JSON_IMAGE);
        int trackNumber = json.getInt(JSON_TRACK_NUMBER);
        int totalTrackCount = json.getInt(JSON_TOTAL_TRACK_COUNT);
        int duration = json.getInt(JSON_DURATION) * 1000; // ms

        LogUtil.d(TAG, "Found music track: " +json);

        // Media is stored relative to JSON file
        if (!source.startsWith("http")) {
            source = basePath + source;
        }
        if (!iconUrl.startsWith("http")) {
            iconUrl = basePath + iconUrl;
        }
        // Since we don't have a unique ID in the server, we fake one using the hashcode of
        // the music source. In a real world app, this could come from the server.
        String id = String.valueOf(source.hashCode());

        // Adding the music source to the MediaMetadata (and consequently using it in the
        // mediaSession.setMetadata) is not a good idea for a real world music app, because
        // the session metadata can be accessed by notification listeners. This is done in this
        // sample for convenience only.
        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
                //.putString(CUSTOM_METADATA_TRACK_SOURCE, source)// TODO: 2016/8/2
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, genre)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, iconUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, trackNumber)
                .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, totalTrackCount)
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
        return mMusicListById.keySet();
    }
    /**
     * Get an iterator over the list of genres
     *
     * @return genres

    public Iterable<String> getGenres() {
        if (mCurrentState != State.INITIALIZED) {
            return Collections.emptyList();
        }
        return mMusicListByGenre.keySet();
    } */

}
