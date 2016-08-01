package com.example.okhttp_library.builder;

import com.example.okhttp_library.OkHttpUtils;
import com.example.okhttp_library.request.OtherRequest;
import com.example.okhttp_library.request.RequestCall;

/**
 * Created by xq823 on 2016/8/1.
 */
public class HeadBuilder extends GetBuilder {

    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }

}
