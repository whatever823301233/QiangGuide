package com.qiang.qiangguide.biz;

import com.qiang.qiangguide.bean.Museum;

import java.util.List;

/**
 * Created by Qiang on 2016/7/29.
 */
public interface IMuseumChooseBiz {

    void initMuseumListByNet(String city, String tag, OnInitBeanListener onInitBeanListener);

    List<Museum> initMuseumListBySQL(String city);
}
