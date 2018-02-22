package com.mathor.technologypolicy.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Author: mathor
 * Date : on 2017/11/14 15:27
 * 内存管理者
 */

public class CacheDataManager {

    private static String mSDPath;

    //获取下载的文件大小
    public static String getTotalCacheSize(Context context) throws Exception {

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            mSDPath = context.getExternalFilesDir("/tpdownload").getAbsolutePath();
        } else {
            mSDPath = context.getFilesDir().getAbsolutePath() + "/tpdownload";
        }
        long cacheSize = getFolderSize(new File(mSDPath));
        return cacheSize / 1024 /1024 +"";
    }

    //清除文件
    public static void clearAllCache() {
        deleteDir(new File(mSDPath));
    }

    private static boolean deleteDir(File dir) {

        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    // 获取文件大小
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            if (fileList != null){
                for (int i = 0; i < fileList.length; i++) {

                    // 如果下面还有文件
                    if (fileList[i].isDirectory()) {
                        size = size + getFolderSize(fileList[i]);
                    } else {
                        size = size + fileList[i].length();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
}
