package com.qiang.qiangguide.biz.bizImpl;

import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.biz.IPlayShowBiz;
import com.qiang.qiangguide.db.DBHandler;

/**
 * Created by Qiang on 2016/8/19.
 */
public class PlayShowBiz implements IPlayShowBiz {
    @Override
    public Exhibit getExhibit(String id) {
        return DBHandler.getInstance(null).queryExhibit(id);
    }


}
