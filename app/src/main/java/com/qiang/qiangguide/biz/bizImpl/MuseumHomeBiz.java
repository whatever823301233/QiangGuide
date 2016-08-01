package com.qiang.qiangguide.biz.bizImpl;

import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.biz.IMuseumHomeBiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Qiang on 2016/8/1.
 */
public class MuseumHomeBiz implements IMuseumHomeBiz {
    @Override
    public void initAudio() {

    }

    @Override
    public List<String> getImgUrls(Museum museum) {
        List<String> imgUrls=new ArrayList<>();
        if(museum==null){return imgUrls;}
        String imgStr=museum.getImgurl();
        String[] imgs = imgStr.split(",");
        Collections.addAll(imgUrls,imgs);
        return imgUrls;
    }
}
