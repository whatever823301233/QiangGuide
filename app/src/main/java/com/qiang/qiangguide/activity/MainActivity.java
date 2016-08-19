package com.qiang.qiangguide.activity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.example.okhttp_library.OkHttpUtils;
import com.example.okhttp_library.callback.FileCallBack;
import com.qiang.qiangguide.R;
import com.qiang.qiangguide.custom.NetImageView;
import com.qiang.qiangguide.custom.RoundImageView;
import com.qiang.qiangguide.util.AndroidUtil;
import com.qiang.qiangguide.util.DateUtil;
import com.qiang.qiangguide.util.LogUtil;
import com.qiang.qiangguide.util.SocketClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    private ServerSocket server;
    private Executor mExecutorService;
    private List<Socket> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
       /* ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("提示");
        pd.setMessage("正在加载中，请稍后...");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置带进度条的
        pd.setMax(100);

        pd.show();
        pd.setProgress(50);*/
      new Thread(new Runnable() {
            @Override
            public void run() {

                List<UseTime> list=new ArrayList<>();
                UseTime u1=new UseTime();
                u1.setBeginTime(DateUtil.getNow());
                u1.setEndTime(DateUtil.getNow());
                u1.setSerialNum(AndroidUtil.getDeviceId(MainActivity.this));

                UseTime u2=new UseTime();
                u2.setBeginTime(DateUtil.getNow());
                u2.setEndTime(DateUtil.getNow());
                u2.setSerialNum(AndroidUtil.getDeviceId(MainActivity.this));

                list.add(u1);
                list.add(u2);

                String json= JSON.toJSONString(list);

                //String id= AndroidUtil.getDeviceId(MainActivity.this);
                //LogUtil.i("","设备序列号为："+id);
                try {
                    SocketClient.send("192.168.1.103",9527,json+"\n");
                    LogUtil.i("","已发送使用情况："+json);
                } catch (IOException e) {
                    LogUtil.e("",e);
                }
            }
        }).start();


     /*   new Thread(new Runnable() {
            @Override
            public void run() {

                String oldPath = "data/data/com.qiang.qiangguide/databases/" + "qiang.db";
                String newPath = Environment.getExternalStorageDirectory() + File.separator + "qiang.db";
                FileUtil.copyFile(oldPath,newPath);
            }
        }).start();*/


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



        //doMain();

    }

    static class UseTime{
        private String serialNum;
        private String beginTime;
        private String endTime;

        public String getSerialNum() {
            return serialNum;
        }

        public void setSerialNum(String serialNum) {
            this.serialNum = serialNum;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        @Override
        public String toString() {
            return "UseTime{" +
                    "serialNum='" + serialNum + '\'' +
                    ", beginTime='" + beginTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    '}';
        }
    }



    private static  final int PORT=9527;

    public void doMain() {
        try {
            server = new ServerSocket(PORT);
            mExecutorService = Executors.newCachedThreadPool();  //create a thread pool
            System.out.println("服务器已启动...");
            Socket client = null;
            while(true) {
                client = server.accept();
                //把客户端放入客户端集合中
                mList.add(client);
                mExecutorService.execute(new Service(client)); //start a new thread to handle the connection
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Service implements Runnable {
        private Socket socket;
        private BufferedReader in = null;
        private String msg = "";

        public Service(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //客户端只要一连到服务器，便向客户端发送下面的信息。
                msg = "服务器地址：" +this.socket.getInetAddress() + "come toal:"
                        +mList.size()+"（服务器发送）";
                this.sendmsg();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            try {
                while(true) {
                    if((msg = in.readLine())!= null) {
                        //当客户端发送的信息为：exit时，关闭连接
                        if(msg.equals("exit")) {
                            System.out.println("ssssssss");
                            mList.remove(socket);
                            in.close();
                            msg = "user:" + socket.getInetAddress()
                                    + "exit total:" + mList.size();
                            socket.close();
                            this.sendmsg();
                            break;
                            //接收客户端发过来的信息msg，然后发送给客户端。
                        } else {
                            msg = socket.getInetAddress() + ":" + msg+"（服务器发送）";
                            this.sendmsg();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**
         * 循环遍历客户端集合，给每个客户端都发送信息。
         */
        public void sendmsg() {
            System.out.println(msg);
            int num =mList.size();
            for (int index = 0; index < num; index ++) {
                Socket mSocket = mList.get(index);
                PrintWriter pout = null;
                try {
                    pout = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(mSocket.getOutputStream())),true);
                    pout.println(msg);
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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


        /*netImageView=(NetImageView) findViewById(R.id.netImageView);
        imageView=(ImageView) findViewById(R.id.imageView);
        roundImageView=(RoundImageView) findViewById(R.id.roundImageView);
        mProgressBar=(ProgressBar) findViewById(R.id.mProgressBar);*/
    }

    @Override
    void errorRefresh() {

    }


}
