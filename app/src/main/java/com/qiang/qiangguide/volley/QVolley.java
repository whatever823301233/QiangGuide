package com.qiang.qiangguide.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.qiang.qiangguide.util.BitmapCache;
import com.qiang.qiangguide.util.BitmapUtil;
import com.qiang.qiangguide.util.LogUtil;

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
        imageLoader = new ImageLoader(requestQueue, BitmapCache.getInstance());
    }

    public static QVolley getInstance( Context context) {

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


    public void loadBlurImage(String imgUrl, final ImageView imageView, final int defaultImageResId, final int errorImageResId) {
        imageLoader.get(imgUrl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bitmap=response.getBitmap();
                if (bitmap != null) {
                    bitmap=BitmapUtil.blur(bitmap,imageView.getContext());
                    imageView.setImageBitmap(bitmap);
                } else if (defaultImageResId != 0) {
                    imageView.setImageResource(defaultImageResId);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorImageResId != 0) {
                    imageView.setImageResource(errorImageResId);
                }
            }
        });
    }


    /**
     * 加载图片
     *
     * @param imgUrl 图片地址
     * @param imageView 图片容器
     * @param defaultImageResId 默认图id
     * @param errorImageResId 图片加载失败的图片id
     */
    public void loadImageIcon(String imgUrl,final ImageView imageView,
                              final int defaultImageResId,final int errorImageResId) {
        imageLoader.get(imgUrl,  new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorImageResId != 0) {
                    imageView.setImageResource(errorImageResId);
                }
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    Bitmap bm=response.getBitmap();
                    Bitmap icon=BitmapUtil.scaleBitmap(bm,BitmapUtil.MAX_ART_WIDTH_ICON,BitmapUtil. MAX_ART_HEIGHT_ICON);
                    icon = BitmapUtil.getRoundedCornerBitmap(icon);
                    imageView.setImageBitmap(icon);
                } else if (defaultImageResId != 0) {
                    imageView.setImageResource(defaultImageResId);
                }
            }
        });
    }


    /**
     * 加载图片
     *
     * @param imgUrl 图片地址
     * @param imageView 图片容器
     */
    public void loadImageIcon(String imgUrl, ImageView imageView) {
        imageLoader.get(imgUrl, ImageLoader.getImageListener(imageView,0,0));
    }

    /**
     * 加载图片
     *
     * @param imgUrl 图片地址
     */
    public void loadImage(final String imgUrl,final FetchImageListener listener) {
        imageLoader.get(imgUrl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap icon=BitmapUtil.scaleBitmap(response.getBitmap(),
                        BitmapUtil.MAX_ART_WIDTH_ICON,BitmapUtil.MAX_ART_HEIGHT_ICON);
                listener.onFetched(imgUrl,response.getBitmap(),icon);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(imgUrl,error);
            }
        });
    }


    public static abstract class FetchImageListener {
        public abstract void onFetched(String artUrl, Bitmap bigImage, Bitmap iconImage);
        public void onError(String artUrl, Exception e) {
            LogUtil.e(TAG, e, "AlbumArtFetchListener: error while downloading " + artUrl);
        }
    }

    /**
     * 通过tag取消队列中的请求
     *
     * @param tag 消息标记
     */
    public void cancelFromRequestQueue(String tag) {
        if(tag==null){return;}
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
