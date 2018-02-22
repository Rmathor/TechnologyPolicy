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
import com.mathor.technologypolicy.activity.CountryPolicyDetailActivity;
import com.mathor.technologypolicy.adapter.CountryPolicyAdapter;
import com.mathor.technologypolicy.base.BasePager;
import com.mathor.technologypolicy.domain.CountryPolicy;
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
 * 国家政策页面
 */

public class CountryPolicyPager extends BasePager {

    private static final String COUNTRYPOLICY_SAVE_KEY = "CountryPolicyPager";

    @ViewInject(R.id.lv_inform_pager)
    private ListView mListView;

    private ArrayList<CountryPolicy> mList;

    @ViewInject(R.id.swipeRefreshLayout)
    private CustomSwipeRL mSwipeRefreshLayout;

    private CountryPolicyAdapter mCountryPolicyAdapter;

    private int pagerNo = 1;//页数

    public CountryPolicyPager(Context context) {
        super(context);

    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.inform_pager_detail, null);
        x.view().inject(CountryPolicyPager.this, view);

        mListView.setDivider(new ColorDrawable(Color.GRAY));
        mListView.setDividerHeight(1);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = mList.get(position).getUrl();
                Intent intent = new Intent(mContext, CountryPolicyDetailActivity.class);
                intent.putExtra(Constants.COUNTRYPOLICY_DETAIL_DETAIL_URL,url);//将url传给activity
                mContext.startActivity(intent);
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setRefreshing(true);
        //设置下拉监听
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {

                        //联网获取数据
                        getDataFromNet();
                        mCountryPolicyAdapter.notifyDataSetChanged();
//                        mSwipeRefreshLayout.setRefreshing(false);//更新数据后，结束刷新
                        Toast.makeText(mContext, "已是最新数据.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //设置加载更多监听
        mSwipeRefreshLayout.setOnLoadListener(new CustomSwipeRL.OnLoadListener() {
            @Override
            public void onLoad() {
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {

                        //联网获取更多数据
                        getMoreDataFromNet();
//                        mSwipeRefreshLayout.setLoading(false);
//                        Toast.makeText(mContext,"已加载更多.",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }

    private void getMoreDataFromNet() {
        pagerNo++;
        final ArrayList<CountryPolicy> datas = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://www.gdstc.gov.cn/zwgk/zcfg/gjzcfg/index@" + pagerNo + ".htm").get();
//                    System.out.println(document.body());
                    Element element = document.select("table.p12_l22").get(1);
//                    System.out.println(element);
                    Elements tbody = element.getElementsByTag("tbody");
                    Elements elements = tbody.first().getElementsByTag("tr");
                    for (Element e : elements) {
                        CountryPolicy countryPolicy = new CountryPolicy();
                        Elements td = e.select("td");
                        String title = td.get(1).select("a").text();
                        String url = td.get(1).select("a").attr("href");
                        String date = td.get(2).text();

                        countryPolicy.setTitle(title);
                        countryPolicy.setUrl(url);
                        countryPolicy.setDate(date);
                        datas.add(countryPolicy);
//                        System.out.println("title:" + title + "==" + "attr:" + url);
                    }
                    Message msg = Message.obtain();
                    msg.what = Constants.GET_DATA_MORE;
                    msg.obj = datas;
                    mHandler.sendMessageDelayed(msg,1000);
                } catch (IOException e) {
                    Message msg = Message.obtain();
                    msg.what = Constants.GET_DATA_ERROR;
                    mHandler.sendMessageDelayed(msg,500);
//                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void initData() {
        super.initData();

        String saveData = CacheUtils.getString(mContext, COUNTRYPOLICY_SAVE_KEY);
        if (!TextUtils.isEmpty(saveData)) {
//            System.out.println(COUNTRYPOLICY_SAVE_KEY + "==" + saveData);

            mList = new Gson().fromJson(saveData, new TypeToken<List<CountryPolicy>>() {}.getType());//将保持的数据转化为集合
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
                    mList = (ArrayList<CountryPolicy>) msg.obj;
                    mCountryPolicyAdapter = new CountryPolicyAdapter(mContext, mList);
                    mListView.setAdapter(mCountryPolicyAdapter);
                    mSwipeRefreshLayout.setRefreshing(false);//更新数据后，结束刷新
                    break;
                case Constants.GET_DATA_MORE:
                    mList.addAll((ArrayList<CountryPolicy>) msg.obj);
                    mCountryPolicyAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setLoading(false);
                    Toast.makeText(mContext, "已加载更多.", Toast.LENGTH_SHORT).show();
                    break;
                case Constants.GET_DATA_ERROR:
                    mSwipeRefreshLayout.setLoading(false);
                    Toast.makeText(mContext, "我是有极限的.", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    });

    private void getDataFromNet() {
        pagerNo = 1;
        final ArrayList<CountryPolicy> datas = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://www.gdstc.gov.cn/zwgk/zcfg/gjzcfg/index@1.htm").get();
//                    System.out.println(document.body());
                    Element element = document.select("table.p12_l22").get(1);
//                    System.out.println(element);
                    Elements tbody = element.getElementsByTag("tbody");
                    Elements elements = tbody.first().getElementsByTag("tr");
                    for (Element e : elements) {
                        CountryPolicy countryPolicy = new CountryPolicy();
                        Elements td = e.select("td");
                        String title = td.get(1).select("a").text();
                        String url = td.get(1).select("a").attr("href");
                        String date = td.get(2).text();

                        countryPolicy.setTitle(title);
                        countryPolicy.setUrl(url);
                        countryPolicy.setDate(date);
                        datas.add(countryPolicy);
//                        System.out.println("title:" + title + "==" + "attr:" + url);
                    }
                    String data = new Gson().toJson(datas);
                    CacheUtils.putString(mContext, COUNTRYPOLICY_SAVE_KEY, data);
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
