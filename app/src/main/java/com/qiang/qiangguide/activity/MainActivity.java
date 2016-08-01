package com.qiang.qiangguide.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.qiang.qiangguide.R;
import com.qiang.qiangguide.custom.NetImageView;
import com.qiang.qiangguide.custom.RoundImageView;
import com.qiang.qiangguide.util.BitmapCache;
import com.qiang.qiangguide.util.LogUtil;

public class MainActivity extends ActivityBase {

    String url="http://c.hiphotos.baidu.com/image/pic/item/a08b87d6277f9e2f2577db011d30e924b899f37d.jpg";
    private RoundImageView roundImageView;
    private NetImageView netImageView;
    private Button button;
    private ImageView imageView;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue= Volley.newRequestQueue(this);
        imageLoader = new ImageLoader(requestQueue, new BitmapCache());
        findView();
        int maxMemory= (int) (Runtime.getRuntime().maxMemory()/1024/1024);
        LogUtil.i("","最大可用内存为： "+maxMemory);

        /*//QVolley.getInstance(this).loadImage(url,netImageView,0,0);
        //QVolley.getInstance(this).loadImage(url,roundImageView,0,0);
        netImageView.setImageUrl(url, QVolley.getInstance(this).getImageLoader());
        roundImageView.setImageUrl(url, QVolley.getInstance(this).getImageLoader());
        QVolley.getInstance(this).loadImage(url,imageView,0,0);*/
        imageLoader.get(url,ImageLoader.getImageListener(imageView,0,0));
    }

    private void findView() {
        netImageView=(NetImageView) findViewById(R.id.netImageView);
        imageView=(ImageView) findViewById(R.id.imageView);
        roundImageView=(RoundImageView) findViewById(R.id.roundImageView);
    }

    @Override
    void errorRefresh() {

    }


}
