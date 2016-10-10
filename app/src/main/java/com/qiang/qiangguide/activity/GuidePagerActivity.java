package com.qiang.qiangguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.adapter.ViewPagerAdapter;
import com.qiang.qiangguide.util.Utility;

import java.util.ArrayList;

/**
 * Created by Qiang on 2016/7/20.
 * 引导页activity
 */
public class GuidePagerActivity extends ActivityBase {

    //定义ViewPager对象
    private ViewPager viewPager;
    //定义ViewPager适配器
    private ViewPagerAdapter vpAdapter;
    //定义一个ArrayList来存放View
    private ArrayList<View> views;
    //引导图片资源
    private static final int[] pics = {R.drawable.guide1,R.drawable.guide2,R.drawable.guide3};
    //底部小点的图片
    private ImageView[] points;
    //记录当前选中位置
    private int currentIndex;
    private Button btnSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_pager);
        initView();
        initData();
    }

    @Override
    void errorRefresh() {

    }

    /**
     * 初始化组件
     */
    private void initView(){
        //实例化ArrayList对象
        views = new ArrayList<>();
        //实例化ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //实例化ViewPager适配器
        vpAdapter = new ViewPagerAdapter(views);

        btnSkip = ( Button ) findViewById(R.id.btn_skip);
        if (btnSkip != null) {
            btnSkip.setOnClickListener(skipListener);
        }
    }

    /**
     * 初始化数据
     */
    private void initData(){
        //定义一个布局并设置参数
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        //初始化引导图片列表
        for (int pic : pics) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageResource(pic);
            views.add(iv);
        }

        //设置数据
        viewPager.setAdapter(vpAdapter);
        //设置监听
        viewPager.addOnPageChangeListener(onPageChangeListener);

        //初始化底部小点
        initPoint();
        //设置跳转按钮是否可见
        setButtonVisible();
    }

    /**
     * 初始化底部小点
     */
    private void initPoint(){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);

        points = new ImageView[pics.length];

        //循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            //得到一个LinearLayout下面的每一个子元素
            if (linearLayout != null) {
                points[i] = (ImageView) linearLayout.getChildAt(i);
            }
            //默认都设为灰色
            points[i].setEnabled(true);
            //给每个小点设置监听
            points[i].setOnClickListener(onClickListener);
            //设置位置tag，方便取出与当前位置对应
            points[i].setTag(i);
        }

        //设置当面默认的位置
        currentIndex = 0;
        //设置为白色，即选中状态
        points[currentIndex].setEnabled(false);
    }


    private View.OnClickListener skipListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Utility.startActivity(getActivity(),new Intent(getActivity(),CityChooseActivity.class));
            finish();
        }
    };


    private View.OnClickListener onClickListener = new View.OnClickListener() {

        /* 通过点击事件来切换当前的页面*/
        @Override
        public void onClick(View v) {
            int position = (Integer)v.getTag();
            setCurView(position);
            setCurDot(position);
        }
    };


    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        //当前页面被滑动时调用
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        //当新的页面被选中时调用
        @Override
        public void onPageSelected(int position) {
            //设置底部小点选中状态
            setCurDot(position);
        }

        //当滑动状态改变时调用
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * 设置当前页面的位置
     */
    private void setCurView(int position){
        if (position < 0 || position >= pics.length) {
            return;
        }
        viewPager.setCurrentItem(position);
    }

    /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int position){
        if (position < 0 || position > pics.length - 1 || currentIndex == position) {
            return;
        }
        points[position].setEnabled(false);
        points[currentIndex].setEnabled(true);

        currentIndex = position;
        setButtonVisible();
    }

    private void setButtonVisible(){
        if( currentIndex == points.length-1 ){
            btnSkip.setVisibility(View.VISIBLE);
        }else{
            btnSkip.setVisibility(View.GONE);
        }
    }


}
