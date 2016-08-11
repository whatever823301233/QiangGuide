package com.qiang.qiangguide.biz.bizImpl;

import android.os.AsyncTask;

import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.bean.MyBeacon;
import com.qiang.qiangguide.biz.IMainGuideBiz;
import com.qiang.qiangguide.biz.OnInitBeanListener;
import com.qiang.qiangguide.db.DBHandler;
import com.qiang.qiangguide.util.LogUtil;

import org.altbeacon.beacon.Beacon;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Qiang on 2016/8/10.
 */
public class MainGuideBiz implements IMainGuideBiz {

    private static Executor executorService;
    public MainGuideBiz(){
        executorService= Executors.newCachedThreadPool();
    }


    @Override
    public void findExhibits(String museumId, final Collection<Beacon> beacons, final OnInitBeanListener listener) {


        AsyncTask<String,Integer,List<Exhibit>> task = new AsyncTask<String,Integer,List<Exhibit>>(){

            @Override
            protected List<Exhibit> doInBackground(String... params) {
                String museumId=params[0];
                List<MyBeacon> myBeacons=DBHandler.getInstance(null).queryBeacons(beacons);
                if(myBeacons!=null){
                    List<Exhibit> exhibitList=DBHandler.getInstance(null).queryExhibit(museumId,myBeacons);
                    if(exhibitList!=null&&exhibitList.size()>0){
                        LogUtil.i("","搜索结果 "+exhibitList.size());
                        return exhibitList;
                    }else{
                        LogUtil.i("","搜索结果为空 ");
                        return Collections.emptyList();
                    }
                }
                LogUtil.i("","MyBeacon==null");
                return Collections.emptyList();
            }

            @Override
            protected void onPostExecute(List<Exhibit> exhibits) {
                if(exhibits!=null){
                    listener.onSuccess(exhibits);
                }
            }
        };
        task.executeOnExecutor(executorService,museumId);
    }


}
