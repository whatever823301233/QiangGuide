package com.qiang.qiangguide;

import android.content.Context;

import com.qiang.qiangguide.bean.DeviceRecorder;
import com.qiang.qiangguide.bean.DeviceUseCount;
import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.config.GlobalConfig;
import com.qiang.qiangguide.db.handler.DeviceRecorderHandler;
import com.qiang.qiangguide.db.handler.DeviceUseCountHandler;
import com.qiang.qiangguide.util.AndroidUtil;
import com.qiang.qiangguide.util.TimeUtil;

/**
 * Created by Qiang on 2016/9/27.
 */
public class RecorderManager {

    public void recorderExhibit(Context context,Exhibit exhibit){
        if(exhibit==null){return;}
        DeviceRecorder deviceRecorder=new DeviceRecorder();
        deviceRecorder.setAreaRoomName(exhibit.getAreaRoomId());
        deviceRecorder.setDeviceNum(AndroidUtil.getDeviceId(context));
        deviceRecorder.setTime(TimeUtil.getNowTimeString());
        deviceRecorder.setExhibitId(exhibit.getId());
        DeviceRecorderHandler.saveExhibitRecorder(deviceRecorder);
    }

    public void recorderUseCountForStart(Context context){
        GlobalConfig.getInstance(context).putString(DeviceUseCount.USE_TIME,TimeUtil.getNowTimeString());
        GlobalConfig.getInstance(context).putString(DeviceUseCount.DEVICE_NUM,AndroidUtil.getDeviceId(context));
    }

    public void recorderUseCountForEnd(Context context){
        String useTime=GlobalConfig.getInstance(context).getString(DeviceUseCount.USE_TIME,null);
        String deviceNum=GlobalConfig.getInstance(context).getString(DeviceUseCount.DEVICE_NUM,null);
        DeviceUseCount deviceUseCount =new DeviceUseCount();

        deviceUseCount.setDeviceNum(deviceNum);
        deviceUseCount.setUseTime(useTime);
        deviceUseCount.setReturnTime(TimeUtil.getNowTimeString());
        DeviceUseCountHandler.saveDeviceUseRecorder(deviceUseCount);

    }


}
