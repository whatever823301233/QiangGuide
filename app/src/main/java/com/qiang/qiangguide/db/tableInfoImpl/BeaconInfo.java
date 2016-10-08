package com.qiang.qiangguide.db.tableInfoImpl;

import android.database.sqlite.SQLiteDatabase;

import com.qiang.qiangguide.bean.MyBeacon;
import com.qiang.qiangguide.db.TableInfo;

import java.util.ArrayList;

/**
 * Created by Qiang on 2016/8/10.
 */
public class BeaconInfo extends TableInfo {

    private static final String CREATE_INFO     = "create table if not exists "
            + MyBeacon.TABLE_NAME               + " (_id integer primary key autoincrement , "
            + MyBeacon.ID                       + " varchar,"
            + MyBeacon.MAJOR                    + " integer, "
            + MyBeacon.MINOR                    + " integer, "
            + MyBeacon.UUID                     + " varchar, "
            + MyBeacon.MUSEUM_ID                + " varchar, "
            + MyBeacon.TYPE                     + " varchar, "
            + MyBeacon.MUSEUM_AREA_ID           + " varchar, "
            + MyBeacon.PERSONX                  + " varchar, "
            + MyBeacon.PERSONY                  + " varchar )";

    private static final ArrayList<String> tableInfo;

    static {
        tableInfo=new ArrayList<>();
        tableInfo.add(CREATE_INFO);
    }

    public BeaconInfo() {
        super(MyBeacon.TABLE_NAME, tableInfo);
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
