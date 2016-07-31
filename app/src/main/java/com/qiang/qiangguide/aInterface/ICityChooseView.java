package com.qiang.qiangguide.aInterface;

import android.content.Context;

import com.qiang.qiangguide.bean.City;

import java.util.List;

/**
 * Created by Qiang on 2016/7/29.
 */
public interface ICityChooseView {


    void showLoading();
    void hideLoading();
    void hideKeyboard();
    void refreshView();
    String getTag();
    String getCurrentInput();
    Context getContext();

    List<City> getListCities();
    void setListCities(List<City> cities);

    void updateTipCities(List<City> cities);

    void setLocationCity(City city);

    void clearInputCity();

    void toNextActivity(String cityName);

    void showFailedError();
    void hideErrorView();

    City getChooseCity();
    void setChooseCity(City city);


}
