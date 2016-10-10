package com.qiang.qiangguide.presenter;

import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;

import com.qiang.qiangguide.manager.AppManager;
import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.ISettingView;
import com.qiang.qiangguide.activity.AboutActivity;
import com.qiang.qiangguide.biz.ISettingBiz;
import com.qiang.qiangguide.biz.bizImpl.SettingBiz;
import com.qiang.qiangguide.config.Constants;

/**
 * Created by Qiang on 2016/10/9.
 *
 */

public class SettingPresenter {

    private ISettingView settingView;
    private ISettingBiz settingBiz;


    public SettingPresenter( ISettingView settingView ){
        this.settingView = settingView;
        this.settingBiz = new SettingBiz();
    }


    public void onCompoundButtonCheckChange ( CompoundButton buttonView, boolean isChecked ) {
        switch ( buttonView.getId() ){
            case R.id.switch_gps:
                settingBiz.setIsAutoGPS( buttonView.getContext(), isChecked );
                break;
            case R.id.switch_auto_museum:
                settingBiz.setIsAutoMuseum( buttonView.getContext(), isChecked );
                break;
            case R.id.switch_auto_update:
                settingBiz.setIsAutoUpdate( buttonView.getContext(), isChecked );
                break;

        }
    }

    public void onTvClick(View v) {
        switch (v.getId()){
            case R.id.btn_green:
                settingBiz.setStyle(Constants.STYLE_GREEN);
                break;
            case R.id.btn_blue:
                settingBiz.setStyle(Constants.STYLE_BLUE);
                break;
            case R.id.btn_red:
                settingBiz.setStyle(Constants.STYLE_RED);
                break;
            case R.id.btn_purple:
                settingBiz.setStyle(Constants.STYLE_PURPLE);
                break;
            case R.id.btn_switch:
                settingBiz.switchStyle();
                break;
            case R.id.tv_about:
                Intent intent = new Intent();
                intent.setClass(settingView.getContext(), AboutActivity.class);
                settingView.goToNextActivity(intent);
                break;
            case R.id.tv_quit:
                AppManager.getInstance(settingView.getContext()).exitApp();
                break;


        }
    }

    public void onCreateView() {
        boolean isAutoGPS = settingBiz.getAutoGPS(settingView.getContext());
        settingView.setAutoGPS ( isAutoGPS );
        boolean isAutoMuseum = settingBiz.getAutoMuseum(settingView.getContext());
        settingView.setAutoMuseum ( isAutoMuseum );
        boolean isAutoUpdate = settingBiz.getAutoUpdate(settingView.getContext());
        settingView.setUpdate ( isAutoUpdate );

    }
}
