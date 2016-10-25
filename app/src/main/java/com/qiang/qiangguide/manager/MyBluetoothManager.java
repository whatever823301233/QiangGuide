package com.qiang.qiangguide.manager;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Qiang on 2016/10/19.
 */

public class MyBluetoothManager {

    public static boolean openBluetooth(Context context){

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(context, "本机没有找到蓝牙硬件或驱动！", Toast.LENGTH_SHORT).show();
            return false;
        }
        // 如果本地蓝牙没有开启，则开启
        if (!mBluetoothAdapter.isEnabled()) {
            // 我们通过startActivityForResult()方法发起的Intent将会在onActivityResult()回调方法中获取用户的选择，比如用户单击了Yes开启，
            // 那么将会收到RESULT_OK的结果，
            // 如果RESULT_CANCELED则代表用户不愿意开启蓝牙
           /* Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context。startActivityForResult(mIntent, 1);*/
            // 用enable()方法来开启，无需询问用户(实惠无声息的开启蓝牙设备),这时就需要用到android.permission.BLUETOOTH_ADMIN权限。
             mBluetoothAdapter.enable();
            // mBluetoothAdapter.disable();//关闭蓝牙
            return true;
        }
        return false;
    }

    public static  void closeBluetooth(){

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return ;
        }
        // 如果本地蓝牙没有开启，则开启
        if (mBluetoothAdapter.isEnabled()) {
            // 我们通过startActivityForResult()方法发起的Intent将会在onActivityResult()回调方法中获取用户的选择，比如用户单击了Yes开启，
            // 那么将会收到RESULT_OK的结果，
            // 如果RESULT_CANCELED则代表用户不愿意开启蓝牙
           /* Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context。startActivityForResult(mIntent, 1);*/
            // 用enable()方法来开启，无需询问用户(实惠无声息的开启蓝牙设备),这时就需要用到android.permission.BLUETOOTH_ADMIN权限。
            //mBluetoothAdapter.enable();
             mBluetoothAdapter.disable();//关闭蓝牙
        }
    }


}
