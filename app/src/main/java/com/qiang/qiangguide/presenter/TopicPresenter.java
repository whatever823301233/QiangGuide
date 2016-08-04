package com.qiang.qiangguide.presenter;

import android.os.Handler;
import android.os.Message;

import com.qiang.qiangguide.aInterface.ITopicView;
import com.qiang.qiangguide.bean.BaseBean;
import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.biz.ITopicBiz;
import com.qiang.qiangguide.biz.OnInitBeanListener;
import com.qiang.qiangguide.biz.bizImpl.TopicBiz;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Qiang on 2016/8/4.
 */
public class TopicPresenter {

    private static final int MSG_WHAT_SHOW_ALL_EXHIBITS =9527;
    private static final int MSG_WHAT_UPDATE_DATA_FAIL=9528;


    private ITopicView topicView;
    private ITopicBiz topicBiz;
    private Handler handler;

    public TopicPresenter(ITopicView topicView){
        this.topicView=topicView;
        topicBiz=new TopicBiz();
        handler=new MyHandler(this.topicView);
    }

    public void initAllExhibitList(){
        topicView.showLoading();
        String museumId=topicView.getMuseumId();
        topicBiz.initAllExhibitByMuseumId(museumId, new OnInitBeanListener() {
            @Override
            public void onSuccess(List<? extends BaseBean> beans) {
                List<Exhibit> exhibitList= (List<Exhibit>) beans;
                topicView.setAllExhibitList(exhibitList);
                handler.sendEmptyMessage(MSG_WHAT_SHOW_ALL_EXHIBITS);
            }

            @Override
            public void onFailed() {
                handler.sendEmptyMessage(MSG_WHAT_UPDATE_DATA_FAIL);
            }
        },topicView.getTag());
    }

    static class MyHandler extends Handler {

        WeakReference<ITopicView> activityWeakReference;
        MyHandler(ITopicView activity){
            this.activityWeakReference=new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            if(activityWeakReference==null){return;}
            ITopicView activity=activityWeakReference.get();
            if(activity==null){return;}
            switch (msg.what){
                case MSG_WHAT_SHOW_ALL_EXHIBITS:
                    activity.showAllExhibits();
                    break;
                case MSG_WHAT_UPDATE_DATA_FAIL:
                    activity.showFailedError();
                    break;
                default:break;
            }
            activity.hideLoading();
        }
    }



}
