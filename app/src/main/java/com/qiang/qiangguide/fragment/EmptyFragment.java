package com.qiang.qiangguide.fragment;

import android.content.Context;
import android.os.Bundle;

import com.qiang.qiangguide.R;

/**
 */
public class EmptyFragment extends BaseFragment {

    public EmptyFragment() {
    }

    public static EmptyFragment newInstance() {
        return new EmptyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    void initView() {
        setContentView(R.layout.fragment_empty);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
