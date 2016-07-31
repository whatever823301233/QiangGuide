package com.qiang.qiangguide;

import android.app.ActivityManager;
import android.app.Application;
import android.text.TextUtils;

import com.qiang.qiangguide.db.DBHandler;
import com.qiang.qiangguide.volley.QVolley;

import java.util.Iterator;

/**
 * Created by Qiang on 2016/7/12.
 */
public class QApplication extends Application {

    private static QApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        if(!isSameAppName()){
            return;
        }
        instance=this;
        AppManager.getInstance(this).initApp("com.qiang.qiangguide",iAppListener);
        QVolley.getInstance(this);
        DBHandler.getInstance(this);
    }

    private IAppListener iAppListener=new IAppListener() {
        @Override
        public void destroy() {

        }
    };

    public static QApplication get(){
        return instance;
    }

    /**
     * 判断是否为相同app名
     *
     * @return
     */
    private boolean isSameAppName() {
        int pid = android.os.Process.myPid();
        String processAppName = getProcessAppName(pid);
        String packageName=getPackageName();
        if (TextUtils.isEmpty(processAppName) || !processAppName.equalsIgnoreCase(packageName)) {
            return false;
        }
        return true;
    }

    /**
     * 获取processAppName
     *
     * @param pid
     * @return
     */
    private String getProcessAppName(int pid) {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        Iterator<ActivityManager.RunningAppProcessInfo> iterator = activityManager.getRunningAppProcesses().iterator();
        while (iterator.hasNext()) {
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = iterator.next();
            try {
                if (runningAppProcessInfo.pid == pid) {
                    return runningAppProcessInfo.processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

}
