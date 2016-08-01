package com.example.okhttp_library.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import okhttp3.Response;

/**
 * Created by xq823 on 2016/8/1.
 */
public abstract class BitmapCallback  extends Callback<Bitmap>{

    @Override
    public Bitmap parseNetworkResponse(Response response , int id) throws Exception
    {
        return BitmapFactory.decodeStream(response.body().byteStream());
    }

}
