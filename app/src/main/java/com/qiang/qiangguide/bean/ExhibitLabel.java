package com.qiang.qiangguide.bean;

import android.content.ContentValues;

/**
 * Created by Qiang on 2016/7/29.
 */
public class ExhibitLabel extends BaseBean {


    public static final String _ID="_ID";
    public static final String ID="id";
    public static final String MUSEUM_ID ="museumId";
    public static final String NAME="name";
    public static final String LABELS="labels";

    private int _id;
    private String id;
    private String msueumId;
    private String name;
    private String labels;

    public ExhibitLabel(){}

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

    public String getMsueumId() {
        return msueumId;
    }

    public void setMsueumId(String msueumId) {
        this.msueumId = msueumId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    @Override
    public String toString() {
        return "ExhibitLabel{" +
                "_id=" + _id +
                ", id='" + id + '\'' +
                ", msueumId='" + msueumId + '\'' +
                ", name='" + name + '\'' +
                ", labels='" + labels + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExhibitLabel that = (ExhibitLabel) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (msueumId != null ? !msueumId.equals(that.msueumId) : that.msueumId != null)
            return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (msueumId != null ? msueumId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    ContentValues toContentValues() {
        ContentValues cv=new ContentValues();
        cv.put(_ID,id);
        cv.put(ID,id);
        cv.put(MUSEUM_ID,id);
        cv.put(NAME,id);
        cv.put(LABELS,id);
        return cv;
    }
}
