package com.qiang.qiangguide.presenter;

import android.text.TextUtils;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.IMainGuideView;
import com.qiang.qiangguide.biz.IMainGuideBiz;
import com.qiang.qiangguide.biz.bizImpl.MainGuideBiz;

import java.util.logging.Handler;

/**
 * Created by Qiang on 2016/8/10.
 */
public class MainGuidePresenter {


    private IMainGuideView mainGuideView;
    private IMainGuideBiz mainGuideBiz;
    private Handler handler;

    public MainGuidePresenter(IMainGuideView mainGuideView){
        this.mainGuideView=mainGuideView;
        mainGuideBiz=new MainGuideBiz();

    }

    public void onCheckedRadioButton(int checkedId) {
        switch(checkedId){
            case R.id.radioButtonList:
                mainGuideView.changeToNearExhibitFragment();
                break;
            case R.id.radioButtonMap:
                mainGuideView.changeToMapExhibitFragment();
                break;
        }
    }
    /**
     * 设置默认fragment
     */
    public void setDefaultFragment() {
        String flag=mainGuideView.getFragmentFlag();
        if(TextUtils.isEmpty(flag)||flag.equals(IMainGuideView.INTENT_FLAG_GUIDE)){
            mainGuideView.changeToNearExhibitFragment();
        }else{
            mainGuideView.changeToMapExhibitFragment();
        }
    }
}
