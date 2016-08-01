package com.qiang.qiangguide.util;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.qiang.qiangguide.ResourceManager;
import com.qiang.qiangguide.config.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Qiang on 2016/7/13.
 */
public class FileUtil {

    private static final String TAG = "FileUtil";


    /**
     * 查看url对应文件是否存在
     * @param url
     * @param museumId
     * @return
     */
    public static boolean checkFileExists(String url,String museumId){
        if(TextUtils.isEmpty(museumId)){return false;}
        String name=changeUrl2Name(url);
        String path= Constants.LOCAL_PATH+museumId+"/"+name;
        File file=new File(path);
        return file.exists();
    }

    /**
     * 将url转换为文件名字
     * @param url
     * @return 文件名
     */
    public static String changeUrl2Name(String url){
        return url.replaceAll("/","_");
    }



    /**
     *
     * 读取assets文件
     *
     * @param context
     *            context对象
     * @param name
     *            读取文件名
     * @return String 读取信息
     * @since 1.0.0
     */
    public static String readAssetsFile(Context context, String name ) {

        if( context == null || TextUtils.isEmpty( name ) ) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        InputStream in;
        try {
            in = context.getResources().getAssets().open( name );
            BufferedReader br = new BufferedReader( new InputStreamReader( in ) );
            String tempStr;
            while( ( tempStr = br.readLine() ) != null ) {// 一行一行的读取
                sb.append( tempStr ).append( "\n" );
            }
            return sb.toString();
        } catch( IOException e ) {
            LogUtil.e( TAG, e.getMessage(), e );
        }
        return null;
    }

    /**
     *
     * 将bitmap图片保存到本地
     *
     * @param ctx
     *            context对象
     * @param bitmap
     *            图片对象
     * @return String 操作信息状态
     * @since 1.0.0
     */
    public static String saveToAlbum( Context ctx, Bitmap bitmap ) {

        ResourceManager mResourceManager = ResourceManager.getInstance( ctx );
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMdd_hhmmss" );
        ContentResolver cr = ctx.getContentResolver();
        String fileName = "IMG_" + sdf.format( new Date() ) + ".png";
        if( android.os.Environment.getExternalStorageDirectory() == null ) {
            return ctx.getResources().getString( mResourceManager.getStringId( "saveToAlbum_error_msg1" ) );
        }
        String url = MediaStore.Images.Media.insertImage( cr, bitmap, fileName, "" );

        if( url != null ) {
            MediaScannerConnection.scanFile( ctx,// 部分机器缓存更新不及时问题，该代码待测试，缩略图可能有此问题
                    new String[] { Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DCIM )
                            .getPath() + "/" + fileName }, null, null );
            return ctx.getResources().getString( mResourceManager.getStringId( "saveToAlbum_succ_msg" ) );
        } else {
            return ctx.getResources().getString( mResourceManager.getStringId( "saveToAlbum_error_msg2" ) );
        }
    }


}
