package com.qiang.qiangguide.db.handler;

import android.content.ContentValues;
import android.database.Cursor;

import com.qiang.qiangguide.bean.User;
import com.qiang.qiangguide.db.DBHandler;
import com.qiang.qiangguide.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiang on 2016/9/26.
 */
public class UserHandler {


    /**
     * add users
     * @param user
     */
    public static  void addUser(User user) {
        try{
            DBHandler.getInstance().getDB().beginTransaction();  //开始事务
             DBHandler.getInstance().getDB().execSQL("INSERT INTO "+User.TABLE_NAME+" VALUES(null, ?, ?)", new Object[]{user.getUsername(), user.getPassword()});
             DBHandler.getInstance().getDB().setTransactionSuccessful();  //设置事务成功完成
        } catch (Exception e){
            LogUtil.e("",e);
        }finally {
            DBHandler.getInstance().getDB().endTransaction();    //结束事务
        }

    }


    /**
     * add users
     * @param users
     */
    public static  void addUsers(List<User> users) {
        DBHandler.getInstance().getDB().beginTransaction();  //开始事务
        try {
            for (User user : users) {
                DBHandler.getInstance().getDB().execSQL("INSERT INTO "+User.TABLE_NAME+" VALUES(null, ?, ?)", new Object[]{user.getUsername(), user.getPassword()});
            }
            DBHandler.getInstance().getDB().setTransactionSuccessful();  //设置事务成功完成
        } catch (Exception e){
            LogUtil.e("",e);
        }finally {
            DBHandler.getInstance().getDB().endTransaction();    //结束事务
        }
    }


    /**
     * update user's password
     * @param user
     */
    public static  void update(User user) {
        ContentValues cv = new ContentValues();
        cv.put(User.PASSWORD, user.getPassword());
        cv.put(User.USERNAME, user.getUsername());
        DBHandler.getInstance().getDB().update(User.TABLE_NAME, cv, User.USERNAME+" = ?", new String[]{user.getUsername()});
    }

    /**
     * delete old user
     * @param userName
     */
    public static  void deleteOldUser(String userName) {
        DBHandler.getInstance().getDB().beginTransaction();
        DBHandler.getInstance().getDB().delete(User.TABLE_NAME, User.USERNAME+"= ?", new String[]{userName});
        DBHandler.getInstance().getDB().endTransaction();
    }

    /**
     * query all users, return list
     * @return List<User>
     */
    public static  List<User> queryAllUser() {
        DBHandler.getInstance().getDB().beginTransaction();
        ArrayList<User> persons = new ArrayList<>();
        Cursor c = queryUserCursor();
        while (c.moveToNext()) {
            User person = new User();
            person.set_id(c.getInt(c.getColumnIndex(User.USER_ID)));
            person.setUsername(c.getString(c.getColumnIndex(User.USERNAME)));
            person.setPassword(c.getString(c.getColumnIndex(User.PASSWORD)));
            persons.add(person);
        }
        c.close();
        DBHandler.getInstance().getDB().setTransactionSuccessful();
        DBHandler.getInstance().getDB().endTransaction();
        return persons;
    }


    public static  User queryUser(String userName){
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
    public static  Cursor queryUserCursor() {
        return DBHandler.getInstance().getDB().rawQuery("SELECT * FROM "+User.TABLE_NAME, null);
    }

    /**
     * query all persons, return cursor
     * @return  Cursor
     */
    public  static Cursor queryUserByName(String name) {
        return DBHandler.getInstance().getDB().rawQuery("SELECT * FROM "+User.TABLE_NAME +" WHERE "+User.USERNAME+" like ?",new String[]{name});
    }



}
