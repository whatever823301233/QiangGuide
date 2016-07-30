package com.qiang.qiangguide.bean;

import android.content.ContentValues;

import java.io.Serializable;

/**
 * Created by Qiang on 2016/7/12.
 */
public abstract class BaseBean implements Serializable{

    abstract ContentValues toContentValues();

}
