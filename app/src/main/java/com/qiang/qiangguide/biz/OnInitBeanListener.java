package com.qiang.qiangguide.biz;

import com.qiang.qiangguide.bean.BaseBean;

import java.util.List;

/**
 * Created by Qiang on 2016/7/29.
 */
public interface OnInitBeanListener {

    void onSuccess(List<BaseBean> beans);
    void onFailed();

}
