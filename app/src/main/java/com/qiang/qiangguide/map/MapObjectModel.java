package com.qiang.qiangguide.map;

import android.location.Location;

/**
 * Created by Qiang on 2016/8/11.
 */
public class MapObjectModel {

    private int x;
    private int y;
    private String id;
    private String caption;
    private Location location;

    public MapObjectModel(String id, Location location, String caption)
    {
        this.location = location;
        this.caption = caption;
        this.id = id;
    }

    public MapObjectModel(String id, int x, int y, String caption)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.caption = caption;
    }

    public String getId()
    {
        return id;
    }


    public int getX()
    {
        return x;
    }


    public int getY()
    {
        return y;
    }


    public Location getLocation()
    {
        return location;
    }


    public String getCaption()
    {
        return caption;
    }


}
