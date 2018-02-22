package com.mathor.technologypolicy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.github.chrisbanes.photoview.PhotoView;
import com.mathor.technologypolicy.Constants;
import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.utils.BitmapCache;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 政策解读详情页面
 */
public class PolicyInterpretationDetailActivity extends Activity {

    private TextView tv_policy_interpretation_detail_title;
    private TextView tv_policy_interpretation_detail_comeFromAndDate;
    private TextView tv_policy_interpretation_detail_detail_content;
    private PhotoView iv_picture;
    private RelativeLayout rl_loading;

    private String mUrl;

    private ArrayList<String> datas = new ArrayList<>();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private ImageLoader.ImageListener mImageListener;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.GET_DATA:
                    datas = (ArrayList<String>) msg.obj;
                    initData();
                    break;
            }
            return true;
        }
    });

    private void initData() {

        tv_policy_interpretation_detail_title.setText(datas.get(0));
        tv_policy_interpretation_detail_comeFromAndDate.setText(datas.get(1) + "  " + "来源：科技部");
        if (TextUtils.isEmpty(datas.get(2))) {
            rl_loading.setVisibility(View.GONE);
            tv_policy_interpretation_detail_detail_content.setVisibility(View.VISIBLE);
            tv_policy_interpretation_detail_detail_content.setText(datas.get(3));
        } else {
            iv_picture.setVisibility(View.VISIBLE);
//            Glide.with(this)
//                    .load(mUrl.substring(0, mUrl.lastIndexOf("/")) + datas.get(2).substring(1))
//                    .skipMemoryCache(true)//跳过内存缓存
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过硬盘缓存
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                @Override
//                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                    Toast.makeText(getApplicationContext(),"资源加载异常",Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//                //这个用于监听图片是否加载完成
//                @Override
//                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
////                    Toast.makeText(getApplicationContext(),"图片加载完成",Toast.LENGTH_SHORT).show();
//                    rl_loading.setVisibility(View.GONE);
//                    if (iv_picture == null) {
//                        return false;
//                    }
//                    if (iv_picture.getScaleType() != ImageView.ScaleType.FIT_XY) {
//                        iv_picture.setScaleType(ImageView.ScaleType.FIT_XY);
//                    }
//                    ViewGroup.LayoutParams params = iv_picture.getLayoutParams();
//                    int vw = iv_picture.getWidth() - iv_picture.getPaddingLeft() - iv_picture.getPaddingRight();
//                    float scale = (float) vw / (float) resource.getIntrinsicWidth();
//                    int vh = Math.round(resource.getIntrinsicHeight() * scale);
//                    params.height = vh + iv_picture.getPaddingTop() + iv_picture.getPaddingBottom();
//                    iv_picture.setLayoutParams(params);
//                    return false;
//                }
//            }).into(iv_picture);
            mRequestQueue = Volley.newRequestQueue(PolicyInterpretationDetailActivity.this);
            mImageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
            mImageListener = ImageLoader.getImageListener(iv_picture, R.mipmap.picture_loading, R.mipmap.picture_error);
            mImageLoader.get(mUrl.substring(0, mUrl.lastIndexOf("/")) + datas.get(2).substring(1), mImageListener);
            rl_loading.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_interpretation_detail);

        initVIew();
        mUrl = getIntent().getStringExtra(Constants.POLICY_INTERPRETATION_DETAIL_URL);
        getDetailFromNet();
    }

    private void getDetailFromNet() {

        final ArrayList<String> datas = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Log.e("TAG",mUrl);
                    Document document = Jsoup.connect(mUrl).get();
                    String title = document.getElementById("Title").text();
                    String dateAndFrom = document.select("div.gray12.lh22").first().text();
                    Element element = document.select("div.trshui13.lh22").first();
                    document.select("br").append("sout");//将页面中的<br>标签替换成“sout”，便于文本的排版
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
                    String picture_url = "";
                    if (element.getElementsByTag("p").size() > 2) {//是否为纯图片
                        Element element1 = element.getElementsByTag("p").get(2);
                        picture_url = element1.getElementsByTag("img").attr("src");
                    }
                    datas.add(title);
                    datas.add(dateAndFrom);
                    datas.add(picture_url);
                    datas.add(content);
                    Message msg = Message.obtain();
                    msg.what = Constants.GET_DATA;
                    msg.obj = datas;
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initVIew() {
        tv_policy_interpretation_detail_title = findViewById(R.id.tv_policy_interpretation_detail_title);
        tv_policy_interpretation_detail_comeFromAndDate = findViewById(R.id.tv_policy_interpretation_detail_comeFromAndDate);
        tv_policy_interpretation_detail_detail_content = findViewById(R.id.tv_policy_interpretation_detail_detail_content);

        iv_picture = findViewById(R.id.iv_picture);
        rl_loading = findViewById(R.id.rl_loading);

    }
}
