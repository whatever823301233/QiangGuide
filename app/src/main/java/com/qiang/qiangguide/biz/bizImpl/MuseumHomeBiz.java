package com.qiang.qiangguide.biz.bizImpl;

import com.example.okhttp_library.OkHttpUtils;
import com.example.okhttp_library.callback.FileCallBack;
import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.biz.IMuseumHomeBiz;
import com.qiang.qiangguide.biz.OnResponseListener;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.util.FileUtil;

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
