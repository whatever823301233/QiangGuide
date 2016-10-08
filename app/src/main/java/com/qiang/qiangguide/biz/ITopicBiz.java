package com.qiang.qiangguide.biz;

import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.custom.channel.ChannelItem;

import java.util.List;

/**
 * Created by Qiang on 2016/8/4.
 */
public interface ITopicBiz {
    void initAllExhibitByMuseumId(String museumId, OnInitBeanListener listener, String tag);

    List<Exhibit> getExhibit(List<ChannelItem> channelItemList);

    List<Exhibit> getExhibit(ChannelItem channelItem);
}
