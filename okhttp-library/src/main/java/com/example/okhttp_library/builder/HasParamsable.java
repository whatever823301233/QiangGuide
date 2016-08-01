package com.example.okhttp_library.builder;

import java.util.Map;

/**
 * Created by xq823 on 2016/8/1.
 */
public interface HasParamsable {

    OkHttpRequestBuilder params(Map<String, String> params);
    OkHttpRequestBuilder addParams(String key, String val);

}
