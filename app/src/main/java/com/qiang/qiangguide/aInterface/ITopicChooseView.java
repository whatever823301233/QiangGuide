package com.qiang.qiangguide.aInterface;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.qiang.qiangguide.custom.channel.ChannelItem;

import java.util.ArrayList;
import java.util.List;

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

    void setUserChannel(ArrayList<ChannelItem> userChannelList);

    void setOtherChannel(ArrayList<ChannelItem> otherChannelList);

    void updateUserChannel();

    void updateOtherChannel();

    List<ChannelItem> getUserChannelList();


    String getMuseumId();


    void showFailedError();


    Context getContext();
}
