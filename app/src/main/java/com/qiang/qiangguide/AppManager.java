package com.qiang.qiangguide;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.os.Build;

import java.util.ArrayList;

/**
 * Created by Qiang on 2016/7/13.
 */
public class AppManager {

    public static final String TAG = "AppManager";
    private Context mContext;
    private static volatile AppManager sInstance;
    private String mAppId;
    private IAppListener mListener;

    private ArrayList<Activity> mActivityList;
    private ArrayList<Service> mServiceList;
    private Activity mCurrentActivity;

    public static final int VERSION_DRAFT = 1;
    public static final int VERSION_FIT_DIRECT_SELL = 2;
    private static int sVersion = VERSION_FIT_DIRECT_SELL;

    private AppManager( Context context ) {

        mContext = context.getApplicationContext();
        mActivityList = new ArrayList<>();
        mServiceList = new ArrayList<>();
    }

    /**
     *
     * 获取AppManager单例
     *
     * @param context
     *            context对象
     * @return AppManager AppManager单例
     * @since 1.0.0
     */
    public static AppManager getInstance( Context context ) {

        if( context == null ) {
            return null;
        }
        if( sInstance == null ) {
            synchronized( AppManager.class ) {
                if( sInstance == null ) {
                    sInstance = new AppManager( context );
                }
            }
        }
        return sInstance;
    }


    /**
     *
     * 应用初始化方法
     *
     * @param appId AppID void
     * @since 1.0.0
     */
    public void initApp( String appId ) {

        initApp( appId, null );
    }

    /**
     *
     * 应用初始化方法
     *
     * @param appId
     *            AppID
     * @param appListener
     *            应用程序监听器，用于退出后清除所有数据 void
     * @since 1.0.0
     */
    public void initApp( String appId, IAppListener appListener ) {

        initApp( appId, appListener, true );
    }


    /**
     *
     * 应用初始化方法
     *
     * @param appId
     *            AppID
     * @param appListener
     *            应用程序监听器，用于退出后清除所有数据
     * @param isStartMonitor
     *            是否启用防劫持和切换后台超时退出的服务（默认启用） void
     * @since 1.0.0
     */
    public void initApp( String appId, IAppListener appListener, boolean isStartMonitor ) {

        initApp( appId, appListener, isStartMonitor, true );
    }

    /**
     *
     * 应用初始化方法
     *
     * @param appId
     *            AppID
     * @param appListener
     *            应用程序监听器，用于退出后清除所有数据
     * @param isStartMonitor
     *            是否启用防劫持和切换后台超时退出的服务（默认启用） void
     * @param isAutoCleanCache
     *            是否自动清除cache，默认为true，则每次启动和退出时都会自动删除cache
     * @since 1.1.0
     */
    public void initApp( String appId, IAppListener appListener, boolean isStartMonitor, boolean isAutoCleanCache ) {

        mAppId = appId;
        mListener = appListener;
        /*if( isStartMonitor ) {
            // 启动防劫持和切换后台超时退出的服务
            mContext.startService( new Intent( mContext.getApplicationContext(), CMBCMonitorService.class ) );
        }

        mAutoCleanCacheFlag = isAutoCleanCache;
        clearWebviewCache();*/
    }
    /**
     *
     * 获取AppID
     *
     * @return String AppID
     * @since 1.0.0
     */
    public String getAppId() {

        return mAppId;
    }

    /**
     *
     * 退出App并终止进程，清除数据 void
     *
     * @since 1.0.0
     */
    public void exitApp() {

        destroy( mContext );
    }

    private void destroy( Context context ) {

        /*if( context != null ) {
            try {
                context.getApplicationContext().stopService( new Intent( context, CMBCMonitorService.class ) );
            } catch( Exception e ) {
                Log.e( TAG, e.getMessage(), e );
            }
        }*/

        if( mListener != null ) {
            mListener.destroy();
        }

        try {
            closeAllActivity();
            stopAllService();
            // 退出应用程序清理缓存
            System.gc();
            if( !Build.MODEL.contains( "MI" ) ) {
                ActivityManager manager = ( ActivityManager )context.getSystemService( Activity.ACTIVITY_SERVICE );
                manager.restartPackage( context.getPackageName() );
                android.os.Process.killProcess( android.os.Process.myPid() );
                System.exit( 0 );
            }
        } catch( Exception e ) {
            System.exit( 0 );
        }
    }

    /**
     *
     * 获取Context对象
     *
     * @return Context Context对象
     * @since 1.0.0
     */
    public Context getContext() {

        return mContext;
    }

    /**
     *
     * activity在onCreate或onNewIntent时，应将activity添加到AppManager统一管理
     *
     * @param activity Activity
     * @since 1.0.0
     */
    public synchronized void addActivity( Activity activity ) {

        mCurrentActivity = activity;
        if( !mActivityList.contains( activity ) ) {
            mActivityList.add( activity );
        }
    }


    /**
     *
     * activity在onDestroy时，应将activity从AppManager移除
     *
     * @param activity
     * @since 1.1.0
     */
    public synchronized void removeActivity( Activity activity ) {

        mActivityList.remove( activity );
    }

    /**
     *
     * 获取当前Activity
     *
     * @return Activity
     * @since 1.1.0
     */
    public Activity getCurrentActivity() {

        return mCurrentActivity;
    }

    private synchronized void closeAllActivity() {

        for( Activity activity : mActivityList ) {
            activity.finish();
        }

        mActivityList.clear();
    }

    /**
     *
     * service在onCreate时，应将service添加到AppManager统一管理
     *
     * @param service
     * @since 1.1.0
     */
    public synchronized void addService( Service service ) {

        if( !mServiceList.contains( service ) ) {
            mServiceList.add( service );
        }
    }


    /**
     *
     * service在onDestory时，应将service从AppManager移除
     *
     * @param service
     * @since 1.1.0
     */
    public synchronized void removeService( Service service ) {

        mServiceList.remove( service );
    }


    private synchronized void stopAllService() {

        for( Service service : mServiceList ) {
            service.stopSelf();
        }
        mServiceList.clear();
    }

    /**
     * sVersion
     *
     * @return the sVersion
     * @since 1.0.0
     */

    public static int getVersion() {

        return sVersion;
    }


    /**
     * @param version
     *            指定框架的版本号，做一些兼容性处理
     */
    public static void setVersion( int version ) {

        AppManager.sVersion = version;
    }

}
