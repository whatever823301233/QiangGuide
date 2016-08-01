package com.example.okhttp_library.utils;

import android.util.Log;

/**
 * Created by xq823 on 2016/8/1.
 */
public class L {

    private static boolean debug = false;

    public static void e(String msg)
    {
        if (debug)
        {
            Log.e("OkHttp", msg);
        }
    }

}
