package com.qiang.qiangguide.volley;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.qiang.qiangguide.util.BitmapCache;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Qiang on 2016/7/15.
 *
 */
public class  QVolley {

    private static final String TAG="QVolley";
    private static QVolley instance;
    private Context context;
    private RequestQueue requestQueue;
    private ArrayList<AsyncPost> mAsyncPosts;
    private static ExecutorService executorService;
    private ImageLoader imageLoader;


    private QVolley(Context context){
        this.context=context.getApplicationContext();
        mAsyncPosts=new ArrayList<>();
        requestQueue=getRequestQueue();
        executorService = Executors.newCachedThreadPool();
        imageLoader = new ImageLoader(requestQueue, new BitmapCache());
    }

    public static QVolley getInstance( Context context) {
        if(context==null){
            throw new IllegalArgumentException("Context can not be null! ");
        }
        if(instance==null){
            synchronized (QVolley.class){
                if(instance==null){
                    instance=new QVolley(context);
                }
            }
        }
        return instance;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            synchronized (QVolley.class){
                if(requestQueue==null){
                    requestQueue= Volley.newRequestQueue(context);
                }
            }
        }
        return requestQueue;
    }

    /**
     * 获得imageloader实例
     *
     * @return imageloader实例
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }


    /**
     * 将请求添加到队列中
     *
     * @param request
     */
    public <T> void addToRequestQueue(Request<T> request, String tag) {
        // 如果tag为空，则添加默认标记
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        // 添加到消息队列中
        getRequestQueue().add(request);
    }

    /**
     * 将请求添加到队列中
     *
     */
    public void addToAsyncQueue(AsyncPost post, String tag) {
        // 如果tag为空，则添加默认标记
        post.setTag(null == tag || "".equals(tag) ? TAG : tag);
        // 添加到消息队列中
        mAsyncPosts.add(post);
        // 执行方法
        post.executeOnExecutor(executorService, "");
    }

    /**
     * 加载图片
     *
     * @param imgUrl 图片地址
     * @param imageView 图片容器
     * @param defaultImageResId 默认图id
     * @param errorImageResId 图片加载失败的图片id
     */
    public void loadImage(String imgUrl, ImageView imageView,
                          int defaultImageResId, int errorImageResId) {
        imageLoader.get(imgUrl, ImageLoader.getImageListener(imageView,
                defaultImageResId, errorImageResId));
    }


    /**
     * 通过tag取消队列中的请求
     *
     * @param tag 消息标记
     */
    public void cancelFromRequestQueue(String tag) {
        if(mAsyncPosts!=null&&mAsyncPosts.size()>0){
            for (int i = 0; i < mAsyncPosts.size(); i++) {
                if (tag.equals(mAsyncPosts.get(i).getTag())) {
                    mAsyncPosts.get(i).cancel(true);
                    mAsyncPosts.remove(i);
                }
            }
        }
        getRequestQueue().cancelAll(null == tag ? TAG : tag);
    }
    public void destroy(){
        requestQueue=null;
    }

}
