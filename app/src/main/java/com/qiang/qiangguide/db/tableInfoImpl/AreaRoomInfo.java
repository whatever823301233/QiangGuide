package com.qiang.qiangguide.db.tableInfoImpl;

import android.database.sqlite.SQLiteDatabase;

import com.qiang.qiangguide.bean.AreaRoom;
import com.qiang.qiangguide.db.TableInfo;

import java.util.ArrayList;

/**
 * Created by Qiang on 2016/9/26.
 */
public class AreaRoomInfo extends TableInfo {

    private static final String CREATE_INFO     = "create table if not exists "
            + AreaRoom.TABLE_NAME               + " (_id integer primary key autoincrement , "
            + AreaRoom.NAME                     + " varchar )";

    private static final ArrayList<String> tableInfo;

    static {
        tableInfo=new ArrayList<>();
        tableInfo.add(CREATE_INFO);
    }

    public AreaRoomInfo() {
        super(AreaRoom.TABLE_NAME, tableInfo);
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: 2016/7/30
    }
}
