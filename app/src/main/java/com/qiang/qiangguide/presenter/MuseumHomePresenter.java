package com.qiang.qiangguide.presenter;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.qiang.qiangguide.AppManager;
import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.IMainGuideView;
import com.qiang.qiangguide.aInterface.IMuseumHomeView;
import com.qiang.qiangguide.activity.CollectionActivity;
import com.qiang.qiangguide.activity.MainGuideActivity;
import com.qiang.qiangguide.activity.TopicActivity;
import com.qiang.qiangguide.bean.BaseBean;
import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.bean.Label;
import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.bean.MyBeacon;
import com.qiang.qiangguide.biz.IMuseumHomeBiz;
import com.qiang.qiangguide.biz.OnInitBeanListener;
import com.qiang.qiangguide.biz.OnResponseListener;
import com.qiang.qiangguide.biz.bizImpl.MuseumHomeBiz;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.util.FileUtil;
import com.qiang.qiangguide.util.LogUtil;
import com.qiang.qiangguide.volley.AsyncPost;
import com.qiang.qiangguide.volley.QVolley;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Qiang on 2016/8/1.
 */
public class MuseumHomePresenter {

    private static final  int  MSG_WHAT_REFRESH_VIEW=9527;
    private static final  int  MSG_WHAT_SET_TITLE=9528;
    private static final  int  MSG_WHAT_REFRESH_ICONS=9529;
    private static final  int  MSG_WHAT_REFRESH_INTRODUCE=9530;
    private static final  int  MSG_WHAT_REFRESH_MEDIA=9531;
    private static final  int  MSG_WHAT_SHOW_ERROR=9532;

    private IMuseumHomeView museumHomeView;
    private IMuseumHomeBiz museumHomeBiz;
    private Handler handler;


    public MuseumHomePresenter(IMuseumHomeView museumHomeView){
        this.museumHomeView=museumHomeView;
        museumHomeBiz=new MuseumHomeBiz();
        handler=new MyHandler(museumHomeView);
    }


    public void initData(){
        museumHomeView.setAdapterMuseumId();
        initTitle();
        initIcons();
        initIntroduce();
        initAudio();
        initExhibits();
        initBeacons();
        initLabels();
    }

    private void initLabels() {
        museumHomeView.showLoading();
        Museum museum=museumHomeView.getCurrentMuseum();
        final String museumId=museum.getId();
        List<Label> labels=museumHomeBiz.getLabels(museumId);
        if(labels!=null&&labels.size()>0){
            museumHomeView.hideLoading();
            return;
        }
        museumHomeBiz.getLabelListByNet(museumId,new OnInitBeanListener(){
            @Override
            public void onSuccess(List<? extends BaseBean> beans) {
                List<Label> labelList= (List<Label>) beans;
                museumHomeBiz.saveLabels(labelList);
            }

            @Override
            public void onFailed() {
                museumHomeView.showFailedError();
            }
        },museumHomeView.getTag());


    }

    private void initBeacons() {
        museumHomeView.showLoading();
        Museum museum=museumHomeView.getCurrentMuseum();
        final String museumId=museum.getId();

        List<MyBeacon> beacons=museumHomeBiz.getMyBeacons(museumId);
        if(beacons!=null&&beacons.size()>0){
            museumHomeView.hideLoading();
            return;
        }
        museumHomeBiz.getBeaconListByNet(museumId,new OnInitBeanListener(){
            @Override
            public void onSuccess(List<? extends BaseBean> beans) {
                List<MyBeacon> beacons= (List<MyBeacon>) beans;
                museumHomeBiz.saveBeacons(beacons);
                LogUtil.i("","addBeacons 保存成功 ");
            }
            @Override
            public void onFailed() {
                museumHomeView.showFailedError();
            }
        },museumHomeView.getTag());
    }

    private void initExhibits() {
        museumHomeView.showLoading();
        Museum museum=museumHomeView.getCurrentMuseum();
        final String museumId=museum.getId();
        museumHomeBiz.getExhibitListByMuseumId(museumId,new OnInitBeanListener(){
            @Override
            public void onSuccess(List<? extends BaseBean> beans) {
                LogUtil.i("","getExhibitListByMuseumId onSuccess");
                handler.sendEmptyMessage(MSG_WHAT_REFRESH_VIEW);
            }

            @Override
            public void onFailed() {
                LogUtil.i("","getExhibitListByMuseumId onFailed");
                museumHomeBiz.getExhibitListByMuseumIdNet(museumId, museumHomeView.getTag(), new OnInitBeanListener() {
                    @Override
                    public void onSuccess(List<? extends BaseBean> beans) {
                        LogUtil.i("","getExhibitListByMuseumIdNet onSuccess");
                        List<Exhibit> exhibitList= (List<Exhibit>) beans;
                        museumHomeBiz.saveExhibit(exhibitList);
                        handler.sendEmptyMessage(MSG_WHAT_REFRESH_VIEW);
                    }

                    @Override
                    public void onFailed() {
                        LogUtil.i("","getExhibitListByMuseumIdNet onFailed");
                        handler.sendEmptyMessage(MSG_WHAT_SHOW_ERROR);
                    }
                });
            }
        });
    }

