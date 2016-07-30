package com.qiang.qiangguide.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiang on 2016/7/13.
 *
 * 消息工具类
 */
public class MessageTools {

    private static String tag = "MessgaeTools";
    private Handler mHandler = new Handler();
    private AlertDialog.Builder builder;
    public WebView webView;
    private static List<AlertDialog> dialogs = new ArrayList<>();


    public MessageTools( WebView webView, AlertDialog.Builder builder ) {

        this.builder = builder;
        this.builder.setCancelable( false );
        this.webView = webView;
    }


    /**
     * 确认信息提示框 供webview使用
     *
     * @param confirmStr
     *            确认消息
     * @param callbackFun
     *            确定后回调函数
     */
    public void confirmWin( final String confirmStr, final String callbackFun ) {

        Log.d( tag, "confirmStr:" + confirmStr + "," + callbackFun );
        mHandler.post( new Runnable() {

            public void run() {

                builder.setNegativeButton( null, null );
                builder.setPositiveButton( null, null );
                builder.setMessage( confirmStr ).setPositiveButton( "确定", new DialogInterface.OnClickListener() {

                    public void onClick( DialogInterface dialog, int which ) {

                        webView.loadUrl( "javascript:" + callbackFun );
                        dismiss();
                    }
                } ).setNegativeButton( "取消", null ).create();
                showDialog( builder );
            }
        } );
    }


    /**
     * 调用提示信息 供webview使用
     *
     * @param str
     *            显示的提示信息
     * @param callbackfun
     *            js回调函数名
     */
    public void alertinfo( final String str, final String callbackfun ) {

        Log.d( tag, str + "," + callbackfun );
        mHandler.post( new Runnable() {

            public void run() {

                builder.setNegativeButton( null, null );
                builder.setPositiveButton( null, null );
                builder.setMessage( str );
                builder.setPositiveButton( "确定", new DialogInterface.OnClickListener() {

                    public void onClick( DialogInterface dialog, int which ) {

                        if( null != callbackfun && !"".equals( callbackfun ) ) {
                            webView.loadUrl( "javascript:" + callbackfun );
                            dismiss();
                        }
                    }
                } );
                showDialog( builder );
            }
        } );
    }


    // 用于屏蔽home键,和back键
    public static void showDialog( AlertDialog.Builder builder ) {

        showDialog( builder, true, true );
    }


