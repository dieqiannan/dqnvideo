package com.dqndyvideo.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.dueeeke.videoplayer.BuildConfig;
import com.dueeeke.videoplayer.ijk.IjkPlayerFactory;
import com.dueeeke.videoplayer.player.VideoViewConfig;
import com.dueeeke.videoplayer.player.VideoViewManager;

/**
 * app
 * Created by Devlin_n on 2017/4/22.
 */

public class MyApplication extends Application {

    private static MyApplication instance;
    public static Context context;
    public static Handler mHanlder;
    public static int mainThreadId;

    //是否是抖音班播放器
    public static boolean isDyType = true;

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化主线程的handler对象
        mHanlder = new Handler();
        //获取主线程的线程id
        mainThreadId = android.os.Process.myTid();

        instance = this;
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
        //播放器配置，注意：此为全局配置，按需开启
        VideoViewManager.setConfig(VideoViewConfig.newBuilder()
                .setLogEnabled(BuildConfig.DEBUG)
//                .setPlayerFactory(IjkPlayerFactory.create())
                .setPlayerFactory(IjkPlayerFactory.create())
//                .setEnableOrientation(true)
//                .setEnableMediaCodec(true)
//                .setUsingSurfaceView(true)
//                .setEnableParallelPlay(true)
//                .setEnableAudioFocus(false)
//                .setScreenScaleType(VideoView.SCREEN_SCALE_MATCH_PARENT)
                .build());

//        if (BuildConfig.DEBUG) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
//        }
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
