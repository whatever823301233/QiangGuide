package com.qiang.qiangguide.biz;

import com.qiang.qiangguide.bean.Label;

import java.util.List;

/**
 * Created by Qiang on 2016/8/12.
 */
public interface ITopicChooseBiz {
    List<Label> getLabels(String museumId);
}
