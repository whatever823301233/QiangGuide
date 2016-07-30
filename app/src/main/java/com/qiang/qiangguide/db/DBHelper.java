package com.qiang.qiangguide.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

        sTableInfo.add(new UserInfo());
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
