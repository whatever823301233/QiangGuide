package com.qiang.qiangguide.biz;

/**
 * Created by Qiang on 2016/8/4.
 */
public interface ITopicBiz {
    void initAllExhibitByMuseumId(String museumId, OnInitBeanListener listener, String tag);
}
