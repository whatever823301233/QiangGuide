package com.example.okhttp_library.callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by xq823 on 2016/8/1.
 */
public abstract class StringCallback extends Callback<String> {

    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException
    {
        return response.body().string();
    }

}
