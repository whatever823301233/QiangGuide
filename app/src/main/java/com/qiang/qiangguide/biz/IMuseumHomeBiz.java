package com.qiang.qiangguide.biz;

import com.qiang.qiangguide.bean.Museum;

import java.util.List;

/**
 * Created by Qiang on 2016/7/29.
 */
public interface IMuseumHomeBiz {

    List<String> getImgUrls(Museum museum);

    void downloadMuseumAudio(String audioUrl, String id,OnResponseListener listener);

    void getExhibitListByMuseumId(String museumId,OnInitBeanListener listener);

    void getExhibitListByMuseumIdNet(String museumId,String tag,OnInitBeanListener listener);


    void getBeaconListByNet(String museumId, OnInitBeanListener listener,String tag);

    void getLabelListByNet(String museumId, OnInitBeanListener onInitBeanListener, String tag);
}
