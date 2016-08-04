package com.qiang.qiangguide.biz.bizImpl;

import android.os.AsyncTask;

import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.biz.ITopicBiz;
import com.qiang.qiangguide.biz.OnInitBeanListener;
import com.qiang.qiangguide.db.DBHandler;

import java.util.List;

/**
 * Created by Qiang on 2016/8/4.
 */
public class TopicBiz implements ITopicBiz {
    @Override
    public void initAllExhibitByMuseumId(String museumId,final  OnInitBeanListener listener, String tag) {

        new AsyncTask<String,Integer,Void>(){
            @Override
            protected Void doInBackground(String... params) {
                String id=params[0];
                List<Exhibit> exhibitList= DBHandler.getInstance(null).queryAllExhibitListByMuseumId(id);
                if(exhibitList==null||exhibitList.size()==0){
                    listener.onFailed();
                }else{
                    listener.onSuccess(exhibitList);
                }
                return null;
            }
        }.execute(museumId);

    }
}
