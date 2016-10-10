package com.qiang.qiangguide.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.qiang.qiangguide.manager.ResourceManager;

/**
 * Created by Qiang on 2016/7/13.
 *
 * 信息对话框工具类
 */
public class MessageUtils {

    /**
     *
     * 显示信息对话框
     *
     * @param act
     *            activity对象
     * @param msgContent
     *            显示信息
     * @since 1.0.0
     */
    public static void showDialog(Activity act, String msgContent ) {

        showDialog( act, null, msgContent );
    }


    /**
     *
     * 显示信息对话框
     *
     * @param act
     *            activity对象
     * @param msgResId
     *            显示信息ID
     * @since 1.0.0
     */
    public static void showDialog( Activity act, int msgResId ) {

        showDialog( act, null, msgResId );
    }


    /**
     *
     * 显示信息对话框
     *
     * @param act
     *            activity对象
     * @param title
     *            标题
     * @param msgContent
     *            显示信息
     * @since 1.0.0
     */
    public static void showDialog( Activity act, String title, String msgContent ) {

        showDialog( act, title, 0, msgContent, null, null, null, false );
    }


    /**
     *
     * 显示信息对话框
     *
     * @param act
     *            activity对象
     * @param title
     *            标题
     * @param msgResId
     *            显示信息ID
     * @since 1.0.0
     */
    public static void showDialog( Activity act, String title, int msgResId ) {

        showDialog( act, title, msgResId, null, null, null, null, false );
    }


    /**
     *
     * 单按钮显示信息对话框
     *
     * @param act
     *            activity对象
     * @param msgContent
     *            显示信息
     * @param dialogClickListener
     *            按钮点击监听事件
     * @since 1.0.0
     */
    public static void showDialogOneBtn( Activity act, String msgContent, DialogClickListener dialogClickListener ) {

        showDialog( act, null, 0, msgContent, null, null, dialogClickListener, false );

    }


    /**
     *
     * 双按钮显示信息对话框
     *
     * @param act
     *            activity对象
     * @param msgContent
     *            显示信息
     * @param dialogClickListener
     *            按钮点击监听事件
     * @since 1.0.0
     */
    public static void showDialogTwoBtn( Activity act, String msgContent, DialogClickListener dialogClickListener ) {

        showDialog( act, null, 0, msgContent, null, null, dialogClickListener, true );

    }


    /**
     *
     *
     * 双按钮点击事件监听器
     *
     * 2015-3-5 上午10:52:54
     *
     * @version 1.0.0
     *
     */
    public interface DialogClickListener {

        public void positiveClick();


        public void negativeClick();
    }

    /**
     *
     *
     * 单按钮点击事件监听器
     *
     * 2015-3-5 上午10:54:25
     *
     * @version 1.0.0
     *
     */
    public interface ChoiceDialogListener {

        public void positiveClick( int which );


        public void setSingleChoiceClick( int which );
    }


