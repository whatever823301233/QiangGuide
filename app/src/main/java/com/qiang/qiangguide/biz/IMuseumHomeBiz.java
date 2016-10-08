package com.qiang.qiangguide.biz;

import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.bean.Label;
import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.bean.MyBeacon;

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

    List<Label> getLabels(String museumId);

    void saveLabels(List<Label> labelList);

    List<MyBeacon> getMyBeacons(String museumId);

    void saveBeacons(List<MyBeacon> beacons);

    void saveExhibit(List<Exhibit> exhibitList);
}
