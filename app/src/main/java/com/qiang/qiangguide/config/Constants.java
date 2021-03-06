package com.qiang.qiangguide.config;

import android.os.Environment;

import com.qiang.qiangguide.QApplication;

/**
 * Created by xq823 on 2016/7/30.
 */
public class Constants {

    public static final String SP_NOT_FIRST_LOGIN="sp_not_first_login";//是否首次登陆

    public static final String SP_MUSEUM_ID="sp_museum_id";//博物馆id
    public static final String SP_DOWNLOAD_MUSEUM_COUNT="sp_download_museum_count";//下载博物馆数据个数
    public static final String SP_IS_IN_MUSEUM="sp_is_in_museum";//是否在博物馆


    public static final String SP_IS_AUTO_MUSEUM="sp_is_auto_museum";//是否在博物馆
    public static final String SP_IS_AUTO_GPS="SP_IS_AUTO_GPS";//是否在博物馆
    public static final String SP_IS_AUTO_UPDATE="sp_is_auto_update";//是否在博物馆

    public static final String BEACON_LAYOUT="m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";


    public static final String INTENT_CITY="intent_city";
    public static final String INTENT_MUSEUM="intent_museum";
    public static final String INTENT_MUSEUM_ID="intent_museum_id";
    public static final String INTENT_EXHIBIT="intent_exhibit";


    /**本地文件位置*/

    public static final String APP_ROOT= QApplication.get().getFilesDir().getAbsolutePath();//存储至本地sdcard位置
    public static final String SDCARD_ROOT= Environment.getExternalStorageDirectory().getAbsolutePath();//存储至本地sdcard位置
    //String LOCAL_ASSETS_PATH=SDCARD_ROOT+"/Guide/";//sdcard存储图片的位置*/
    public static final String LOCAL_PATH= APP_ROOT+"/";//app内部存储图片的位置*/


    /*关于URL*/
    public static final String BASE_URL ="http://182.92.82.70";

    public static final String URL_CITY_LIST=BASE_URL+"/api/cityService/cityList"; //城市路径

    public static final String URL_MUSEUM_LIST=BASE_URL+"/api/museumService/museumList";//todo city下博物馆列表

    public static final String URL_EXHIBIT_LIST="http://182.92.82.70/api/exhibitService/exhibitList?museumId="; //博物馆下展品列表*/

    public static final String URL_BEACON_LIST="http://182.92.82.70/api/beaconService/beaconList?museumId="; //博物馆下信标列表*/

    public static final String URL_LABEL_LIST ="http://182.92.82.70/api/labelsService/labelsList?museumId=";

    public static final String URL_ASSETS_LIST ="http://182.92.82.70/api/assetsService/assetsList?museumId=";

    public static final int  STYLE_GREEN = 110;
    public static final int  STYLE_BLUE = 111;
    public static final int  STYLE_PURPLE = 112;
    public static final int  STYLE_RED = 113;
}
