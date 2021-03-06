package com.qiang.qiangguide.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.qiang.qiangguide.db.tableInfoImpl.AreaRoomInfo;
import com.qiang.qiangguide.db.tableInfoImpl.BeaconInfo;
import com.qiang.qiangguide.db.tableInfoImpl.CityInfo;
import com.qiang.qiangguide.db.tableInfoImpl.DeviceRecorderInfo;
import com.qiang.qiangguide.db.tableInfoImpl.DeviceUseCountInfo;
import com.qiang.qiangguide.db.tableInfoImpl.ExhibitInfo;
import com.qiang.qiangguide.db.tableInfoImpl.LabelInfo;
import com.qiang.qiangguide.db.tableInfoImpl.MuseumInfo;
import com.qiang.qiangguide.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by Qiang on 2016/7/13.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";
    private static final String DATABASE_NAME = "qiang.db";
    private static final int DATABASE_VERSION = 1;

    private ArrayList<TableInfo> sTableInfo = new ArrayList<>();


    public DBHelper( Context context ) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );

        sTableInfo.add(new CityInfo());
        sTableInfo.add(new MuseumInfo());
        sTableInfo.add(new ExhibitInfo());
        sTableInfo.add(new BeaconInfo());
        sTableInfo.add(new LabelInfo());
        sTableInfo.add(new AreaRoomInfo());
        sTableInfo.add(new DeviceRecorderInfo());
        sTableInfo.add(new DeviceUseCountInfo());

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        LogUtil.d( TAG, "onCreate" );
        try{
            for( TableInfo info : sTableInfo) {
                for( String sql : info.getCreateSql() ) {
                    db.execSQL( sql );
                }
            }
        }catch (SQLException e){
            LogUtil.e(TAG,e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        LogUtil.d( TAG, "onUpgrade" );
        for( TableInfo info : sTableInfo) {
            info.upgrade( db, oldVersion, newVersion );
        }
    }
}
