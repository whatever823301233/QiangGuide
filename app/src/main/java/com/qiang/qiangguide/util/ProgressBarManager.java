package com.qiang.qiangguide.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.qiang.qiangguide.ResourceManager;

/**
 * Created by Qiang on 2016/7/13.
 *
 * 进度条管理类  
 */
public class ProgressBarManager {

    private final static String tag = "ProgressBarManager";
    private final static int MSG_TYPE_CLOSS_PROGRESSBAR = 0;// 关闭等待层
    private final static int CLOSE_WAIT_PANEL_MILLISECOND = 50000;// 等待层超时关闭时间
    private static ProgressDialog progressBar = null;
    // private static Context ctx;
    private static int sn = 0;
    private static AutoCloseListener autoCloseListener;
    private static Context ctx;

    private final static Handler mHandler = new Handler() {

        @Override
        public void handleMessage( Message msg ) {

            super.handleMessage( msg );
            switch( msg.what ) {
                case MSG_TYPE_CLOSS_PROGRESSBAR:

                    dismissProgressBar( msg.arg1 );
                    // if( msg.arg1 != sn )
                    // return;
                    // if( autoCloseListener != null && progressBar != null &&
                    // progressBar.isShowing() )
                    // autoCloseListener.onEvent();
                    // if( progressBar != null && progressBar.isShowing() ) {
                    // Log.e( tag, "ls强制关闭" );
                    // }
                    // dismissProgressBar();
            }
        }

    };


    /**
     *
     *
     * AutoCloseListener 进度框超时关闭的回调监听器
     *
     * 2015-3-5 下午2:25:59  
     *
     * @version 1.0.0
     *
     */
    public interface AutoCloseListener {

        public void onEvent();
    }

    /**
     *
     * 关闭进度条 
     * @param sn
     * @since  1.0.0
     */
    private synchronized static void dismissProgressBar( int sn ) {

        try {
            // 如果sn不同，则说明该超时线程对应的进度框已经关闭
            if( ProgressBarManager.sn != sn ) {
                return;
            }

            if( progressBar != null && progressBar.isShowing() ) {
                progressBar.dismiss();
                if( autoCloseListener != null )
                    autoCloseListener.onEvent();
                if( sn > 100000000 )
                    sn = 0;
                else
                    sn++;
            }
        } catch( Exception e ) {
            LogUtil.e( tag, "dismissProgressBar() error", e );
        }

    }


    /**
     *	关闭进度条
     *
     * @since 1.0.0
     */
    public synchronized static void dismissProgressBar() {

        try {
            if( progressBar != null && progressBar.isShowing() ) {
                progressBar.dismiss();
                if( sn > 100000000 )
                    sn = 0;
                else
                    sn++;
            }
        } catch( Exception e ) {
            LogUtil.e( tag, "dismissProgressBar error", e );
        }

    }


    /**
     * 释放ProgressBarManager持有的窗体ctx
     *
     * @since 1.0.0
     */
    public synchronized static void releaseCtx() {

        // ProgressBarManager.ctx = null;
    }


    /**
     * 加载进度条，超时会自动关闭
     *
     * @param ctx	Context对象
     * @param message
     *            进度条显示的文本，缺省“正在加载,请稍候......”
     * @param isCancelable
     *            是否可以取消
     * @since 1.0.0
     */
    public synchronized static void loadWaitPanelAutoClose( Context ctx, String message, boolean isCancelable,
                                                            AutoCloseListener autoCloseListener ) {

        try {
            if( progressBar != null && progressBar.isShowing() ) {
                // dismissProgressBar();
                sn++;
                // Log.e( "lsno", "sn=" + sn );
            } else {
                show( ctx, message, isCancelable );
                // Log.e( "lsshow", "sn=" + sn );
            }
            // show( ctx, message, isCancelable );
            ProgressBarManager.autoCloseListener = autoCloseListener;
            final int progressBar_sn = sn;
            // 防止程序长时间等待，关闭等待层
            new Thread() {

                public void run() {

                    try {
                        Thread.sleep( CLOSE_WAIT_PANEL_MILLISECOND );
                        Message msg = new Message();
                        msg.what = MSG_TYPE_CLOSS_PROGRESSBAR;
                        msg.arg1 = progressBar_sn;
                        mHandler.sendMessage( msg );
                    } catch( Exception e ) {
                        LogUtil.e( tag, "loadWaitPanelAutoClose error", e );
                    }

                }
            }.start();
        } catch( Exception e ) {
            LogUtil.e( tag, "loadWaitPanelAutoClose error", e );
        }

    }

    /**
     * 加载进度条
     *
     * @param ctx	Context对象
     * @param message
     *            进度条显示的文本，缺省“正在加载,请稍候......”
     * @param isCancelable
     *            是否可以取消
     * @since 1.0.0
     */
    public synchronized static void loadWaitPanel( Context ctx, String message, boolean isCancelable ) {

        try {
            if( progressBar != null && progressBar.isShowing() ) {
                dismissProgressBar();
            }
            show( ctx, message, isCancelable );
        } catch( Exception e ) {
            LogUtil.e( tag, "loadWaitPanel error", e );
        }
    }

    /**
     *
     * 加载进度条  
     * @param ctx   Context对象 
     * @since  1.0.0
     */
    public synchronized static void loadWaitPanel( Context ctx ) {

        ResourceManager mResourceManager = ResourceManager.getInstance(ctx);
        try {
            if( progressBar != null && progressBar.isShowing() ) {
                dismissProgressBar();
            }
            show( ctx, mResourceManager.getStringId("fw_loading"), true );
        } catch( Exception e ) {
            LogUtil.e( tag, "loadWaitPanel error", e );
        }
    }


    /**
     *
     * 显示进度条
     * @param ctx	Context对象
     * @param message	字符串文本
     * @param isCancelable   是否可以取消    
     * @since  1.0.0
     */
    private static void show( Context ctx, String message, boolean isCancelable ) {
        ResourceManager mResourceManager = ResourceManager.getInstance(ctx);

        if( progressBar == null || ProgressBarManager.ctx != ctx ) {// 相同activity下重用一个ProgressDialog在红米下会报错
            progressBar = new ProgressDialog( ctx );
        }
        ProgressBarManager.ctx = ctx;// 防止相同的ctx重复new ProgressDialog
        progressBar.setCancelable( isCancelable );
        progressBar.setProgressStyle( ProgressDialog.STYLE_SPINNER );
        if( message == null || "".equals( message ) ) {
            progressBar.setMessage( ctx.getString(mResourceManager.getStringId("fw_ProgressBar_tip") ) );
        } else {
            progressBar.setMessage( message );
        }
        progressBar.setIndeterminate( false );
        progressBar.show();
    }

    /**
     *
     * 显示进度条
     * @param ctx	Context对象
     * @param message	整型数文本
     * @param isCancelable   是否可以取消    
     * @since  1.0.0
     */
    private static void show(Context ctx, int message, boolean isCancelable ) {

        if( progressBar == null || ProgressBarManager.ctx != ctx ) {// 相同activity下重用一个ProgressDialog在红米下会报错
            progressBar = new ProgressDialog( ctx );
        }
        ProgressBarManager.ctx = ctx;// 防止相同的ctx重复new ProgressDialog
        progressBar.setCancelable( isCancelable );
        progressBar.setProgressStyle( ProgressDialog.STYLE_SPINNER );
        progressBar.setMessage( ctx.getString( message ) );
        progressBar.setIndeterminate( false );
        progressBar.show();
    }
    
}
