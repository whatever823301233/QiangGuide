package com.qiang.qiangguide.aInterface;

import com.qiang.qiangguide.bean.Museum;

/**
 * Created by Qiang on 2016/7/29.
 */
public interface IMuseumHomeView {

    void initData();

    void refreshView();

    void showLoading();

    void hideLoading();

    void toNextActivity();

    void setCurrentMuseum(Museum museum);

    Museum getCurrentMuseum();


}
