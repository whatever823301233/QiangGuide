package com.qiang.qiangguide.bean;

/**
 * Created by Qiang on 2016/9/12.
 *
 * 多角度图片实体类
 */
public class MultiAngleImg {

    private int time;
    private String url;
    private String museumId;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMuseumId() {
        return museumId;
    }

    public void setMuseumId(String museumId) {
        this.museumId = museumId;
    }

    @Override
    public String toString() {
        return "MultiAngleImg{" +
                "time=" + time +
                ", url='" + url + '\'' +
                ", museumId='" + museumId + '\'' +
                '}';
    }

}
