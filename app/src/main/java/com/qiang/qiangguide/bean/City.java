package com.qiang.qiangguide.bean;

import android.content.ContentValues;

/**
 * Created by Qiang on 2016/7/29.
 */
public class City extends BaseBean {


    public static final String TABLE_NAME="city";

    public static final String _ID="_id";
    public static final String NAME="name";
    public static final String ALPHA="alpha";


    private int _id;
    private String name;//城市名称
    private String alpha;//城市名称首字母

    public City(){}

    public City(int _id, String name, String alpha) {
        this._id = _id;
        this.name = name;
        this.alpha = alpha;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlpha() {
        return alpha;
    }

    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        if (_id != city._id) return false;
        if (name != null ? !name.equals(city.name) : city.name != null) return false;
        return alpha != null ? alpha.equals(city.alpha) : city.alpha == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "City{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", alpha='" + alpha + '\'' +
                '}';
    }

    @Override
    ContentValues toContentValues() {
        ContentValues cv=new ContentValues();
        cv.put(_ID,_id);
        cv.put(NAME,name);
        cv.put(ALPHA,alpha);
        return cv;
    }
}
