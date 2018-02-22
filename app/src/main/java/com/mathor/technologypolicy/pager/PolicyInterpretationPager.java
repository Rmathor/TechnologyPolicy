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
import com.mathor.technologypolicy.activity.AffixActivity;
import com.mathor.technologypolicy.activity.PolicyInterpretationDetailActivity;
import com.mathor.technologypolicy.adapter.PolicyInterpretationAdapter;
import com.mathor.technologypolicy.base.BasePager;
import com.mathor.technologypolicy.domain.PolicyInterpretation;
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
 * 政策解读页面
 */

public class PolicyInterpretationPager extends BasePager {

    private static final String POLICYINTERPRETATION_SAVE_DATA = "policyinterpretation_save_data";

    @ViewInject(R.id.lv_technology_pager)
    private ListView mListView;

    @ViewInject(R.id.swipeRefreshLayout)
    private CustomSwipeRL mSwipeRefreshLayout;

    private ArrayList<PolicyInterpretation> mPolicyInterpretations;//政策解读数据的集合
    private PolicyInterpretationAdapter mPolicyInterpretationAdapter;//适配器

    private int pageNum = 0;//页数

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.GET_DATA:
                    mPolicyInterpretations = (ArrayList<PolicyInterpretation>) msg.obj;
//                    System.out.println(mTechnologies.size());
                    mPolicyInterpretationAdapter = new PolicyInterpretationAdapter(mContext, mPolicyInterpretations);
                    mListView.setAdapter(mPolicyInterpretationAdapter);
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case Constants.GET_DATA_MORE:
                    mPolicyInterpretations.addAll((ArrayList<PolicyInterpretation>) msg.obj);
                    mPolicyInterpretationAdapter.notifyDataSetChanged();
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

    public PolicyInterpretationPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
//        Log.e("TAG", "initView");
        View view = View.inflate(mContext, R.layout.policy_interpretation_pager_detail, null);
        x.view().inject(PolicyInterpretationPager.this, view);

        mListView.setDivider(new ColorDrawable(Color.GRAY));//设置listview的分割线颜色
        mListView.setDividerHeight(1);//设置分割线高度

        //设置ListView的item点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = mPolicyInterpretations.get(position).getUrl();
                Intent intent;
                if (url.substring(url.lastIndexOf(".") + 1).equals("pdf")) {
                    intent = new Intent(mContext, AffixActivity.class);
                    intent.putExtra(AffixActivity.AFFIX_URL, Constants.POLICY_INTERPRETATION_BASE_URL + url.substring(url.lastIndexOf("./")+2));
                    mContext.startActivity(intent);
                } else if (url.substring(url.lastIndexOf(".") + 1).equals("htm")) {
                    intent = new Intent(mContext, PolicyInterpretationDetailActivity.class);
                    if (url.startsWith("http")){
                        intent.putExtra(Constants.POLICY_INTERPRETATION_DETAIL_URL,url);
                    }else if (url.startsWith("../")){
                        intent.putExtra(Constants.POLICY_INTERPRETATION_DETAIL_URL, Constants.POLICY_INTERPRETATION_BASE_URL + url.substring(url.lastIndexOf("./")+2));//将url传给activity
                    }else if (url.startsWith("./")){
                        intent.putExtra(Constants.POLICY_INTERPRETATION_DETAIL_URL,Constants.POLICY_INTERPRETATION_PAGER_URL+url.substring(url.lastIndexOf("./")+2));
                    }
//                    Log.e("TAG","url=="+url);
//                    Log.e("TAG","url=="+url.substring(url.lastIndexOf("./")+2));
                    mContext.startActivity(intent);
                }
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
                        mPolicyInterpretationAdapter.notifyDataSetChanged();
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
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
//        Log.e("TAG", "initData");
        //获取缓存数据
        String saveData = CacheUtils.getString(mContext, POLICYINTERPRETATION_SAVE_DATA);
        if (!TextUtils.isEmpty(saveData)) {
//            System.out.println(POLICYINTERPRETATION_SAVE_DATA + "==" + saveData);
            mPolicyInterpretations = new Gson().fromJson(saveData, new TypeToken<List<PolicyInterpretation>>() {
            }.getType());//将json数据转化成集合
            Message msg = Message.obtain();
            msg.what = Constants.GET_DATA;
            msg.obj = mPolicyInterpretations;
            mHandler.sendMessage(msg);
        } else {
            //联网请求数据
            getDataFromNet();
        }
    }

    private void getDataFromNet() {
        pageNum = 0;
        final ArrayList<PolicyInterpretation> policyInterpretations = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://www.most.gov.cn/kjzc/zdkjzcjd/index.htm").get();
//                    System.out.println(document);
                    Element element = document.select("div.list").first();
                    Elements elements = element.getElementsByTag("ul").first().getElementsByTag("li");
                    for (Element e : elements) {
                        PolicyInterpretation policyInterpretation = new PolicyInterpretation();
                        String url = e.getElementsByTag("a").attr("href");
                        String titleAndDate = e.getElementsByTag("a").text();
                        String title = titleAndDate.substring(0,titleAndDate.lastIndexOf("("));
                        String date = titleAndDate.substring(titleAndDate.lastIndexOf("(")+1,titleAndDate.lastIndexOf(")"));

                        policyInterpretation.setTitle(title);
                        policyInterpretation.setDate(date);
                        policyInterpretation.setUrl(url);
                        policyInterpretations.add(policyInterpretation);
                    }
                    String json = new Gson().toJson(policyInterpretations);
                    CacheUtils.putString(mContext, POLICYINTERPRETATION_SAVE_DATA, json);
                    Message msg = Message.obtain();
                    msg.what = Constants.GET_DATA;
                    msg.obj = policyInterpretations;
                    mHandler.sendMessageDelayed(msg,1000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getMoreData() {
        pageNum++;
        final ArrayList<PolicyInterpretation> policyInterpretations = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://www.most.gov.cn/kjzc/zdkjzcjd/index_" + pageNum + ".htm").get();
//                    System.out.println(document);
                    Element element = document.select("div.list").first();
                    Elements elements = element.getElementsByTag("ul").first().getElementsByTag("li");
                    for (Element e : elements) {
                        PolicyInterpretation policyInterpretation = new PolicyInterpretation();
                        String url = e.getElementsByTag("a").attr("href");
                        String titleAndDate = e.getElementsByTag("a").text();
                        String title = titleAndDate.substring(0,titleAndDate.lastIndexOf("("));
                        String date = titleAndDate.substring(titleAndDate.lastIndexOf("(")+1,titleAndDate.lastIndexOf(")"));

                        policyInterpretation.setTitle(title);
                        policyInterpretation.setDate(date);
                        policyInterpretation.setUrl(url);
                        policyInterpretations.add(policyInterpretation);
                    }
                    Message msg = Message.obtain();
                    msg.what = Constants.GET_DATA_MORE;
                    msg.obj = policyInterpretations;
                    mHandler.sendMessageDelayed(msg,1000);
                } catch (IOException e) {
                    Message msg = Message.obtain();
                    msg.what = Constants.GET_DATA_ERROR;
                    mHandler.sendMessageDelayed(msg, 500);
//                    e.printStackTrace();
                }
            }
        }).start();
    }
}
