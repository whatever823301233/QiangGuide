package com.qiang.qiangguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.ISettingView;
import com.qiang.qiangguide.presenter.SettingPresenter;
import com.qiang.qiangguide.util.Utility;

public class SettingActivity extends ActivityBase implements ISettingView{

    private TextView btn_green;
    private TextView btn_purple;
    private TextView btn_red;
    private TextView btn_blue;
    private SwitchCompat switch_gps;
    private SwitchCompat switch_auto_museum;
    private SwitchCompat switch_auto_update;
    private TextView tv_about;
    private TextView tv_quit;
    private TextView btn_switch;


    private SettingPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        presenter = new SettingPresenter(this);
        setToolbar();
        findView();
        addListener();
        presenter.onCreateView();
    }

    @Override
    void errorRefresh() {

    }

    private void addListener() {
        btn_green.setOnClickListener(onClickListener);
        btn_purple.setOnClickListener(onClickListener);
        btn_red.setOnClickListener(onClickListener);
        btn_blue.setOnClickListener(onClickListener);
        tv_about.setOnClickListener(onClickListener);
        tv_quit.setOnClickListener(onClickListener);
        btn_switch.setOnClickListener(onClickListener);

        switch_gps.setOnCheckedChangeListener(onCheckedChangeListener);
        switch_auto_museum.setOnCheckedChangeListener(onCheckedChangeListener);
        switch_auto_update.setOnCheckedChangeListener(onCheckedChangeListener);

    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            presenter.onCompoundButtonCheckChange(buttonView,isChecked);
        }
    };


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.onTvClick(v);
        }
    };

    private void findView() {
        btn_green=(TextView)findViewById(R.id.btn_green);
        btn_purple=(TextView)findViewById(R.id.btn_purple);
        btn_red=(TextView)findViewById(R.id.btn_red);
        btn_blue=(TextView)findViewById(R.id.btn_blue);
        tv_about=(TextView)findViewById(R.id.tv_about);
        tv_quit=(TextView)findViewById(R.id.tv_quit);
        btn_switch=(TextView)findViewById(R.id.btn_switch);
        switch_gps=(SwitchCompat)findViewById(R.id.switch_gps);
        switch_auto_museum=(SwitchCompat)findViewById(R.id.switch_auto_museum);
        switch_auto_update=(SwitchCompat)findViewById(R.id.switch_auto_update);
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

    @Override
    public void goToNextActivity ( Intent intent ) {
        Utility.startActivity ( getActivity(),intent );
    }

    @Override
    public void setAutoGPS(boolean isAutoGPS) {
        if(switch_gps==null){return;}
        switch_gps.setChecked ( isAutoGPS );
    }

    @Override
    public void setAutoMuseum(boolean isAutoMuseum) {
        if(switch_auto_museum==null){return;}
        switch_auto_museum.setChecked ( isAutoMuseum );

    }

    @Override
    public void setUpdate(boolean isAutoUpdate) {
        if(switch_auto_update==null){return;}
        switch_auto_update.setChecked ( isAutoUpdate );
    }
}
