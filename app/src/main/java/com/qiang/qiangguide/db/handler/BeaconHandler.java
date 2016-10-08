package com.qiang.qiangguide.db.handler;

import android.database.Cursor;

import com.qiang.qiangguide.bean.MyBeacon;
import com.qiang.qiangguide.db.DBHandler;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Qiang on 2016/9/26.
 */
public class BeaconHandler {

    /**
     * add MyBeacons
     * @param beacons
     */
    public static void addBeacons(List<MyBeacon> beacons) {
        DBHandler.getInstance().getDB().beginTransaction();  //开始事务
        for (MyBeacon beacon : beacons) {
            DBHandler.getInstance().getDB().execSQL("INSERT INTO "+MyBeacon.TABLE_NAME
                            +" VALUES(null,?,?,?,?,?,?,?,?,?)",
                    new Object[]{
                            beacon.getId(),
                            beacon.getMajor(),
                            beacon.getMinor(),
                            beacon.getUuid(),
                            beacon.getMuseumId(),
                            beacon.getType(),
                            beacon.getMuseumAreaId(),
                            beacon.getPersonx(),
                            beacon.getPersony(),
                    });

        }
        DBHandler.getInstance().getDB().setTransactionSuccessful();  //设置事务成功完成
        DBHandler.getInstance().getDB().endTransaction();    //结束事务
    }


    /**
     * 查询beacon
     * @param museumId museumId
     * @return   List<MyBeacon>
     */
    public static List<MyBeacon> queryBeacons(String museumId) {
        DBHandler.getInstance().getDB().beginTransaction();
        Cursor c = DBHandler.getInstance().getDB().rawQuery(
                "SELECT * FROM "
                        + MyBeacon.TABLE_NAME
                        +" WHERE "
                        + MyBeacon.MUSEUM_ID
                        +" = ? ",
                new String[]{
                        museumId
                });
        List<MyBeacon> beacons = new ArrayList<>();
        while (c.moveToNext()) {
            MyBeacon beacon = buildBeaconByCursor(c);
            beacons.add(beacon);
        }
        c.close();
        DBHandler.getInstance().getDB().endTransaction();
        return beacons;
    }
    /**
     * 查询beacon
     * @param minor minor
     * @param major major
     * @return   List<MyBeacon>
     */
    public static MyBeacon queryBeacons(Identifier minor, Identifier major) {
        DBHandler.getInstance().getDB().beginTransaction();
        Cursor c = DBHandler.getInstance().getDB().rawQuery(
                "SELECT * FROM "
                        + MyBeacon.TABLE_NAME
                        +" WHERE "
                        +MyBeacon.MINOR
                        +" = ? AND "
                        +MyBeacon.MAJOR
                        +" = ?",
                new String[]{
                        String.valueOf(minor),
                        String.valueOf(major)
                });
        MyBeacon beacon=null;
        if (c.moveToNext()) {
            beacon = buildBeaconByCursor(c);
        }
        c.close();
        DBHandler.getInstance().getDB().endTransaction();
        return beacon;
    }
    /**
     * 查询beacon
     * @param minor minor
     * @param major major
     * @return   List<MyBeacon>
     */
    public static MyBeacon queryBeacons(String minor, String major) {
        DBHandler.getInstance().getDB().beginTransaction();
        Cursor c = DBHandler.getInstance().getDB().rawQuery(
                "SELECT * FROM "
                        + MyBeacon.TABLE_NAME
                        +" WHERE "
                        +MyBeacon.MINOR
                        +" = ? AND "
                        +MyBeacon.MAJOR
                        +" = ?",
                new String[]{
                        String.valueOf(minor),
                        String.valueOf(major)
                });
        MyBeacon beacon=null;
        while (c.moveToNext()) {
            beacon = buildBeaconByCursor(c);
        }
        c.close();
        DBHandler.getInstance().getDB().endTransaction();
        return beacon;
    }
    /**
     * 查询beacon
     * @param beacons beacons
     * @return   List<MyBeacon>
     */
    public  static List<MyBeacon> queryBeacons(Collection<Beacon> beacons) {
        DBHandler.getInstance().getDB().beginTransaction();
        List<MyBeacon> mBeacons = new ArrayList<>();
        String sql="SELECT * FROM beacon WHERE minor = ? ";/* "SELECT * FROM "
                + MyBeacon.TABLE_NAME
                +" WHERE "
                +MyBeacon.MINOR
                          *//*  +" = ? AND "
                            +MyBeacon.MAJOR*//*
                +" = ?"*/
        for(Beacon b:beacons){
            Cursor c = DBHandler.getInstance().getDB().rawQuery(
                    sql,
                    new String[]{
                            String.valueOf(b.getId3())//, // TODO: 2016/8/10
                            // String.valueOf(b.getId2())
                    });
            while (c.moveToNext()) {
                MyBeacon beacon = buildBeaconByCursor(c);
                mBeacons.add(beacon);
            }
            c.close();
        }
        DBHandler.getInstance().getDB().endTransaction();
        return mBeacons;
    }

    private  static MyBeacon buildBeaconByCursor(Cursor c) {
        MyBeacon b = new MyBeacon();
        b.set_id(c.getInt(c.getColumnIndex(MyBeacon._ID)));
        b.setId(c.getString(c.getColumnIndex(MyBeacon.ID)));
        b.setMuseumId(c.getString(c.getColumnIndex(MyBeacon.MUSEUM_ID)));
        b.setMajor(c.getInt(c.getColumnIndex(MyBeacon.MAJOR)));
        b.setMinor(c.getInt(c.getColumnIndex(MyBeacon.MINOR)));
        b.setMuseumAreaId(c.getString(c.getColumnIndex(MyBeacon.MUSEUM_AREA_ID)));
        b.setUuid(c.getString(c.getColumnIndex(MyBeacon.UUID)));
        b.setType(c.getString(c.getColumnIndex(MyBeacon.TYPE)));
        b.setPersonx(c.getDouble(c.getColumnIndex(MyBeacon.PERSONX)));
        b.setPersony(c.getDouble(c.getColumnIndex(MyBeacon.PERSONY)));
        return b;
    }




}
