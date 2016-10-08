package com.qiang.qiangguide.aInterface;

import android.content.Context;
import android.content.Intent;

import com.qiang.qiangguide.bean.Museum;

/**
 * Created by Qiang on 2016/9/22.
 */
public interface IBeginView {

    void goToNextActivity();

    Context getContext();

    void setCurrentMuseum(Museum museum);

    void setTargetIntent(Intent intent);

    Museum getCurrentMuseum();
}
