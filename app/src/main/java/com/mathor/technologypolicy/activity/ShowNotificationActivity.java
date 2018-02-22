package com.mathor.technologypolicy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.mathor.technologypolicy.R;

import cn.jpush.android.api.JPushInterface;

/**
 * 显示普通通知页面
 */
public class ShowNotificationActivity extends Activity {

    private TextView tv_show_notification_title;
    private TextView tv_show_notification_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notification);

        initView();
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = getIntent().getExtras();
            String title = null;
            String content = null;
            if (bundle != null) {
                title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                content = bundle.getString(JPushInterface.EXTRA_ALERT);
            }
            tv_show_notification_title.setText(title);//标题
            tv_show_notification_content.setText(content);//内容
        }
    }

    private void initView() {
        tv_show_notification_title = findViewById(R.id.tv_show_notification_title);
        tv_show_notification_content = findViewById(R.id.tv_show_notification_content);
    }
}
