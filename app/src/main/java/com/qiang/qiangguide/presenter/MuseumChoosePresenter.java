package com.qiang.qiangguide.presenter;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.qiang.qiangguide.AppManager;
import com.qiang.qiangguide.aInterface.IMuseumChooseView;
import com.qiang.qiangguide.activity.MuseumHomeActivity;
import com.qiang.qiangguide.bean.BaseBean;
import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.biz.IMuseumChooseBiz;
import com.qiang.qiangguide.biz.OnInitBeanListener;
import com.qiang.qiangguide.biz.bizImpl.MuseumChooseBiz;
import com.qiang.qiangguide.config.Constants;
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
        final List<Museum> museumList=museumChooseBiz.initMuseumListBySQL(city);
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
                List<Museum> mList= (List<Museum>) beans;
                if(mList!=null){
                    mList.removeAll(museumList);
                    museumChooseBiz.saveMuseumBySQL(mList);
                    mList.addAll(museumList);
                }
                museumChooseView.setMuseumList(mList);
                handler.sendEmptyMessage(MSG_WHAT_REFRESH_MUSEUM_LIST);
            }

            @Override
            public void onFailed() {
                if(museumList==null||museumList.size()==0){
                    handler.sendEmptyMessage(MSG_WHAT_UPDATE_DATA_FAIL);
                }else{
                    museumChooseView.setMuseumList(museumList);
                    handler.sendEmptyMessage(MSG_WHAT_REFRESH_MUSEUM_LIST);
                }
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

    public void onMuseumChoose() {
        Museum museum=museumChooseView.getChooseMuseum();
        //存储博物馆id
        AppManager.getInstance(museumChooseView.getContext()).setMuseumId(museum.getId());
        if(museum.getDownloadState()== FileDownloadStatus.completed){
            Intent intent=new Intent(museumChooseView.getContext(), MuseumHomeActivity.class);
            intent.putExtra(Constants.INTENT_MUSEUM,museum);
            museumChooseView.toNextActivity(intent);
        }else{
            museumChooseView.showDownloadTipDialog();
        }

    }

    public void onDownloadMuseum() {

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
