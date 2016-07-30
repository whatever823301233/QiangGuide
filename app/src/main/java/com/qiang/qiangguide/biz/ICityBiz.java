package com.qiang.qiangguide.biz;

import com.qiang.qiangguide.bean.City;

import java.util.List;

/**
 * Created by Qiang on 2016/7/29.
 */
public interface ICityBiz {

    List<City> fixCityName(String cityName, List<City> cities);

    void initCities(OnInitBeanListener cityListener);

}
