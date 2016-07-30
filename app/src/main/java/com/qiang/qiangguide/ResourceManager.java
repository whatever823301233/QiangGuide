package com.qiang.qiangguide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;

import com.qiang.qiangguide.config.GlobalConfig;
import com.qiang.qiangguide.util.LogUtil;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Qiang on 2016/7/13.
 */
public class ResourceManager {

    private static final String TAG = ResourceManager.class.getName();

    private static volatile ResourceManager sInstance;
    private Context mContext;

    private String mSkinId;
    private String mResPath;
    private String mDrawablePath;
    private String mImgCachePath;

    private HashMap<String, String> mDrawableMap;

    private WeakHashMap<Integer, Drawable> mDrawableCacheMap;
    private WeakHashMap<String, Bitmap> mBitmapCacheMap;

    public static final String DEFAULT_SKIN_ID = "default";
    private static final String SHARED_SKIN_ID = "skin_id";
    public static final String HUILIFE_FOLDER = "drawable/huilife";


    private ResourceManager( Context context ) {

        mContext = context.getApplicationContext();

        mDrawableCacheMap = new WeakHashMap<>();
        mBitmapCacheMap = new WeakHashMap<>();

        mSkinId = GlobalConfig.getInstance( mContext ).getString( SHARED_SKIN_ID, null );
        init( mSkinId );
    }


    public static ResourceManager getInstance( Context context ) {

        if( context == null ) {
            return null;
        }
        if( sInstance == null ) {
            synchronized( ResourceManager.class ) {
                if( sInstance == null ) {
                    sInstance = new ResourceManager( context );
                }
            }
        }
        return sInstance;
    }


    public int getLayoutId( String layoutName ) {

        return mContext.getResources().getIdentifier( layoutName, "layout", mContext.getPackageName() );
    }


    public int getStringId( String name ) {

        return mContext.getResources().getIdentifier( name, "string", mContext.getPackageName() );
    }


    public int getStyleId( String name ) {

        return mContext.getResources().getIdentifier( name, "style", mContext.getPackageName() );
    }


    public int getColorId( String name ) {

        return mContext.getResources().getIdentifier( name, "color", mContext.getPackageName() );
    }


    public int getDrawableId( String name ) {

        return mContext.getResources().getIdentifier( name, "drawable", mContext.getPackageName() );
    }


    public int getDimenId( String name ) {

        return mContext.getResources().getIdentifier( name, "dimen", mContext.getPackageName() );
    }


    public int getArrayId( String name ) {

        return mContext.getResources().getIdentifier( name, "array", mContext.getPackageName() );
    }


    public int getAnimId( String name ) {

        return mContext.getResources().getIdentifier( name, "anim", mContext.getPackageName() );
    }


    public int getId( String idName ) {

        return mContext.getResources().getIdentifier( idName, "id", mContext.getPackageName() );
    }


    public int getRawId( String name ) {

        return mContext.getResources().getIdentifier( name, "raw", mContext.getPackageName() );
    }


    public int getStyleable( String name ) {

        return (int)getResourceId( name, "styleable" );
    }


    public int[] getStyleableArray( String name ) {

        return ( int[] )getResourceId( name, "styleable" );
    }


    /**
     *
     * 对于context.getResources().getIdentifier无法获取的数据或者数组，采用资源反射值
     *
     * @param name
     * @param type
     * @since 1.0.0
     * @return
     */
    private Object getResourceId( String name, String type ) {

        String className = mContext.getPackageName() + ".R";
        try {
            Class<?> cls = Class.forName( className );
            for( Class<?> childClass : cls.getClasses() ) {
                String simple = childClass.getSimpleName();
                if( simple.equals( type ) ) {
                    for( Field field : childClass.getFields() ) {
                        String fieldName = field.getName();
                        if( fieldName.equals( name ) ) {
                            return field.get( null );
                        }
                    }
                }
            }
        } catch( Exception e ) {
            LogUtil.e( TAG, e.getMessage(), e );
        }

        return null;
    }


    public String getResPath() {

        return mResPath;
    }


    public String getImageCachePath() {

        return mImgCachePath;
    }


    public String getSkinId() {

        return mSkinId;
    }


    public void reInit( String skinId ) {

        clearCache();
        init( skinId );
    }


