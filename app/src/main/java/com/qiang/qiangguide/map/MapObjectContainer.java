package com.qiang.qiangguide.map;

import java.util.ArrayList;

/**
 * Created by Qiang on 2016/8/11.
 */
public class MapObjectContainer {

    private ArrayList<MapObjectModel> container;

    public MapObjectContainer()
    {
        container = new ArrayList<MapObjectModel>();
    }


    public void addObject(MapObjectModel object)
    {
        container.add(object);
    }


    public void removeObject(MapObjectModel object)
    {
        container.remove(object);
    }


    public MapObjectModel getObject(int index)
    {
        return container.get(index);
    }


    public MapObjectModel getObjectById(String id)
    {
        for (MapObjectModel model:container) {
            if (model.getId() .equals(id) ) {
                return model;
            }
        }

        return null;
    }


    public int size()
    {
        return container.size();
    }


}
