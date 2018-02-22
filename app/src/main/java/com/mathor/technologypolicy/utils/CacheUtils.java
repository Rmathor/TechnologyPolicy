package com.mathor.technologypolicy.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import org.xutils.common.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Author: xiaoma
 * Date : on 2016/11/29
 * 缓存工具类
 */
public class CacheUtils {

    /**
     * 获取是否已经登录的缓存信息
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("mathor", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    /**
     * 添加是否登录的缓存信息
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("mathor", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 缓存数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
        //if (isGrantExternalRW((Activity) context,Manifest.permission.READ_EXTERNAL_STORAGE)){
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                try {
                    String fileName = MD5Encoder.encode(key);
                    //mnt/sdCard/technologypolicy/files/xxxx
                    File file = new File(Environment.getExternalStorageDirectory() + "/technologypolicy/files", fileName);

                    //一定要加上
                    File parentFile = file.getParentFile();
                    if (!parentFile.exists()) {
                        //创建目录
                        parentFile.mkdirs();
                    }

                    if (!file.exists()) {
                        file.createNewFile();
                    }

                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(value.getBytes());
                    fos.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e("缓存文件数据失败");
                }
            } else {

                SharedPreferences sp = context.getSharedPreferences("mathor", Context.MODE_PRIVATE);
                sp.edit().putString(key, value).commit();
            }
        //}
    }

    /**
     * 获取缓存信息
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {

        String result = "";
       // if (isGrantExternalRW((Activity) context,Manifest.permission.READ_EXTERNAL_STORAGE)){

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                try {
                    //对名称进行加密
                    String fileName = MD5Encoder.encode(key);
                    File file = new File(Environment.getExternalStorageDirectory() + "/technologypolicy/files", fileName);
                    if (file.exists()) {

                        FileInputStream is = new FileInputStream(file);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        byte[] bytes = new byte[1024];
                        int len = -1;
                        while ((len = is.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, len);
                        }
                        is.close();
                        outputStream.close();
                        result = outputStream.toString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e("获取文件数据失败");
                }
            } else {

                SharedPreferences sp = context.getSharedPreferences("mathor", Context.MODE_PRIVATE);
                result = sp.getString(key, "");
            }
       // }
        return result;
    }

    /**
     * 解决安卓6.0以上版本动态申请权限
     *
     * @param activity
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
            }, 1);

            return false;
        }
        return true;
    }
}