    private void initIntroduce() {
        Museum museum=museumHomeView.getCurrentMuseum();
        String introduce=museum.getTexturl();
        museumHomeView.setIntroduce(introduce);
        handler.sendEmptyMessage(MSG_WHAT_REFRESH_INTRODUCE);
    }

    private void initAudio() {
        museumHomeView.showLoading();
        final Museum museum=museumHomeView.getCurrentMuseum();
        String audioUrl=museum.getAudiourl();
        boolean isAudioExists= FileUtil.checkFileExists(audioUrl,museum.getId());
        if(isAudioExists){
            String name=audioUrl.replaceAll("/","_");
            museumHomeView.setMediaPath(Constants.LOCAL_PATH+museum.getId()+"/"+name);
            handler.sendEmptyMessage(MSG_WHAT_REFRESH_MEDIA);
        }else{
            museumHomeBiz.downloadMuseumAudio(audioUrl,museum.getId(),new OnResponseListener(){
                @Override
                public void onSuccess(String path) {
                    museumHomeView.setMediaPath(path);
                    handler.sendEmptyMessage(MSG_WHAT_REFRESH_MEDIA);
                }

                @Override
                public void onFail(String error) {
                    handler.sendEmptyMessage(MSG_WHAT_SHOW_ERROR);
                }
            });
        }
        AsyncPost post =new AsyncPost(Constants.BASE_URL+audioUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtil.i("",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("",error);
            }
        });
        QVolley.getInstance(null).addToAsyncQueue(post,museumHomeView.getTag());
    }

    private void initIcons() {
        Museum museum=museumHomeView.getCurrentMuseum();
        List<String> imgUrls = museumHomeBiz.getImgUrls(museum);
        museumHomeView.setIconUrls(imgUrls);
        handler.sendEmptyMessage(MSG_WHAT_REFRESH_ICONS);
    }

    private void initTitle() {
        Museum museum=museumHomeView.getCurrentMuseum();
        if(museum==null){return;}
        Message msg=Message.obtain();
        msg.what=MSG_WHAT_SET_TITLE;
        msg.obj=museum.getName();
        handler.sendMessage(msg);
    }

    public void onImageButtonClick() {
        View view=museumHomeView.getOnClickView();
        Intent intent=null;
        switch (view.getId()){
            case R.id.rlGuideHome:
                intent=new Intent(museumHomeView.getContext(), MainGuideActivity.class);
                intent.putExtra(IMainGuideView.INTENT_FRAGMENT_FLAG,IMainGuideView.INTENT_FLAG_GUIDE);
                intent.putExtra(Constants.INTENT_MUSEUM_ID,museumHomeView.getCurrentMuseum().getId());
                break;
            case R.id.rlMapHome:
                intent=new Intent(museumHomeView.getContext(), MainGuideActivity.class);
                intent.putExtra(IMainGuideView.INTENT_FRAGMENT_FLAG,IMainGuideView.INTENT_FLAG_GUIDE_MAP);
                intent.putExtra(Constants.INTENT_MUSEUM_ID,museumHomeView.getCurrentMuseum().getId());
                break;
            case R.id.rlTopicHome:
                intent=new Intent(museumHomeView.getContext(), TopicActivity.class);
                intent.putExtra(Constants.INTENT_MUSEUM_ID,museumHomeView.getCurrentMuseum().getId());
                break;
            case R.id.rlCollectionHome:
                intent=new Intent(museumHomeView.getContext(), CollectionActivity.class);
                intent.putExtra(Constants.INTENT_MUSEUM_ID,museumHomeView.getCurrentMuseum().getId());
                break;
        }
        if(intent!=null){
            museumHomeView.toNextActivity(intent);
        }
    }

    /*用于计算点击返回键时间*/
    private long mExitTime=0;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(museumHomeView.isDrawerOpen()){
                museumHomeView.closeDrawer();
            }else {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(museumHomeView.getContext(), "在按一次退出", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                    AppManager.getInstance(museumHomeView.getContext()).exitApp();
                }
            }
            return true;
        }
        //拦截MENU按钮点击事件，让他无任何操作
        else if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return false;
    }


    static class MyHandler extends Handler {

        WeakReference<IMuseumHomeView> activityWeakReference;
        MyHandler(IMuseumHomeView activity){
            this.activityWeakReference=new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            if(activityWeakReference==null){return;}
            IMuseumHomeView activity=activityWeakReference.get();
            if(activity==null){return;}
            switch (msg.what){
                case MSG_WHAT_REFRESH_VIEW:
                    activity.refreshView();
                    activity.hideLoading();
                    break;
                case MSG_WHAT_SET_TITLE:
                    String museumName = (String) msg.obj;
                    activity.setTitle(museumName);
                    break;
                case MSG_WHAT_REFRESH_ICONS:
                    activity.refreshIcons();
                    break;
                case MSG_WHAT_REFRESH_INTRODUCE:
                    activity.refreshIntroduce();
                    break;
                case MSG_WHAT_REFRESH_MEDIA:
                    activity.refreshMedia();
                    break;
                case MSG_WHAT_SHOW_ERROR:
                    activity.hideLoading();
                    activity.showFailedError();
                    break;
                default:break;
            }
        }
    }


}
