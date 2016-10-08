package com.qiang.qiangguide.biz.bizImpl;

import com.qiang.qiangguide.bean.Label;
import com.qiang.qiangguide.biz.ITopicChooseBiz;
import com.qiang.qiangguide.db.handler.LabelHandler;

import java.util.List;

/**
 * Created by Qiang on 2016/8/12.
 */
public class TopicChooseBiz implements ITopicChooseBiz {
    @Override
    public List<Label> getLabels(String museumId) {
        return LabelHandler.queryLabels(museumId);
    }
}
