package com.qiang.qiangguide.bean;

import android.content.ContentValues;

/**
 * Created by Qiang on 2016/9/26.
 */
public class Device extends BaseBean{

    public static final String TABLE_NAME = "device";
    public static final String _ID = "_id";
    public static final String NUMBER = "number";

    private int _id;

    private String number;


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Device device = (Device) o;

        return number != null ? number.equals(device.number) : device.number == null;

    }

    @Override
    public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Device{" +
                "_id=" + _id +
                ", number='" + number + '\'' +
                '}';
    }

    @Override
    ContentValues toContentValues() {
        ContentValues c = new ContentValues();
        c.put(_ID,_id);
        c.put(NUMBER,number);
        return c;
    }
}
