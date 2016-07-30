package com.qiang.qiangguide.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Qiang on 2016/7/13.
 */
public class MD5Util {

    private static final String HASH_ALGORITHM = "MD5";
    private static final int RADIX = 10 + 26; // 10 digits + 26 letters
    private static final String TAG = "MD5Util";

    /**
     *
     *  获得MD5值	  
     * @param data	二进制编码数据
     * @return
     *String  数据MD5值
     * @exception  	NoSuchAlgorithmException
     * @since  1.0.0
     */
    public static String getMD5( byte[] data ) throws Exception {

        try {
            MessageDigest digest = MessageDigest.getInstance( HASH_ALGORITHM );
            digest.update( data );
            byte[] hash = digest.digest();
            BigInteger bi = new BigInteger( hash ).abs();
            return bi.toString( RADIX );
        } catch( NoSuchAlgorithmException e ) {
            LogUtil.e( TAG, e.getMessage(), e );
        }
        return "";
    }

    /**
     *
     * 获得MD5值	    
     * @param str	编码数据
     * @return
     *String  数据MD5值
     * @since  1.0.0
     */
    public static String md5( String str ) throws Exception {

        return MD5Util.getMD5( str.getBytes() );
    }



    /**
     *
     * 获得文件MD5值 
     * @param fileName	文件名
     * @return
     *String  文件MD5值
     * @exception   FileNotFoundException	<p>
     * 				NoSuchAlgorithmException	<p>
     * 				IOException
     * @since  1.0.0
     */
    public static String getFileMD5( String fileName ) {

        InputStream fis = null;
        String md5 = null;

        try {
            fis = new FileInputStream( fileName );
            byte[] buffer = new byte[ MIN_LENGTH ];
            MessageDigest messagedigest = MessageDigest.getInstance( "MD5" );
            int numRead = 0;
            while( ( numRead = fis.read( buffer ) ) > 0 ) {
                messagedigest.update( buffer, 0, numRead );
            }
            md5 = toHexString( messagedigest.digest() );
        } catch( FileNotFoundException e ) {
            e.printStackTrace();
        } catch( NoSuchAlgorithmException e ) {
            e.printStackTrace();
        } catch( IOException e ) {
            e.printStackTrace();
        } finally {
            if( fis != null ) {
                try {
                    fis.close();
                } catch( IOException e ) {
                    e.printStackTrace();
                }
            }
        }
        return md5;
    }

    /**
     *
     * 转换成16进制数据
     * @param b	待转换数据
     * @return
     *String  16进制数据
     * @since  1.0.0
     */
    private static String toHexString( byte[] b ) {

        StringBuilder sb = new StringBuilder( b.length * 2 );
        for( int i = 0; i < b.length; i++ ) {
            sb.append( hexChar[ ( b[ i ] & 0xf0 ) >>> 4 ] );
            sb.append( hexChar[ b[ i ] & 0x0f ] );
        }
        return sb.toString();
    }


    private static char hexChar[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * the minimal file size is 256k
     */
    private final static int MIN_LENGTH = 1024;

}