    public static void showDialog( AlertDialog.Builder builder, boolean isSetTitle, boolean isCatchHomeKey ) {

        if( null == builder ) {
            Log.i( tag, "显示对话框,builder is null." );
            return;
        }
        builder.setCancelable( false );
        if( isSetTitle ) {
            builder.setTitle( "提示" );
        }
        AlertDialog dialog = builder.create();
        dialogs.add( dialog );
        builder.show();
        // 不屏蔽home键
        if( !isCatchHomeKey ) {
            return;
        }
        try {
            Log.d( tag, "3进入showDialog.用于屏蔽home键,和back键." );
            // TYPE_KEYGUARD : 背景不是透明的
            // TYPE_KEYGUARD_DIALOG : 对话框显示不出来
            dialog.getWindow().setType( WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG );
            Log.d( tag, "5进入showDialog." );
            builder.setOnKeyListener( new android.content.DialogInterface.OnKeyListener() {

                public boolean onKey( DialogInterface dialog, int keyCode, KeyEvent event ) {

                    switch( keyCode ) {
                        case KeyEvent.KEYCODE_HOME:
                            Log.d( tag, "home键已屏蔽." );
                            return true;
                    }
                    return false;
                }
            } );
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }


    /**
     * 显示dialog,只有一个确定按钮
     */
    public static void showDialogOk( Context ctx, String message ) {

        if( ctx == null ) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder( ctx ).setMessage( message ).setPositiveButton( "确定",
                new DialogInterface.OnClickListener() {

                    public void onClick( DialogInterface dialog, int which ) {

                        dialog.dismiss();
                    }
                } );
        AlertDialog dialog = builder.create();
        dialogs.add( dialog );
        if( !( ( Activity )ctx ).isFinishing() ) {
            showDialog( builder );
        }

    }


    /**
     * 显示dialog,只有一个确定按钮
     */
    public static void showDialogOk( Context ctx, String title, String content ) {

        showDialogOk( ctx, title, content, false );
    }


    public static void showDialogOk( Context ctx, String title, String content, boolean isShowTitle ) {

        AlertDialog.Builder builder = new AlertDialog.Builder( ctx ).setTitle( title ).setMessage( content )
                .setPositiveButton( "确定", new DialogInterface.OnClickListener() {

                    public void onClick( DialogInterface dialog, int which ) {

                        dialog.dismiss();
                    }
                } );
        AlertDialog dialog = builder.create();
        dialogs.add( dialog );
        if( !( ( Activity )ctx ).isFinishing() ) {
            showDialog( builder, isShowTitle, true );

        }
    }


    /**
     * 显示退出对话框
     *
     * @param thisActivity
     *            当前的activity
     * @param desctionActivity
     *            跳转到目标的activity
     * @param confirmMsg
     *            提示的确认信息
     */
    @SuppressWarnings( "rawtypes" )
    public static void showQuitConfirm( final Activity thisActivity, final Class desctionActivity, String confirmMsg ) { // 对话框的弹出

        AlertDialog.Builder builder = new AlertDialog.Builder( thisActivity ).setMessage( confirmMsg )
                .setNegativeButton( "取消", // “取消”按钮的事件处理
                        null ).setPositiveButton( "确定", // “确定”按钮的事件处理
                        new DialogInterface.OnClickListener() {

                            public void onClick( DialogInterface dialog, int whichButton ) {

                                Intent intent = new Intent();
                                intent.setClass( thisActivity, desctionActivity );
                                thisActivity.startActivity( intent );
                                thisActivity.finish();
                            }
                        } );
        AlertDialog dialog = builder.create();
        dialogs.add( dialog );
        if( !thisActivity.isFinishing() ) {
            showDialog( builder );
        }
    }


    public static void dismiss() {

        for( AlertDialog dialog : dialogs ) {
            if( null != dialog ) {
                dialog.cancel();
                Log.d( tag, "关闭的对话框:" + dialog );
            }
        }
        dialogs.clear();
    }


    public static DatePickerDialog showDateWidget(Context act, int year, int month, int day,
                                                  DatePickerDialog.OnDateSetListener setListener ) {

        DatePickerDialog datePickerDialog = new DatePickerDialog( act, setListener, year, month, day );
        datePickerDialog.setCancelable( false );
        datePickerDialog.show();
        return datePickerDialog;
    }


    /**
     *
     * showThreeBtnDialog(3个按钮btn)
     *
     * @param ctx
     * @param message void
     * @since 1.0.0
     */
    public static void showThreeBtnDialog( final Context ctx, String message, final String pageName ) {

        if( ctx == null ) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder( ctx ).setMessage( message ).setPositiveButton( "确定",
                new DialogInterface.OnClickListener() {

                    public void onClick( DialogInterface dialog, int which ) {

                        dialog.dismiss();
                    }
                } );
        builder.setNeutralButton( "中间", new DialogInterface.OnClickListener() {

            @Override
            public void onClick( DialogInterface dialog, int which ) {

                PackageManager packageManager = ctx.getPackageManager();
                Intent intent = new Intent();
                intent = packageManager.getLaunchIntentForPackage( pageName );
                if( intent == null ) {
                    System.out.println( "APP not found!" );
                }
                ctx.startActivity( intent );
            }
        } );

        builder.setNegativeButton( "取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick( DialogInterface dialog, int which ) {

                dialog.dismiss();

            }
        } );
        if( !( ( Activity )ctx ).isFinishing() ) {
            showDialog( builder );
        }

    }


    /**
     *
     * showReCommendDialog(强制更新弹出提示框)
     *
     * @param ctx
     * @param onClickListener
     *            void
     * @since 1.0.0
     */
    public static void showReCommendDialog( final Context ctx, String conformBtn, String message,
                                            DialogInterface.OnClickListener onClickListener ) {

        if( ctx == null ) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder( ctx );
        builder.setMessage( message );
        builder.setPositiveButton( conformBtn, onClickListener );
        if( !( (Activity)ctx ).isFinishing() ) {
            showDialog( builder );
        }
    }


    public static void showDialog(final Context ctx, String confirmBtn, String cancelBtn, String message,
                                  DialogInterface.OnClickListener onConfirmListener, DialogInterface.OnClickListener onCancelListener ) {

        if( ctx == null ) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder( ctx );
        builder.setMessage( message );
        builder.setNegativeButton( cancelBtn, onCancelListener );
        builder.setPositiveButton( confirmBtn, onConfirmListener );
        if( !( ( Activity )ctx ).isFinishing() ) {
            showDialog( builder );
        }
    }

}
