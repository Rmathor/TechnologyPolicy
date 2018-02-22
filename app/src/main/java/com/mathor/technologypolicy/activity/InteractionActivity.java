package com.mathor.technologypolicy.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.mathor.technologypolicy.R;

/**
 * 互动activity
 */
public class InteractionActivity extends Activity {

    private WebView wv_interaction;
    private LinearLayout ll_interaction_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interaction);

        initView();

        WebSettings webSettings = wv_interaction.getSettings();

        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        wv_interaction.loadUrl("http://hdapp.gdstc.gov.cn/mobile/index.jsp");

        //设置不用系统浏览器打开,直接显示在当前Webview
//        wv_interaction.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });

        //设置WebViewClient类
        wv_interaction.setWebViewClient(new WebViewClient() {
            //设置加载前的函数
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                Log.e("TAG","onPageStarted");
                ll_interaction_loading.setVisibility(View.VISIBLE);
//                System.out.println("开始加载了");
            }

            //设置结束加载函数
            @Override
            public void onPageFinished(WebView view, String url) {
                ll_interaction_loading.setVisibility(View.GONE);
//                Log.e("TAG","onPageFinished");
            }

            //设置不用系统浏览器打开,直接显示在当前Webview
            @Override
            public void onLoadResource(WebView view, String url) {
                if (url.equals("http://app.gdstc.gov.cn/app/h5/index.jsp")) {
                    url = "http://hdapp.gdstc.gov.cn/mobile/index.jsp";
                    view.stopLoading();
                    view.loadUrl(url);
                }
            }
        });
    }

    private void initView() {
        wv_interaction = (WebView) findViewById(R.id.wv_interaction);
        ll_interaction_loading = (LinearLayout) findViewById(R.id.ll_interaction_loading);
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (wv_interaction != null) {
            wv_interaction.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            wv_interaction.clearHistory();

            ((ViewGroup) wv_interaction.getParent()).removeView(wv_interaction);
            wv_interaction.destroy();
            wv_interaction = null;
        }
    }
}
