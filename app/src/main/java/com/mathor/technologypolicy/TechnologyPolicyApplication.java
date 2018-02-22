package com.mathor.technologypolicy;

import com.mob.MobApplication;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;


/**
 * Author: mathor
 * Date : on 2017/11/3 18:44
 * APP的application
 */

public class TechnologyPolicyApplication extends MobApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化xUtils3
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.

        JPushInterface.setDebugMode(true);     // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

    }
}
