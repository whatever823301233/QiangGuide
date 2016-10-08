package com.qiang.qiangguide.db.handler;

import android.database.Cursor;
import android.text.TextUtils;

import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.db.DBHandler;
import com.qiang.qiangguide.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiang on 2016/9/26.
 */
public class MuseumHandler {

    /**
     * add museumList
     * @param m
     */
    public static  void addMuseum(Museum m) {
        if(m==null){return;}
        DBHandler.getInstance().getDB().beginTransaction();  //开始事务
        try {
            DBHandler.getInstance().getDB().execSQL("INSERT INTO "+Museum.TABLE_NAME
                            +" VALUES(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    new Object[]{
                            m.getId(),
                            m.getName(),
                            m.getLongitudex(),
                            m.getLongitudey(),
                            m.getIconurl(),
                            m.getAddress(),
                            m.getOpentime(),
                            m.getIsopen(),
                            m.getTexturl(),
                            m.getFloorcount(),
                            m.getImgurl(),
                            m.getAudiourl(),
                            m.getCity(),
                            m.getVersion(),
                            m.getDownloadState(),
                            m.getPriority()
                    });
            DBHandler.getInstance().getDB().setTransactionSuccessful();  //设置事务成功完成
            LogUtil.i("","addMuseum 保存成功");
        } catch (Exception e){
            LogUtil.e("",e);
        }finally {
            DBHandler.getInstance().getDB().endTransaction();    //结束事务
        }
    }


    /**
     * add museumList
     * @param museumList
     */
    public static  void addMuseumList(List<Museum> museumList) {
        if(museumList==null){return;}
        DBHandler.getInstance().getDB().beginTransaction();  //开始事务
        try {
            for (Museum m : museumList) {
                DBHandler.getInstance().getDB().execSQL("INSERT INTO "+Museum.TABLE_NAME
                                +" VALUES(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                        new Object[]{
                                m.getId(),
                                m.getName(),
                                m.getLongitudex(),
                                m.getLongitudey(),
                                m.getIconurl(),
                                m.getAddress(),
                                m.getOpentime(),
                                m.getIsopen(),
                                m.getTexturl(),
                                m.getFloorcount(),
                                m.getImgurl(),
                                m.getAudiourl(),
                                m.getCity(),
                                m.getVersion(),
                                m.getDownloadState(),
                                m.getPriority()
                        });
            }
            DBHandler.getInstance().getDB().setTransactionSuccessful();  //设置事务成功完成
            LogUtil.i("","addMuseums 保存成功");
        } catch (Exception e){
            LogUtil.e("",e);
        }finally {
            DBHandler.getInstance().getDB().endTransaction();    //结束事务
        }
    }



    /**
     * 更新museum
     * @param museum
     */
    public static  void updateMuseum(Museum museum) {

        if(museum==null){return;}
        String sql="UPDATE "+Museum.TABLE_NAME+" SET "+Museum.DOWNLOAD_STATE+" = ? WHERE "+Museum.ID+" = ? ";
        DBHandler.getInstance().getDB().execSQL(sql,new String[]{String.valueOf(museum.getDownloadState()),museum.getId()});

    }

    private static  void removeOldMuseum(Museum oldMuseum) {
        if(oldMuseum==null){return;}
        DBHandler.getInstance().getDB().beginTransaction();
        //getDB().delete(Museum.TABLE_NAME, Museum.ID +"= ?", new String[]{oldMuseum.getId()});
        String sql="DELETE  FROM "+Museum.TABLE_NAME +"  WHERE "+ Museum.NAME +" = ?";
        DBHandler.getInstance().getDB().execSQL(sql, new String[]{oldMuseum.getName()});
        DBHandler.getInstance().getDB().endTransaction();
    }

    /**
     * 查找数据库中博物馆
     * @param id id
     */
    public static  Museum queryMuseum(String id) {
        Museum museum=null;
        if(TextUtils.isEmpty(id)){return null;}
        DBHandler.getInstance().getDB().beginTransaction();
        Cursor cursor=DBHandler.getInstance().getDB().rawQuery(
                "SELECT * FROM " + Museum.TABLE_NAME
                        +" WHERE "+Museum.ID
                        +" = ? ",
                new String[]{id});
        if(cursor.moveToFirst()){
            museum=buildMuseumByCursor(cursor);
        }
        DBHandler.getInstance().getDB().setTransactionSuccessful();
        DBHandler.getInstance().getDB().endTransaction();
        return museum;
    }

    private  static Museum buildMuseumByCursor(Cursor c) {

        Museum museum = new Museum();
        museum.set_id(c.getInt(c.getColumnIndex(Museum._ID)));
        museum.setId(c.getString(c.getColumnIndex(Museum.ID)));
        museum.setName(c.getString(c.getColumnIndex(Museum.NAME)));
        museum.setIconurl(c.getString(c.getColumnIndex(Museum.ICON_URL)));
        museum.setAudiourl(c.getString(c.getColumnIndex(Museum.AUDIO_URL)));
        museum.setImgurl(c.getString(c.getColumnIndex(Museum.IMG_URL)));
        museum.setAddress(c.getString(c.getColumnIndex(Museum.ADDRESS)));
        museum.setCity(c.getString(c.getColumnIndex(Museum.CITY)));
        museum.setFloorcount(c.getInt(c.getColumnIndex(Museum.FLOOR_COUNT)));
        museum.setIsopen(c.getString(c.getColumnIndex(Museum.IS_OPEN)));
        museum.setLongitudex(c.getString(c.getColumnIndex(Museum.LONGITUDE_X)));
        museum.setLongitudey(c.getString(c.getColumnIndex(Museum.LONGITUDE_Y)));
        museum.setOpentime(c.getString(c.getColumnIndex(Museum.OPEN_TIME)));
        museum.setTexturl(c.getString(c.getColumnIndex(Museum.TEXT_URL)));
        museum.setDownloadState(c.getInt(c.getColumnIndex(Museum.DOWNLOAD_STATE)));
        museum.setPriority(c.getInt(c.getColumnIndex(Museum.PRIORITY)));
        museum.setVersion(c.getInt(c.getColumnIndex(Museum.VERSION)));
        return museum;
    }


    /**
     * query all users, return list
     * @param city
     * @return List<Museum>
     */
    public static  List<Museum> queryAllMuseumByCity(String city) {
        List<Museum> museumList = new ArrayList<>();
        Cursor c = DBHandler.getInstance().getDB().rawQuery("SELECT * FROM "+Museum.TABLE_NAME +" WHERE "+Museum.CITY+" = ?",new String[]{city});
        while (c.moveToNext()) {
            Museum museum = new Museum();
            museum.set_id(c.getInt(c.getColumnIndex(Museum._ID)));
            museum.setId(c.getString(c.getColumnIndex(Museum.ID)));
            museum.setName(c.getString(c.getColumnIndex(Museum.NAME)));
            museum.setLongitudex(c.getString(c.getColumnIndex(Museum.LONGITUDE_X)));
            museum.setLongitudey(c.getString(c.getColumnIndex(Museum.LONGITUDE_Y)));
            museum.setIconurl(c.getString(c.getColumnIndex(Museum.ICON_URL)));
            museum.setAddress(c.getString(c.getColumnIndex(Museum.ADDRESS)));
            museum.setOpentime(c.getString(c.getColumnIndex(Museum.OPEN_TIME)));
            museum.setIsopen(c.getString(c.getColumnIndex(Museum.IS_OPEN)));
            museum.setTexturl(c.getString(c.getColumnIndex(Museum.TEXT_URL)));
            museum.setFloorcount(c.getInt(c.getColumnIndex(Museum.FLOOR_COUNT)));
            museum.setImgurl(c.getString(c.getColumnIndex(Museum.IMG_URL)));
            museum.setAudiourl(c.getString(c.getColumnIndex(Museum.AUDIO_URL)));
            museum.setCity(c.getString(c.getColumnIndex(Museum.CITY)));
            museum.setVersion(c.getInt(c.getColumnIndex(Museum.VERSION)));
            museum.setDownloadState(c.getInt(c.getColumnIndex(Museum.DOWNLOAD_STATE)));
            museum.setPriority(c.getInt(c.getColumnIndex(Museum.PRIORITY)));
            museumList.add(museum);
        }
        c.close();
        return museumList;
    }

}
