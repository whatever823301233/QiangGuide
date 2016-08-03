package com.qiang.qiangguide.util;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Qiang on 2016/7/14.
 */
public class BitmapCache implements ImageLoader.ImageCache {

    private static final String TAG = LogUtil.makeLogTag(BitmapCache.class);

    private static final int MAX_ALBUM_ART_CACHE_SIZE = 12*1024*1024;  // 12 MB

    private static final int BIG_BITMAP_INDEX = 0;
    private static final int ICON_BITMAP_INDEX = 1;

    private final LruCache<String, Bitmap> mCache;

    private static final BitmapCache sInstance = new BitmapCache();

    public static BitmapCache getInstance() {
        return sInstance;
    }

    private BitmapCache() {
        // Holds no more than MAX_ALBUM_ART_CACHE_SIZE bytes, bounded by maxmemory/4 and
        // Integer.MAX_VALUE:
        int maxSize = Math.min(MAX_ALBUM_ART_CACHE_SIZE,
                (int) (Math.min(Integer.MAX_VALUE, Runtime.getRuntime().maxMemory()/4)));
        mCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return  bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url, bitmap);
    }




    /*

     public static abstract class FetchImageListener {

        public abstract void onFetched(String artUrl, Bitmap bigImage, Bitmap iconImage);
        public void onError(String artUrl, Exception e) {
            LogUtil.e(TAG, e, "AlbumArtFetchListener: error while downloading " + artUrl);
        }
    }


    // Resolution reasonable for carrying around as an icon (generally in
    // MediaDescription.getIconBitmap). This should not be bigger than necessary, because
    // the MediaDescription object should be lightweight. If you set it too high and try to
    // serialize the MediaDescription, you may get FAILED BINDER TRANSACTION errors.

    private static final int MAX_ART_WIDTH_ICON = 128;  // pixels
    private static final int MAX_ART_HEIGHT_ICON = 128;  // pixels

    private static final int MAX_ART_WIDTH = 800;  // pixels
    private static final int MAX_ART_HEIGHT = 480;  // pixels

    public void fetch(final String artUrl, final FetchImageListener listener) {
         // WARNING: for the sake of simplicity, simultaneous multi-thread fetch requests
         // are not handled properly: they may cause redundant costly operations, like HTTP
         // requests and bitmap rescales. For production-level apps, we recommend you use
         // a proper image loading library, like Glide.
         Bitmap[] bitmap = mCache.get(artUrl);
         if (bitmap != null) {
             LogUtil.d(TAG, "getOrFetch: album art is in cache, using it", artUrl);
             listener.onFetched(artUrl, bitmap[BIG_BITMAP_INDEX], bitmap[ICON_BITMAP_INDEX]);
             return;
         }
         LogUtil.d(TAG, "getOrFetch: starting asynctask to fetch ", artUrl);

         new AsyncTask<Void, Void, Bitmap[]>() {
             @Override
             protected Bitmap[] doInBackground(Void[] objects) {
                 Bitmap[] bitmaps;
                 try {
                     Bitmap bitmap = BitmapUtil.fetchAndRescaleBitmap(artUrl, MAX_ART_WIDTH, MAX_ART_HEIGHT);
                     Bitmap icon = BitmapUtil.scaleBitmap(bitmap, MAX_ART_WIDTH_ICON, MAX_ART_HEIGHT_ICON);
                     bitmaps = new Bitmap[] {bitmap, icon};
                     mCache.put(artUrl, bitmaps);
                 } catch (IOException e) {
                     return null;
                 }
                 LogUtil.d(TAG, "doInBackground: putting bitmap in cache. cache size=" +
                         mCache.size());
                 return bitmaps;
             }

             @Override
             protected void onPostExecute(Bitmap[] bitmaps) {
                 if (bitmaps == null) {
                     listener.onError(artUrl, new IllegalArgumentException("got null bitmaps"));
                 } else {
                     listener.onFetched(artUrl, bitmaps[BIG_BITMAP_INDEX], bitmaps[ICON_BITMAP_INDEX]);
                 }
             }
         }.execute();
     }*/


}
