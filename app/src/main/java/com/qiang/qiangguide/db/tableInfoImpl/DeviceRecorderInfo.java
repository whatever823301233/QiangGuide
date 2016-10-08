package com.qiang.qiangguide.db.tableInfoImpl;

import android.database.sqlite.SQLiteDatabase;

import com.qiang.qiangguide.bean.Device;
import com.qiang.qiangguide.bean.DeviceRecorder;
import com.qiang.qiangguide.db.TableInfo;

import java.util.ArrayList;

/**
 * Created by Qiang on 2016/9/26.
 */
public class DeviceRecorderInfo extends TableInfo {

    private static final String CREATE_INFO     = "create table if not exists "
            + DeviceRecorder.TABLE_NAME         + " (_id integer primary key autoincrement , "
            + DeviceRecorder.EXHIBIT_ID         + " varchar,"
            + DeviceRecorder.AREA_ROOM_NAME     + " varchar,"
            + DeviceRecorder.DEVICE_NUM         + " varchar,"
            + DeviceRecorder.TIME               + " datetime )";

    private static final ArrayList<String> tableInfo;

    static {
        tableInfo=new ArrayList<>();
        tableInfo.add(CREATE_INFO);
    }
    public DeviceRecorderInfo() {
        super(Device.TABLE_NAME, tableInfo);
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
