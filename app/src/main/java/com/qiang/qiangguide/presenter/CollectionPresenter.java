package com.qiang.qiangguide.presenter;

import android.os.Handler;
import android.os.Message;

import com.example.okhttp_library.OkHttpUtils;
import com.example.okhttp_library.callback.FileCallBack;
import com.qiang.qiangguide.aInterface.ICollectionView;
import com.qiang.qiangguide.bean.BaseBean;
import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.biz.ICollectionBiz;
import com.qiang.qiangguide.biz.OnInitBeanListener;
import com.qiang.qiangguide.biz.bizImpl.CollectionBiz;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.util.FileUtil;
import com.qiang.qiangguide.util.LogUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Qiang on 2016/8/9.
 */
public class CollectionPresenter {


    private static final int MSG_WHAT_SHOW_ALL_EXHIBITS =9527;
    private static final int MSG_WHAT_UPDATE_DATA_FAIL=9528;


    private ICollectionView collectionView;
    private ICollectionBiz collectionBiz;
    private Handler handler;


    public CollectionPresenter(ICollectionView collectionView){
        this.collectionView=collectionView;
        collectionBiz=new CollectionBiz();
        handler=new MyHandler(collectionView);
    }


    public void initExhibitList() {
        collectionView.showLoading();
        String museumId=collectionView.getMuseumId();
        collectionBiz.initExhibitByMuseumId(museumId, new OnInitBeanListener() {
            @Override
            public void onSuccess(List<? extends BaseBean> beans) {
                List<Exhibit> exhibitList= (List<Exhibit>) beans;
                collectionView.setFavoriteExhibitList(exhibitList);
                handler.sendEmptyMessage(MSG_WHAT_SHOW_ALL_EXHIBITS);
            }

            @Override
            public void onFailed() {
                handler.sendEmptyMessage(MSG_WHAT_UPDATE_DATA_FAIL);
            }

        },collectionView.getTag());
    }

    public void onExhibitChoose() {
        collectionView.showLoading();
        Exhibit exhibit=collectionView.getChooseExhibit();
        String url=exhibit.getAudiourl();
        String name= FileUtil.changeUrl2Name(url);
        boolean fileExists=FileUtil.checkFileExists(url,exhibit.getMuseumId());
        if(!fileExists){
            collectionView.showLoading();
            OkHttpUtils
                    .get().url(Constants.BASE_URL+exhibit.getAudiourl())
                    .build()
                    .execute(new FileCallBack(Constants.LOCAL_PATH+exhibit.getMuseumId(),name) {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtil.e("",e.toString());
                            collectionView.hideLoading();
                            collectionView.showFailedError();
                        }

                        @Override
                        public void onResponse(File response, int id) {
                            collectionView.hideLoading();
                            collectionView.toPlay();
                        }
                    });
        }else{
            LogUtil.i("File is Exists");
            collectionView.toPlay();
        }

    }


    static class MyHandler extends Handler {

        WeakReference<ICollectionView> activityWeakReference;
        MyHandler(ICollectionView activity){
            this.activityWeakReference=new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            if(activityWeakReference==null){return;}
            ICollectionView activity=activityWeakReference.get();
            if(activity==null){return;}
            switch (msg.what){
                case MSG_WHAT_SHOW_ALL_EXHIBITS:
                    activity.showFavoriteExhibits();
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
