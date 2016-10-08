package com.qiang.qiangguide.biz;

import com.qiang.qiangguide.bean.Museum;

import org.altbeacon.beacon.Beacon;

import java.util.Collection;

/**
 * Created by Qiang on 2016/9/22.
 */
public interface IBeginBiz {


    Museum getMuseumByBeacons(Collection<Beacon> beacons);
}
