package com.qiang.qiangguide.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.qiang.qiangguide.bean.User;
import com.qiang.qiangguide.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiang on 2016/7/13.
 *
 */
public class DBHandler {

    private static final String TAG = "DBHandler";

    private static volatile DBHandler mInstance = null;
    protected volatile SQLiteDatabase mDB = null;
    protected SQLiteOpenHelper mDatabaseHelper = null;


    /**
     *  必须在应用启动的时候调用一下
     **/
    public static DBHandler getInstance( Context context ) {

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


    private DBHandler( Context context ) {
        mDatabaseHelper = new DBHelper( context.getApplicationContext() );
        open();
    }

    /**
     * 打开数据库
     *
     * @return SQLiteDatabase
     */
    private SQLiteDatabase open() {

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
     * add users
     * @param users
     */
    public void addUsers(List<User> users) {
        getDB().beginTransaction();  //开始事务
        try {
            for (User user : users) {
                getDB().execSQL("INSERT INTO "+User.TABLE_NAME+" VALUES(null, ?, ?)", new Object[]{user.getUsername(), user.getPassword()});
            }
            getDB().setTransactionSuccessful();  //设置事务成功完成
        } catch (Exception e){
            LogUtil.e("",e);
        }finally {
            getDB().endTransaction();    //结束事务
        }
    }

    /**
     * update user's password
     * @param user
     */
    public void updatePassword(User user) {
        ContentValues cv = new ContentValues();
        cv.put(User.PASSWORD, user.getPassword());
        getDB().update(User.TABLE_NAME, cv, User.PASSWORD+" = ?", new String[]{user.getPassword()});
    }

    /**
     * delete old user
     * @param userName
     */
    public void deleteOldUser(String userName) {
        getDB().delete(User.TABLE_NAME, User.USERNAME+"= ?", new String[]{userName});
    }

    /**
     * query all users, return list
     * @return ArrayList<User>
     */
    public ArrayList<User> query() {
        ArrayList<User> persons = new ArrayList<>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            User person = new User();
            person.set_id(c.getInt(c.getColumnIndex(User.USER_ID)));
            person.setUsername(c.getString(c.getColumnIndex(User.USERNAME)));
            person.setPassword(c.getString(c.getColumnIndex(User.PASSWORD)));
            persons.add(person);
        }
        c.close();
        return persons;
    }


    public User queryUser(String userName){
        User person =null;
        Cursor c=queryUserByName(userName);
        if(c.moveToNext()){
            person = new User();
            person.set_id(c.getInt(c.getColumnIndex(User.USER_ID)));
            person.setUsername(c.getString(c.getColumnIndex(User.USERNAME)));
            person.setPassword(c.getString(c.getColumnIndex(User.PASSWORD)));
        }
        return person;
    }

    /**
     * query all persons, return cursor
     * @return  Cursor
     */
    public Cursor queryTheCursor() {
        return getDB().rawQuery("SELECT * FROM "+User.TABLE_NAME, null);
    }

    /**
     * query all persons, return cursor
     * @return  Cursor
     */
    public Cursor queryUserByName(String name) {
        return getDB().rawQuery("SELECT * FROM "+User.TABLE_NAME +" WHERE "+User.USERNAME+" like ?",new String[]{name});
    }

    /**
     * close database
     */
    public void closeDB() {
        getDB().close();
    }

}
