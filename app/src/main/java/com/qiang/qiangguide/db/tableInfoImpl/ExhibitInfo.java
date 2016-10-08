package com.qiang.qiangguide.db.tableInfoImpl;

import android.database.sqlite.SQLiteDatabase;

import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.db.TableInfo;

import java.util.ArrayList;

/**
 * Created by Qiang on 2016/8/2.
 */
public class ExhibitInfo extends TableInfo {

    private static final String CREATE_INFO     = "create table if not exists "
            + Exhibit.TABLE_NAME                + " (_id integer primary key autoincrement , "
            + Exhibit.ID                        + " varchar,"
            + Exhibit.MUSEUM_ID                 + " varchar,"
            + Exhibit.NAME                      + " varchar,"
            + Exhibit.NUMBER                    + " integer,"
            + Exhibit.MAP_X                     + " double,"
            + Exhibit.MAP_Y                     + " double,"
            + Exhibit.MUSEUM_AREA_ID            + " varchar,"
            + Exhibit.BEACON_ID                 + " varchar,"
            + Exhibit.TEXT_URL                  + " varchar,"
            + Exhibit.ICON_URL                  + " varchar,"
            + Exhibit.AUDIO_URL                 + " varchar,"
            + Exhibit.IMGS_URL                  + " varchar,"
            + Exhibit.LABELS                    + " varchar,"
            + Exhibit.INTRODUCE                 + " varchar,"
            + Exhibit.CONTENT                   + " varchar,"
            + Exhibit.L_EXHIBIT                 + " varchar,"
            + Exhibit.R_EXHIBIT                 + " varchar,"
            + Exhibit.VERSION                   + " integer,"
            + Exhibit.IS_FAVORITE               + " integer,"
            + Exhibit.PRIORITY                  + " integer )";

    private static final ArrayList<String> tableInfo;

    static {
        tableInfo=new ArrayList<>();
        tableInfo.add(CREATE_INFO);
    }

    public ExhibitInfo() {
        super(Exhibit.TABLE_NAME, tableInfo);
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: 2016/7/30
    }

}
