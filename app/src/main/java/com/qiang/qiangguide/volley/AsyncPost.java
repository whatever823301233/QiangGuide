package com.qiang.qiangguide.volley;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Qiang on 2016/7/18.
 *
 */
public class AsyncPost extends AsyncTask<String,Integer,String> {


    private String tag = "";
    private String url = "";
    private String charsetName = "UTF-8";
    private Response.Listener<String> listener;
    private Response.ErrorListener errorListener;
    private static final int TIMEOUT_IN_MILLIONS=5000;

    private Map<String,String> postParams;
    private int responseCode;

    public void putParam(String key,String value) {
        if(postParams==null){
            postParams=new HashMap<>();
        }
        postParams.put(key,value);
    }

    /**
     * 主构造
     *
     * @param url
     *            请求地址
     * @param listener
     *            请求成功的回调监听
     * @param errorListener
     *            请求失败的回调监听
     */
    public AsyncPost(String url, Response.Listener<String> listener,
                     Response.ErrorListener errorListener) {
        this.url = url;
        this.listener = listener;
        this.errorListener = errorListener;
        postParams=new HashMap<>();
    }

    /**
     * 获取请求标记
     *
     * @return 标记
     */
    public String getTag() {
        return tag;
    }

    /**
     * 设置请求标记
     *
     * @param tag
     *            标记
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * 获取编码格式
     *
     * @return 编码格式
     */
    public String getCharsetName() {
        return charsetName;
    }

    /**
     * 设置编码格式
     *
     * @param charsetName
     *            编码格式
     */
    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }



    /*
     * Function  :   封装请求体信息
     * Param     :   params请求体内容，encode编码格式
     */
    public StringBuffer getRequestData(Map<String, String> params) {
        if(params==null){return null;}
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), charsetName))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    @Override
    protected String doInBackground(String... params) {
        String mParam=null;
        if(postParams!=null&&!postParams.isEmpty()){
            mParam=getRequestData(postParams).toString();
        }
        DataOutputStream out = null;
        BufferedReader in = null;
        String result = "";
        try
        {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setUseCaches(false);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);

            if (!TextUtils.isEmpty(mParam)&&!mParam.trim().equals(""))
            {
                // 获取URLConnection对象对应的输出流
                out = new DataOutputStream(conn.getOutputStream());
                // 发送请求参数
                out.writeBytes(mParam);
                // flush输出流的缓冲
                out.flush();
            }
            responseCode=conn.getResponseCode();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null)
            {
                result += line;
            }
        } catch (IOException e)
        {
            result=e.toString();
        }
        // 使用finally块来关闭输出流、输入流
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        return result;
    }


    @Override
    protected void onPostExecute(String s) {
        if(null!=listener&&null!=errorListener){
            if(responseCode==200){
                listener.onResponse(s);
            }else{
                errorListener.onErrorResponse(new VolleyError(s));
            }
        }
    }

}
