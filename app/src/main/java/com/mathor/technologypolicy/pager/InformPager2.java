package com.mathor.technologypolicy.pager;

import android.app.Activity;
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
import com.mathor.technologypolicy.activity.InformDetailActivity;
import com.mathor.technologypolicy.adapter.InformAdapter2;
import com.mathor.technologypolicy.base.BasePager;
import com.mathor.technologypolicy.domain.Inform2;
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
 * Date : on 2017/11/7 11:04
 */

public class InformPager2 extends BasePager {

    private static final String INFORM2_SAVE_KEY = "InformPager2";

    @ViewInject(R.id.lv_inform_pager)
    private ListView mListView;

    private ArrayList<Inform2> mList;

    @ViewInject(R.id.swipeRefreshLayout)
    private CustomSwipeRL mSwipeRefreshLayout;

    private InformAdapter2 mInformAdapter2;

    private int pagerNo = 1;//页数

    public InformPager2(Context context) {
        super(context);

    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.inform_pager_detail, null);
        x.view().inject(InformPager2.this, view);

        mListView.setDivider(new ColorDrawable(Color.GRAY));//设置listview的分割线颜色
        mListView.setDividerHeight(1);//设置分割线高度
        //设置ListView的item点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = mList.get(position).getUrl();
                Intent intent = new Intent(mContext, InformDetailActivity.class);
                intent.putExtra(Constants.INFORM_DETAIL_URL,url);//将url传给activity
                mContext.startActivity(intent);
            }
        });

        //设置圆圈颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        //进入页面时打开刷新
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
                        mInformAdapter2.notifyDataSetChanged();
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
        final ArrayList<Inform2> datas = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://www.gdstc.gov.cn/zwgk/tzgg/index@" + pagerNo + ".htm").get();
//                    System.out.println(document.body());
                    Element element = document.select("table.p12_l22").get(1);
//                    System.out.println(element);
                    Elements tbody = element.getElementsByTag("tbody");
                    Elements elements = tbody.first().getElementsByTag("tr");
                    for (Element e : elements) {
                        Inform2 inform2 = new Inform2();
                        Elements td = e.select("td");
                        String title = td.get(1).select("a").text();//抓取标题
                        String url = td.get(1).select("a").attr("href");//抓取连接
                        String date = td.get(2).text();//日期

                        inform2.setTitle(title);
                        inform2.setUrl(url);
                        inform2.setDate(date);
                        datas.add(inform2);
//                        System.out.println("title:" + title + "==" + "attr:" + url);
                    }
                    Message msg = Message.obtain();
                    msg.what = Constants.GET_DATA_MORE;
                    msg.obj = datas;
                    mHandler.sendMessageDelayed(msg,1000);
                } catch (IOException e) {//把页面加载失败当做没有更多数据（由于没有更多数据会出现没有此页面的错误）
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

        //获取缓存数据
        String saveData = CacheUtils.getString(mContext, INFORM2_SAVE_KEY);
        if (!TextUtils.isEmpty(saveData)) {

//            System.out.println(INFORM2_SAVE_KEY + "==" + saveData);
            mList = new Gson().fromJson(saveData, new TypeToken<List<Inform2>>(){}.getType());//将json数据转化成集合
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
                    mList = (ArrayList<Inform2>) msg.obj;
                    mInformAdapter2 = new InformAdapter2(mContext, mList);
                    mListView.setAdapter(mInformAdapter2);
                    mSwipeRefreshLayout.setRefreshing(false);//更新数据后，结束刷新
                    break;
                case Constants.GET_DATA_MORE:
                    mList.addAll((ArrayList<Inform2>) msg.obj);
                    mInformAdapter2.notifyDataSetChanged();
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
        final ArrayList<Inform2> datas = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://www.gdstc.gov.cn/zwgk/tzgg/index@1.htm").get();
//                    System.out.println(document.body());
                    Element element = document.select("table.p12_l22").get(1);
//                    System.out.println(element);
                    Elements tbody = element.getElementsByTag("tbody");
                    Elements elements = tbody.first().getElementsByTag("tr");
                    for (Element e : elements) {
                        Inform2 inform2 = new Inform2();
                        Elements td = e.select("td");
                        String title = td.get(1).select("a").text();
                        String url = td.get(1).select("a").attr("href");
                        String date = td.get(2).text();

                        inform2.setTitle(title);
                        inform2.setUrl(url);
                        inform2.setDate(date);
                        datas.add(inform2);
//                        System.out.println("title:" + title + "==" + "attr:" + url);
                    }

                    String data = new Gson().toJson(datas);//将集合转换json格式的字符串
                    CacheUtils.isGrantExternalRW((Activity) mContext);
                    CacheUtils.putString(mContext, INFORM2_SAVE_KEY, data);//缓存数据
                    Message msg = Message.obtain();
                    msg.what = Constants.GET_DATA;
                    msg.obj = datas;
                    mHandler.sendMessageDelayed(msg,1000);//发送信息
                } catch (IOException e) {
                    getDataFromNet();
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
