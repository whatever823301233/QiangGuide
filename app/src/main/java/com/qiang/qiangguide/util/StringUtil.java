package com.qiang.qiangguide.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Qiang on 2016/7/13.
 *
 * 字符串工具类
 */
public class StringUtil {

    private static DecimalFormat fmt = new DecimalFormat( "##,###,###,##0.00" );

    /**
     *
     * 格式化字符串
     * （精确到小数点后两位，最大支持到99,999,999,999.99）
     * @param str	待格式化字符串
     * @return
     *String  格式化后字符串
     * @since  1.0.0
     */
    public static String parseFormatStr( String str ) {

        String result = "";
        try {
            result = "" + fmt.parse( str );
        } catch( ParseException e ) {
            LogUtil.e( "StringUtil", "parse the double error!", e );
        }
        return result;
    }

    /**
     *
     * 格式化字符串  
     * @param str	待格式化字符串
     * @param df	自定义格式
     * @return
     *String  格式化后字符串
     * @since  1.0.0
     */
    public static String parseFormatStr( String str, DecimalFormat df ) {

        String result = "";
        try {
            result = "" + df.parse( str );
        } catch( ParseException e ) {
            LogUtil.e( "StringUtil", "parse the double error!", e );
        }
        return result;
    }


    /**
     *
     * 字符串星号化
     * @param s	待星号化字符串
     * @return
     *String  星号化后字符串 
     * @since  1.0.0
     */
    public static String ZhuanHuanStar( String s ) {

        if( !TextUtils.isEmpty( s ) ) {

            String first = s.substring( 0, 3 );
            String end = s.substring( 7 );
            String all = first + "****" + end;
            return all;
        }
        return "";
    }


    /**
     *
     * 使用java正则表达式去掉多余的.与0 
     * @param s	待处理字符串
     * @return
     *String  处理后字符串
     * @since  1.0.0
     */
    public static String subZeroAndDot( String s ) {

        if( s.indexOf( "." ) > 0 ) {
            s = s.replaceAll( "0+?$", "" );// 去掉多余的0
            s = s.replaceAll( "[.]$", "" );// 如最后一位是.则去掉
        }
        return s;
    }


    /**
     *
     * 将字符串转化为int型
     * @param s	待转化字符串
     * @return
     *int  转化后字符串
     * @since  1.0.0
     */
    public static int string2Int( String s ) {

        if( TextUtils.isEmpty( s ) )
            return 0;
        int result = 0;
        try {
            result = Integer.parseInt( s );
        } catch( Exception e ) {
            e.printStackTrace();
        }
        return result;
    }



    /**
     *
     * 对数字转化为字符串  
     * @param value	待处理数字
     * @param num	保留小数位数
     * @param isMakeup	如果小数点后面的位数小于要保留小数点位数，是否补0
     * @return
     *String	转化后字符串  
     * @since  1.0.0
     */
    public static String RoundOf( double value, int num, boolean isMakeup ) {

        return fmt.format( value );
    }


    /**
     * 对数字进行四舍五入处理
     *
     * @param str   待处理字符串
     * @param num	 保留小数位数
     * @param isMakeup	如果小数点后面的位数小于要保留小数点位数，是否补0
     * @return
     * String	处理后字符串
     */
    public static String RoundOf( String str, int num, boolean isMakeup ) {

        // 对数字字符串进行四舍五入处理
        str = RoundOf( str, num );

        // 取得小数点后面的数字字符串
        String str1 = str.substring( str.indexOf( "." ) + 1, str.length() );
        // 如果小数点后面的位数小于要保留小数点位数
        if( str1.length() < num ) {
            if( isMakeup ) {
                for( int n = 0; n < ( num - str1.length() ); n++ ) {
                    str = str + "0";
                }
            }
        }

        return str;
    }


    /**
     * 将数字四舍五入，保留scale精度，转化为字符串
     *
     * @param str	处理参数
     * @param scale	保留小数位数
     * @return
     * String	转化后字符串
     * @exception	IllegalArgumentException
     */
    public static String RoundOf( String str, int scale ) {

        // 输入精度小于0则抛出异常
        if( scale < 0 ) {
            throw new IllegalArgumentException( "The scale must be a positive integer or zero" );
        }

        // 取得数值
        BigDecimal b = new BigDecimal( str );
        // 取得数值1
        BigDecimal one = new BigDecimal( "1" );
        // 原始值除以1，保留scale位小数，进行四舍五入
        return b.divide( one, scale, BigDecimal.ROUND_HALF_UP ).toString();
    }

    public static double todouble(String str){

        Number number = null;
        try {
            number = NumberFormat.getNumberInstance(Locale.FRENCH).parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        double d= 0;
        if (number != null) {
            d = number.doubleValue();
        }
        return d;

    }

}
