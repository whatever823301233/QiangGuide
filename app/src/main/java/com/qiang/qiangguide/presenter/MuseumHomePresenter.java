package com.qiang.qiangguide.presenter;

import android.os.Handler;
import android.os.Message;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.qiang.qiangguide.aInterface.IMuseumHomeView;
import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.biz.IMuseumHomeBiz;
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
        //initAudio();
    }

    private void initIntroduce() {
        Museum museum=museumHomeView.getCurrentMuseum();
        String introduce=museum.getTexturl();
        museumHomeView.setIntroduce(introduce);
        handler.sendEmptyMessage(MSG_WHAT_REFRESH_INTRODUCE);
    }

    private void initAudio() {
        museumHomeView.showLoading();
        Museum museum=museumHomeView.getCurrentMuseum();
        String audioUrl=museum.getAudiourl();
        boolean isAudioExists= FileUtil.checkFileExists(audioUrl,museum.getId());
        if(isAudioExists){
            String name=audioUrl.replaceAll("/","_");
            museumHomeView.setMediaPath(Constants.LOCAL_PATH+museum.getId()+"/"+name);
            handler.sendEmptyMessage(MSG_WHAT_REFRESH_MEDIA);
        }else{

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
                    activity.refreshIntroduce();
                    break;
                default:break;
            }
        }
    }


}
