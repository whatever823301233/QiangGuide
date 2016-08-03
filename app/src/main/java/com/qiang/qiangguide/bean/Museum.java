package com.qiang.qiangguide.bean;

import android.content.ContentValues;

/**
 * Created by Qiang on 2016/7/29.
 */
public class Museum extends BaseBean {

    public static final String TABLE_NAME="museum";


    public static final String _ID = "_id";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String LONGITUDE_X = "longitudex";
    public static final String LONGITUDE_Y = "longitudey";
    public static final String ICON_URL = "iconurl";
    public static final String ADDRESS = "address";
    public static final String OPEN_TIME = "opentime";
    public static final String IS_OPEN = "isopen";
    public static final String TEXT_URL = "texturl";
    public static final String FLOOR_COUNT = "floorcount";
    public static final String IMG_URL = "imgurl";
    public static final String AUDIO_URL = "audiourl";
    public static final String CITY = "city";
    public static final String VERSION = "version";
    public static final String PRIORITY = "priority";


    private int _id;
    private String id;
    private String name;
    private String longitudex;
    private String longitudey;
    private String iconurl;
    private String address;
    private String opentime;
    private String isopen;
    private String texturl;
    private int floorcount;
    private String imgurl;
    private String audiourl;
    private String city;
    private int version;
    private int priority;


    public Museum (){}

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongitudex() {
        return longitudex;
    }

    public void setLongitudex(String longitudex) {
        this.longitudex = longitudex;
    }

    public String getLongitudey() {
        return longitudey;
    }

    public void setLongitudey(String longitudey) {
        this.longitudey = longitudey;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getIsopen() {
        return isopen;
    }

    public void setIsopen(String isopen) {
        this.isopen = isopen;
    }

    public String getTexturl() {
        return texturl;
    }

    public void setTexturl(String texturl) {
        this.texturl = texturl;
    }

    public int getFloorcount() {
        return floorcount;
    }

    public void setFloorcount(int floorcount) {
        this.floorcount = floorcount;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getAudiourl() {
        return audiourl;
    }

    public void setAudiourl(String audiourl) {
        this.audiourl = audiourl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Museum{" +
                "_id=" + _id +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", longitudex='" + longitudex + '\'' +
                ", longitudey='" + longitudey + '\'' +
                ", iconurl='" + iconurl + '\'' +
                ", address='" + address + '\'' +
                ", opentime='" + opentime + '\'' +
                ", isopen='" + isopen + '\'' +
                ", texturl='" + texturl + '\'' +
                ", floorcount=" + floorcount +
                ", imgurl='" + imgurl + '\'' +
                ", audiourl='" + audiourl + '\'' +
                ", city='" + city + '\'' +
                ", version=" + version +
                ", priority=" + priority +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Museum museum = (Museum) o;

        if (id != null ? !id.equals(museum.id) : museum.id != null) return false;
        if (name != null ? !name.equals(museum.name) : museum.name != null) return false;
        if (address != null ? !address.equals(museum.address) : museum.address != null)
            return false;
        return city != null ? city.equals(museum.city) : museum.city == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result;
    }

    @Override
    ContentValues toContentValues() {
        ContentValues cv=new ContentValues();
        cv.put(_ID,_id);
        cv.put(NAME,name);
        cv.put(LONGITUDE_X,longitudex);
        cv.put(LONGITUDE_Y,longitudey);
        cv.put(ICON_URL,iconurl);
        cv.put(ADDRESS,address);
        cv.put(OPEN_TIME,opentime);
        cv.put(TEXT_URL,texturl);
        cv.put(FLOOR_COUNT,floorcount);
        cv.put(IMG_URL,imgurl);
        cv.put(AUDIO_URL,audiourl);
        cv.put(CITY,city);
        cv.put(VERSION,version);
        cv.put(PRIORITY,priority);
        return cv;
    }
}