    private void init( String skinId ) {

        if( TextUtils.isEmpty( skinId )  || DEFAULT_SKIN_ID.equals( skinId ) ) {
            mResPath = mContext.getFilesDir().getAbsolutePath() + "/" + DEFAULT_SKIN_ID + "/";
            mSkinId = null;
        } else {
            mResPath = mContext.getFilesDir().getAbsolutePath() + "/" + skinId + "/";

            mSkinId = skinId;
        }

        mDrawablePath = mResPath + "drawable/";
        mImgCachePath = mResPath + "drawable/image_cache/";

        GlobalConfig.getInstance( mContext ).getString( SHARED_SKIN_ID, skinId );

        File imgCacheFile = new File( mImgCachePath );
        if( !imgCacheFile.exists() ) {
            imgCacheFile.mkdirs();
        }
    }


    private void parseColor( String xmlPath ) {

        if( xmlPath == null ) {
            return;
        }

        File file = new File( xmlPath );
        if( file.exists() ) {
            HashMap<String, String> colorMap = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            // InputStream is = fil
        }

    }


    /**
     *
     * 根据Drawable名字获取Drawable
     *
     * @param id
     * @since 1.1.0

    public Drawable getDrawable( String name ) {

        if( !TextUtils.isEmpty( name ) ) {
            return ImageCache.getInstance( mContext ).getDrawableByPath( getImageUrl( name ) );
        }
        return null;
    }*/


    public Drawable getDrawable( int id ) {

        if( mContext == null ) {
            return null;
        }

        Drawable drawable = null;
        // 魅族手机从内存中获取图片，在切换activity时会导致图片大小失常，因此如果是魅族手机则每次都从本地读取图片
        if( !"Meizu".endsWith( android.os.Build.MANUFACTURER ) ) {
            drawable = mDrawableCacheMap.get( id );
            // 如果cache中有，则直接使用cache
            if( drawable != null ) {
                return drawable;
            }
        }

        if( mDrawablePath != null ) {
            Resources resources = mContext.getResources();
            String fileName = resources.getResourceEntryName( id );
            if( fileName != null ) {
                String path = mDrawablePath + fileName + ".png";
                File file = new File( path );
                if( file.exists() ) {
                    drawable = Drawable.createFromPath( path );
                } else {
                    String ninthpath = mDrawablePath + fileName + ".9.png";
                    file = new File( ninthpath );
                    if( file.exists() ) {
                        drawable = getNinePatchDrawable( file );
                    }
                }
            }
        }

        if( drawable == null ) {
            drawable = mContext.getResources().getDrawable( id );
        } else {
            mDrawableCacheMap.put( id, drawable );
        }

        return drawable;

    }


    public Drawable getNinePatchDrawable( File file ) {

        if( file == null || !file.exists() ) {
            return null;
        }

        try {
            InputStream in = new FileInputStream( file );
            Bitmap bitmap = BitmapFactory.decodeStream( in );
            byte[] chunk = bitmap.getNinePatchChunk();
            if( NinePatch.isNinePatchChunk( chunk ) ) {
                return new NinePatchDrawable( bitmap, chunk, new Rect(), null );
            }
        } catch( FileNotFoundException e ) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param
     * @return

    public Bitmap getBitmap( String name ) {

        if( mBitmapCacheMap != null ) {
            String url = getImageUrl( name );
            if( TextUtils.isEmpty( url ) ) {
                String key = ImageCache.hashKeyForDisk( url );
                Bitmap bitmap = mBitmapCacheMap.get( key );
                if( bitmap == null ) {
                    String path = mDrawablePath + name;
                    File file = new File( path );
                    if( file.exists() ) {
                        bitmap = BitmapFactory.decodeFile( path );
                        if( bitmap != null ) {
                            mBitmapCacheMap.put( key, bitmap );
                        }
                    }
                }

                return bitmap;
            }
        }
        return null;
    }*/


    public void setBitmap( String key, Bitmap bitmap ) {

        if( mBitmapCacheMap != null ) {
            mBitmapCacheMap.put( key, bitmap );
        }
    }

    /**
     *
     * @param name

    public void removeBitmap( String name ) {

        if( mBitmapCacheMap != null ) {
            String key = ImageCache.hashKeyForDisk( getImageUrl( name ) );
            if( !TextUtils.isEmpty( key ) ) {
                Bitmap bitmap = mBitmapCacheMap.get( key );
                if( bitmap != null ) {
                    bitmap.recycle();
                    try {
                        mBitmapCacheMap.remove( key );
                    } catch( UnsupportedOperationException e ) {
                        LogUtil.e( TAG, e.getMessage(), e );
                    }
                }
            }
        }
    }*/

    /**
     *
     * @param name
     * @return

    public String getImageUrl( String name ) {

        String prefixUrl = RemoteMenuManager.getInstance( mContext ).getImageUrl();
        if( TextUtils.isEmpty( prefixUrl ) ) {
            return null;
        }

        String url = null;
        if( !TextUtils.isEmpty( mSkinId ) && !Skin.DEFAULT_SKIN_ID.equals( mSkinId )
                && !mSkinId.equals( ResourceManager.DEFAULT_SKIN_ID ) ) {
            url = prefixUrl + "/" + mSkinId + "/" + name;
        } else {
            url = prefixUrl + "/" + name;
        }

        return url;
    }*/


    @SuppressLint( "NewApi" )
    public void setBackgroundResource(View view, int resId ) {

        if( view != null ) {
            Drawable background = getDrawable( resId );
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
                view.setBackground( background );
            } else {
                view.setBackgroundDrawable( background );
            }
        }
    }


