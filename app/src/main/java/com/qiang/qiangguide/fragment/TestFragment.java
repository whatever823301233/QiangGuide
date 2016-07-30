package com.qiang.qiangguide.fragment;

import android.support.v4.app.Fragment;

import com.qiang.qiangguide.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends BaseFragment {


    public TestFragment() {
        // Required empty public constructor
    }
    public static TestFragment newInstance(){
        return  new TestFragment();
    }
    @Override
    void initView() {
        setContentView(R.layout.fragment_test);
    }
}
