package com.qiang.qiangguide.aInterface;

import com.qiang.qiangguide.bean.Exhibit;

import java.util.List;

/**
 * Created by Qiang on 2016/7/29.
 */
public interface ITopicView {

    void showLoading();

    void hideLoading();

    void refreshView();

    List<String> getChoosedLabels();

    void addChooseLabels(String label);

    void removeLabel();

    void removeLabels(List<String> labels);

    void toMapView(List<Exhibit> exhibits);

}
