package com.dqndyvideo.util.cache;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;
import com.dqndyvideo.TiktokBean;
import com.dueeeke.videoplayer.util.L;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;

public class PreloadTask implements Runnable {

    private final static String TAG = "PreloadTask";
    /**
     * 原始地址
     */
    public String mRawUrl;

    /**
     * 列表中的位置
     */
    public int mPosition;

    /**
     * VideoCache服务器
     */
    public HttpProxyCacheServer mCacheServer;

    /**
     * 是否被取消
     */
    private boolean mIsCanceled;

    /**
     * 是否正在预加载
     */
    private boolean mIsExecuted;

    public TiktokBean bean;

    @Override
    public void run() {
        if (!mIsCanceled) {
            start();
        }
        mIsExecuted = false;
        mIsCanceled = false;
    }

    /**
     * 开始预加载
     */
    private void start() {
        Log.e("加载", "dd");
        Log.e("开始预加载：" + mPosition, "");

        if (mRawUrl.startsWith("http")) {
            HttpURLConnection connection = null;
            try {
                //获取HttpProxyCacheServer的代理地址
                String proxyUrl = mCacheServer.getProxyUrl(mRawUrl);
                URL url = new URL(proxyUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5_000);
                connection.setReadTimeout(5_000);
                InputStream in = new BufferedInputStream(connection.getInputStream());
                int length;
                int read = -1;
                byte[] bytes = new byte[8 * 1024];
                while ((length = in.read(bytes)) != -1) {
                    read += length;
                    //预加载完成或者取消预加载
                    if (mIsCanceled || read >= PreloadManager.PRELOAD_LENGTH) {
                        L.i("结束预加载：" + mPosition);
                        connection.disconnect();
                        break;
                    }
                }

            } catch (Exception e) {
                L.i("异常结束预加载：" + mPosition);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } else {
            File f = new File(mRawUrl);
            Log.e(TAG, "提前预加载本地缓存=" + f.getAbsolutePath());
            if (f != null) {
                try {
                    BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));

                    Log.e(TAG, "提前预加载本地缓存 01");
                    File cacheFile = mCacheServer.getCacheRoot();

                    if (!cacheFile.exists()) {
                        cacheFile.mkdirs();
                    }
                    File cacheFile02 = mCacheServer.getCacheFile(mRawUrl);

                    Log.e(TAG, "提前预加载本地缓存 02 cacheFile02=" + cacheFile02.getAbsolutePath());


                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(cacheFile02));

                    Log.e(TAG, "提前预加载本地缓存 03");
                    int length;
                    int read = -1;
                    byte[] bytes = new byte[8 * 1024];
                    while ((length = in.read(bytes)) != -1) {
                        read += length;
                        //预加载完成或者取消预加载
                        if (mIsCanceled || read >= PreloadManager.PRELOAD_LENGTH) {
                            Log.e(TAG, "提前预加载本地缓存 缓存超出");
                            break;
                        }
                        Log.e(TAG, "提前预加载本地缓存 04");
                        out.write(bytes);
                    }

                    out.flush();
                    out.close();

                    if (bean != null) {
                        /*Log.e(TAG, "提前预加载本地缓存 生成图片");
                        MediaMetadataRetriever media = new MediaMetadataRetriever();
                        media.setDataSource(cacheFile02.getAbsolutePath());
                        Bitmap bitmap = media.getFrameAtTime();
                        bean.bt = bitmap;
                        if (bean.bt == null) {
                            Log.e(TAG, "提前预加载本地缓存 生成图片失败");
                        }*/
                    }


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e("提前预加载本地缓存", "FileNotFoundException");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("提前预加载本地缓存", "IOException");
                }
            } else {
                Log.e("提前预加载本地缓存", "文件为空");
            }
        }


    }

    /**
     * 将预加载任务提交到线程池，准备执行
     */
    public void executeOn(ExecutorService executorService) {
        if (mIsExecuted) return;
        mIsExecuted = true;
        executorService.submit(this);
    }

    /**
     * 取消预加载任务
     */
    public void cancel() {
        if (mIsExecuted) {
            mIsCanceled = true;
        }
    }
}
