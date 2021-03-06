package com.qiang.qiangguide.presenter;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.qiang.qiangguide.aInterface.IMuseumChooseView;
import com.qiang.qiangguide.activity.MuseumHomeActivity;
import com.qiang.qiangguide.bean.BaseBean;
import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.biz.IMuseumChooseBiz;
import com.qiang.qiangguide.biz.OnInitBeanListener;
import com.qiang.qiangguide.biz.bizImpl.MuseumChooseBiz;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.manager.AppManager;
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

    private void initMuseumList(){
        museumChooseView.showLoading();
        final String city=museumChooseView.getCityName();
        if(TextUtils.isEmpty(city)){
            museumChooseView.showFailedError();
            return;
        }
        boolean isNetConn= AndroidUtil.isNetworkConnected(museumChooseView.getContext());
        if(!isNetConn){
            final List<Museum> museumList = museumChooseBiz.initMuseumListBySQL(city);
            if( museumList == null || museumList.size() == 0 ){
                Toast.makeText(museumChooseView.getContext(),"加载失败，请检查网络状态...",Toast.LENGTH_SHORT ).show();
                handler.sendEmptyMessage( MSG_WHAT_UPDATE_DATA_FAIL );
            }else{
                museumChooseView.setMuseumList( museumList );
                handler.sendEmptyMessage( MSG_WHAT_REFRESH_MUSEUM_LIST );
            }

        }else {
            museumChooseBiz.initMuseumListByNet(city, museumChooseView.getTag(), new OnInitBeanListener() {
                @Override
                public void onSuccess( List< ? extends BaseBean > beans ) {
                    //final List<Museum> museumList = museumChooseBiz.initMuseumListBySQL(city);
                    if( beans == null || beans.size() == 0 ){return;}
                    List<Museum> mList = (List<Museum>) beans;
                    List<Museum> museumList = museumChooseBiz.initMuseumListBySQL(city);
                    if(museumList != null && museumList.size() > 0 ){
                        mList.removeAll( museumList );
                        mList.addAll( museumList );
                    }
                    museumChooseView.setMuseumList( mList );
                    handler.sendEmptyMessage( MSG_WHAT_REFRESH_MUSEUM_LIST );
                /*if( beans == null || beans.size() == 0 ){
                    museumChooseBiz.saveMuseumBySQL(museumList);
                }else{
                    List<Museum> mList = (List<Museum>) beans;
                    mList.removeAll( museumList );
                    museumChooseBiz.saveMuseumBySQL( mList );
                    mList.addAll( museumList );
                    museumChooseView.setMuseumList( mList );
                }*/
                }

                @Override
                public void onFailed() {
                    final List<Museum> museumList = museumChooseBiz.initMuseumListBySQL(city);
                    if( museumList == null || museumList.size() == 0 ){
                        handler.sendEmptyMessage( MSG_WHAT_UPDATE_DATA_FAIL );
                    }else{
                        museumChooseView.setMuseumList(museumList);
                        handler.sendEmptyMessage( MSG_WHAT_REFRESH_MUSEUM_LIST );
                    }
                }
            });
        }

    }

    public void onErrorFresh(){
        handler.sendEmptyMessage( MSG_WHAT_HIDE_ERROR_VIEW );
        initMuseumList();
    }

    public void onGetCity() {
        String city = museumChooseView.getCityName();
        if(TextUtils.isEmpty(city)){
            museumChooseView.showFailedError();
            return;
        }
        handler.sendEmptyMessage(MSG_WHAT_REFRESH_TITLE);
        initMuseumList();
    }

    public void onMuseumChoose() {
        Museum museum = museumChooseView.getChooseMuseum();
        //存储博物馆id
        AppManager.getInstance(museumChooseView.getContext()).setMuseumId(museum.getId());
        if( museum.getDownloadState() == Museum.DOWNLOAD_STATE_FINISH ){
            Intent intent=new Intent(museumChooseView.getContext(), MuseumHomeActivity.class);
            intent.putExtra(Constants.INTENT_MUSEUM,museum);
            museumChooseView.toNextActivity(intent);
        }else{
            museumChooseView.showDownloadTipDialog();
        }

    }

    public void onDownloadMuseum() {
        Museum museum = museumChooseView.getChooseMuseum();
        museumChooseView.showProgressDialog();
        museumChooseBiz.downloadMuseum(museum,progressListener);// TODO: 2016/9/2

    }
    private IMuseumChooseBiz.DownloadProgressListener progressListener=new IMuseumChooseBiz.DownloadProgressListener(){

        @Override
        public void onProgress(int progress, int totalSize) {
            museumChooseView.setDownloadProgress(progress,totalSize);
            //下载完成，保存至数据库
            if(progress == totalSize){
                Museum museum=museumChooseView.getChooseMuseum();
                museum.setDownloadState(Museum.DOWNLOAD_STATE_FINISH);
                ///museumChooseBiz.updateDownloadState(museum);
                museumChooseBiz.addMuseum(museum);
                museumChooseView.hideProgressDialog();
            }

        }

        @Override
        public void onError() {

        }
    };

    private static class MyHandler extends Handler {

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
