package com.mathor.technologypolicy.pager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mathor.technologypolicy.Constants;
import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.adapter.InformAdapter;
import com.mathor.technologypolicy.base.BasePager;
import com.mathor.technologypolicy.domain.Inform;
import com.mathor.technologypolicy.utils.CacheUtils;
import com.mathor.technologypolicy.view.CustomSwipeRL;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Author: mathor
 * Date : on 2017/11/5 12:25
 */

public class InformPager extends BasePager {

    private static final String TAG = "InformPager";
    private static final String INFORM_CACHE_KEY = "InformPager";

    @ViewInject(R.id.lv_inform_pager)
    private ListView mListView;

    @ViewInject(R.id.swipeRefreshLayout)
    private CustomSwipeRL swipeRefreshLayout;

    private List<Inform.DataBean.RowsBean> informs;

    private InformAdapter mInformAdapter;

    //列表最后一个元素的id
    private int lastItemId;

    public InformPager(Context context) {
        super(context);

    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.inform_pager_detail, null);
        x.view().inject(InformPager.this, view);

        mListView.setDivider(new ColorDrawable(Color.GRAY));
        mListView.setDividerHeight(1);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        //显示加载中视图
        swipeRefreshLayout.setRefreshing(true);
        //设置下拉监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {

                        //联网获取数据
                        getDataFromNet();
//                        mInformAdapter.notifyDataSetChanged();
//                        swipeRefreshLayout.setRefreshing(false);//更新数据后，结束刷新
//                        Toast.makeText(mContext,"已是最新数据.",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //设置加载更多监听
        swipeRefreshLayout.setOnLoadListener(new CustomSwipeRL.OnLoadListener() {
            @Override
            public void onLoad() {
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {

                        //联网获取更多数据
                        getMoreDataFromNet();
                    }
                });
            }
        });

        return view;
    }

    private void getMoreDataFromNet() {

        lastItemId = informs.get(informs.size() - 1).getId();
//        System.out.println(lastItemId);
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestParams entity = new RequestParams("http://app.gdstc.gov.cn/news/page?t=1010&max_id=" + lastItemId);
                x.http().get(entity, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {

                        //解析数据
                        processDataForMore(result);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.e(TAG, ex.toString());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Log.e(TAG, cex.toString());
                    }

                    @Override
                    public void onFinished() {
                        swipeRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                //关闭加载更多
                                swipeRefreshLayout.setLoading(false);
                                Toast.makeText(mContext, "加载成功.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    @Override
    public void initData() {
        super.initData();
//        System.out.println("initData");

        //获取缓存数据
        String saveData = CacheUtils.getString(mContext, INFORM_CACHE_KEY);
//        System.out.println("saveDaa==="+saveData);
        if (!TextUtils.isEmpty(saveData)) {
            //解析数据
            processData(saveData);
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        } else {

            //联网请求数据
            getDataFromNet();
        }

    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.GET_DATA:
                    informs = (List<Inform.DataBean.RowsBean>) msg.obj;
                    mInformAdapter = new InformAdapter(mContext, informs);
                    mInformAdapter.notifyDataSetChanged();
                    mListView.setAdapter(mInformAdapter);
                    break;
                case Constants.GET_DATA_MORE:
                    informs = (List<Inform.DataBean.RowsBean>) msg.obj;
                    mInformAdapter.notifyDataSetChanged();
                    break;
            }
            return true;
        }
    });

    private void getDataFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestParams entity = new RequestParams("http://app.gdstc.gov.cn/news/page?t=1010");
                x.http().get(entity, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {

                        //缓存数据
                        CacheUtils.putString(mContext, INFORM_CACHE_KEY, result);
//                        System.out.println("result==="+result);

                        //联网获取数据
                        processData(result);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.e(TAG, ex.toString());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Log.e(TAG, cex.toString());
                    }

                    @Override
                    public void onFinished() {
                        swipeRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(false);
                                Toast.makeText(mContext, "已是最新数据.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void processDataForMore(String json) {
        Inform inform = parseJsonForMore(json);
        informs.addAll(inform.getData().getRows());
        Message msg = Message.obtain();
        msg.what = Constants.GET_DATA_MORE;
        msg.obj = informs;
        mHandler.sendMessage(msg);
    }

    private Inform parseJsonForMore(String json) {
        return new Gson().fromJson(json, Inform.class);
    }

    private void processData(String json) {
        Inform inform = parseJson(json);
        informs = inform.getData().getRows();
        Message msg = Message.obtain();
        msg.what = Constants.GET_DATA;
        msg.obj = informs;
        mHandler.sendMessage(msg);
    }

    private Inform parseJson(String json) {
        return new Gson().fromJson(json, Inform.class);
    }
}
