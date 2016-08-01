package com.example.okhttp_library.builder;

import com.example.okhttp_library.request.PostStringRequest;
import com.example.okhttp_library.request.RequestCall;

import okhttp3.MediaType;

/**
 * Created by xq823 on 2016/8/1.
 */
public class PostStringBuilder  extends OkHttpRequestBuilder<PostStringBuilder> {

    private String content;
    private MediaType mediaType;


    public PostStringBuilder content(String content)
    {
        this.content = content;
        return this;
    }

    public PostStringBuilder mediaType(MediaType mediaType)
    {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCall build()
    {
        return new PostStringRequest(url, tag, params, headers, content, mediaType,id).build();
    }

}
