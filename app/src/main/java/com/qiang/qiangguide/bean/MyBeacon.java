package com.qiang.qiangguide.bean;

import android.content.ContentValues;

/**
 * Created by Qiang on 2016/7/29.
 */
public class MyBeacon extends BaseBean{

    public static final String TABLE_NAME="beacon";

    public static final String _ID ="_ID";
    public static final String ID ="ID";
    public static final String MAJOR ="major";
    public static final String MINOR ="minor";
    public static final String UUID ="uuid";
    public static final String MUSEUM_ID ="museumId";
    public static final String TYPE ="type";
    public static final String MUSEUM_AREA_ID ="museumareaId";
    public static final String PERSONX ="personx";
    public static final String PERSONY ="persony";
    public static final String DISTANCE ="distance";

    private int _id;
    private String id;
    private String museumId;
    private String museumAreaId;
    private double personx;
    private double persony;
    private String type;
    private String uuid;
    private int major;
    private int minor;
    private double distance;


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMuseumId() {
        return museumId;
    }

    public void setMuseumId(String museumId) {
        this.museumId = museumId;
    }

    public String getMuseumAreaId() {
        return museumAreaId;
    }

    public void setMuseumAreaId(String museumAreaId) {
        this.museumAreaId = museumAreaId;
    }

    public double getPersonx() {
        return personx;
    }

    public void setPersonx(double personx) {
        this.personx = personx;
    }

    public double getPersony() {
        return persony;
    }

    public void setPersony(double persony) {
        this.persony = persony;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyBeacon beacon = (MyBeacon) o;

        if (id != null ? !id.equals(beacon.id) : beacon.id != null) return false;
        return museumId != null ? museumId.equals(beacon.museumId) : beacon.museumId == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (museumId != null ? museumId.hashCode() : 0);
        return result;
    }

    @Override
    ContentValues toContentValues() {

        ContentValues cv=new ContentValues();
        cv.put(_ID,_id);
        cv.put(ID,id);
        cv.put(MUSEUM_ID,museumId);
        cv.put(MUSEUM_AREA_ID,museumAreaId);
        cv.put(PERSONX,personx);
        cv.put(PERSONY,persony);
        cv.put(TYPE,type);
        cv.put(UUID,uuid);
        cv.put(MAJOR,major);
        cv.put(MINOR,minor);
        cv.put(DISTANCE,distance);
        return cv;
    }
}
