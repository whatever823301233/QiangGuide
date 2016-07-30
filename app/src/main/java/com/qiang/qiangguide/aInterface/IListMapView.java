package com.qiang.qiangguide.aInterface;

import com.qiang.qiangguide.bean.Exhibit;

import java.util.List;

/**
 * Created by Qiang on 2016/7/29.
 */
public interface IListMapView {

    void showLoading();

    void hideLoading();

    void refreshView();

    void changeFragment();

    void getCurrentFocusFragment();

    void showCtrlCardView();

    void hideCtrlCardView();

    void refreshExhibitList(List<Exhibit> exhibits);

    void refreshMap();


}
