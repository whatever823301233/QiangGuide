package com.qiang.qiangguide.biz;

import com.qiang.qiangguide.beacon.OnBeaconCallback;
import com.qiang.qiangguide.bean.Exhibit;

import org.altbeacon.beacon.Beacon;

import java.util.Collection;

/**
 * Created by Qiang on 2016/8/19.
 */
public interface IPlayShowBiz {


    Exhibit getExhibit(String id);

    void findExhibits(String museumId, Collection<Beacon> beacons, OnInitBeanListener listener);

    void getExhibits(String museumId, Collection<Beacon> beacons, OnBeaconCallback listener);

}
