package com.qiang.qiangguide.activity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.example.okhttp_library.OkHttpUtils;
import com.example.okhttp_library.callback.FileCallBack;
import com.qiang.qiangguide.R;
import com.qiang.qiangguide.custom.NetImageView;
import com.qiang.qiangguide.custom.RoundImageView;
import com.qiang.qiangguide.util.FileUtil;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;

public class MainActivity extends ActivityBase {

    String url="http://c.hiphotos.baidu.com/image/pic/item/a08b87d6277f9e2f2577db011d30e924b899f37d.jpg";
    private RoundImageView roundImageView;
    private NetImageView netImageView;
    private Button button;
    private ImageView imageView;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();

        new Thread(new Runnable() {
            @Override
            public void run() {

                String oldPath = "data/data/com.qiang.qiangguide/databases/" + "qiang.db";
                String newPath = Environment.getExternalStorageDirectory() + File.separator + "qiang.db";
                FileUtil.copyFile(oldPath,newPath);
            }
        }).start();


        /*requestQueue= Volley.newRequestQueue(this);
        imageLoader = new ImageLoader(requestQueue, BitmapCache.getInstance());
        int maxMemory= (int) (Runtime.getRuntime().maxMemory()/1024/1024);
        LogUtil.i("","最大可用内存为： "+maxMemory);*/

        //QVolley.getInstance(this).loadImage(url,netImageView,0,0);
        //QVolley.getInstance(this).loadImage(url,roundImageView,0,0);
       //netImageView.setImageUrl(url, QVolley.getInstance(this).getImageLoader());
        //roundImageView.setImageUrl(url, QVolley.getInstance(this).getImageLoader());
        //QVolley.getInstance(this).loadImage(url,imageView,0,0);
        //QVolley.getInstance(this).loadImageIcon(url,imageView,0,0);
        //imageLoader.get(url,ImageLoader.getImageListener(imageView,0,0));
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                downloadFile();
            }
        }).start();*/
    }

    public void downloadFile() {
        String url = "http://d.hiphotos.baidu.com/image/pic/item/a5c27d1ed21b0ef4400edb2fdec451da80cb3ed8.jpg";
        OkHttpUtils//
                .get()//
                .url(url)//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "girl.jpg")//
                {

                    @Override
                    public void onBefore(Request request, int id)
                    {
                    }

                    @Override
                    public void inProgress(float progress, long total, int id)
                    {
                        mProgressBar.setProgress((int) (100 * progress));
                        Log.e(TAG, "inProgress :" + (int) (100 * progress));
                    }

                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        Log.e(TAG, "onError :" + e.getMessage());
                    }

                    @Override
                    public void onResponse(File file, int id)
                    {
                        Log.e(TAG, "onResponse :" + file.getAbsolutePath());
                    }
                });
    }





    private void findView() {
        netImageView=(NetImageView) findViewById(R.id.netImageView);
        imageView=(ImageView) findViewById(R.id.imageView);
        roundImageView=(RoundImageView) findViewById(R.id.roundImageView);
        mProgressBar=(ProgressBar) findViewById(R.id.mProgressBar);
    }

    @Override
    void errorRefresh() {

    }


}
