package com.qiang.qiangguide.bizImpl;

import com.qiang.qiangguide.bean.City;
import com.qiang.qiangguide.biz.ICityBiz;
import com.qiang.qiangguide.biz.OnInitBeanListener;

import java.util.List;

/**
 * Created by Qiang on 2016/7/29.
 */
public class CityBiz implements ICityBiz {


    @Override
    public List<City> fixCityName(String cityName, List<City> cities) {
        return null;
    }

    @Override
    public void initCities(OnInitBeanListener cityListener) {

    }


}
