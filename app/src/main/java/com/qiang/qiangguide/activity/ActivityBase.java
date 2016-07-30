package com.qiang.qiangguide.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.qiang.qiangguide.AppManager;
import com.qiang.qiangguide.volley.QVolley;

/**
 * Created by Qiang on 2016/7/12.
 *
 */
public abstract class ActivityBase extends AppCompatActivity {

    /**
     * 类唯一标记
     */
    public String TAG = getClass().getSimpleName();

    public ActivityBase getActivity(){
        return this;
    }

    /**
     * 获得当前activity的tag
     *
     * @return activity的tag
     */
    public String getTag() {
        return TAG;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance( getApplicationContext() ).addActivity( this );
    }

    @Override
    protected void onStop() {
        super.onStop();
        QVolley.getInstance(this).cancelFromRequestQueue(getTag());

    }

    public void showToast(String content){
        Toast.makeText(getActivity(),content,Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance( getApplicationContext() ).removeActivity( this );
    }

    /**
     * 响应后退按键
     */
    public void keyBack() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean onKey = true;
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                keyBack();
                break;
            default:
                onKey = super.onKeyDown(keyCode, event);
                break;
        }
        return onKey;
    }


}
