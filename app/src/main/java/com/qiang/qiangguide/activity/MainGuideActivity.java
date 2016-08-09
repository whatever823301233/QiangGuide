package com.qiang.qiangguide.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.fragment.MapFragment;
import com.qiang.qiangguide.fragment.NearExhibitFragment;

public class MainGuideActivity extends ActivityBase {

    public static final String EXTRA_START_FULLSCREEN =
            "com.example.android.uamp.EXTRA_START_FULLSCREEN";

    /**
     * Optionally used with {@link #EXTRA_START_FULLSCREEN} to carry a MediaDescription to
     * the {@link PlayActivity}, speeding up the screen rendering
     * while the {@link android.support.v4.media.session.MediaControllerCompat} is connecting.
     */
    public static final String EXTRA_CURRENT_MEDIA_DESCRIPTION =
            "com.example.android.uamp.CURRENT_MEDIA_DESCRIPTION";
    private NearExhibitFragment exhibitListFragment;
    private MapFragment mapFragment;


    public static final String INTENT_FLAG_GUIDE_MAP="intent_flag_guide_map";
    public static final String INTENT_FLAG_GUIDE="intent_flag_guide";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_guide);
    }

    /**
     * 设置默认fragment
     */
    private void setDefaultFragment() {
        String flag=getIntent().getStringExtra(INTENT_FLAG_GUIDE_MAP);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        exhibitListFragment = NearExhibitFragment.newInstance();
        mapFragment = MapFragment.newInstance();
        if (flag.equals(INTENT_FLAG_GUIDE)){
            transaction.replace(R.id.llExhibitListContent, exhibitListFragment);
            //radioButtonList.setChecked(true);
        }else{
            transaction.replace(R.id.llExhibitListContent, mapFragment);
            //radioButtonMap.setChecked(true);
        }
        transaction.commit();
    }


    @Override
    void errorRefresh() {

    }
}
