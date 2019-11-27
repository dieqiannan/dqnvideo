package com.dqndyvideo.util;

import com.dueeeke.videoplayer.player.VideoViewConfig;
import com.dueeeke.videoplayer.player.VideoViewManager;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class Utils {


    /**
     * 获取当前的播放核心
     */
    public static Object getCurrentPlayerFactory() {
        VideoViewConfig config = VideoViewManager.getConfig();
        Object playerFactory = null;
        try {
            Field mPlayerFactoryField = config.getClass().getDeclaredField("mPlayerFactory");
            mPlayerFactoryField.setAccessible(true);
            playerFactory = mPlayerFactoryField.get(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playerFactory;
    }


    /**
     * 获取视频文件
     *
     * @param file
     * @return
     */
    public static List<File> getVideoFile(final ArrayList<File> data, File file) {

        file.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {


                if (file.isDirectory()) {

                    String[] strings = file.list();
                    if (strings != null && strings.length > 0 && Utils.isVideo(file.listFiles()[0])) {

                        data.add(file);
                        return true;
                    }
                } else if (file.isFile() && Utils.isVideo(file)) {
                    data.add(file);
                    return true;
                }
                return false;
            }
        });


        return data;
    }

    /**
     * 判断是否是视频
     * @param path
     * @return
     */
    public static boolean isVideo(File path) {
        String name = path.getName();
        int i = name.indexOf('.');
        if (i == -1) {
            return false;
        }

        name = name.substring(i);
        // Log.e(TAG, "file  name=" + name);
        return name.equalsIgnoreCase(".mp4")
                || name.equalsIgnoreCase(".3gp")
                || name.equalsIgnoreCase(".wmv")
                || name.equalsIgnoreCase(".ts")
                || name.equalsIgnoreCase(".rmvb")
                || name.equalsIgnoreCase(".mov")
                || name.equalsIgnoreCase(".m4v")
                || name.equalsIgnoreCase(".avi")
                || name.equalsIgnoreCase(".m3u8")
                || name.equalsIgnoreCase(".3gpp")
                || name.equalsIgnoreCase(".3gpp2")
                || name.equalsIgnoreCase(".mkv")
                || name.equalsIgnoreCase(".mk")
                || name.equalsIgnoreCase(".MK")
                || name.equalsIgnoreCase(".flv")
                || name.equalsIgnoreCase(".divx")
                || name.equalsIgnoreCase(".f4v")
                || name.equalsIgnoreCase(".rm")
                || name.equalsIgnoreCase(".asf")
                || name.equalsIgnoreCase(".ram")
                || name.equalsIgnoreCase(".mpg")
                || name.equalsIgnoreCase(".v8")
                || name.equalsIgnoreCase(".swf")
                || name.equalsIgnoreCase(".m2v")
                || name.equalsIgnoreCase(".asx")
                || name.equalsIgnoreCase(".ra")
                || name.equalsIgnoreCase(".ndivx")
                || name.equalsIgnoreCase(".xvid");
    }

}
