package com.qiang.qiangguide.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.qiang.qiangguide.R;

import java.util.List;

public class MainActivity extends PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);

        loadHeadersFromResource(R.xml.settings_headers, target);

    }
}
