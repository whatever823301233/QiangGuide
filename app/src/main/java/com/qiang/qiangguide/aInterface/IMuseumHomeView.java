package com.qiang.qiangguide.aInterface;

import com.qiang.qiangguide.bean.Museum;

import java.util.List;

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

    void setTitle(String title);

    void setIntroduce(String text);

    void refreshIntroduce();

    void setIconUrls(List<String> iconUrls);

    void refreshIcons();

    void refreshMedia();

    void setAdapterMuseumId();

    void setMediaPath(String s);

    String getTag();
}
