package com.qiang.qiangguide.biz.bizImpl;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.qiang.qiangguide.bean.City;
import com.qiang.qiangguide.biz.ICityBiz;
import com.qiang.qiangguide.biz.OnInitBeanListener;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.db.handler.CityHandler;
import com.qiang.qiangguide.util.LogUtil;
import com.qiang.qiangguide.volley.AsyncPost;
import com.qiang.qiangguide.volley.QVolley;

import java.util.List;

/**
 * Created by Qiang on 2016/7/29.
 *
 */
public class CityBiz implements ICityBiz {

    private List<City> cities;
    @Override
    public List<City> fixCityName(String cityName, List<City> cities) {
        return null;
    }

    @Override
    public void initCitiesByNet(String tag, final OnInitBeanListener cityListener) {
        AsyncPost post=new AsyncPost(Constants.URL_CITY_LIST,new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                LogUtil.i("",response);
                List<City> cities= JSON.parseArray(response,City.class);
                if(cities!=null&&cities.size()>0){
                    cityListener.onSuccess(cities);
                }else{
                    cityListener.onFailed();
                }
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("",error);
            }
        });
        QVolley.getInstance(null).addToAsyncQueue(post,tag);
    }

    @Override
    public List<City> initCitiesBySQL(OnInitBeanListener cityListener) {
        cities= CityHandler.queryAllCities();
        if(cityListener==null){return cities;}
        if(cities==null||cities.size()==0){
            cityListener.onFailed();
        }else{
            cityListener.onSuccess(cities);
        }
        return cities;
    }

    @Override
    public void saveCitiesBySQL(List<City> cities) {
        CityHandler.addCities(cities);
    }


}
