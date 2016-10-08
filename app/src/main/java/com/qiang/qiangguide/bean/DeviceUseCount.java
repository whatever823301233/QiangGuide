package com.qiang.qiangguide.bean;

import android.content.ContentValues;

/**
 * Created by Qiang on 2016/9/26.
 */
public class DeviceUseCount extends BaseBean{

    public static final String TABLE_NAME="device_use_count";
    public static final String _ID="_id";
    public static final String USE_TIME="useTime";
    public static final String RETURN_TIME="returnTime";
    public static final String DEVICE_NUM="deviceNum";
    public static final String USE_ID="useId";

    private int _id;

    private String useTime;

    private String returnTime;

    private String deviceNum;

    private String useId;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    public String getUseId() {
        return useId;
    }

    public void setUseId(String useId) {
        this.useId = useId;
    }

    @Override
    public String toString() {
        return "DeviceUseCount{" +
                "_id=" + _id +
                ", useTime=" + useTime +
                ", returnTime=" + returnTime +
                ", deviceNum='" + deviceNum + '\'' +
                ", useId='" + useId + '\'' +
                '}';
    }


    @Override
    ContentValues toContentValues() {

        ContentValues c=new ContentValues();
        c.put(_ID,_id);
        c.put(USE_TIME,useTime);
        c.put(RETURN_TIME,returnTime);
        c.put(DEVICE_NUM, deviceNum);
        c.put(USE_ID, useId);

        return c;
    }
}
