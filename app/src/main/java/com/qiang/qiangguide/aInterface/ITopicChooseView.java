package com.qiang.qiangguide.aInterface;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.qiang.qiangguide.custom.channel.ChannelItem;

import java.util.ArrayList;

/**
 * Created by Qiang on 2016/8/12.
 */
public interface ITopicChooseView {

    void showLoading();

    void hideLoading();

    void toNextActivity(Intent intent);

    String getTag();


    boolean isMove();

    void hideUserAddOtherItem(AdapterView<?> parent, View view, int position, long id);

    void hideOtherAddUserItem(AdapterView<?> parent, View view, int position, long id);

    void updateUserChannel(ArrayList<ChannelItem> userChannelList);

    void updateOtherChannel(ArrayList<ChannelItem> otherChannelList);
}
