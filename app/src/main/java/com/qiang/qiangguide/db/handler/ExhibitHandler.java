package com.qiang.qiangguide.db.handler;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.bean.MyBeacon;
import com.qiang.qiangguide.custom.channel.ChannelItem;
import com.qiang.qiangguide.db.DBHandler;
import com.qiang.qiangguide.util.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Qiang on 2016/9/26.
 */
public class ExhibitHandler {


    /**
     * add ExhibitList
     * @param exhibitList
     */
    public  static void addExhibitList(List<Exhibit> exhibitList) {
        if(exhibitList==null){return;}
        DBHandler.getInstance().getDB().beginTransaction();  //开始事务
        try {
            for (Exhibit e : exhibitList) {
                DBHandler.getInstance().getDB().execSQL("INSERT INTO "+Exhibit.TABLE_NAME
                                +" VALUES(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                        new Object[]{
                                e.getId(),
                                e.getMuseumId(),
                                e.getName(),
                                e.getNumber(),
                                e.getMapx(),
                                e.getMapy(),
                                e.getMuseumAreaId(),
                                e.getBeaconId(),
                                e.getTexturl(),
                                e.getIconurl(),
                                e.getAudiourl(),
                                e.getImgsurl(),
                                e.getLabels(),
                                e.getIntroduce(),
                                e.getContent(),
                                e.getLexhibit(),
                                e.getRexhibit(),
                                e.getVersion(),
                                e.getPriority(),
                                e.getIsFavorite()
                        });
            }
            DBHandler.getInstance().getDB().setTransactionSuccessful();  //设置事务成功完成
            LogUtil.i("","addExhibitList 保存成功");
        } catch (Exception e){
            LogUtil.e("",e);
        }finally {
            DBHandler.getInstance().getDB().endTransaction();    //结束事务
        }
    }



    /**
     * query all exhibits, return list
     * @return List<Exhibit>
     */
    public  static  List<Exhibit> queryAllExhibitListByMuseumId(String museumId) {
        List<Exhibit> exhibitList = new ArrayList<>();
        Cursor c = DBHandler.getInstance().getDB().rawQuery("SELECT * FROM "+Exhibit.TABLE_NAME +" WHERE "+Exhibit.MUSEUM_ID+" = ?",new String[]{museumId});
        while (c.moveToNext()) {
            Exhibit e =buildExhibitByCursor(c);
            exhibitList.add(e);
        }
        c.close();
        return exhibitList;
    }

    /**
     * query all exhibits, return list
     * @return List<Exhibit>
     */
    public  static List<Exhibit> queryFavoriteExhibitListByMuseumId(String museumId) {
        List<Exhibit> exhibitList = new ArrayList<>();
        Cursor c = DBHandler.getInstance().getDB().rawQuery("SELECT * FROM "+Exhibit.TABLE_NAME
                +" WHERE "+Exhibit.MUSEUM_ID + " = ? AND "
                +Exhibit.IS_FAVORITE +" = ?",new String[]{museumId,String.valueOf(1)});
        while (c.moveToNext()) {
            Exhibit e =buildExhibitByCursor(c);
            exhibitList.add(e);
        }
        c.close();
        return exhibitList;
    }


    /**
     * query all exhibits, return list
     * @return List<Exhibit>
     */
    public  static List<Exhibit> queryAllExhibitList() {
        List<Exhibit> exhibitList = new ArrayList<>();
        DBHandler.getInstance().getDB().beginTransaction();
        Cursor c = DBHandler.getInstance().getDB().rawQuery("SELECT * FROM "+Exhibit.TABLE_NAME ,null);
        while (c.moveToNext()) {
            Exhibit e = buildExhibitByCursor(c);
            exhibitList.add(e);
        }
        c.close();
        DBHandler.getInstance().getDB().endTransaction();
        return exhibitList;
    }

    /**
     * query  exhibit, return exhibit
     * @return Exhibit
     */
    public static  Exhibit queryExhibitById(String id,String museumId) {
        Cursor c = DBHandler.getInstance().getDB().rawQuery("SELECT * FROM "+Exhibit.TABLE_NAME +" WHERE "
                        +Exhibit.ID+" = ? AND "+Exhibit.MUSEUM_ID +" = ?",
                new String[]{id,museumId});
        Exhibit e=null;
        if (c.moveToNext()) {
            e=buildExhibitByCursor(c);
        }
        c.close();
        return e;
    }


    /**
     * @param exhibit
     */
    public static  void updateExhibit(Exhibit exhibit) {
        DBHandler.getInstance().getDB().beginTransaction();
        ContentValues cv =exhibit.toContentValues();
        DBHandler.getInstance().getDB().update(Exhibit.TABLE_NAME, cv, Exhibit.ID+" = ?", new String[]{exhibit.getId()});
        DBHandler.getInstance().getDB().setTransactionSuccessful();
        DBHandler.getInstance().getDB().endTransaction();
    }


    /**
     * 查询展品
     * @param exhibitId exhibitId
     * @return Exhibit
     */
    public static  Exhibit queryExhibit(String exhibitId) {
        DBHandler.getInstance().getDB().beginTransaction();
        Cursor c=DBHandler.getInstance().getDB().rawQuery(
                "SELECT * FROM "+ Exhibit.TABLE_NAME
                        +" WHERE "
                        +Exhibit.ID
                        +" = ?",
                new String[]{
                        exhibitId
                }
        );
        Exhibit exhibit = null;
        if(c.moveToNext()){
            exhibit=buildExhibitByCursor(c);
        }
        c.close();
        DBHandler.getInstance().getDB().setTransactionSuccessful();
        DBHandler.getInstance().getDB().endTransaction();
        return exhibit;
    }

    /**
     * 查询展品
     * @param museumId museumId
     * @param beaconId beaconId
     * @return List<Exhibit>
     */
    public static  List<Exhibit> queryExhibit(String museumId, String  beaconId) {
        DBHandler.getInstance().getDB().beginTransaction();
        Cursor c=DBHandler.getInstance().getDB().rawQuery(
                "SELECT * FROM "+ Exhibit.TABLE_NAME
                        +" WHERE "
                        +Exhibit.MUSEUM_ID
                        +" = ? AND "
                        +Exhibit.BEACON_ID
                        +" = ?",
                new String[]{
                        museumId,
                        beaconId
                }
        );
        List<Exhibit> exhibits = new ArrayList<>();
        while(c.moveToNext()){
            Exhibit e=buildExhibitByCursor(c);
            exhibits.add(e);
        }
        c.close();
        DBHandler.getInstance().getDB().setTransactionSuccessful();
        DBHandler.getInstance().getDB().endTransaction();
        return exhibits;
    }
    /**
     * 查询展品
     * @param museumId museumId
     * @param exhibitId exhibitId
     * @return exhibit
     */
    public static  Exhibit querySingleExhibit(String museumId, String  exhibitId) {
        DBHandler.getInstance().getDB().beginTransaction();
        Cursor c=DBHandler.getInstance().getDB().rawQuery(
                "SELECT * FROM "+ Exhibit.TABLE_NAME
                        +" WHERE "
                        +Exhibit.MUSEUM_ID
                        +" = ? AND "
                        +Exhibit.ID
                        +" = ?",
                new String[]{
                        museumId,
                        exhibitId
                }
        );
        Exhibit e=null;
        if(c.moveToNext()){
            e=buildExhibitByCursor(c);
        }
        c.close();
        DBHandler.getInstance().getDB().setTransactionSuccessful();
        DBHandler.getInstance().getDB().endTransaction();
        return e;
    }


    /**
     * 查询展品
     * @param museumId museumId
     * @param beaconList beaconList
     * @return List<Exhibit>
     */
    public  static List<Exhibit> queryExhibit(String museumId, List<MyBeacon>  beaconList) {
        DBHandler.getInstance().getDB().beginTransaction();
        List<Exhibit> exhibits = new ArrayList<>();

        for(MyBeacon b:beaconList){
            Cursor c=DBHandler.getInstance().getDB().rawQuery(
                    "SELECT * FROM "+ Exhibit.TABLE_NAME
                            +" WHERE "
                            +Exhibit.MUSEUM_ID
                            +" = ? AND "
                            +Exhibit.BEACON_ID
                            +" = ?",
                    new String[]{
                            museumId,
                            b.getId()
                    }
            );
            while(c.moveToNext()){
                Exhibit e=buildExhibitByCursor(c);
                exhibits.add(e);
            }
            c.close();
        }
        DBHandler.getInstance().getDB().setTransactionSuccessful();
        DBHandler.getInstance().getDB().endTransaction();
        return exhibits;
    }



    public  static List<Exhibit> queryExhibit(ChannelItem channelItem) {

        if(channelItem==null){return null;}
        List<Exhibit> exhibitList=new ArrayList<>();
        DBHandler.getInstance().getDB().beginTransaction();
        String name =channelItem.getName();
        Cursor c=DBHandler.getInstance().getDB().rawQuery(
                "SELECT * FROM " + Exhibit.TABLE_NAME
                        +" WHERE "+Exhibit.LABELS
                        +" LIKE ? ",
                new String[]{"%"+name+"%"});
        while (c.moveToNext()){
            Exhibit e=buildExhibitByCursor(c);
            if(!exhibitList.contains(e)){
                exhibitList.add(e);
            }
        }
        c.close();
        DBHandler.getInstance().getDB().setTransactionSuccessful();
        DBHandler.getInstance().getDB().endTransaction();
        return exhibitList;
    }


    public  static List<Exhibit> queryExhibitByLabels(List<ChannelItem> userChannelList) {

        if(userChannelList==null||userChannelList.size()==0){ return null;}
        List<Exhibit> allLabelList=queryExhibit(userChannelList);
        if(userChannelList.size()==3){ return allLabelList;}
        DBHandler.getInstance().getDB().beginTransaction();
        List<Exhibit> resultList=new ArrayList<>();
        for(int i=2; i < userChannelList.size();i++){
            String name=userChannelList.get(i).getName();
            List<Exhibit> sLabelList=queryExhibitByLabel(name);
            for(Exhibit e:sLabelList){
                if(allLabelList.contains(e)){
                    resultList.add(e);
                }
            }
            if(resultList.size()==0){return Collections.emptyList();}
            allLabelList=resultList;
            resultList = new ArrayList<>();
        }
        DBHandler.getInstance().getDB().setTransactionSuccessful();
        DBHandler.getInstance().getDB().endTransaction();
        return allLabelList;
    }

    /**
     * 根据标签查询展品
     * @param label 标签
     * @return 展品集合
     */
    private  static List<Exhibit> queryExhibitByLabel(String label) {

        if(TextUtils.isEmpty(label)){return Collections.emptyList();}
        List<Exhibit> exhibitList=new ArrayList<>();
        Cursor c=DBHandler.getInstance().getDB().rawQuery(
                "SELECT * FROM " + Exhibit.TABLE_NAME
                        +" WHERE "+Exhibit.LABELS
                        +" LIKE ? ",
                new String[]{"%"+label+"%"});
        while (c.moveToNext()){
            Exhibit e=buildExhibitByCursor(c);
            if(!exhibitList.contains(e)){
                exhibitList.add(e);
            }
        }
        c.close();
        return exhibitList;
    }

    public  static List<Exhibit> queryExhibit(List<ChannelItem> channelItemList) {

        if(channelItemList==null||channelItemList.size()==0){return null;}
        List<Exhibit> exhibitList=new ArrayList<>();
        DBHandler.getInstance().getDB().beginTransaction();
        for(ChannelItem channel:channelItemList){
            String name =channel.getName();
            Cursor c=DBHandler.getInstance().getDB().rawQuery(
                    "SELECT * FROM " + Exhibit.TABLE_NAME
                            +" WHERE "+Exhibit.LABELS
                            +" LIKE ? ",
                    new String[]{"%"+name+"%"});
            while (c.moveToNext()){
                Exhibit e=buildExhibitByCursor(c);
                if(!exhibitList.contains(e)){
                    exhibitList.add(e);
                }
            }
            c.close();
        }
        DBHandler.getInstance().getDB().setTransactionSuccessful();
        DBHandler.getInstance().getDB().endTransaction();
        return exhibitList;
    }


    @NonNull
    private  static Exhibit buildExhibitByCursor(Cursor c) {
        Exhibit e = new Exhibit();
        e.set_id(c.getInt(c.getColumnIndex(Exhibit._ID)));
        e.setId(c.getString(c.getColumnIndex(Exhibit.ID)));
        e.setMuseumId(c.getString(c.getColumnIndex(Exhibit.MUSEUM_ID)));
        e.setName(c.getString(c.getColumnIndex(Exhibit.NAME)));
        e.setNumber(c.getInt(c.getColumnIndex(Exhibit.NUMBER)));
        e.setMapx(c.getString(c.getColumnIndex(Exhibit.MAP_X)));
        e.setMapy(c.getString(c.getColumnIndex(Exhibit.MAP_Y)));
        e.setMuseumAreaId(c.getString(c.getColumnIndex(Exhibit.MUSEUM_AREA_ID)));
        e.setBeaconId(c.getString(c.getColumnIndex(Exhibit.BEACON_ID)));
        e.setTexturl(c.getString(c.getColumnIndex(Exhibit.TEXT_URL)));
        e.setIconurl(c.getString(c.getColumnIndex(Exhibit.ICON_URL)));
        e.setAudiourl(c.getString(c.getColumnIndex(Exhibit.AUDIO_URL)));
        e.setImgsurl(c.getString(c.getColumnIndex(Exhibit.IMGS_URL)));
        e.setLabels(c.getString(c.getColumnIndex(Exhibit.LABELS)));
        e.setIntroduce(c.getString(c.getColumnIndex(Exhibit.INTRODUCE)));
        e.setContent(c.getString(c.getColumnIndex(Exhibit.CONTENT)));
        e.setLexhibit(c.getString(c.getColumnIndex(Exhibit.L_EXHIBIT)));
        e.setRexhibit(c.getString(c.getColumnIndex(Exhibit.R_EXHIBIT)));
        e.setVersion(c.getString(c.getColumnIndex(Exhibit.VERSION)));
        e.setPriority(c.getString(c.getColumnIndex(Exhibit.PRIORITY)));
        e.setIsFavorite(c.getInt(c.getColumnIndex(Exhibit.IS_FAVORITE)));
        return e;
    }



}
