package com.qiang.qiangguide.db.tableInfoImpl;

import android.database.sqlite.SQLiteDatabase;

import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.db.TableInfo;

import java.util.ArrayList;

/**
 * Created by Qiang on 2016/8/2.
 */
public class MuseumInfo extends TableInfo {

    private static final String CREATE_INFO     = "create table if not exists "
            + Museum.TABLE_NAME                 + " (_id integer primary key autoincrement , "
            + Museum.ID                         + " varchar,"
            + Museum.NAME                       + " varchar,"
            + Museum.LONGITUDE_X                + " varchar,"
            + Museum.LONGITUDE_Y                + " varchar,"
            + Museum.ICON_URL                   + " varchar,"
            + Museum.ADDRESS                    + " varchar,"
            + Museum.OPEN_TIME                  + " varchar,"
            + Museum.IS_OPEN                    + " varchar,"
            + Museum.TEXT_URL                   + " varchar,"
            + Museum.FLOOR_COUNT                + " integer,"
            + Museum.IMG_URL                    + " varchar,"
            + Museum.AUDIO_URL                  + " varchar,"
            + Museum.CITY                       + " varchar,"
            + Museum.VERSION                    + " integer,"
            + Museum.DOWNLOAD_STATE             + " integer,"
            + Museum.PRIORITY                   + " integer )";

    private static final ArrayList<String> tableInfo;

    static {
        tableInfo=new ArrayList<>();
        tableInfo.add(CREATE_INFO);
    }

    public MuseumInfo() {
        super(Museum.TABLE_NAME, tableInfo);
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: 2016/7/30
    }


}
