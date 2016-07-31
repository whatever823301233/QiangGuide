package com.qiang.qiangguide.biz.bizImpl;

import com.qiang.qiangguide.bean.User;
import com.qiang.qiangguide.biz.IUserBiz;
import com.qiang.qiangguide.biz.OnLoginListener;
import com.qiang.qiangguide.util.LogUtil;

/**
 * Created by Qiang on 2016/7/18.
 */
public class UserBiz implements IUserBiz {


    @Override
    public void login(final String username,final String password, final OnLoginListener loginListener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Thread.sleep(12000);
                } catch (InterruptedException e)
                {
                    LogUtil.e("",e);
                }
                //模拟登录成功
                if ("zq".equals(username) && "123".equals(password))
                {
                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    loginListener.loginSuccess(user);
                } else
                {
                    loginListener.loginFailed();
                }
            }
        }).start();

    }


}
