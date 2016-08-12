package com.qiang.qiangguide.bean;

import android.content.ContentValues;

/**
 * Created by Qiang on 2016/8/12.
 */
public class Label extends BaseBean {

    public static final String TABLE_NAME = "label";

    public static final String _ID = "_id";
    public static final String ID = "id";
    public static final String MUSEUM_ID = "museumId";
    public static final String NAME = "name";
    public static final String LABELS = "lables";


    private int _id;
    private String id;
    private String name;
    private String museumId;
    private String labels;

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

    public String getMuseumId() {
        return museumId;
    }

    public void setMuseumId(String museumId) {
        this.museumId = museumId;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    @Override
    public String toString() {
        return "Label{" +
                "_id=" + _id +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", museumId='" + museumId + '\'' +
                ", labels='" + labels + '\'' +
                '}';
    }

    @Override
    ContentValues toContentValues() {
        ContentValues c=new ContentValues();
        c.put(_ID,_id);
        c.put(ID,id);
        c.put(NAME,name);
        c.put(MUSEUM_ID,museumId);
        c.put(LABELS,labels);
        return c;
    }
}
