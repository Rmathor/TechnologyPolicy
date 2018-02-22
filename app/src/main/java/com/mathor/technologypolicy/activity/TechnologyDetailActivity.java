package com.mathor.technologypolicy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.mathor.technologypolicy.Constants;
import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.utils.BitmapCache;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class TechnologyDetailActivity extends Activity {

    private static final int SHOW_TECHNOLOGY = 0x6789;

    private TextView tv_technology_detail_title;
    private TextView tv_technology_detail_comeFromAndDate;
    private ImageView iv_picture;
    private TextView tv_technology_detail_content;
    private RelativeLayout rl_loading;
    private TextView tv_technology_daodu;

    private String mUrl;
    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;
    private ImageLoader.ImageListener mImageListener;

    private ArrayList<String> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technology_detail);

        initView();
        mUrl = getIntent().getStringExtra(Constants.TECHNOLOGY_DETAIL_URL);
        getTechnologyDetailFromNet();
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_TECHNOLOGY:
                    datas = (ArrayList<String>) msg.obj;
                    initData();
                    break;
            }
            return true;
        }
    });

    private void initData() {

        rl_loading.setVisibility(View.GONE);
        tv_technology_detail_title.setText(datas.get(0));
        tv_technology_detail_comeFromAndDate.setText(datas.get(1));
        tv_technology_daodu.setText(datas.get(2));
        tv_technology_detail_content.setText(datas.get(4));
        if (!TextUtils.isEmpty(datas.get(3))){
            final String base_url = mUrl.substring(0,mUrl.lastIndexOf("/"));
            iv_picture.setVisibility(View.VISIBLE);
            mRequestQueue = Volley.newRequestQueue(TechnologyDetailActivity.this);
            mImageLoader = new ImageLoader(mRequestQueue,new BitmapCache());
            mImageListener = ImageLoader.getImageListener(iv_picture,R.mipmap.picture_loading,R.mipmap.picture_error);
            mImageLoader.get(base_url+datas.get(3).substring(1),mImageListener);
            iv_picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TechnologyDetailActivity.this,ShowBigPictureActivity.class);
                    intent.putExtra("picture_url",base_url+datas.get(3).substring(1));
                    startActivity(intent);
                }
            });
        }
    }

    private void getTechnologyDetailFromNet() {

        final ArrayList<String> datas = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(mUrl).get();
//                    System.out.println(document);
                    Element element1 = document.getElementById("artibodyTitle");
                    String title = element1.getElementsByTag("h1").text();
                    String origin = document.select("span.origin").text();
                    String author = document.select("span.author").text();
                    String pub_date = document.getElementById("pub_date").text();
                    String ddfun = document.select("div.ddfun").text();
//                    String picture_url = document.select("img").attr("src");
                    Element element = document.select("div.TRS_Editor").first();
                    Elements elements = element.getElementsByTag("p");
                    String picture_url = "";
                    String content = "";
                    for (int i = 0;i<elements.size();i++){
                       String url =  elements.get(i).getElementsByTag("img").attr("src");
                        if (!TextUtils.isEmpty(url)){
                            picture_url = url;
                        }
                        content = content+"    "+elements.get(i).text()+"\n";
                    }
                    datas.add(title);
                    datas.add(origin+"  "+author+"\n"+pub_date);
                    datas.add(ddfun);
                    datas.add(picture_url);
                    datas.add(content);
                    Message msg = Message.obtain();
                    msg.obj = datas;
                    msg.what = SHOW_TECHNOLOGY;
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    getTechnologyDetailFromNet();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initView() {
        tv_technology_detail_title = findViewById(R.id.tv_technology_detail_title);
        tv_technology_detail_comeFromAndDate = findViewById(R.id.tv_technology_detail_comeFromAndDate);
        tv_technology_detail_content = findViewById(R.id.tv_technology_detail_content);
        tv_technology_daodu = findViewById(R.id.tv_technology_detail_daodu);

        iv_picture = findViewById(R.id.iv_picture);
        rl_loading = findViewById(R.id.rl_loading);
    }

}
