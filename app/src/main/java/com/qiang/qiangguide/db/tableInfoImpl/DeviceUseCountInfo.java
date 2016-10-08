package com.qiang.qiangguide.db.tableInfoImpl;

import android.database.sqlite.SQLiteDatabase;

import com.qiang.qiangguide.bean.DeviceUseCount;
import com.qiang.qiangguide.db.TableInfo;

import java.util.ArrayList;

/**
 * Created by Qiang on 2016/9/26.
 */
public class DeviceUseCountInfo extends TableInfo{

    private static final String CREATE_INFO     = "create table if not exists "
            + DeviceUseCount.TABLE_NAME         + " (_id integer primary key autoincrement , "
            + DeviceUseCount.USE_TIME           + " datetime,"
            + DeviceUseCount.RETURN_TIME        + " datetime,"
            + DeviceUseCount.DEVICE_NUM         + " varchar,"
            + DeviceUseCount.USE_ID             + " varchar )";

    private static final ArrayList<String> tableInfo;

    static {
        tableInfo=new ArrayList<>();
        tableInfo.add(CREATE_INFO);
    }

    public DeviceUseCountInfo() {
        super(DeviceUseCount.TABLE_NAME, tableInfo);
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
