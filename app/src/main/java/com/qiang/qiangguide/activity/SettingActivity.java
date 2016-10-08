package com.qiang.qiangguide.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.qiang.qiangguide.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setToolbar();
    }

    private void setToolbar() {
        View v = findViewById(R.id.toolbar);
        if (v == null) {return;}
        Toolbar toolbar = (Toolbar) v;
        setSupportActionBar(toolbar);
        TextView toolbarTitle = (TextView) v.findViewById(R.id.toolbar_title);
        if (toolbarTitle == null) {return;}
        toolbarTitle.setText(getResources().getString(R.string.setting_title));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar !=null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

}
