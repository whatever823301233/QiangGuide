package com.qiang.qiangguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.config.GlobalConfig;
import com.qiang.qiangguide.util.Utility;

import java.lang.ref.WeakReference;

public class BeginActivity extends ActivityBase {


    private Class<?> targetClass;
    private boolean isFirstLogin;
    private String currentMuseumId;
    private static final int MSG_WHAT_CHANGE_ACTIVITY=1;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);
        handler=new MyHandler(this);
        initData();
    }

    private void initData() {
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isFirstLogin= GlobalConfig.getInstance(getActivity()).getBoolean(Constants.SP_NOT_FIRST_LOGIN,true);
                if(isFirstLogin){
                    GlobalConfig.getInstance(getActivity()).putBoolean(Constants.SP_NOT_FIRST_LOGIN,false);
                    targetClass=GuidePagerActivity.class;
                }else{
                        /*默认跳转界面为城市选择*/
                    targetClass=MainActivity.class;
                    //targetClass=CityChooseActivity.class;
                }
               /* if(!isFirstLogin){
                    currentMuseumId= (String) DataBiz.getTempValue(BeginActivity.this, SP_MUSEUM_ID, "");
                    if(!TextUtils.isEmpty(currentMuseumId)){
                        targetClass=MuseumHomeActivity.class;
                    }
                }*/
                handler.sendEmptyMessage(MSG_WHAT_CHANGE_ACTIVITY);
            }
        }.start();
    }


    private void goToNextActivity(){
        Intent intent=new Intent();
        /*if(!TextUtils.isEmpty(currentMuseumId)){
            intent.putExtra(INTENT_MUSEUM_ID, currentMuseumId);
        }*/
        intent.setClass(BeginActivity.this, targetClass);
        Utility.startActivity(getActivity(),intent);
        finish();
    }


    static class MyHandler extends Handler {

        WeakReference<BeginActivity> activityWeakReference;
        MyHandler(BeginActivity activity){
            this.activityWeakReference=new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            if(activityWeakReference==null){return;}
            BeginActivity activity=activityWeakReference.get();
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
