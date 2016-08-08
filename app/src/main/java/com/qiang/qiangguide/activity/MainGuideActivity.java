package com.qiang.qiangguide.activity;

import android.os.Bundle;

import com.qiang.qiangguide.R;

public class MainGuideActivity extends ActivityBase {

    public static final String EXTRA_START_FULLSCREEN =
            "com.example.android.uamp.EXTRA_START_FULLSCREEN";

    /**
     * Optionally used with {@link #EXTRA_START_FULLSCREEN} to carry a MediaDescription to
     * the {@link FullScreenPlayerActivity}, speeding up the screen rendering
     * while the {@link android.support.v4.media.session.MediaControllerCompat} is connecting.
     */
    public static final String EXTRA_CURRENT_MEDIA_DESCRIPTION =
            "com.example.android.uamp.CURRENT_MEDIA_DESCRIPTION";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_guide);
    }

    @Override
    void errorRefresh() {

    }
}
