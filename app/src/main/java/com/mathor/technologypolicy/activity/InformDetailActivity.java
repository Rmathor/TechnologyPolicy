package com.mathor.technologypolicy.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
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
import com.mathor.technologypolicy.utils.CacheUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 通知详情页面
 */
public class InformDetailActivity extends Activity {

    private TextView tv_inform_detail_title;//标题
    private TextView tv_inform_detail_comeFromAndDate;//来源和日期
    private TextView tv_inform_detail_content;//内容

    //加载中
    private RelativeLayout rl_loading;

    private ListView lv_fujian;

    private ArrayList<Fujian> mFujian = new ArrayList<>();//附件集合

    private TextView tv_show_fujian;

    private String mInform_url;//传过来的url

    private ArrayList<String> inform_detail = new ArrayList<>();

    private static final int SHOW_INFORM_DETAIL = 0x987;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_detail);

        initView();
        mInform_url = getIntent().getStringExtra(Constants.INFORM_DETAIL_URL);//获取传过来的url
        getDataFromNet();//联网获取详情
    }

    private void getDataFromNet() {

        new Thread(new Runnable() {
            ArrayList<String> datas = new ArrayList<>();
            String inform_content = "";

            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(Constants.BASEURL + mInform_url).get();
                    Element title_element = document.select("div.p20").first();
                    String title = title_element.getElementsByTag("strong").first().text();//抓取标题
                    String comeFromAndDate = document.select("span.p12").first().text();//抓取日期和来源
                    if (comeFromAndDate.contains("发布日期")) {

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
//                                System.out.println(str);
                            }
                            content = string;
                        }
                        inform_content = inform_content + content + "\n";
                    }
                    datas.add(title);
                    datas.add(comeFromAndDate);
                    datas.add(inform_content);
                    Message msg = Message.obtain();
                    msg.what = SHOW_INFORM_DETAIL;
                    msg.obj = datas;
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    getDataFromNet();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_INFORM_DETAIL:
                    inform_detail = (ArrayList<String>) msg.obj;
//                    Log.e("TAG", mFujian.size() + "");
                    rl_loading.setVisibility(View.GONE);
                    tv_inform_detail_title.setText(inform_detail.get(0));
                    tv_inform_detail_comeFromAndDate.setText(inform_detail.get(1));
                    tv_inform_detail_content.setText(inform_detail.get(2));
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
        View popView = LayoutInflater.from(InformDetailActivity.this).inflate(
                R.layout.pop_bottom, null);
        lv_fujian = popView.findViewById(R.id.lv_fujian);
        lv_fujian.setAdapter(new FuJianAdapter(InformDetailActivity.this, mFujian));

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
                Intent intent = new Intent(InformDetailActivity.this, AffixActivity.class);
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

    private void initView() {
        tv_inform_detail_title = findViewById(R.id.tv_inform_detail_title);
        tv_inform_detail_comeFromAndDate = findViewById(R.id.tv_inform_detail_comeFromAndDate);
        tv_inform_detail_content = findViewById(R.id.tv_inform_detail_content);

        tv_show_fujian = findViewById(R.id.tv_show_fujian);

        rl_loading = findViewById(R.id.rl_loading);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        System.out.println(requestCode);
        switch (requestCode) {
            case 1:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //已获取权限

                    //响应事件
                } else {
                    //权限被拒绝
                    CacheUtils.isGrantExternalRW(InformDetailActivity.this);
//                    Toast.makeText(this, "有权限被拒绝，可能会影响使用", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
//    public static String br2nl(String html) {
//        if (html == null) return html;
//        Document document = Jsoup.parse(html);
//        document.outputSettings(new
//                Document.OutputSettings().prettyPrint(false));
//        document.select("br").append("\\n");
//        document.select("p").prepend("\\n\\n");
//        String s =
//                document.html().replaceAll("\\\\n", "\n");
//        return Jsoup.clean(s, "",
//                Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
//    }
}
