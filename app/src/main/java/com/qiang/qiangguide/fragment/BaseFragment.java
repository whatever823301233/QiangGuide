package com.qiang.qiangguide.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.qiang.qiangguide.volley.QVolley;

/**
 * Created by Qiang on 2016/7/20.
 */
public abstract class BaseFragment extends Fragment {

    /**
     * 主视图
     */
    public View contentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return contentView;
    }

    public void setContentView(@LayoutRes int res) {
        if (contentView == null) {
            contentView = LinearLayout.inflate(getActivity(), res, null);
        }
    }

    /**
     * 初始化控件
     */
    abstract void initView();
    /**
     * 显示一个toast
     *
     * @param msg
     *            toast内容
     */
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        QVolley.getInstance(getActivity()).cancelFromRequestQueue(getTag());
    }

    @Override
    public void onStop() {
        super.onStop();
        QVolley.getInstance(getActivity()).cancelFromRequestQueue(getTag());
    }

}
