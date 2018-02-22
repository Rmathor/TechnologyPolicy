package com.mathor.technologypolicy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.db.DataBaseAdapter;
import com.mathor.technologypolicy.domain.UserInfo;
import com.mathor.technologypolicy.receiver.ExampleUtil;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 推送定制页面
 */
public class TuiSongActivity extends Activity {

    private static final int MSG_SET_TAG = 0x546;

    private static final String TAG = TuiSongActivity.class.getSimpleName();

    private ImageButton ib_tuisong;

    private CheckBox cb_tuisong_test1;//科技
    private CheckBox cb_tuisong_test2;//政策
    private CheckBox cb_tuisong_test3;//通知

    private String mPhone;//本机号码

    private DataBaseAdapter mDataBaseAdapter;//数据库操作类

    private String mTag = "";//设备别名

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SET_TAG:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tui_song);

        initView();

        mPhone = getIntent().getStringExtra("phone");//获取传过来的参数

        initData();

        ib_tuisong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_tuisong_test1.isChecked()) {
                    if (cb_tuisong_test2.isChecked()) {
                        if (cb_tuisong_test3.isChecked()) {
                            mTag = cb_tuisong_test1.getText() + "_" + cb_tuisong_test2.getText() + "_" + cb_tuisong_test3.getText();
                        } else {
                            mTag = cb_tuisong_test1.getText() + "_" + cb_tuisong_test2.getText();
                        }
                    } else {
                        if (cb_tuisong_test3.isChecked()) {
                            mTag = cb_tuisong_test1.getText() + "_" + cb_tuisong_test3.getText();
                        } else {
                            mTag = cb_tuisong_test1.getText() + "";
                        }
                    }
                } else {
                    if (cb_tuisong_test2.isChecked()) {
                        if (cb_tuisong_test3.isChecked()) {
                            mTag = cb_tuisong_test2.getText() + "_" + cb_tuisong_test3.getText();
                        } else {
                            mTag = cb_tuisong_test2.getText() + "";
                        }
                    } else {
                        if (cb_tuisong_test3.isChecked()) {
                            mTag = cb_tuisong_test3.getText() + "";
                        } else {
                            mTag = "";
                        }
                    }
                }
                mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAG, mTag));
            }
        });
    }

    private void initData() {
        mDataBaseAdapter = new DataBaseAdapter(TuiSongActivity.this);
        UserInfo userInfo = mDataBaseAdapter.findByPhone_num(mPhone);
        String tuisong_tag = userInfo.getTuisong_tag();
        if (tuisong_tag != null) {

            if (!tuisong_tag.contains("_")) {
                if (tuisong_tag.equals(cb_tuisong_test1.getText())) {
                    cb_tuisong_test1.setChecked(true);
                } else if (tuisong_tag.equals(cb_tuisong_test2.getText())) {
                    cb_tuisong_test2.setChecked(true);
                } else if (tuisong_tag.equals(cb_tuisong_test3.getText())) {
                    cb_tuisong_test3.setChecked(true);
                }
            } else {
                String[] tags = tuisong_tag.split("_");
                for (String tag : tags) {
                    if (tag.equals(cb_tuisong_test1.getText())) {
                        cb_tuisong_test1.setChecked(true);
                    } else if (tag.equals(cb_tuisong_test2.getText())) {
                        cb_tuisong_test2.setChecked(true);
                    } else if (tag.equals(cb_tuisong_test3.getText())) {
                        cb_tuisong_test3.setChecked(true);
                    }
                }
            }
        }
    }

    private void initView() {
        ib_tuisong = (ImageButton) findViewById(R.id.ib_tuisong);
        cb_tuisong_test1 = (CheckBox) findViewById(R.id.cb_tuisong_test1);
        cb_tuisong_test2 = (CheckBox) findViewById(R.id.cb_tuisong_test2);
        cb_tuisong_test3 = (CheckBox) findViewById(R.id.cb_tuisong_test3);
    }

    //回调接口
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "修改成功！";
                    Log.i(TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    UserInfo userInfo = mDataBaseAdapter.findByPhone_num(mPhone);
                    userInfo.setTuisong_tag(mTag);
                    mDataBaseAdapter.updateByPhone_num(userInfo);
//                    Toast.makeText(TuiSongActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 6002:
                    logs = "修改超时，延迟60秒重试.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAG, alias), 1000 * 60);
                    break;
                default:
                    logs = "错误码 = " + code;
                    Log.e(TAG, logs);
            }
            ExampleUtil.showToast(logs, getApplicationContext());
        }
    };
}
