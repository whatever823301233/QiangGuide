package com.qiang.qiangguide.bean;

import android.content.ContentValues;

/**
 * Created by Qiang on 2016/9/26.
 */
public class DeviceRecorder extends BaseBean{

    public static final String TABLE_NAME = "device_recorder";
    public static final String _ID = "_id";
    public static final String EXHIBIT_ID = "exhibitId";
    public static final String AREA_ROOM_NAME = "areaRoomName";
    public static final String DEVICE_NUM = "deviceNum";
    public static final String TIME = "time";

    private int _id;
    private String exhibitId;
    private String areaRoomName;
    private String deviceNum;
    private String time;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getExhibitId() {
        return exhibitId;
    }

    public void setExhibitId(String exhibitId) {
        this.exhibitId = exhibitId;
    }

    public String getAreaRoomName() {
        return areaRoomName;
    }

    public void setAreaRoomName(String areaRoomName) {
        this.areaRoomName = areaRoomName;
    }

    public String getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    @Override
    public String toString() {
        return "DeviceRecorder{" +
                "_id=" + _id +
                ", exhibitId='" + exhibitId + '\'' +
                ", areaRoomName='" + areaRoomName + '\'' +
                ", deviceNum='" + deviceNum + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    @Override
    ContentValues toContentValues() {

        ContentValues c =new ContentValues();
        c.put(_ID,_id);
        c.put(EXHIBIT_ID,exhibitId);
        c.put(AREA_ROOM_NAME,areaRoomName);
        c.put(DEVICE_NUM, deviceNum);
        c.put(TIME,time);
        return c ;
    }
}
