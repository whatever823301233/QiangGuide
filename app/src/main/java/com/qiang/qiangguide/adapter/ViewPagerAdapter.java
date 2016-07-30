package com.qiang.qiangguide.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Qiang on 2016/7/20.
 */
public class ViewPagerAdapter extends PagerAdapter {


    //界面列表
    private List<View> views;

    public ViewPagerAdapter (List<View> views){
        this.views = views;
    }

    @Override
    public int getCount() {
        return views==null?0:views.size();
    }

    /**
     * 判断是否由对象生成界面
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position), 0);
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

}
