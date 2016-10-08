package com.qiang.qiangguide.biz.bizImpl;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.biz.ICollectionBiz;
import com.qiang.qiangguide.biz.OnInitBeanListener;
import com.qiang.qiangguide.db.handler.ExhibitHandler;

import java.util.List;

/**
 * Created by Qiang on 2016/8/9.
 */
public class CollectionBiz implements ICollectionBiz {


    @Override
    public void initExhibitByMuseumId(String museumId,final OnInitBeanListener listener, String tag) {

        // TODO: 2016/8/9 QVolley 中加入普通异步任务
        new AsyncTask<String,Integer,Void>(){
            @Override
            protected Void doInBackground(String... params) {
                String id=params[0];
                if(TextUtils.isEmpty(id)){
                    listener.onFailed();
                }else{
                    List<Exhibit> exhibitList= ExhibitHandler.queryFavoriteExhibitListByMuseumId(id);
                    listener.onSuccess(exhibitList);
                }
                return null;
            }
        }.execute(museumId);
    }
}
