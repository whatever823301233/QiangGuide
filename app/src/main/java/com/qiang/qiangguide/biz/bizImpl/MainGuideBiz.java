package com.qiang.qiangguide.biz.bizImpl;

import android.os.AsyncTask;

import com.qiang.qiangguide.beacon.CustomBeaconManager;
import com.qiang.qiangguide.beacon.OnBeaconCallback;
import com.qiang.qiangguide.beacon.SystekBeacon;
import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.bean.MyBeacon;
import com.qiang.qiangguide.biz.IMainGuideBiz;
import com.qiang.qiangguide.biz.OnInitBeanListener;
import com.qiang.qiangguide.db.handler.BeaconHandler;
import com.qiang.qiangguide.db.handler.ExhibitHandler;
import com.qiang.qiangguide.util.LogUtil;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Qiang on 2016/8/10.
 */
public class MainGuideBiz implements IMainGuideBiz {

    private Executor executorService;
    public MainGuideBiz(){
        executorService= Executors.newCachedThreadPool();
    }

    @Override
    public void findExhibits(String museumId, final Collection<Beacon> beacons, final OnInitBeanListener listener) {


        AsyncTask<String,Integer,List<Exhibit>> task = new AsyncTask<String,Integer,List<Exhibit>>(){

            @Override
            protected List<Exhibit> doInBackground(String... params) {
                String museumId=params[0];
                List<MyBeacon> myBeacons= BeaconHandler.queryBeacons(beacons);
                if(myBeacons!=null){
                    List<Exhibit> exhibitList= ExhibitHandler.queryExhibit(museumId,myBeacons);
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
                }else{
                    listener.onFailed();
                }
            }
        };
        task.executeOnExecutor(executorService,museumId);
    }

    @Override
    public void getExhibits(String museumId, Collection<Beacon> beacons, OnBeaconCallback callback) {
        CustomBeaconManager.getInstance().calculateDistance(beacons);
        List<SystekBeacon> exhibitLocateBeacons = CustomBeaconManager.getInstance().getExhibitLocateBeacons();
        if (callback==null){return;}
        List<MyBeacon> beaconList=changeToBeaconList(exhibitLocateBeacons,20.0);
        if(beaconList==null||beaconList.size()==0){return;}
        List<Exhibit> exhibitBeansList=searchExhibitByBeacon(beaconList);
        if (exhibitBeansList==null||exhibitBeansList.size()==0) {return;}
        // TODO: 2016/9/27
        callback.getNearestBeacon(beaconList.get(0));
        callback.getExhibits(exhibitBeansList);
        callback.getNearestExhibit(exhibitBeansList.get(0));
    }

    /**
     * 根据beaconforsort集合找出beacon集合
     * @param beacons beacon结合
     * @param dis 规定距离内beacon
     * @return
     */
    private static List<MyBeacon> changeToBeaconList( List<SystekBeacon> beacons,double dis) {
        List <MyBeacon> beaconBeans=new ArrayList<>();
        if(beacons==null||beacons.size()==0){
            return null;
        }

        for (SystekBeacon beacon:beacons) {
            String major = beacon.getMajor();
            String minor = beacon.getMinor();
            //根据beacon的minor和major参数获得beacon对象
            MyBeacon beaconBean= BeaconHandler.queryBeacons(minor, major);
            if(beaconBean==null){continue;}
            beaconBean.setDistance(beacon.getDistance());
            //设定距离范围，暂定小于1米则放入列表
          /* if(systekBeacon.getDistance()<dis){
            }*/
            beaconBeans.add(beaconBean);
        }
        return beaconBeans;
    }

    /**
     * 根据beacon集合找出展品集合
     * @param beaconBeans
     * @return
     */
    private static List<Exhibit> searchExhibitByBeacon(List<MyBeacon> beaconBeans) {

        List<Exhibit> exhibitList= new ArrayList<>();

        if(beaconBeans==null||beaconBeans.size()==0){return exhibitList;}
        String museumId= beaconBeans.get(0).getMuseumId();
            /*遍历beacon结合，获得展品列表*/
        for(int i=0;i<beaconBeans.size();i++){
            MyBeacon beacon=beaconBeans.get(i);
            if(beacon==null){continue;}
            String beaconId=beacon.getId();
            List<Exhibit> tempList= ExhibitHandler.queryExhibit(museumId,beaconId);
            if(tempList==null||tempList.size()==0){continue;}
            /*for(Exhibit beaconBean:tempList){
                beaconBean.setDistance(beaconBeans.get(i).getDistance());
            }*/
                /*去重复*/
            exhibitList.removeAll(tempList);
            exhibitList.addAll(tempList);
        }
        return exhibitList;
    }

}
