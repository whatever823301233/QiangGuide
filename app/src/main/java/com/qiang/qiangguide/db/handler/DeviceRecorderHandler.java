package com.qiang.qiangguide.db.handler;

import com.qiang.qiangguide.bean.DeviceRecorder;
import com.qiang.qiangguide.db.DBHandler;

/**
 * Created by Qiang on 2016/9/27.
 */
public class DeviceRecorderHandler {


    public static void saveExhibitRecorder(DeviceRecorder deviceRecorder) {
        DBHandler.getInstance().getDB().beginTransaction();  //开始事务
        DBHandler.getInstance().getDB().execSQL(
                "INSERT INTO "+ DeviceRecorder.TABLE_NAME
                        +" VALUES(null,?,?,?,?)",
                new String[]{
                        deviceRecorder.getExhibitId(),
                        deviceRecorder.getAreaRoomName(),
                        deviceRecorder.getDeviceNum(),
                        deviceRecorder.getTime()
                });
        DBHandler.getInstance().getDB().setTransactionSuccessful();
        DBHandler.getInstance().getDB().endTransaction();
    }
}
