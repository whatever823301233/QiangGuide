package com.qiang.qiangguide.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.qiang.qiangguide.bean.City;
import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.bean.Label;
import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.bean.MyBeacon;
import com.qiang.qiangguide.bean.User;
import com.qiang.qiangguide.util.LogUtil;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collection;
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




    /**
     * add users
     * @param city
     */
    public void addUser(City city) {
        try{
            getDB().beginTransaction();  //开始事务
            getDB().execSQL("INSERT INTO "+User.TABLE_NAME+" VALUES(null, ?, ?)",
                    new Object[]{city.getAlpha(), city.getName()});
            getDB().setTransactionSuccessful();  //设置事务成功完成
        } catch (Exception e){
            LogUtil.e("",e);
        }finally {
            getDB().endTransaction();    //结束事务
        }

    }


    /**
     * add cities
     * @param cities
     */
    public void addCities(List<City> cities) {
        if(cities==null){return;}
        getDB().beginTransaction();  //开始事务
        try {
            for (City city : cities) {
                getDB().execSQL("INSERT INTO "+City.TABLE_NAME+" VALUES(null, ?, ?)",
                        new Object[]{city.getAlpha(), city.getName()});
            }
            getDB().setTransactionSuccessful();  //设置事务成功完成
            LogUtil.i("","addCities 保存成功");
        } catch (Exception e){
            LogUtil.e("",e);
        }finally {
            getDB().endTransaction();    //结束事务
        }
    }

    /**
     * query all users, return list
     * @return ArrayList<User>
     */
    public ArrayList<City> queryAllCities() {
        ArrayList<City> persons = new ArrayList<>();
        Cursor c = getDB().rawQuery("SELECT * FROM "+City.TABLE_NAME, null);
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


    /**
     * add users
     * @param user
     */
    public void addUser(User user) {
        try{
            getDB().beginTransaction();  //开始事务
            getDB().execSQL("INSERT INTO "+User.TABLE_NAME+" VALUES(null, ?, ?)", new Object[]{user.getUsername(), user.getPassword()});
            getDB().setTransactionSuccessful();  //设置事务成功完成
        } catch (Exception e){
            LogUtil.e("",e);
        }finally {
            getDB().endTransaction();    //结束事务
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
    public void update(User user) {
        ContentValues cv = new ContentValues();
        cv.put(User.PASSWORD, user.getPassword());
        cv.put(User.USERNAME, user.getUsername());
        getDB().update(User.TABLE_NAME, cv, User.USERNAME+" = ?", new String[]{user.getUsername()});
    }

    /**
     * delete old user
     * @param userName
     */
    public void deleteOldUser(String userName) {
        getDB().beginTransaction();
        getDB().delete(User.TABLE_NAME, User.USERNAME+"= ?", new String[]{userName});
        getDB().endTransaction();
    }

    /**
     * query all users, return list
     * @return List<User>
     */
    public List<User> queryAllUser() {
        getDB().beginTransaction();
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
        getDB().setTransactionSuccessful();
        getDB().endTransaction();
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
    public Cursor queryUserCursor() {
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
     * add museumList
     * @param museumList
     */
    public void addMuseumList(List<Museum> museumList) {
        if(museumList==null){return;}
        getDB().beginTransaction();  //开始事务
        try {
            for (Museum m : museumList) {
                getDB().execSQL("INSERT INTO "+Museum.TABLE_NAME
                                +" VALUES(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                        new Object[]{
                                m.getId(),
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
                                m.getPriority()
                        });
            }
            getDB().setTransactionSuccessful();  //设置事务成功完成
            LogUtil.i("","addMuseums 保存成功");
        } catch (Exception e){
            LogUtil.e("",e);
        }finally {
            getDB().endTransaction();    //结束事务
        }
    }


    /**
     * query all users, return list
     * @param city
     * @return List<Museum>
     */
    public List<Museum> queryAllMuseumByCity(String city) {
        List<Museum> museumList = new ArrayList<>();
        Cursor c = getDB().rawQuery("SELECT * FROM "+Museum.TABLE_NAME +" WHERE "+Museum.CITY+" = ?",new String[]{city});
        while (c.moveToNext()) {
            Museum museum = new Museum();
            museum.set_id(c.getInt(c.getColumnIndex(Museum._ID)));
            museum.setId(c.getString(c.getColumnIndex(Museum.ID)));
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
            museum.setPriority(c.getInt(c.getColumnIndex(Museum.PRIORITY)));
            museumList.add(museum);
        }
        c.close();
        return museumList;
    }




    /**
     * add museumList
     * @param exhibitList
     */
    public void addExhibitList(List<Exhibit> exhibitList) {
        if(exhibitList==null){return;}
        getDB().beginTransaction();  //开始事务
        try {
            for (Exhibit e : exhibitList) {
                getDB().execSQL("INSERT INTO "+Exhibit.TABLE_NAME
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
            getDB().setTransactionSuccessful();  //设置事务成功完成
            LogUtil.i("","addExhibitList 保存成功");
        } catch (Exception e){
            LogUtil.e("",e);
        }finally {
            getDB().endTransaction();    //结束事务
        }
    }



    /**
     * query all exhibits, return list
     * @return List<Exhibit>
     */
    public List<Exhibit> queryAllExhibitListByMuseumId(String museumId) {
        List<Exhibit> exhibitList = new ArrayList<>();
        Cursor c = getDB().rawQuery("SELECT * FROM "+Exhibit.TABLE_NAME +" WHERE "+Exhibit.MUSEUM_ID+" = ?",new String[]{museumId});
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
    public List<Exhibit> queryFavoriteExhibitListByMuseumId(String museumId) {
        List<Exhibit> exhibitList = new ArrayList<>();
        Cursor c = getDB().rawQuery("SELECT * FROM "+Exhibit.TABLE_NAME
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
    public List<Exhibit> queryAllExhibitList() {
        List<Exhibit> exhibitList = new ArrayList<>();
        getDB().beginTransaction();
        Cursor c = getDB().rawQuery("SELECT * FROM "+Exhibit.TABLE_NAME ,null);
        while (c.moveToNext()) {
            Exhibit e = buildExhibitByCursor(c);
            exhibitList.add(e);
        }
        c.close();
        getDB().endTransaction();
        return exhibitList;
    }

    @NonNull
    private Exhibit buildExhibitByCursor(Cursor c) {
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


    /**
     * query  exhibit, return exhibit
     * @return Exhibit
     */
    public Exhibit queryExhibitById(String id,String museumId) {
        Cursor c = getDB().rawQuery("SELECT * FROM "+Exhibit.TABLE_NAME +" WHERE "
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
    public void updateExhibit(Exhibit exhibit) {
        getDB().beginTransaction();
        ContentValues cv =exhibit.toContentValues();
        getDB().update(Exhibit.TABLE_NAME, cv, Exhibit.ID+" = ?", new String[]{exhibit.getId()});
        getDB().setTransactionSuccessful();
        getDB().endTransaction();
    }

    /**
     * 查询展品
     * @param museumId museumId
     * @param beaconId beaconId
     * @return List<Exhibit>
     */
    public List<Exhibit> queryExhibit(String museumId, String  beaconId) {
        getDB().beginTransaction();
        Cursor c=getDB().rawQuery(
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
        getDB().setTransactionSuccessful();
        getDB().endTransaction();
        return exhibits;
    }


    /**
     * 查询展品
     * @param museumId museumId
     * @param exhibitId exhibitId
     * @return exhibit
     */
    public Exhibit querySingleExhibit(String museumId, String  exhibitId) {
        getDB().beginTransaction();
        Cursor c=getDB().rawQuery(
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
        getDB().setTransactionSuccessful();
        getDB().endTransaction();
        return e;
    }



    /**
     * 查询展品
     * @param museumId museumId
     * @param beaconList beaconList
     * @return List<Exhibit>
     */
    public List<Exhibit> queryExhibit(String museumId, List<MyBeacon>  beaconList) {
        getDB().beginTransaction();
        List<Exhibit> exhibits = new ArrayList<>();

        for(MyBeacon b:beaconList){
            Cursor c=getDB().rawQuery(
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
        getDB().setTransactionSuccessful();
        getDB().endTransaction();
        return exhibits;
    }


    /**
     * add MyBeacons
     * @param beacons
     */
    public void addBeacons(List<MyBeacon> beacons) {
        getDB().beginTransaction();  //开始事务
        for (MyBeacon beacon : beacons) {
            getDB().execSQL("INSERT INTO "+MyBeacon.TABLE_NAME
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
        getDB().setTransactionSuccessful();  //设置事务成功完成
        getDB().endTransaction();    //结束事务
    }


    /**
     * 查询beacon
     * @param museumId museumId
     * @return   List<MyBeacon>
     */
    public List<MyBeacon> queryBeacons(String museumId) {
        getDB().beginTransaction();
        Cursor c = getDB().rawQuery(
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
        getDB().endTransaction();
        return beacons;
    }
    /**
     * 查询beacon
     * @param minor minor
     * @param major major
     * @return   List<MyBeacon>
     */
    public List<MyBeacon> queryBeacons(int minor,int major) {
        getDB().beginTransaction();
        Cursor c = getDB().rawQuery(
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
        List<MyBeacon> beacons = new ArrayList<>();
        while (c.moveToNext()) {
            MyBeacon beacon = buildBeaconByCursor(c);
            beacons.add(beacon);
        }
        c.close();
        getDB().endTransaction();
        return beacons;
    }
    /**
     * 查询beacon
     * @param beacons beacons
     * @return   List<MyBeacon>
     */
    public List<MyBeacon> queryBeacons(Collection<Beacon> beacons) {
        getDB().beginTransaction();
        List<MyBeacon> mBeacons = new ArrayList<>();
        String sql="SELECT * FROM beacon WHERE minor = ? ";/* "SELECT * FROM "
                + MyBeacon.TABLE_NAME
                +" WHERE "
                +MyBeacon.MINOR
                          *//*  +" = ? AND "
                            +MyBeacon.MAJOR*//*
                +" = ?"*/
        for(Beacon b:beacons){
            Cursor c = getDB().rawQuery(
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
        getDB().endTransaction();
        return mBeacons;
    }

    private MyBeacon buildBeaconByCursor(Cursor c) {
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

    /**
     * add Labels
     * @param labelList
     */
    public void addLabels(List<Label> labelList) {
        if(labelList==null){return;}
        getDB().beginTransaction();  //开始事务
        try {
            for (Label label : labelList) {
                getDB().execSQL(
                        "INSERT INTO "+Label.TABLE_NAME +" VALUES(null,?,?,?,?)",
                        new Object[]{
                                label.getId(),
                                label.getMuseumId(),
                                label.getName(),
                                label.getLabels()
                        });
            }
            getDB().setTransactionSuccessful();  //设置事务成功完成
            LogUtil.i("","addLabels 保存成功");
        } catch (Exception e){
            LogUtil.e("",e);
        }finally {
            getDB().endTransaction();    //结束事务
        }
    }


    /**
     * query all Label, return list
     * @return List<Label>
     */
    public List<Label> queryLabels(String museumId) {
        List<Label> labelList = new ArrayList<>();
        Cursor c = getDB().rawQuery(
                "SELECT * FROM " + Label.TABLE_NAME
                +" WHERE "+Label.MUSEUM_ID
                        +" = ?",
                new String[]{museumId}
        );
        while (c.moveToNext()) {
            Label l =buildLabelByCursor(c);
            labelList.add(l);
        }
        c.close();
        return labelList;
    }

    private Label buildLabelByCursor(Cursor c) {

        Label l = new Label();
        l.set_id(c.getInt(c.getColumnIndex(Label._ID)));
        l.setId(c.getString(c.getColumnIndex(Label.ID)));
        l.setMuseumId(c.getString(c.getColumnIndex(Label.MUSEUM_ID)));
        l.setName(c.getString(c.getColumnIndex(Label.NAME)));
        l.setLabels(c.getString(c.getColumnIndex(Label.LABELS)));
        return l;
    }


}
