package com.qiang.qiangguide.presenter;

import android.os.Handler;
import android.os.Message;

import com.qiang.qiangguide.aInterface.ICityChooseView;
import com.qiang.qiangguide.bean.BaseBean;
import com.qiang.qiangguide.bean.City;
import com.qiang.qiangguide.bizImpl.CityBiz;
import com.qiang.qiangguide.biz.ICityBiz;
import com.qiang.qiangguide.biz.OnInitBeanListener;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Qiang on 2016/7/29.
 */
public class CityChoosePresenter {

    private static final int MSG_WHAT_REFRESH_VIEW =9527;
    private static final int MSG_WHAT_UPDATE_DATA_FAIL =9528;

    private ICityBiz cityBiz;
    private ICityChooseView cityChooseView;
    private Handler handler;

    public CityChoosePresenter( ICityChooseView cityChooseView){
        this.cityChooseView=cityChooseView;
        cityBiz=new CityBiz();
        handler=new MyHandler(cityChooseView);
    }

    public void initData(){
        cityChooseView.showLoading();
        cityChooseView.hideKeyboard();
        cityBiz.initCities(new OnInitBeanListener() {
            @Override
            public void onSuccess(List<BaseBean> beans) {
                if(beans==null||beans.size()==0){return;}
                if( beans.get(0) instanceof City){
                    List<City> cities=(List)beans;
                    cityChooseView.setListCities(cities);
                    handler.sendEmptyMessage(MSG_WHAT_REFRESH_VIEW);
                }
            }

            @Override
            public void onFailed() {
                handler.sendEmptyMessage(MSG_WHAT_UPDATE_DATA_FAIL);
            }
        });
    }

    public void clearInputCity(){
        cityChooseView.clearInputCity();
    }


    public void onCityChoose(City city){
        cityChooseView.toNextActivity(city.getName());
    }


    public void fixCityName(){
        String input=cityChooseView.getCurrentInput();
        List<City> cities=cityChooseView.getListCities();
        List<City> tipCities=cityBiz.fixCityName(input,cities);
        cityChooseView.updateTipCities(tipCities);
    }


    static class MyHandler extends Handler {

        WeakReference<ICityChooseView> activityWeakReference;
        MyHandler(ICityChooseView activity){
            this.activityWeakReference=new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            if(activityWeakReference==null){return;}
            ICityChooseView activity=activityWeakReference.get();
            if(activity==null){return;}
            switch (msg.what){
                case MSG_WHAT_REFRESH_VIEW:
                    activity.refreshView();
                    break;
                case MSG_WHAT_UPDATE_DATA_FAIL:
                    activity.showFailedError();
                default:
                    break;
            }
            activity.hideLoading();
        }
    }

}
