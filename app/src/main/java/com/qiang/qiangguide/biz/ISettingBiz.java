package com.qiang.qiangguide.biz;

import android.content.Context;

/**
 * Created by Qiang on 2016/10/9.
 */

public interface ISettingBiz {


    void setIsAutoGPS( Context context , boolean isChecked );

    boolean getAutoGPS( Context context );

    void setIsAutoMuseum( Context context , boolean isChecked );

    boolean getAutoMuseum( Context context );

    void setIsAutoUpdate( Context context , boolean isChecked );

    boolean getAutoUpdate( Context context );

    void setStyle( int styleGreen );

    void switchStyle();
}
