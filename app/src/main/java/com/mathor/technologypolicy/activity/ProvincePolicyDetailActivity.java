package com.mathor.technologypolicy.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.github.chrisbanes.photoview.PhotoView;
import com.mathor.technologypolicy.Constants;
import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.adapter.FuJianAdapter;
import com.mathor.technologypolicy.adapter.MyListAdapter;
import com.mathor.technologypolicy.domain.Fujian;
import com.mathor.technologypolicy.utils.BitmapCache;
import com.mathor.technologypolicy.view.SingleRequestQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static com.mathor.technologypolicy.Constants.BASEURL;

public class ProvincePolicyDetailActivity extends Activity {

    private TextView tv_provincePolicy_detail_title;
    private TextView tv_provincePolicy_detail_comeFromAndDate;
    private TextView tv_provincePolicy_detail_content;

    private PhotoView pv_picture;//单张图片

    private ListView lv_picture;//多张图片
    private ArrayList<String> lv_urls = new ArrayList<>();//图片的url

    private ArrayList<Fujian> mFujian = new ArrayList<>();//附件集合

    private TextView tv_show_fujian;

    private ListView lv_fujian;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private ImageLoader.ImageListener mImageListener;

    private ScrollView scrollView;

    //加载中
    private RelativeLayout rl_loading;

    private String mProvincePolicy_url;//传过来的url

    private ArrayList<String> provincePolicy_detail = new ArrayList<>();

