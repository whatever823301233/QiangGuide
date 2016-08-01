package com.qiang.qiangguide.presenter;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.qiang.qiangguide.aInterface.IMuseumChooseView;
import com.qiang.qiangguide.bean.BaseBean;
import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.biz.IMuseumChooseBiz;
import com.qiang.qiangguide.biz.OnInitBeanListener;
import com.qiang.qiangguide.biz.bizImpl.MuseumChooseBiz;
import com.qiang.qiangguide.util.AndroidUtil;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by xq823 on 2016/7/31.
 *
 */
public class MuseumChoosePresenter {

    private static final int MSG_WHAT_REFRESH_MUSEUM_LIST =9527;
    private static final int MSG_WHAT_REFRESH_TITLE =9529;
    private static final int MSG_WHAT_UPDATE_DATA_FAIL =9530;
    private static final int MSG_WHAT_HIDE_ERROR_VIEW =9531;

    private IMuseumChooseView museumChooseView;
    private IMuseumChooseBiz museumChooseBiz;
    private Handler handler;

    public MuseumChoosePresenter(IMuseumChooseView museumChooseView){
        this.museumChooseView=museumChooseView;
        museumChooseBiz =new MuseumChooseBiz();
        handler=new MyHandler(museumChooseView);
    }

    public void initMuseumList(){
        museumChooseView.showLoading();

        String city=museumChooseView.getCityName();
        if(TextUtils.isEmpty(city)){
            museumChooseView.showFailedError();
            return;
        }
        boolean isNetConn=AndroidUtil.isNetworkConnected(museumChooseView.getContext());
        if(!isNetConn){
            //museumChooseView.showToast
            // ("暂无网络，请检查网络状态...");// TODO: 2016/7/31  显示本地数据？
            museumChooseView.showFailedError();
        }
        museumChooseBiz.initMuseumListByNet(city, museumChooseView.getTag(), new OnInitBeanListener() {
            @Override
            public void onSuccess(List<? extends BaseBean> beans) {
                if(beans==null||beans.size()==0){
                    museumChooseView.showFailedError();
                }
                List<Museum> museumList= (List<Museum>) beans;
                museumChooseView.setMuseumList(museumList);
                handler.sendEmptyMessage(MSG_WHAT_REFRESH_MUSEUM_LIST);
            }

            @Override
            public void onFailed() {
                handler.sendEmptyMessage(MSG_WHAT_UPDATE_DATA_FAIL);
            }
        });
    }

    public void onErrorFresh(){
        handler.sendEmptyMessage(MSG_WHAT_HIDE_ERROR_VIEW);
        initMuseumList();
    }

    public void onGetCity() {
        String city=museumChooseView.getCityName();
        if(TextUtils.isEmpty(city)){
            museumChooseView.showFailedError();
            return;
        }
        handler.sendEmptyMessage(MSG_WHAT_REFRESH_TITLE);

        initMuseumList();
    }


    static class MyHandler extends Handler {

        WeakReference<IMuseumChooseView> activityWeakReference;
        MyHandler(IMuseumChooseView activity){
            this.activityWeakReference=new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            if(activityWeakReference==null){return;}
            IMuseumChooseView activity=activityWeakReference.get();
            if(activity==null){return;}
            switch (msg.what){
                case MSG_WHAT_REFRESH_MUSEUM_LIST:
                    activity.refreshMuseumList();
                    break;
                case MSG_WHAT_REFRESH_TITLE:
                    activity.setTitle();
                    break;
                case MSG_WHAT_UPDATE_DATA_FAIL:
                    activity.showFailedError();
                    break;
                case MSG_WHAT_HIDE_ERROR_VIEW:
                    activity.hideErrorView();
                    break;
            }
            activity.hideLoading();
        }
    }

}
