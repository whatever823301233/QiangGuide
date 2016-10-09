package com.qiang.qiangguide.biz.bizImpl;

import android.content.Context;

import com.qiang.qiangguide.biz.ISettingBiz;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.config.GlobalConfig;

/**
 * Created by Qiang on 2016/10/9.
 */

public class SettingBiz implements ISettingBiz {


    @Override
    public void setIsAutoGPS ( Context context, boolean isChecked ) {
        GlobalConfig.getInstance(context).putBoolean( Constants.SP_IS_AUTO_GPS , isChecked );
    }

    @Override
    public boolean getAutoGPS(Context context) {
        return GlobalConfig.getInstance ( context ).getBoolean( Constants.SP_IS_AUTO_GPS , false );
    }

    @Override
    public void setIsAutoMuseum ( Context context, boolean isChecked ) {
        GlobalConfig.getInstance ( context ).putBoolean( Constants.SP_IS_AUTO_MUSEUM , isChecked );
    }

    @Override
    public boolean getAutoMuseum(Context context) {
        return GlobalConfig.getInstance ( context ).getBoolean( Constants.SP_IS_AUTO_MUSEUM , false );
    }

    @Override
    public void setIsAutoUpdate( Context context, boolean isChecked ) {
        GlobalConfig.getInstance(context).putBoolean( Constants.SP_IS_AUTO_UPDATE , isChecked );
    }

    @Override
    public boolean getAutoUpdate(Context context) {
        return GlobalConfig.getInstance ( context ).getBoolean( Constants.SP_IS_AUTO_UPDATE , false );
    }

    @Override
    public void setStyle(int styleGreen) {

    }

    @Override
    public void switchStyle() {

    }
}
