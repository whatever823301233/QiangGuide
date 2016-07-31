package com.qiang.qiangguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.custom.SwipeBackLayout;

/**
 * Created by Qiang on 2016/7/20.
 *
 * 想要实现向右滑动删除Activity效果只需要继承SwipeBackActivity即可，如果当前页面含有ViewPager
 * 只需要调用SwipeBackLayout的setViewPager()方法即可
 */
public abstract class ActivitySwipeBack extends ActivityBase {

    protected SwipeBackLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(R.layout.swipe_back_base, null);
        layout.attachToActivity(this);
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }

    // Press the back button in mobile phone
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }


}
