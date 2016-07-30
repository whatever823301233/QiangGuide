package com.qiang.qiangguide.aInterface;

import com.qiang.qiangguide.bean.User;

/**
 * Created by Qiang on 2016/7/12.
 */
public interface IUserLoginView {

    String getUserName();

    String getPassword();

    void clearUserName();

    void clearPassword();

    void showLoading();

    void hideLoading();

    void hideKeyboard();

    void toMainActivity(User user);

    void showFailedError();

}
