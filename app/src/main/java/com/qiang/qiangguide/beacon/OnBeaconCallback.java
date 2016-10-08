package com.qiang.qiangguide.beacon;

import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.bean.MyBeacon;

import java.util.List;

/**
 * Created by Qiang on 2016/9/28.
 */

public interface OnBeaconCallback {

    void getExhibits(List<Exhibit> exhibits);
    void getNearestExhibit(Exhibit exhibit);
    void getNearestBeacon(MyBeacon bean);

}
