package com.qiang.qiangguide.biz.bizImpl;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.okhttp_library.OkHttpUtils;
import com.example.okhttp_library.callback.FileCallBack;
import com.example.okhttp_library.callback.StringCallback;
import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.biz.IMuseumChooseBiz;
import com.qiang.qiangguide.biz.OnInitBeanListener;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.db.handler.MuseumHandler;
import com.qiang.qiangguide.util.FileUtil;
import com.qiang.qiangguide.util.LogUtil;
import com.qiang.qiangguide.volley.AsyncPost;
import com.qiang.qiangguide.volley.QVolley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;

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
                onInitBeanListener.onFailed();
            }
        });
        QVolley.getInstance(null).addToAsyncQueue(post,tag);
    }

    @Override
    public List<Museum> initMuseumListBySQL(String city) {
        return  MuseumHandler.queryAllMuseumByCity(city);
    }

    @Override
    public void saveMuseumBySQL(List<Museum> museumList) {
        MuseumHandler.addMuseumList(museumList);
    }

    @Override
    public void downloadMuseum(Museum museum,final DownloadProgressListener listener) {
        if(museum==null){return;}
        final String museumId=museum.getId();
        String url=Constants.URL_ASSETS_LIST+museumId;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.e("",e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.i("",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray array= (JSONArray) jsonObject.get("url");
                            LogUtil.i("",array.toString());
                            List<String> urlList=JSON.parseArray(array.toString(),String.class);
                            totalSize=urlList.size();
                            LogUtil.i("",urlList.size());
                            downloadFiles(urlList,museumId,listener);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });


    }

    @Override
    public void updateDownloadState( Museum museum ) {
        if( museum == null ){ return; }
        MuseumHandler.updateMuseum(museum);
    }

    @Override
    public void addMuseum( Museum museum ) {
        MuseumHandler.addMuseum( museum );
    }

    private void downloadFiles(List<String> urlList,String museumId,DownloadProgressListener listener) {
        if(urlList==null){return;}
        ExecutorService executor= Executors.newFixedThreadPool(5);
        for(String str:urlList){
            if(TextUtils.isEmpty(str)){continue;}
            DownloadTask task=new DownloadTask(listener);
            task.executeOnExecutor(executor,str,museumId);
        }

    }
    int totalSize;
    int progress;

    private class DownloadTask extends AsyncTask<String,Integer,String>{


        DownloadProgressListener listener;

        public DownloadTask(DownloadProgressListener listener){
            this.listener=listener;
        }

        @Override
        protected String doInBackground(String... params) {
            String mUrl=params[0];
            String name= FileUtil.changeUrl2Name(mUrl);
            String url=Constants.BASE_URL+mUrl;
            String savePath=Constants.LOCAL_PATH+params[1];
            boolean flag=FileUtil.checkFileExists(savePath+"/"+name);
            if(flag){
                progress++;
                if(listener!=null){
                    listener.onProgress(progress,totalSize);
                }
            }else{
                OkHttpUtils
                        .get()
                        .url(url)
                        .build()
                        .execute(new FileCallBack(savePath,name) {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                LogUtil.e("",e);
                            }

                            @Override
                            public void onResponse(File response, int id) {
                                LogUtil.i("",response.getAbsolutePath());
                                progress++;
                                if(listener!=null){
                                    listener.onProgress(progress,totalSize);
                                }
                            }
                        });
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
           /* progress++;
            if(listener!=null){
                listener.onProgress(progress,totalSize);
            }*/

        }
    }

}
