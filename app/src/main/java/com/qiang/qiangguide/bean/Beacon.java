package com.qiang.qiangguide.bean;

import android.content.ContentValues;

/**
 * Created by Qiang on 2016/7/29.
 */
public class Beacon extends BaseBean{

    public static final String _ID ="_ID";
    public static final String ID ="ID";
    public static final String MUSEUM_ID ="museumId";
    public static final String MUSEUM_AREA_ID ="museumareaId";
    public static final String PERSONX ="personx";
    public static final String PERSONY ="persony";
    public static final String TYPE ="type";
    public static final String UUID ="uuid";
    public static final String MAJOR ="major";
    public static final String MINOR ="minor";

    private int _id;
    private String id;
    private String museumId;
    private String museumAreaId;
    private String personx;
    private String persony;
    private String type;
    private String uuid;
    private String major;
    private String minor;

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

    public String getPersonx() {
        return personx;
    }

    public void setPersonx(String personx) {
        this.personx = personx;
    }

    public String getPersony() {
        return persony;
    }

    public void setPersony(String persony) {
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

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }


    @Override
    public String toString() {
        return "Beacon{" +
                "_id=" + _id +
                ", id='" + id + '\'' +
                ", museumId='" + museumId + '\'' +
                ", museumAreaId='" + museumAreaId + '\'' +
                ", personx='" + personx + '\'' +
                ", persony='" + persony + '\'' +
                ", type='" + type + '\'' +
                ", uuid='" + uuid + '\'' +
                ", major='" + major + '\'' +
                ", minor='" + minor + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Beacon beacon = (Beacon) o;

        if (id != null ? !id.equals(beacon.id) : beacon.id != null) return false;
        if (uuid != null ? !uuid.equals(beacon.uuid) : beacon.uuid != null) return false;
        if (major != null ? !major.equals(beacon.major) : beacon.major != null) return false;
        return minor != null ? minor.equals(beacon.minor) : beacon.minor == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (major != null ? major.hashCode() : 0);
        result = 31 * result + (minor != null ? minor.hashCode() : 0);
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
        return cv;
    }
}
