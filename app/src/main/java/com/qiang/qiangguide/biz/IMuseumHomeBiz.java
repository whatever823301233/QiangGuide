package com.qiang.qiangguide.biz;

import com.qiang.qiangguide.bean.Museum;

import java.util.List;

/**
 * Created by Qiang on 2016/7/29.
 */
public interface IMuseumHomeBiz {

    void initAudio();

    List<String> getImgUrls(Museum museum);
}
