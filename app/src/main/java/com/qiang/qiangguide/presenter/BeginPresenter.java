package com.qiang.qiangguide.presenter;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.qiang.qiangguide.aInterface.IBeginView;
import com.qiang.qiangguide.activity.CityChooseActivity;
import com.qiang.qiangguide.activity.GuidePagerActivity;
import com.qiang.qiangguide.activity.MuseumHomeActivity;
import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.biz.IBeginBiz;
import com.qiang.qiangguide.biz.bizImpl.BeginBiz;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.config.GlobalConfig;
import com.qiang.qiangguide.manager.MyBluetoothManager;
import com.qiang.qiangguide.util.LogUtil;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Region;

import java.lang.ref.WeakReference;
import java.util.Collection;

/**
 * Created by Qiang on 2016/9/22.
 */
public class BeginPresenter {

    private IBeginBiz beginBiz;
    private IBeginView beginView;
    private boolean isFirstLogin;
    private String currentMuseumId;
    private static final int MSG_WHAT_CHANGE_ACTIVITY=1;
    private Handler handler;

    public BeginPresenter(IBeginView beginView){
        this.beginView=beginView;
        beginBiz=new BeginBiz();
        handler=new MyHandler(beginView);
    }

    public void rangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        Museum museum = beginBiz.getMuseumByBeacons(beacons);
        if(museum==null){
            LogUtil.i("","museum = null");
            return;
        }
        beginView.setCurrentMuseum(museum);
    }

    public void onCreate() {
        MyBluetoothManager.openBluetooth(beginView.getContext());

        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Class<?> targetClass;
                isFirstLogin= GlobalConfig.getInstance(beginView.getContext()).getBoolean(Constants.SP_NOT_FIRST_LOGIN,true);
                if(isFirstLogin){
                    GlobalConfig.getInstance(beginView.getContext()).putBoolean(Constants.SP_NOT_FIRST_LOGIN,false);
                    targetClass=GuidePagerActivity.class;
                }else{
                        /*默认跳转界面为城市选择*/
                    targetClass=CityChooseActivity.class;
                }
                if(!isFirstLogin){
                    if(beginView.getCurrentMuseum()!=null){
                        targetClass=MuseumHomeActivity.class;
                    }else{
                        LogUtil.i("","beginView.getCurrentMuseum()=null");
                    }
                }
                Intent intent =new Intent(beginView.getContext(),targetClass);
                if(beginView.getCurrentMuseum()!=null){
                    intent.putExtra(Constants.INTENT_MUSEUM,beginView.getCurrentMuseum());
                }
                beginView.setTargetIntent(intent);
                handler.sendEmptyMessage(MSG_WHAT_CHANGE_ACTIVITY);
            }
        }.start();
    }

    private static class MyHandler extends Handler {

        WeakReference<IBeginView> activityWeakReference;
        MyHandler(IBeginView activity){
            this.activityWeakReference=new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            if(activityWeakReference==null){return;}
            IBeginView activity=activityWeakReference.get();
            if(activity==null){return;}
            switch (msg.what){
                case MSG_WHAT_CHANGE_ACTIVITY:
                    activity.goToNextActivity();
                    break;
                default:break;
            }
        }
    }


}
