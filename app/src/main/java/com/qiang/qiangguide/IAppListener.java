package com.qiang.qiangguide;

/**
 * Created by Qiang on 2016/7/13.
 *
 * 监听app的生命状态
 */
public interface IAppListener {

    /**
     *  app退出时，清除数据及状态使用
     */
    void destroy();

}
