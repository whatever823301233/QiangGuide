package com.qiang.qiangguide.presenter;

import android.view.View;
import android.widget.AdapterView;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.ITopicChooseView;
import com.qiang.qiangguide.biz.ITopicChooseBiz;
import com.qiang.qiangguide.biz.bizImpl.TopicChooseBiz;
import com.qiang.qiangguide.custom.channel.ChannelItem;

import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * Created by Qiang on 2016/8/12.
 */
public class TopicChoosePresenter {

    private ITopicChooseView topicChooseView;

    private ITopicChooseBiz topicChooseBiz;

    private Handler handler;


    public TopicChoosePresenter(ITopicChooseView topicChooseView){
        this.topicChooseView=topicChooseView;
        topicChooseBiz=new TopicChooseBiz();

    }


    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //如果点击的时候，之前动画还没结束，那么就让点击事件无效
        if(topicChooseView.isMove()){return;}
        switch (parent.getId()) {
            case R.id.userGridView:
                //position为 0，1 的不可以进行任何操作
                if (position != 0 && position != 1) {
                    topicChooseView.hideUserAddOtherItem(parent,view,position,id);
                }
                break;
            case R.id.otherGridView:
                topicChooseView.hideOtherAddUserItem(parent,view,position,id);
                break;
            default:
                break;
        }
    }


    public void initChannels() {

        /** 初始化数据*/
        ArrayList<ChannelItem> userChannelList = new ArrayList<>();
        ArrayList<ChannelItem> otherChannelList = new ArrayList<>();
            userChannelList.add(new ChannelItem(1, "推荐", 1, 1));
            userChannelList.add(new ChannelItem(2, "热点", 2, 1));
            userChannelList.add(new ChannelItem(3, "杭州", 3, 1));
            userChannelList.add(new ChannelItem(4, "时尚", 4, 1));
            userChannelList.add(new ChannelItem(5, "科技", 5, 1));
            userChannelList.add(new ChannelItem(6, "体育", 6, 1));
            userChannelList.add(new ChannelItem(7, "军事", 7, 1));
            otherChannelList.add(new ChannelItem(8, "财经", 1, 0));
            otherChannelList.add(new ChannelItem(9, "汽车", 2, 0));
            otherChannelList.add(new ChannelItem(10, "房产", 3, 0));
            otherChannelList.add(new ChannelItem(11, "社会", 4, 0));
            otherChannelList.add(new ChannelItem(12, "情感", 5, 0));
            otherChannelList.add(new ChannelItem(13, "女人", 6, 0));
            otherChannelList.add(new ChannelItem(14, "旅游", 7, 0));
            otherChannelList.add(new ChannelItem(15, "健康", 8, 0));
            otherChannelList.add(new ChannelItem(16, "美女", 9, 0));
            otherChannelList.add(new ChannelItem(17, "游戏", 10, 0));
            otherChannelList.add(new ChannelItem(18, "数码", 11, 0));
            userChannelList.add(new ChannelItem(19, "娱乐", 12, 0));

        topicChooseView.updateUserChannel(userChannelList);
        topicChooseView.updateOtherChannel(otherChannelList);
    }
}
