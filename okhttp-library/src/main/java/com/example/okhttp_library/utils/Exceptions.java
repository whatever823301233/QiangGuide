package com.example.okhttp_library.utils;

/**
 * Created by xq823 on 2016/8/1.
 */
public class Exceptions {

    public static void illegalArgument(String msg, Object... params)
    {
        throw new IllegalArgumentException(String.format(msg, params));
    }

}
