package com.qiang.qiangguide.config;

import android.content.Context;

import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.custom.channel.ChannelItem;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Qiang on 2016/7/13.
 *
 * 应用级的pref缓存
 */
public class GlobalConfig extends Config{

    private static final String CONFIG_FILE_NAME = "global_config";

    private static volatile GlobalConfig mInstance;
    private List<ChannelItem> channelList;
    private String currentAreaId;

    private HashMap<String,Integer> visitAreaCount = new HashMap<>();



    private GlobalConfig( Context context, String configFileName ) {

        super( context, configFileName );
    }

    private GlobalConfig() {
    }


    /**
     * 获取单例对象
     *
     * @param context
     * @return GlobalConfig
     * @since 1.0.0
     */
    public static GlobalConfig getInstance( Context context ) {

        if( mInstance == null ) {
            synchronized( GlobalConfig.class ) {
                if( mInstance == null ) {
                    mInstance = new GlobalConfig( context, CONFIG_FILE_NAME );
                }
            }
        }
        return mInstance;
    }


    public void saveUserChannelList(List<ChannelItem> channelItems){
        this.channelList=channelItems;
    }

    public List<ChannelItem> getUserChannelList(){
        return channelList;
    }




    /*
     * 销毁单例，建议在应用退出的时候调用
     */
    @Override
    public void destroy() {

        super.destroy();
        mInstance = null;
    }


    /**
     * 检查是否需要禁止播放
     * @param currentAreaId 当前区域id
     * @return 是否阻止
     */
   /* public boolean checkNeedForbidPlay( String currentAreaId ){
        if ( currentAreaId == null) { return false; }
        if ( ! visitAreaCount.containsKey( currentAreaId ) ) {
            visitAreaCount.put( currentAreaId, 1 );
            return false;
        }else{
            int count = visitAreaCount.get( currentAreaId );
            if( count < 3 ){
                count ++;
                visitAreaCount.put( currentAreaId,count );
                return false;
            }else{
                return true;
            }
        }
    }*/


    /**
     * 检查是否需要禁止播放
     * @param exhibit 当前展品
     * @return 是否阻止
     */
    public boolean checkNeedForbidPlay( Exhibit exhibit ){
        if( exhibit == null ){ return false; }
        if( currentAreaId == null ){
            currentAreaId = exhibit.getAreaRoomId();
            visitAreaCount.put( currentAreaId, 1 );
            return false;
        }else {
            int count = visitAreaCount.get( currentAreaId );
            if( count < 3 ){
                count ++;
                visitAreaCount.put( currentAreaId,count );
                return false;
            }else{
                return true;
            }
        }
    }



}
