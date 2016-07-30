package com.qiang.qiangguide.biz;

import com.qiang.qiangguide.bean.User;

/**
 * Created by Qiang on 2016/7/18.
 */
public interface OnLoginListener {

    void loginSuccess(User user);

    void loginFailed();

}
