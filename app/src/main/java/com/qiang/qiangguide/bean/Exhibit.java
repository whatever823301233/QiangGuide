package com.qiang.qiangguide.bean;

import android.content.ContentValues;
import android.support.v4.media.MediaMetadataCompat;

/**
 * Created by Qiang on 2016/7/29.
 * 展品
 */
public class Exhibit extends BaseBean {

    public static final String TABLE_NAME="exhibit";


    public static final String _ID="_id";
    public static final String ID="id";
    public static final String MUSEUM_ID="museumId";
    public static final String MUSEUM_AREA_ID ="museumAreaId";
    public static final String BEACON_ID="beaconId";
    public static final String MAP_X="mapx";
    public static final String MAP_Y="mapy";
    public static final String NAME="name";
    public static final String NUMBER="number";
    public static final String TEXT_URL="texturl";
    public static final String ICON_URL="iconurl";
    public static final String AUDIO_URL="audiourl";
    public static final String IMGS_URL="imgsurl";
    public static final String LABELS="labels";
    public static final String INTRODUCE="introduce";
    public static final String CONTENT="content";
    public static final String L_EXHIBIT="lexhibit";
    public static final String R_EXHIBIT="rexhibit";
    public static final String VERSION="version";
    public static final String PRIORITY="priority";
    public static final String IS_FAVORITE="isfavorite";




    private int _id;
    private String id;
    private String museumId;
    private String museumAreaId;
    private String beaconId;
    private String mapx;
    private String mapy;
    private String name;
    private int number;
    private String texturl;
    private String iconurl;
    private String audiourl;
    private String imgsurl;
    private String labels;
    private String introduce;
    private String content;
    private String lexhibit;
    private String rexhibit;
    private String version;
    private String priority;
    private int isFavorite;

    public MediaMetadataCompat metadata;


    public String trackId;
    public Exhibit(){}


    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

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

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public String getMapx() {
        return mapx;
    }

    public void setMapx(String mapx) {
        this.mapx = mapx;
    }

    public String getMapy() {
        return mapy;
    }

    public void setMapy(String mapy) {
        this.mapy = mapy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTexturl() {
        return texturl;
    }

    public void setTexturl(String texturl) {
        this.texturl = texturl;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getAudiourl() {
        return audiourl;
    }

    public void setAudiourl(String audiourl) {
        this.audiourl = audiourl;
    }

    public String getImgsurl() {
        return imgsurl;
    }

    public void setImgsurl(String imgsurl) {
        this.imgsurl = imgsurl;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLexhibit() {
        return lexhibit;
    }

    public void setLexhibit(String lexhibit) {
        this.lexhibit = lexhibit;
    }

    public String getRexhibit() {
        return rexhibit;
    }

    public void setRexhibit(String rexhibit) {
        this.rexhibit = rexhibit;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Exhibit{" +
                "_id=" + _id +
                ", id='" + id + '\'' +
                ", museumId='" + museumId + '\'' +
                ", museumAreaId='" + museumAreaId + '\'' +
                ", beaconId='" + beaconId + '\'' +
                ", mapx=" + mapx +
                ", mapy=" + mapy +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", texturl='" + texturl + '\'' +
                ", iconurl='" + iconurl + '\'' +
                ", audiourl='" + audiourl + '\'' +
                ", imgsurl='" + imgsurl + '\'' +
                ", labels='" + labels + '\'' +
                ", introduce='" + introduce + '\'' +
                ", content='" + content + '\'' +
                ", lexhibit='" + lexhibit + '\'' +
                ", rexhibit='" + rexhibit + '\'' +
                ", version='" + version + '\'' +
                ", priority=" + priority +
                ", metadata=" + metadata +
                ", trackId='" + trackId + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Exhibit exhibit = (Exhibit) o;

        if (number != exhibit.number) return false;
        if (id != null ? !id.equals(exhibit.id) : exhibit.id != null) return false;
        if (museumId != null ? !museumId.equals(exhibit.museumId) : exhibit.museumId != null)
            return false;
        if (beaconId != null ? !beaconId.equals(exhibit.beaconId) : exhibit.beaconId != null)
            return false;
        return (name != null ? !name.equals(exhibit.name) : exhibit.name != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (museumId != null ? museumId.hashCode() : 0);
        result = 31 * result + (beaconId != null ? beaconId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + number;
        return result;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues cv=new ContentValues();
        cv.put(_ID,_id);
        cv.put(ID,id);
        cv.put(MUSEUM_ID,museumId);
        cv.put(MUSEUM_AREA_ID,museumAreaId);
        cv.put(BEACON_ID,beaconId);
        cv.put(MAP_X,mapx);
        cv.put(MAP_Y,mapy);
        cv.put(NAME,name);
        cv.put(NUMBER,number);
        cv.put(TEXT_URL,texturl);
        cv.put(ICON_URL,iconurl);
        cv.put(AUDIO_URL,audiourl);
        cv.put(IMGS_URL,imgsurl);
        cv.put(LABELS,labels);
        cv.put(INTRODUCE,introduce);
        cv.put(CONTENT,content);
        cv.put(L_EXHIBIT,lexhibit);
        cv.put(R_EXHIBIT,rexhibit);
        cv.put(VERSION,version);
        cv.put(PRIORITY,priority);
        cv.put(IS_FAVORITE,isFavorite);
        return cv;
    }

}
