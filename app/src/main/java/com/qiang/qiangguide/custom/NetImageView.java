package com.qiang.qiangguide.custom;

import android.content.Context;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;
import com.qiang.qiangguide.volley.QVolley;

/**
 * Created by Qiang on 2016/7/29.
 */
public class NetImageView extends NetworkImageView {


    public NetImageView(Context context) {
        super(context);
    }

    public NetImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NetImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 显示网络图片
     *
     * @param imgUrl
     *            图片地址
     */
    public void displayImage(String imgUrl) {
        displayImage(imgUrl, 0, 0);
    }

    /**
     * 显示网络图片
     *
     * @param imgUrl
     *            图片地址
     * @param defaultImage
     *            默认图片
     * @param errorImage
     *            错误图片
     */
    public void displayImage(String imgUrl, int defaultImage, int errorImage) {
        // 设置错误图
        setErrorImageResId(errorImage);
        // 设置默认图
        setDefaultImageResId(defaultImage);
        // 加载图片
        setImageUrl(imgUrl, QVolley.getInstance(getContext()).getImageLoader());
    }


}
