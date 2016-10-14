package com.qiang.qiangguide.db.handler;

import android.database.Cursor;

import com.qiang.qiangguide.bean.DeviceRecorder;
import com.qiang.qiangguide.db.DBHandler;

/**
 * Created by Qiang on 2016/9/27.
 */
public class DeviceRecorderHandler {

    /***
     * 保存设备播放记录
     * @param deviceRecorder
     */
    public static void saveExhibitRecorder( DeviceRecorder deviceRecorder ) {
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

    /***
     * 查找展品播放记录
     * @param exhibitId
     */
    public static DeviceRecorder queryRecorder( String exhibitId ) {
        DBHandler.getInstance().getDB().beginTransaction();  //开始事务
        Cursor c = DBHandler.getInstance()
                .getDB()
                .rawQuery (
                        "SELECT * FROM "+ DeviceRecorder.TABLE_NAME
                                + " WHERE "+ DeviceRecorder.EXHIBIT_ID
                                + " = ?",
                        new String[]{
                                exhibitId
                        }
                );
        DeviceRecorder deviceRecorder = null;
        if ( c.moveToNext() ) {
            deviceRecorder = buildDeviceRecorderByCursor(c);
        }
        c.close();
        DBHandler.getInstance().getDB().setTransactionSuccessful();
        DBHandler.getInstance().getDB().endTransaction();
        return deviceRecorder;
    }

    private static DeviceRecorder buildDeviceRecorderByCursor( Cursor cursor ) {
        DeviceRecorder deviceRecorder = new DeviceRecorder();

        deviceRecorder.set_id ( cursor.getInt( cursor.getColumnIndex( DeviceRecorder._ID ) ) );
        deviceRecorder.setExhibitId ( cursor.getString( cursor.getColumnIndex( DeviceRecorder.EXHIBIT_ID ) ) );
        deviceRecorder.setAreaRoomName ( cursor.getString( cursor.getColumnIndex( DeviceRecorder.AREA_ROOM_NAME ) ) );
        deviceRecorder.setDeviceNum ( cursor.getString( cursor.getColumnIndex( DeviceRecorder.DEVICE_NUM ) ) );
        deviceRecorder.setTime ( cursor.getString( cursor.getColumnIndex( DeviceRecorder.TIME ) ) );

        return deviceRecorder;
    }


}
