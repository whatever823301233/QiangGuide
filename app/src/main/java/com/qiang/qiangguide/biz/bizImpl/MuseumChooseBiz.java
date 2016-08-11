package com.qiang.qiangguide.biz.bizImpl;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.biz.IMuseumChooseBiz;
import com.qiang.qiangguide.biz.OnInitBeanListener;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.util.LogUtil;
import com.qiang.qiangguide.volley.AsyncPost;
import com.qiang.qiangguide.volley.QVolley;

import java.util.List;

/**
 * Created by xq823 on 2016/7/31.
 */
public class MuseumChooseBiz implements IMuseumChooseBiz{


    @Override
    public void initMuseumListByNet(String city, String tag, final OnInitBeanListener onInitBeanListener) {
        AsyncPost post=new AsyncPost(Constants.URL_MUSEUM_LIST,new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                LogUtil.i("",response);
                List<Museum> museumList= JSON.parseArray(response, Museum.class);
                if(museumList!=null&&museumList.size()>0){
                    onInitBeanListener.onSuccess(museumList);
                }else{
                    onInitBeanListener.onFailed();
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
    public List<Museum> initMuseumListBySQL(String city) {
        return null;
    }


}
