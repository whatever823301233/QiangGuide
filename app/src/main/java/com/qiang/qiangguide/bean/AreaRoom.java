package com.qiang.qiangguide.bean;

import android.content.ContentValues;

/**
 * Created by Qiang on 2016/9/26.
 */
public class AreaRoom  extends BaseBean{

    public static String TABLE_NAME="area_room";
    public static final String _ID="_id";
    public static final String NAME="name";


    private int _id;

    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AreaRoom areaRoom = (AreaRoom) o;

        return name != null ? name.equals(areaRoom.name) : areaRoom.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AreaRoom{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    ContentValues toContentValues() {
        ContentValues c=new ContentValues();
        c.put(_ID,_id);
        c.put(NAME,name);
        return c;
    }
}
