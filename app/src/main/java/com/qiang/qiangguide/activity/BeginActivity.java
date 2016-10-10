package com.qiang.qiangguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.IBeginView;
import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.presenter.BeginPresenter;
import com.qiang.qiangguide.util.LogUtil;
import com.qiang.qiangguide.util.Utility;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class BeginActivity extends ActivityBase implements IBeginView,BeaconConsumer {


    private BeaconManager beaconManager;
    private BeginPresenter presenter;
    private Museum currentMuseum;
    private Intent targetIntent;

    private Region region = new Region(Constants.BEACON_LAYOUT, null, null, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);
        presenter = new BeginPresenter(this);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);
        presenter.onCreate();
    }

    @Override
    void errorRefresh() {

    }


    @Override
    public  void goToNextActivity(){
        try {
            beaconManager.stopRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            LogUtil.e("",e);
        }
        Utility.startActivity(getActivity(),targetIntent);
        finish();
    }

    @Override
    public void setCurrentMuseum(Museum museum) {
        this.currentMuseum = museum;
    }

    @Override
    public void setTargetIntent(Intent intent) {
        this.targetIntent = intent;
    }

    @Override
    public Museum getCurrentMuseum() {
        return currentMuseum;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.i("","onDestroy");
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if(beacons == null){
                    LogUtil.i("","返回搜索结果 beacons=null");
                    return;
                }
                LogUtil.i("","返回搜索结果 size = "+beacons.size());
                presenter.rangeBeaconsInRegion(beacons,region);
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(region);
            LogUtil.i("","已经开始搜索");
        } catch (RemoteException e) {
            LogUtil.e("",e);
        }

    }




}