    private static final int SHOW_PROVINCEPOLICY_DETAIL = 0x345;
    private static final int PROVINCEPOLICY_CONTENT_PICTURE = 0x1020;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_PROVINCEPOLICY_DETAIL:
                    provincePolicy_detail = (ArrayList<String>) msg.obj;
                    rl_loading.setVisibility(View.GONE);
                    tv_provincePolicy_detail_title.setText(provincePolicy_detail.get(0));
                    tv_provincePolicy_detail_comeFromAndDate.setText(provincePolicy_detail.get(1));
                    tv_provincePolicy_detail_content.setText(provincePolicy_detail.get(2));
                    if (mFujian.size() > 0) {
                        tv_show_fujian.setVisibility(View.VISIBLE);
                        tv_show_fujian.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openPop();
                            }
                        });
                    }
                    break;
                case PROVINCEPOLICY_CONTENT_PICTURE:
                    if (lv_urls.size() == 1) {
                        scrollView.setVisibility(View.VISIBLE);
                        mRequestQueue = SingleRequestQueue.getRequestQueue(ProvincePolicyDetailActivity.this);
                        mImageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
                        mImageListener = ImageLoader.getImageListener(pv_picture, R.mipmap.picture_loading, R.mipmap.picture_error);
                        mImageLoader.get(lv_urls.get(0), mImageListener, 2000, 8000);
                    } else {
                        scrollView.setVisibility(View.GONE);
                        lv_picture.setAdapter(new MyListAdapter(ProvincePolicyDetailActivity.this, lv_urls));
                        lv_picture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(ProvincePolicyDetailActivity.this, ShowBigPictureActivity.class);//点击跳转到显示大图的activity
                                intent.putExtra("picture_url", lv_urls.get(position));
                                startActivity(intent);
                            }
                        });
                    }
                    break;
            }
            return true;
        }
    });

    /**
     * 弹出底部对话框
     */
    public void openPop() {
        View popView = LayoutInflater.from(ProvincePolicyDetailActivity.this).inflate(
                R.layout.pop_bottom, null);
        lv_fujian = popView.findViewById(R.id.lv_fujian);
        lv_fujian.setAdapter(new FuJianAdapter(ProvincePolicyDetailActivity.this, mFujian));

        View rootView = findViewById(R.id.rl_root); // 當前頁面的根佈局
        final PopupWindow popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

//        setBackgroundAlpha(1);//设置屏幕透明度

        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        if (mFujian.size() >= 8) {
            // 重置PopupWindow高度
            popupWindow.setHeight(Math.round(screenHeight * 0.6f));
        }

        popupWindow.setBackgroundDrawable(new ColorDrawable(0xff00ddff));
        popupWindow.setFocusable(true);// 点击空白处时，隐藏掉pop窗口
        // 顯示
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.LEFT, 0, 0);

        lv_fujian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mFujian.size() == 1) {
                    popupWindow.dismiss();
                }

                Intent intent = new Intent(ProvincePolicyDetailActivity.this, AffixActivity.class);
                intent.putExtra(AffixActivity.AFFIX_URL, mFujian.get(position).getUrl());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province_policy_detail);

        initView();
        mProvincePolicy_url = getIntent().getStringExtra(Constants.PROVINCEPOLICY_DETAIL_URL);
        getDataFromNet();//联网获取详情

    }

    private void getDataFromNet() {

        new Thread(new Runnable() {
            ArrayList<String> datas = new ArrayList<>();
            String inform_content = "";

            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(BASEURL + mProvincePolicy_url).get();
                    Element title_element = document.select("div.p20").first();
                    String title = title_element.getElementsByTag("strong").first().text();//抓取标题
                    String comeFromAndDate = document.select("span.p12").first().text();//抓取日期和来源
                    if (comeFromAndDate.contains("发布日期")) {
//                        String comeFromAndDate_str = "";
                        String[] strings = comeFromAndDate.split("发布日期");//将comeFromAndDate切割成两部分
                        comeFromAndDate = strings[0] + "\n" + "发布日期" + strings[1];
                    }
                    Element content_element = document.select("div.content").first();
                    Elements elements = content_element.getElementsByTag("p");
                    document.select("br").append("sout");//将页面中的<br>标签替换成“sout”，便于文本的排版

                    if (elements.size() >= 4) {
                        if (!TextUtils.isEmpty(elements.get(1).select("input").attr("src"))
                                || !TextUtils.isEmpty(elements.get(1).select("img").attr("src"))
                                || !TextUtils.isEmpty(elements.get(3).select("img").attr("src"))) {
                            if (!TextUtils.isEmpty(elements.get(1).select("input").attr("src"))) {

                                String attr = elements.get(1).select("input").attr("src");
                                lv_urls.add(BASEURL + attr);
                                Message msg = mHandler.obtainMessage(PROVINCEPOLICY_CONTENT_PICTURE);
                                mHandler.sendMessage(msg);
                            } else if (!TextUtils.isEmpty(elements.get(1).select("img").attr("src"))
                                    || !TextUtils.isEmpty(elements.get(3).select("img").attr("src"))) {

                                for (Element element : elements) {
                                    String url = element.select("img").attr("src");
                                    if (!TextUtils.isEmpty(url)) {

                                        lv_urls.add(BASEURL + url);
                                    }
                                }
                                Message msg = mHandler.obtainMessage(PROVINCEPOLICY_CONTENT_PICTURE);
                                mHandler.sendMessage(msg);
                            }
                        } else {
                            for (Element element : elements) {//循环获取内容
                                Fujian fujian = null;
                                for (int i = 0; i < element.select("a").size(); i++) {
                                    String attr = element.select("a").get(i).attr("href");
                                    String fujian_text = element.select("a").get(i).text();
                                    if (!TextUtils.isEmpty(attr) && !"http://www.most.gov.cn/".equals(attr)
                                            && (attr.substring(attr.lastIndexOf(".") + 1).equals("pdf")
                                            || attr.substring(attr.lastIndexOf(".") + 1).equals("doc"))
                                            || attr.substring(attr.lastIndexOf(".") + 1).equals("xls")) {
                                        fujian = new Fujian();
                                        fujian.setTitle(fujian_text);
                                        fujian.setUrl(attr);
                                        mFujian.add(fujian);
                                    }
                                }

                                String content = element.text();
                                if (content.contains("sout")) {
                                    String string = "";
                                    String[] strings = content.split("sout");
                                    for (String str : strings) {
                                        string = string + str + "\n";
                                    }
                                    content = string;
                                }
                                inform_content = inform_content + content + "\n";
                            }
                        }
                    } else {
                        if (!TextUtils.isEmpty(elements.get(1).select("input").attr("src"))
                                || !TextUtils.isEmpty(elements.get(1).select("img").attr("src"))) {
                            if (!TextUtils.isEmpty(elements.get(1).select("input").attr("src"))) {

                                String attr = elements.get(1).select("input").attr("src");
                                lv_urls.add(BASEURL + attr);
                                Message msg = mHandler.obtainMessage(PROVINCEPOLICY_CONTENT_PICTURE);
                                mHandler.sendMessage(msg);
                            } else if (!TextUtils.isEmpty(elements.get(1).select("img").attr("src"))) {

                                for (Element element : elements) {
                                    String url = element.select("img").attr("src");
                                    if (!TextUtils.isEmpty(url)) {

                                        lv_urls.add(BASEURL + url);
                                    }
                                }
                                Message msg = mHandler.obtainMessage(PROVINCEPOLICY_CONTENT_PICTURE);
                                mHandler.sendMessage(msg);
                            }
                        } else {
                            for (Element element : elements) {//循环获取内容
                                Fujian fujian = null;
                                for (int i = 0; i < element.select("a").size(); i++) {
                                    String attr = element.select("a").get(i).attr("href");
                                    String fujian_text = element.select("a").get(i).text();
                                    if (!TextUtils.isEmpty(attr) && !"http://www.most.gov.cn/".equals(attr)
                                            && (attr.substring(attr.lastIndexOf(".") + 1).equals("pdf")
                                            || attr.substring(attr.lastIndexOf(".") + 1).equals("doc"))
                                            || attr.substring(attr.lastIndexOf(".") + 1).equals("xls")) {
                                        fujian = new Fujian();
                                        fujian.setTitle(fujian_text);
                                        fujian.setUrl(attr);
                                        mFujian.add(fujian);
                                    }
                                }

                                String content = element.text();
                                if (content.contains("sout")) {
                                    String string = "";
                                    String[] strings = content.split("sout");
                                    for (String str : strings) {
                                        string = string + str + "\n";
                                    }
                                    content = string;
                                }
                                inform_content = inform_content + content + "\n";
                            }
                        }
                    }
                    datas.add(title);
                    datas.add(comeFromAndDate);
                    datas.add(inform_content);
                    Message msg = Message.obtain();
                    msg.what = SHOW_PROVINCEPOLICY_DETAIL;
                    msg.obj = datas;
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    getDataFromNet();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //初始化组件
    private void initView() {
        tv_provincePolicy_detail_title = findViewById(R.id.tv_provincePolicy_detail_title);
        tv_provincePolicy_detail_comeFromAndDate = findViewById(R.id.tv_provincePolicy_detail_comeFromAndDate);
        tv_provincePolicy_detail_content = findViewById(R.id.tv_provincePolicy_detail_content);

        pv_picture = findViewById(R.id.pv_picture);
        lv_picture = findViewById(R.id.lv_picture);

        scrollView = findViewById(R.id.scrollView);
        tv_show_fujian = findViewById(R.id.tv_show_fujian);

        rl_loading = findViewById(R.id.rl_loading);
    }
}
