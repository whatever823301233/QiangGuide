package com.qiang.qiangguide.presenter;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.example.okhttp_library.OkHttpUtils;
import com.example.okhttp_library.callback.FileCallBack;
import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.IMainGuideView;
import com.qiang.qiangguide.bean.BaseBean;
import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.biz.IMainGuideBiz;
import com.qiang.qiangguide.biz.OnInitBeanListener;
import com.qiang.qiangguide.biz.bizImpl.MainGuideBiz;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.util.FileUtil;
import com.qiang.qiangguide.util.LogUtil;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Region;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Qiang on 2016/8/10.
 */
public class MainGuidePresenter {

    private static final int MSG_WHAT_UPDATE_DATA_FAIL=9527;
    private static final int MSG_WHAT_REFRESH_NEAR_EXHIBIT_LIST =9528;

    private IMainGuideView mainGuideView;
    private IMainGuideBiz mainGuideBiz;
    private Handler handler;

    public MainGuidePresenter(IMainGuideView mainGuideView){
        this.mainGuideView=mainGuideView;
        mainGuideBiz=new MainGuideBiz();
        handler=new MyHandler(mainGuideView);
    }

    public void onCheckedRadioButton(int checkedId) {
        switch(checkedId){
            case R.id.radioButtonList:
                mainGuideView.changeToNearExhibitFragment();
                break;
            case R.id.radioButtonMap:
                mainGuideView.changeToMapExhibitFragment();
                break;
        }
    }
    /**
     * 设置默认fragment
     */
    public void setDefaultFragment() {
        String flag=mainGuideView.getFragmentFlag();
        if(TextUtils.isEmpty(flag)||flag.equals(IMainGuideView.INTENT_FLAG_GUIDE)){
            mainGuideView.changeToNearExhibitFragment();
        }else{
            mainGuideView.changeToMapExhibitFragment();
        }
    }

    private long lastTime;

    public void rangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

        if(System.currentTimeMillis()-lastTime<3500){return;}
        lastTime=System.currentTimeMillis();
        String museumId=mainGuideView.getMuseumId();
        mainGuideBiz.findExhibits(museumId, beacons, new OnInitBeanListener() {
            @Override
            public void onSuccess(List<? extends BaseBean> beans) {
                List<Exhibit> exhibits= (List<Exhibit>) beans;
                mainGuideView.setNearExhibits(exhibits);
                LogUtil.i("","rangeBeaconsInRegion onSuccess "+exhibits.size());
                handler.sendEmptyMessage(MSG_WHAT_REFRESH_NEAR_EXHIBIT_LIST);
            }

            @Override
            public void onFailed() {
                //handler.sendEmptyMessage(MSG_WHAT_UPDATE_DATA_FAIL);
            }
        });
    }

    public void onExhibitChoose() {

        Exhibit exhibit=mainGuideView.getChooseExhibit();
        String url=exhibit.getAudiourl();
        String name= FileUtil.changeUrl2Name(url);
        boolean fileExists=FileUtil.checkFileExists(url,exhibit.getMuseumId());
        if(!fileExists){
            mainGuideView.showLoading();
            OkHttpUtils
                    .get().url(Constants.BASE_URL+exhibit.getAudiourl())
                    .build()
                    .execute(new FileCallBack(Constants.LOCAL_PATH+exhibit.getMuseumId(),name) {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtil.e("",e.toString());
                            mainGuideView.showFailedError();
                        }

                        @Override
                        public void onResponse(File response, int id) {
                            mainGuideView.hideLoading();
                            mainGuideView.toPlay();
                        }
                    });
        }else{
            LogUtil.i("File is Exists");
            mainGuideView.toPlay();
        }


    }


    static class MyHandler extends android.os.Handler {

        WeakReference<IMainGuideView> activityWeakReference;
        MyHandler(IMainGuideView activity){
            this.activityWeakReference=new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            if(activityWeakReference==null){return;}
            IMainGuideView activity=activityWeakReference.get();
            if(activity==null){return;}
            switch (msg.what){
                case MSG_WHAT_REFRESH_NEAR_EXHIBIT_LIST:
                    activity.refreshNearExhibitList();
                    break;
                case MSG_WHAT_UPDATE_DATA_FAIL:
                    activity.showFailedError();
                    break;
                default:break;
            }
        }
    }








}
