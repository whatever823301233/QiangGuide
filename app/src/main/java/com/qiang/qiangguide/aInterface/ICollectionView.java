package com.qiang.qiangguide.aInterface;

import android.content.Context;
import android.content.Intent;

import com.qiang.qiangguide.bean.Exhibit;

import java.util.List;

/**
 * Created by Qiang on 2016/7/29.
 */
public interface ICollectionView {

    void refreshView();

    void showLoading();

    void hideLoading();

    void toNextActivity(Intent intent);

    void showFailedError();

    void onNoData();

    void hideErrorView();

    void showFavoriteExhibits();

    String getMuseumId();

    String getTag();

    void setFavoriteExhibitList(List<Exhibit> exhibitList);

    void setChooseExhibit(Exhibit exhibit);

    Exhibit getChooseExhibit();

    Context getContext();

    void toPlay();

}