    /**
     *
     * 显示信息弹对话框
     *
     *
     * @param act
     *            activity对象
     * @param title
     *            标题
     * @param msgResId
     *            显示信息ID
     * @param msgContent
     *            显示信息
     * @param positiveMsg
     *            左按钮名称
     * @param negativeMsg
     *            右按钮名称
     * @param dialogClickListener
     *            按钮点击监听事件
     * @param showNegBtn
     *            右按钮显示控制，true显示，false隐藏 void
     * @exception
     * @since 1.0.0
     */
    public static void showDialog( Activity act, String title, int msgResId, String msgContent, String positiveMsg,
                                   String negativeMsg, final DialogClickListener dialogClickListener, boolean showNegBtn ) {

        ResourceManager mResourceManager = ResourceManager.getInstance( act.getApplicationContext() );
        AlertDialog.Builder builder = new AlertDialog.Builder( act );
        if( TextUtils.isEmpty( title ) ) {
            builder.setTitle( act.getResources().getString( mResourceManager.getStringId( "fw_msg_utiles_title" ) ) );
        } else {
            builder.setTitle( title );
        }

        if( msgResId != 0 ) {
            builder.setMessage( act.getResources().getString( msgResId ) );
        } else if( !TextUtils.isEmpty( msgContent ) ) {
            builder.setMessage( msgContent );
        } else {
            builder.setMessage( act.getResources().getString( mResourceManager.getStringId( "fw_msg_utiles_message" ) ) );
        }
        builder.setCancelable( false );
        String postiveStr = "";
        if( TextUtils.isEmpty( positiveMsg ) ) {
            postiveStr = act.getResources().getString( mResourceManager.getStringId( "fw_msg_utiles_sure" ) );
        } else {
            postiveStr = positiveMsg;
        }
        builder.setPositiveButton( postiveStr, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which ) {

                if( dialogClickListener != null ) {
                    dialogClickListener.positiveClick();

                }
                dialog.dismiss();
            }
        } );
        if( showNegBtn ) {
            String negtiveStr = "";
            if( TextUtils.isEmpty( negativeMsg ) ) {
                negtiveStr = act.getResources().getString( mResourceManager.getStringId( "fw_msg_utiles_cancer" ) );
            } else {
                negtiveStr = negativeMsg;
            }
            builder.setNegativeButton( negtiveStr, new DialogInterface.OnClickListener() {

                @Override
                public void onClick( DialogInterface dialog, int which ) {

                    if( dialogClickListener != null ) {
                        dialogClickListener.negativeClick();
                    }
                    dialog.dismiss();
                }
            } );
        }

        if( !( act.isFinishing() ) ) {
            showDialog( builder );
        }
    }


    public static void showDialog( AlertDialog.Builder builder ) {

        if( builder == null ) {
            return;
        }
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    /**
     *
     * 单选信息对话框
     *
     * @param act
     *            activity对象
     * @param parms
     *            项目数组
     * @param ID
     *            项目ID
     * @param title
     *            标题
     * @param choiceDialogListener
     *            按钮点击监听器
     * @since 1.0.0
     */
    public static void showChoiceDialog( Activity act, final String[] parms, final String[] ID, String title,
                                         final int defaultSelectIndex, final ChoiceDialogListener choiceDialogListener ) {

        ResourceManager mResourceManager = ResourceManager.getInstance( act.getApplicationContext() );
        AlertDialog.Builder builder = new AlertDialog.Builder( act );
        if( TextUtils.isEmpty( title ) ) {
            builder.setTitle( act.getResources().getString( mResourceManager.getStringId( "fw_msg_utiles_title" ) ) );
        } else {
            builder.setTitle( title );
        }
        String postiveStr = "";

        postiveStr = act.getResources().getString( mResourceManager.getStringId( "fw_msg_utiles_sure" ) );
        builder.setSingleChoiceItems( parms, defaultSelectIndex, new DialogInterface.OnClickListener() {

            @Override
            public void onClick( DialogInterface dialog, int which ) {

                choiceDialogListener.setSingleChoiceClick( which );

            }
        } );
        builder.setPositiveButton(
                act.getResources().getString( mResourceManager.getStringId( "fw_msg_utiles_sure" ) ),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick( DialogInterface dialog, int which ) {

                        // TODO Auto-generated method stub
                        choiceDialogListener.positiveClick( defaultSelectIndex );
                    }
                } );

        MessageUtils.showDialog( builder );
    }


    /**
     *
     * 设置日期对话框方法
     *
     * @param act
     *            activity对象
     * @param year
     *            年
     * @param month
     *            月
     * @param day
     *            日
     * @param setListener
     *            设置日期监听器
     * @return DatePickerDialog 设置日期对话框
     * @since 1.0.0
     */
    public static DatePickerDialog showDateWidget(Activity act, int year, int month, int day,
                                                  DatePickerDialog.OnDateSetListener setListener ) {

        DatePickerDialog datePickerDialog = new DatePickerDialog( act, setListener, year, month, day );
        datePickerDialog.setCancelable( false );
        datePickerDialog.show();
        return datePickerDialog;
    }

}