    public void setImageResource(ImageView view, int resId ) {

        if( view != null ) {
            Drawable background = getDrawable( resId );
            view.setImageDrawable( background );
        }
    }

    /**
     *

    public void setImageCacheResource( ImageView view, String name, int defaultImgId ) {

        if( view != null ) {
            Bitmap bitmap = getBitmap( name );
            if( bitmap == null ) {
                ImageCache cacheUtil = ImageCache.getInstance( mContext );
                if( defaultImgId == 0 ) {
                    cacheUtil.displayImage( getImageUrl( name ), view, getDrawableId( "fw_ic_default_img" ) );
                } else {
                    cacheUtil.displayImage( getImageUrl( name ), view, defaultImgId );
                }
            } else {
                view.setImageBitmap( bitmap );
            }
        }
    }*/


    private int getColor( int resid ) {

        if( mContext == null ) {
            return -1;
        }

        Resources resources = mContext.getResources();
        String colorName = resources.getResourceEntryName( resid );
        if( colorName != null ) {

        }
        return resources.getColor( resid );
    }


    public void clearCache() {

        if( mDrawableCacheMap != null ) {
            mDrawableCacheMap.clear();
        }

        if( mBitmapCacheMap != null ) {
            mBitmapCacheMap.clear();
        }

        if( mDrawableMap != null ) {
            mDrawableMap.clear();
        }

        if( mDrawableCacheMap != null ) {
            for( Map.Entry<Integer, Drawable> entry : mDrawableCacheMap.entrySet() ) {
                if( entry != null && entry.getValue() != null ) {
                    entry.getValue().setCallback( null );
                }

            }
            mDrawableCacheMap.clear();
        }
    }


    public void destroy() {

        clearCache();
        mBitmapCacheMap = null;
        mDrawableMap = null;
        mDrawableCacheMap = null;
        sInstance = null;
    }


    /**
     *
     * 清除历史图片，一般用于升级后避免图片错乱时使用
     *
     * @param handler
     * @permission void
     * @exception
     * @since 1.0.0

    public void clearHistory( Handler handler ) {

        new ClearHistoryThread( mContext, handler ).start();
    }


    private class ClearHistoryThread extends Thread {

        private Context mContext;
        private Handler mHandler;


        public ClearHistoryThread( Context context, Handler handler ) {

            mContext = context;
            mHandler = handler;
        }


        @Override
        public void run() {

            String preSkinId = GlobalConfig.getInstance( mContext ).getString( SHARED_SKIN_ID, null );
            if( !TextUtils.isEmpty( preSkinId ) ) {
                GlobalConfig.getInstance( mContext ).getString( SHARED_SKIN_ID, null );
                ResourceManager manager = ResourceManager.getInstance( mContext );
                FileUtil.deleteFile( manager.getResPath() );
                manager.reInit( null );
            }

            try {
                DBHandler.getInstance( mContext ).deleteTable( SkinDbHandler.getTableInfo().getTableName() );
            } catch( IOException e ) {
                LogUtil.e( TAG, e.getMessage(), e );
            }

            mHandler.sendEmptyMessage( FwConstants.MSG_CLEAR_HISTORY_SKIN );
        }
    }*/

}
