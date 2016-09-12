package com.qiang.qiangguide.biz.bizImpl;

import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.biz.IPlayShowBiz;
import com.qiang.qiangguide.db.DBHandler;

/**
 * Created by Qiang on 2016/8/19.
 */
public class PlayShowBiz implements IPlayShowBiz {


    @Override
    public Museum getMuseum(String museumId) {
        return DBHandler.getInstance(null).queryMuseum(museumId);
    }
}
