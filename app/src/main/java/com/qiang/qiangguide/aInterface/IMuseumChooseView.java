package com.qiang.qiangguide.aInterface;

import android.content.Context;

import com.qiang.qiangguide.bean.Museum;

import java.util.List;

/**
 * Created by Qiang on 2016/7/29.
 */
public interface IMuseumChooseView {

    String getTag();

    void refreshMuseumList();

    void showLoading();

    void hideLoading();

    void toNextActivity();

    void setTitle();

    String getCityName();

    void setMuseumList(List<Museum> museumList);


    void showFailedError();

    void hideErrorView();

    Context getContext();

    void showToast(String content);

}
