package com.qiang.qiangguide.db.handler;

import android.database.Cursor;

import com.qiang.qiangguide.bean.City;
import com.qiang.qiangguide.db.DBHandler;
import com.qiang.qiangguide.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiang on 2016/9/26.
 */
public class CityHandler {

    /**
     * add cities
     * @param cities
     */
    public  static void addCities(List<City> cities) {
        if(cities==null){return;}
        DBHandler.getInstance().getDB().beginTransaction();  //开始事务
        try {
            for (City city : cities) {
                DBHandler.getInstance().getDB().execSQL("INSERT INTO "+City.TABLE_NAME+" VALUES(null, ?, ?)",
                        new Object[]{city.getAlpha(), city.getName()});
            }
            DBHandler.getInstance().getDB().setTransactionSuccessful();  //设置事务成功完成
            LogUtil.i("","addCities 保存成功");
        } catch (Exception e){
            LogUtil.e("",e);
        }finally {
            DBHandler.getInstance().getDB().endTransaction();    //结束事务
        }
    }

    /**
     * query all users, return list
     * @return ArrayList<User>
     */
    public  static ArrayList<City> queryAllCities() {
        ArrayList<City> persons = new ArrayList<>();
        Cursor c = DBHandler.getInstance().getDB().rawQuery("SELECT * FROM "+City.TABLE_NAME, null);
        while (c.moveToNext()) {
            City city = new City();
            city.set_id(c.getInt(c.getColumnIndex(City._ID)));
            city.setAlpha(c.getString(c.getColumnIndex(City.ALPHA)));
            city.setName(c.getString(c.getColumnIndex(City.NAME)));
            persons.add(city);
        }
        c.close();
        return persons;
    }
}
