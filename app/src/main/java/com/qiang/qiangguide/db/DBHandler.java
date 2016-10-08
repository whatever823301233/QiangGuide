package com.qiang.qiangguide.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Qiang on 2016/7/13.
 *
 */
public class DBHandler {

    private static final String TAG = "DBHandler";

    private static volatile DBHandler mInstance ;
    protected volatile SQLiteDatabase mDB ;
    protected SQLiteOpenHelper mDatabaseHelper ;

    /**
     *  必须在应用启动的时候调用一下
     **/
    public static DBHandler init(Context context){
        if( null == mInstance ) {
            synchronized( DBHandler.class ) {
                if( mInstance == null ) {
                    if( null == context ) {
                        throw new IllegalArgumentException( "context is null" );
                    } else {
                        mInstance = new DBHandler( context );
                    }
                }
            }
        }
        return mInstance;
    }

    public static DBHandler getInstance() {
        return mInstance;
    }

    private DBHandler( Context context ) {
        mDatabaseHelper = new DBHelper( context.getApplicationContext() );
        //open();
    }

    /**
     * 打开数据库
     *
     * @return SQLiteDatabase
     */
    public SQLiteDatabase open() {

        if( null == mDB ) {
            mDB = mDatabaseHelper.getWritableDatabase();
        }
        return mDB;
    }


    /**
     * 数据库
     *
     * @return
     */
    public SQLiteDatabase getDB() {

        if( null == mDB ) {
            synchronized( this ) {
                if( mDB == null ) {
                    mDB = mDatabaseHelper.getWritableDatabase();
                }
            }
        }
        return mDB;
    }


    /**
     * 关闭数据库
     */
    public synchronized void destroy() {

        if( mDB != null ) {
            if( mDB.isOpen() ) {
                mDB.close();
                mDB = null;
            }
        }
        mInstance = null;
    }


    public void deleteTable( String tableName ) {

        if( mDB != null ) {
            mDB.delete( tableName, null, null );
        }
    }

    /**
     * close database
     */
    public void closeDB() {
        getDB().close();
    }


}
