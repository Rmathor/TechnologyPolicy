package com.mathor.technologypolicy.pager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mathor.technologypolicy.Constants;
import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.activity.ProvincePolicyDetailActivity;
import com.mathor.technologypolicy.adapter.ProvincePolicyAdapter;
import com.mathor.technologypolicy.base.BasePager;
import com.mathor.technologypolicy.domain.ProvincePolicy;
import com.mathor.technologypolicy.utils.CacheUtils;
import com.mathor.technologypolicy.view.CustomSwipeRL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: mathor
 * Date : on 2017/11/5 12:25
 * 省政策页面
 */

public class ProvincePolicyPager extends BasePager {

    private static final String PROVINCEPOLICY_SAVE_KEY = "ProvincePolicyPager";

    @ViewInject(R.id.lv_inform_pager)
    private ListView mListView;

    @ViewInject(R.id.swipeRefreshLayout)
    private CustomSwipeRL mCustomSwipeRL;

    private ArrayList<ProvincePolicy> mList;

    private ProvincePolicyAdapter mProvincePolicyAdapter;

    private int pagerNo = 1;//页数

    public ProvincePolicyPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.inform_pager_detail, null);

        x.view().inject(ProvincePolicyPager.this, view);
        mListView.setDivider(new ColorDrawable(Color.GRAY));
        mListView.setDividerHeight(1);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = mList.get(position).getUrl();
                Intent intent = new Intent(mContext, ProvincePolicyDetailActivity.class);
                intent.putExtra(Constants.PROVINCEPOLICY_DETAIL_URL,url);//将url传给activity
                mContext.startActivity(intent);
            }
        });
        mCustomSwipeRL.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mCustomSwipeRL.setRefreshing(true);
        //设置下拉监听
        mCustomSwipeRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCustomSwipeRL.post(new Runnable() {
                    @Override
                    public void run() {

                        //联网获取数据
                        getDataFromNet();
                        mProvincePolicyAdapter.notifyDataSetChanged();
                        Toast.makeText(mContext, "已是最新数据.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //设置加载更多监听
        mCustomSwipeRL.setOnLoadListener(new CustomSwipeRL.OnLoadListener() {
            @Override
            public void onLoad() {
                mCustomSwipeRL.post(new Runnable() {
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
        pagerNo++;
        final ArrayList<ProvincePolicy> datas = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://www.gdstc.gov.cn/zwgk/zcfg/sfggz/index@" + pagerNo + ".htm").get();
//                    System.out.println(document.body());
                    Element element = document.select("table.p12_l22").get(1);
//                    System.out.println(element);
                    Elements tbody = element.getElementsByTag("tbody");
                    Elements elements = tbody.first().getElementsByTag("tr");
                    for (Element e : elements) {
                        Elements td = e.select("td");
                        String title = td.get(1).select("a").text();
                        String url = td.get(1).select("a").attr("href");
                        String date = td.get(2).text();

                        ProvincePolicy provincePolicy = new ProvincePolicy();
                        provincePolicy.setTitle(title);
                        provincePolicy.setUrl(url);
                        provincePolicy.setDate(date);
                        datas.add(provincePolicy);
//                        System.out.println("title:" + title + "==" + "attr:" + attr);
                    }
                    Message msg = Message.obtain();
                    msg.what = Constants.GET_DATA_MORE;
                    msg.obj = datas;
                    mHandler.sendMessageDelayed(msg,1000);
                } catch (IOException e) {
                    Message msg = Message.obtain();
                    msg.what = Constants.GET_DATA_ERROR;
                    msg.obj = datas;
                    mHandler.sendMessageDelayed(msg,500);
//                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void initData() {
        super.initData();
//        System.out.println("initData");

        String saveData = CacheUtils.getString(mContext, PROVINCEPOLICY_SAVE_KEY);
        if (!TextUtils.isEmpty(saveData)) {
//            System.out.println(PROVINCEPOLICY_SAVE_KEY+"=="+saveData);

            mList = new Gson().fromJson(saveData, new TypeToken<List<ProvincePolicy>>() {}.getType());
            Message msg = Message.obtain();
            msg.what = Constants.GET_DATA;
            msg.obj = mList;
            mHandler.sendMessage(msg);
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
                    mList = (ArrayList<ProvincePolicy>) msg.obj;
                    mProvincePolicyAdapter = new ProvincePolicyAdapter(mContext, mList);
                    mListView.setAdapter(mProvincePolicyAdapter);
                    mCustomSwipeRL.setRefreshing(false);
                    break;
                case Constants.GET_DATA_MORE:
                    mList.addAll((ArrayList<ProvincePolicy>) msg.obj);
                    mProvincePolicyAdapter.notifyDataSetChanged();
                    mCustomSwipeRL.setLoading(false);
                    Toast.makeText(mContext, "已加载更多.", Toast.LENGTH_SHORT).show();
                    break;
                case Constants.GET_DATA_ERROR:
                    mCustomSwipeRL.setLoading(false);
                    Toast.makeText(mContext, "我是有极限的.", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    });

    private void getDataFromNet() {
        pagerNo = 1;
        final ArrayList<ProvincePolicy> datas = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://www.gdstc.gov.cn/zwgk/zcfg/sfggz/index@1.htm").get();
//                    System.out.println(document.body());
                    Element element = document.select("table.p12_l22").get(1);
//                    System.out.println(element);
                    Elements tbody = element.getElementsByTag("tbody");
                    Elements elements = tbody.first().getElementsByTag("tr");
                    for (Element e : elements) {
                        Elements td = e.select("td");
                        String title = td.get(1).select("a").text();
                        String url = td.get(1).select("a").attr("href");
                        String date = td.get(2).text();

                        ProvincePolicy provincePolicy = new ProvincePolicy();
                        provincePolicy.setTitle(title);
                        provincePolicy.setUrl(url);
                        provincePolicy.setDate(date);
                        datas.add(provincePolicy);
//                        System.out.println("title:" + title + "==" + "attr:" + attr);
                    }
                    String data = new Gson().toJson(datas);
                    CacheUtils.putString(mContext,PROVINCEPOLICY_SAVE_KEY,data);
                    Message msg = Message.obtain();
                    msg.what = Constants.GET_DATA;
                    msg.obj = datas;
                    mHandler.sendMessageDelayed(msg,1000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
