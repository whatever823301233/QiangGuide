package com.qiang.qiangguide.aInterface;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Qiang on 2016/7/29.
 */
public interface ISettingView {

    Context getContext();

    void goToNextActivity(Intent intent);

    void setAutoGPS(boolean isAutoGPS);

    void setAutoMuseum(boolean isAutoMuseum);

    void setUpdate(boolean isAutoUpdate);
}
