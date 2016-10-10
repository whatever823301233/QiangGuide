package com.qiang.qiangguide.activity;

import android.os.Bundle;

import com.qiang.qiangguide.R;

public class AboutActivity extends ActivityBase {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    void errorRefresh() {

    }
}
