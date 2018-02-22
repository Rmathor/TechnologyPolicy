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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mathor.technologypolicy.Constants;
import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.adapter.FuJianAdapter;
import com.mathor.technologypolicy.domain.Fujian;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class CountryPolicyDetailActivity extends Activity {

    private TextView tv_countryPolicy_detail_title;
    private TextView tv_countryPolicy_detail_comeFromAndDate;
    private TextView tv_countryPolicy_detail_content;

    private RelativeLayout rl_loading;

    private String mCountryPolicy_url;

    private static final int SHOW_COUNTRYPOLICY_DETAIL = 0x234;

    private ArrayList<String> countryPolicies = new ArrayList<>();

    private ArrayList<Fujian> mFujian = new ArrayList<>();//附件集合
    private TextView tv_show_fujian;
    private ListView lv_fujian;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_COUNTRYPOLICY_DETAIL:
                    countryPolicies = (ArrayList<String>) msg.obj;
                    rl_loading.setVisibility(View.GONE);
                    tv_countryPolicy_detail_title.setText(countryPolicies.get(0));
                    tv_countryPolicy_detail_comeFromAndDate.setText(countryPolicies.get(1));
                    tv_countryPolicy_detail_content.setText(countryPolicies.get(2));
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
            }
            return true;
        }
    });

    /**
     * 弹出底部对话框
     */
    public void openPop() {
        View popView = LayoutInflater.from(CountryPolicyDetailActivity.this).inflate(
                R.layout.pop_bottom, null);
        lv_fujian = popView.findViewById(R.id.lv_fujian);
        lv_fujian.setAdapter(new FuJianAdapter(CountryPolicyDetailActivity.this, mFujian));

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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//设置点击监听

                if (mFujian.size() == 1) {//只有一个附件时点击附件，附件消失
                    popupWindow.dismiss();//使popWindow消失
                }
                Intent intent = new Intent(CountryPolicyDetailActivity.this, AffixActivity.class);
                intent.putExtra(AffixActivity.AFFIX_URL, mFujian.get(position).getUrl());
                startActivity(intent);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_policy_detail);

        initView();
        mCountryPolicy_url = getIntent().getStringExtra(Constants.COUNTRYPOLICY_DETAIL_DETAIL_URL);
        getDataFromNet();//联网获取详情
    }

    private void getDataFromNet() {

        new Thread(new Runnable() {
            ArrayList<String> datas = new ArrayList<>();
            String inform_content = "";

            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(Constants.BASEURL + mCountryPolicy_url).get();
                    Element title_element = document.select("div.p20").first();
                    String title = title_element.getElementsByTag("strong").first().text();//抓取标题
                    String comeFromAndDate = document.select("span.p12").first().text();//抓取日期和来源
                    if ("发布日期".indexOf(comeFromAndDate) == -1) {
                        String[] strings = comeFromAndDate.split("发布日期");//将comeFromAndDate切割成两部分
                        comeFromAndDate = strings[0] + "\n" + "发布日期" + strings[1];
                    }
                    Element content_element = document.select("div.content").first();
                    Elements elements = content_element.getElementsByTag("p");
                    document.select("br").append("sout");//将页面中的<br>标签替换成“sout”，便于文本的排版

                    for (Element element : elements) {//循环获取内容
                        Fujian fujian = null;
                        for (int i = 0; i < element.select("a").size(); i++) {
                            String attr = element.select("a").get(i).attr("href");
                            String fujian_text = element.select("a").get(i).text();
                            if (!TextUtils.isEmpty(attr) && !"http://www.most.gov.cn/".equals(attr)
                                    && (attr.substring(attr.lastIndexOf(".")+1).equals("pdf")
                                    || attr.substring(attr.lastIndexOf(".")+1).equals("doc"))
                                    || attr.substring(attr.lastIndexOf(".")+1).equals("xls")) {
                                fujian = new Fujian();
                                fujian.setTitle(fujian_text);
                                fujian.setUrl(attr);
                                mFujian.add(fujian);
                            }
                        }

                        String content = element.text();
                        if ("sout".indexOf(content) == -1) {
                            String string = "";
                            String[] strings = content.split("sout");
                            for (String str : strings) {
                                string = string + str + "\n";
//                                System.out.println(str);
                            }
                            content = string;
                        }
                        inform_content = inform_content + content + "\n";
                    }

                    String[] split = inform_content.split("\n");
                    String strs = "";
                    for (int i = 0; i < split.length; i++) {
                        strs = strs + split[i];
                    }

                    if (TextUtils.isEmpty(strs)) {//网站格式不一致
                        inform_content = "";
                        Elements elements1 = content_element.select("div");
                        for (Element element : elements1) {//循环获取内容
                            Fujian fujian = null;
                            for (int i = 0; i < element.select("a").size(); i++) {
                                String attr = element.select("a").get(i).attr("href");
                                String fujian_text = element.select("a").get(i).text();
                                if (!TextUtils.isEmpty(attr) && !"http://www.most.gov.cn/".equals(attr)) {
                                    fujian = new Fujian();
                                    fujian.setTitle(fujian_text);
                                    fujian.setUrl(attr);
                                    mFujian.add(fujian);
                                }
                            }

                            String content = element.text();
//                            System.out.println(content);
                            if (TextUtils.isEmpty(content)) {
                                content = "" + "\n";
                            }

                            if (!"sout".contains(content)) {
                                String string = "";
                                String[] strings = content.split("sout");
                                for (String str : strings) {
                                    string = string + str + "\n";
                                }
                                content = string;
                            }

                            String s = "";
                            String[] strings = content.split("\\s+");//根据一个或多个空格切断
                            for (String str : strings) {
                                s = s + str + "\n" + "    ";
                            }
                            content = s;

                            inform_content = inform_content + "\n" + content;
                        }
                    }

//                    System.out.println(inform_content);
                    datas.add(title);
                    datas.add(comeFromAndDate);
                    datas.add(inform_content);
                    Message msg = Message.obtain();
                    msg.what = SHOW_COUNTRYPOLICY_DETAIL;
                    msg.obj = datas;
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    getDataFromNet();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initView() {
        tv_countryPolicy_detail_title = findViewById(R.id.tv_countryPolicy_detail_title);
        tv_countryPolicy_detail_comeFromAndDate = findViewById(R.id.tv_countryPolicy_detail_comeFromAndDate);
        tv_countryPolicy_detail_content = findViewById(R.id.tv_countryPolicy_detail_content);

        rl_loading = findViewById(R.id.rl_loading);

        tv_show_fujian = findViewById(R.id.tv_show_fujian);

    }
}
