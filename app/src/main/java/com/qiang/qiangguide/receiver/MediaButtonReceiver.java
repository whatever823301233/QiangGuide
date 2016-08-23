package com.qiang.qiangguide.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by Qiang on 2016/8/23.
 *
 */
public class MediaButtonReceiver extends BroadcastReceiver {

    private static String TAG = "MediaButtonReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        // 获得Action
        String intentAction = intent.getAction();
        // 获得KeyEvent对象
        KeyEvent keyEvent = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

        Log.i(TAG, "Action ---->" + intentAction + "  KeyEvent----->"+ keyEvent.toString());

        if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            // 获得按键字节码
            int keyCode = keyEvent.getKeyCode();
            // 按下 / 松开 按钮
            int keyAction = keyEvent.getAction();
            // 获得事件的时间
            long downtime = keyEvent.getEventTime();

            // 获取按键码 keyCode
            StringBuilder sb = new StringBuilder();
            // 这些都是可能的按键码 ， 打印出来用户按下的键
            if (KeyEvent.KEYCODE_MEDIA_NEXT == keyCode) {
                sb.append("KEYCODE_MEDIA_NEXT");
            }
            // 说明：当我们按下MEDIA_BUTTON中间按钮时，实际出发的是 KEYCODE_HEADSETHOOK 而不是
            // KEYCODE_MEDIA_PLAY_PAUSE
            if (KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE == keyCode) {
                sb.append("KEYCODE_MEDIA_PLAY_PAUSE");
            }
            if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) {
                sb.append("KEYCODE_HEADSETHOOK");
            }
            if (KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode) {
                sb.append("KEYCODE_MEDIA_PREVIOUS");
            }
            if (KeyEvent.KEYCODE_MEDIA_STOP == keyCode) {
                sb.append("KEYCODE_MEDIA_STOP");
            }
            // 输出点击的按键码
            Log.i(TAG, sb.toString());
        }
    }

}
