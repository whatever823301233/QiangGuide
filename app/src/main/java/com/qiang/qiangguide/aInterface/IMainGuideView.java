package com.qiang.qiangguide.aInterface;

import android.content.Context;
import android.content.Intent;

import com.qiang.qiangguide.bean.Exhibit;

import java.util.List;

/**
 * Created by Qiang on 2016/8/10.
 */
public interface IMainGuideView {


    String INTENT_FRAGMENT_FLAG="intent_fragment_flag";
    String INTENT_FLAG_GUIDE_MAP="intent_flag_guide_map";
    String INTENT_FLAG_GUIDE="intent_flag_guide";

    void refreshView();

    void showLoading();

    void hideLoading();

    void toNextActivity(Intent intent);

    void showFailedError();

    void hideErrorView();

    void showNearExhibits();

    void setNearExhibits(List<Exhibit> exhibitList);

    String getMuseumId();

    String getTag();

    void setChooseExhibit(Exhibit exhibit);

    Exhibit getChooseExhibit();

    Context getContext();

    void toPlay();

    void changeToNearExhibitFragment();

    void changeToMapExhibitFragment();

    String getFragmentFlag();

    void setFragmentFlag(String flag);
}
