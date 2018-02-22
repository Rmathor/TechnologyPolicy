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
import com.mathor.technologypolicy.activity.TechnologyDetailActivity;
import com.mathor.technologypolicy.adapter.TechnologyAdapter;
import com.mathor.technologypolicy.base.BasePager;
import com.mathor.technologypolicy.domain.Technology;
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
 * Date : on 2017/11/10 15:50
 * 科技页面
 */

public class TechnologyPager extends BasePager {

    private static final String TECHNOLOGY_SAVE_DATA = "technology_save_data";

    @ViewInject(R.id.lv_technology_pager)
    private ListView mListView;

    @ViewInject(R.id.swipeRefreshLayout)
    private CustomSwipeRL mSwipeRefreshLayout;

    private ArrayList<Technology> mTechnologies;//科技数据的集合
    private TechnologyAdapter mTechnologyAdapter;//适配器


    private int pageNum = 0;//页数

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.GET_DATA:
                    mTechnologies = (ArrayList<Technology>) msg.obj;
//                    System.out.println(mTechnologies.size());
                    mTechnologyAdapter = new TechnologyAdapter(mContext, mTechnologies);
                    mListView.setAdapter(mTechnologyAdapter);
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case Constants.GET_DATA_MORE:
                    mTechnologies.addAll((ArrayList<Technology>) msg.obj);
                    mTechnologyAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setLoading(false);
                    Toast.makeText(mContext, "已加载更多.", Toast.LENGTH_SHORT).show();
                    break;
                case Constants.GET_DATA_ERROR:
                    mSwipeRefreshLayout.setLoading(false);
                    Toast.makeText(mContext, "已经见底了..", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    });

    public TechnologyPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.technology_pager_detail, null);
        x.view().inject(TechnologyPager.this, view);
        mListView.setDivider(new ColorDrawable(Color.GRAY));//设置listview的分割线颜色
        mListView.setDividerHeight(1);//设置分割线高度
        //设置ListView的item点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = mTechnologies.get(position).getUrl();
                Intent intent = new Intent(mContext, TechnologyDetailActivity.class);
                intent.putExtra(Constants.TECHNOLOGY_DETAIL_URL, url);//将url传给activity
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
                        mTechnologyAdapter.notifyDataSetChanged();
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
                        getMoreData();
//                        mSwipeRefreshLayout.setLoading(false);
//                        Toast.makeText(mContext,"已加载更多.",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        //获取缓存数据
        String saveData = CacheUtils.getString(mContext, TECHNOLOGY_SAVE_DATA);
        if (!TextUtils.isEmpty(saveData)) {

//            System.out.println(INFORM2_SAVE_KEY + "==" + saveData);
            mTechnologies = new Gson().fromJson(saveData, new TypeToken<List<Technology>>() {
            }.getType());//将json数据转化成集合
            Message msg = Message.obtain();
            msg.what = Constants.GET_DATA;
            msg.obj = mTechnologies;
            mHandler.sendMessage(msg);
        } else {
            //联网请求数据
            getDataFromNet();
        }
    }

    private void getDataFromNet() {
        pageNum = 0;
        final ArrayList<Technology> technologies = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://www.kepu.gov.cn/index/datas/shows/index.shtml").get();
//                    System.out.println(document);
                    Element element = document.select("div.box1_l_main").first();
                    Elements elements = element.getElementsByTag("ul");
                    for (Element e : elements) {
                        Elements elements1 = e.getElementsByTag("li");
                        for (Element e1 : elements1) {
                            Technology technology = new Technology();
                            String title = e1.select("span.box1_l_title").text();
                            String url = e1.select("span.box1_l_title").select("a").attr("href");
                            String date = e1.select("span.box1_l_time").text();
                            technology.setTitle(title);
                            technology.setUrl(url);
                            technology.setDate(date);

                            technologies.add(technology);
                        }
                    }
                    String json = new Gson().toJson(technologies);
                    CacheUtils.putString(mContext, TECHNOLOGY_SAVE_DATA, json);
                    Message msg = Message.obtain();
                    msg.what = Constants.GET_DATA;
                    msg.obj = technologies;
                    mHandler.sendMessageDelayed(msg, 1000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getMoreData() {
        pageNum++;
        final ArrayList<Technology> technologies = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://www.kepu.gov.cn/index/datas/shows/index_" + pageNum + ".shtml").get();
//                    System.out.println(document);
                    Element element = document.select("div.box1_l_main").first();
                    Elements elements = element.getElementsByTag("ul");
                    for (Element e : elements) {
                        Elements elements1 = e.getElementsByTag("li");
                        for (Element e1 : elements1) {
                            Technology technology = new Technology();
                            String title = e1.select("span.box1_l_title").text();
                            String url = e1.select("span.box1_l_title").select("a").attr("href");
                            String date = e1.select("span.box1_l_time").text();
                            technology.setTitle(title);
                            technology.setUrl(url);
                            technology.setDate(date);

                            technologies.add(technology);
                        }
                    }

                    Message msg = Message.obtain();
                    msg.what = Constants.GET_DATA_MORE;
                    msg.obj = technologies;
                    mHandler.sendMessageDelayed(msg, 1000);
                } catch (IOException e) {
                    Message msg = Message.obtain();
                    msg.what = Constants.GET_DATA_ERROR;
                    mHandler.sendMessageDelayed(msg, 500);
//                    e.printStackTrace();
                }
            }
        }).start();
    }

//    private void getDataFromNet2() {
//        pageNum = 0;
//        final ArrayList<Technology> technologies = new ArrayList<>();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Document document = Jsoup.connect("http://zdkjzx.gdstc.gov.cn/ShowMoreNews.do?newsTypeId=64").get();
//                    System.out.println(document);
//                    Element element = document.select("table").get(3);
//                    Elements elements = element.getElementsByTag("tbody").first().getElementsByTag("tr");
//                    for (Element e:elements){
//                        Technology technology = new Technology();
//                        Elements td = e.select("td");
//                        String title = td.get(0).text();
//                        String url = td.get(0).select("a").attr("href");
////                        String date = td.get(1).text();
//
//                        technology.setTitle(title);
//                        technology.setUrl(url);
////                        technology.setDate(date);
//
//                        technologies.add(technology);
//                    }
//                    Message msg = Message.obtain();
//                    msg.what = Constants.GET_DATA;
//                    msg.obj = technologies;
//                    mHandler.sendMessage(msg);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
}
