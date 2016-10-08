package com.qiang.qiangguide.db.tableInfoImpl;

import android.database.sqlite.SQLiteDatabase;

import com.qiang.qiangguide.bean.User;
import com.qiang.qiangguide.db.TableInfo;

import java.util.ArrayList;

/**
 * Created by Qiang on 2016/7/22.
 */
public class UserInfo extends TableInfo {

    private static final String CREATE_INFO     = "create table if not exists "
            + User.TABLE_NAME                   + " (_id integer primary key autoincrement , "
            + User.USERNAME                     + " varchar,"
            + User.PASSWORD                     + " varchar )";

    private static final ArrayList<String> tableInfo;

    static {
        tableInfo=new ArrayList<>();
        tableInfo.add(CREATE_INFO);
    }

    public UserInfo() {
        super(User.TABLE_NAME, tableInfo);
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("alter table "+User.TABLE_NAME+" add column "+User.OTHER+" varchar");
    }

}
