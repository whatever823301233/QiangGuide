package com.qiang.qiangguide;

import android.app.ActivityManager;
import android.app.Application;
import android.text.TextUtils;

import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.db.DBHandler;
import com.qiang.qiangguide.volley.QVolley;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.Iterator;

/**
 * Created by Qiang on 2016/7/12.
 */
public class QApplication extends Application implements BootstrapNotifier {

    private static QApplication instance;
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;


    @Override
    public void onCreate() {
        super.onCreate();
        if(!isSameAppName()){
            return;
        }
        instance=this;
        AppManager.getInstance(this).initApp("com.qiang.qiangguide",iAppListener);
        QVolley.getInstance(this);
        DBHandler.getInstance(this);
        initBluetoothFrame();
    }

    private IAppListener iAppListener=new IAppListener() {
        @Override
        public void destroy() {

        }
    };

    public static QApplication get(){
        return instance;
    }

    /**
     * 判断是否为相同app名
     *
     * @return
     */
    private boolean isSameAppName() {
        int pid = android.os.Process.myPid();
        String processAppName = getProcessAppName(pid);
        String packageName=getPackageName();
        if (TextUtils.isEmpty(processAppName) || !processAppName.equalsIgnoreCase(packageName)) {
            return false;
        }
        return true;
    }

    /**
     * 获取processAppName
     *
     * @param pid
     * @return
     */
    private String getProcessAppName(int pid) {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        Iterator<ActivityManager.RunningAppProcessInfo> iterator = activityManager.getRunningAppProcesses().iterator();
        while (iterator.hasNext()) {
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = iterator.next();
            try {
                if (runningAppProcessInfo.pid == pid) {
                    return runningAppProcessInfo.processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    private void initBluetoothFrame() {
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setForegroundScanPeriod(450);
        beaconManager.setBackgroundScanPeriod(450);
        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(Constants.BEACON_LAYOUT));//"m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"
        // wake up the app when a beacon is seen
        Region region = new Region("backgroundRegion", null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);
        // simply constructing this class and holding a reference to it in your custom Application
        // class will automatically cause the BeaconLibrary to save battery whenever the application
        // is not visible.  This reduces bluetooth power usage by about 60%
        backgroundPowerSaver = new BackgroundPowerSaver(this);
    }


    @Override
    public void didEnterRegion(Region region) {

    }

    @Override
    public void didExitRegion(Region region) {

    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {

    }
}
