package com.qiang.qiangguide.biz;

import com.qiang.qiangguide.beacon.OnBeaconCallback;

import org.altbeacon.beacon.Beacon;

import java.util.Collection;

/**
 * Created by Qiang on 2016/8/10.
 */
public interface IMainGuideBiz {


    void findExhibits(String museumId, Collection<Beacon> beacons,OnInitBeanListener listener);

    void getExhibits(String museumId, Collection<Beacon> beacons, OnBeaconCallback listener);


}
