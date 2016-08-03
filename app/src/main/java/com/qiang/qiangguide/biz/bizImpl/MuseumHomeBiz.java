package com.qiang.qiangguide.biz.bizImpl;

import android.os.AsyncTask;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.okhttp_library.OkHttpUtils;
import com.example.okhttp_library.callback.FileCallBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.biz.IMuseumHomeBiz;
import com.qiang.qiangguide.biz.OnInitBeanListener;
import com.qiang.qiangguide.biz.OnResponseListener;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.db.DBHandler;
import com.qiang.qiangguide.util.FileUtil;
import com.qiang.qiangguide.util.LogUtil;
import com.qiang.qiangguide.volley.AsyncPost;
import com.qiang.qiangguide.volley.QVolley;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Qiang on 2016/8/1.
 */
public class MuseumHomeBiz implements IMuseumHomeBiz {

    @Override
    public List<String> getImgUrls(Museum museum) {
        List<String> imgUrls=new ArrayList<>();
        if(museum==null){return imgUrls;}
        String imgStr=museum.getImgurl();
        String[] imgs = imgStr.split(",");
        Collections.addAll(imgUrls,imgs);
        return imgUrls;
    }


    @Override
    public void getExhibitListByMuseumId(String museumId,final  OnInitBeanListener listener) {
        new AsyncTask<String,Integer,List<Exhibit>>(){

            @Override
            protected List<Exhibit> doInBackground(String... params) {
                String id=params[0];
                return DBHandler.getInstance(null).queryAllExhibitListByMuseumId(id);
            }

            @Override
            protected void onPostExecute(List<Exhibit> exhibitList) {
                if(exhibitList!=null&&exhibitList.size()>0){
                    listener.onSuccess(exhibitList);
                }else{
                    listener.onFailed();
                }
            }
        }.execute(museumId);

    }

    @Override
    public void getExhibitListByMuseumIdNet(String museumId,final String tag,final OnInitBeanListener listener) {
        String url=Constants.URL_EXHIBIT_LIST+museumId;
        AsyncPost post=new AsyncPost(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();
                List<Exhibit> exhibitList=gson.fromJson(response,new TypeToken<List<Exhibit>>(){}.getType());
                listener.onSuccess(exhibitList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onFailed();
                LogUtil.e("",error);
            }
        });
        QVolley.getInstance(null).addToAsyncQueue(post,tag);
    }


    @Override
    public void downloadMuseumAudio(String audioUrl,final String id,final OnResponseListener listener) {
        OkHttpUtils
                .get()
                .url(Constants.BASE_URL+audioUrl)
                .build()
                .execute(new FileCallBack(Constants.LOCAL_PATH+id, FileUtil.changeUrl2Name(audioUrl)) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.onFail(e.toString());
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        listener.onSuccess(response.getAbsolutePath());
                    }

                });
    }

}
