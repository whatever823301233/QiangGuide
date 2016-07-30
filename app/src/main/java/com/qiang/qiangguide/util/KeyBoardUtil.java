package com.qiang.qiangguide.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Qiang on 2016/7/13.
 *
 * 键盘工具类
 */
public class KeyBoardUtil {

    /**
     *
     * 隐藏键盘
     * @param act   activity对象
     * @since  1.0.0
     */
    public static void hideKeyBoard( Activity act ) {

        if (act == null){
            return;
        }
        try {
            InputMethodManager imm = ( InputMethodManager )act.getSystemService( act.INPUT_METHOD_SERVICE );
            if( act != null && act.getCurrentFocus() != null ) {
                imm.hideSoftInputFromWindow( act.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS );
            }
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

}
