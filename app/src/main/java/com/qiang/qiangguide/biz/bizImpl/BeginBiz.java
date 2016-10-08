package com.qiang.qiangguide.biz.bizImpl;

import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.bean.MyBeacon;
import com.qiang.qiangguide.biz.IBeginBiz;
import com.qiang.qiangguide.db.handler.BeaconHandler;
import com.qiang.qiangguide.db.handler.MuseumHandler;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Qiang on 2016/9/22.
 */
public class BeginBiz implements IBeginBiz{


    @Override
    public Museum getMuseumByBeacons(Collection<Beacon> beacons) {
        if(beacons==null||beacons.size()==0){return null;}
        List<Beacon> beaconList=new ArrayList<>(beacons);
        Beacon beacon=beaconList.get(0);
        if(beacon==null){return null;}
        MyBeacon mBeacon= BeaconHandler.queryBeacons(beacon.getId3(),beacon.getId2());
        if(mBeacon==null){return null;}
        String museumId = mBeacon.getMuseumId();
        return  MuseumHandler.queryMuseum(museumId);
    }
}
