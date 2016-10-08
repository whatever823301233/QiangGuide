package com.qiang.qiangguide.db.handler;

import com.qiang.qiangguide.bean.DeviceUseCount;
import com.qiang.qiangguide.db.DBHandler;

/**
 * Created by Qiang on 2016/9/27.
 */
public class DeviceUseCountHandler {


    public static void saveDeviceUseRecorder(DeviceUseCount deviceUseCount) {

        DBHandler.getInstance().getDB().beginTransaction();  //开始事务
        DBHandler.getInstance().getDB().execSQL(
                "INSERT INTO "+ DeviceUseCount.TABLE_NAME
                        +" VALUES(null,?,?,?,?)",
                new String[]{
                        deviceUseCount.getUseTime(),
                        deviceUseCount.getReturnTime(),
                        deviceUseCount.getDeviceNum(),
                        deviceUseCount.getUseId(),
                });
        DBHandler.getInstance().getDB().setTransactionSuccessful();
        DBHandler.getInstance().getDB().endTransaction();
    }








}
