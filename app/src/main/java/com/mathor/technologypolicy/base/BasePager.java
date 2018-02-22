package com.mathor.technologypolicy.base;

import android.content.Context;
import android.view.View;

/**
 * Author: mathor
 * Date : on 2017/11/5 12:21
 * 页面的基类
 */

public abstract class BasePager {

    //上下文
    public Context mContext;

    //各个子视图
    public View rootView;


    public BasePager(Context context){

        this.mContext = context;
        rootView = initView();
    }

    /**
     * 初始化视图，每个页面实现不同的效果
     *
     * @return
     */
    public abstract View initView();

    /**
     * 初始化数据，联网请求
     */
    public void initData() {

    }
}
