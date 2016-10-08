package com.qiang.qiangguide.db.tableInfoImpl;

import android.database.sqlite.SQLiteDatabase;

import com.qiang.qiangguide.bean.City;
import com.qiang.qiangguide.db.TableInfo;

import java.util.ArrayList;

/**
 * Created by xq823 on 2016/7/30.
 */
public class CityInfo extends TableInfo {

    private static final String CREATE_INFO     = "create table if not exists "
            + City.TABLE_NAME                   + " (_id integer primary key autoincrement , "
            + City.ALPHA                        + " varchar,"
            + City.NAME                         + " varchar )";

    private static final ArrayList<String> tableInfo;

    static {
        tableInfo=new ArrayList<>();
        tableInfo.add(CREATE_INFO);
    }

    public CityInfo() {
        super(City.TABLE_NAME, tableInfo);
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: 2016/7/30
    }
}
