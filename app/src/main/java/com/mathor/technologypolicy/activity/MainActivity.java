package com.mathor.technologypolicy.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RadioGroup;

import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.fragment.HomeFragment;
import com.mathor.technologypolicy.fragment.SearchFragment;
import com.mathor.technologypolicy.fragment.SettingFragment;
import com.mathor.technologypolicy.fragment.TechnologyFragment;

/**
 * 主页面
 */
public class MainActivity extends Activity {

    private RadioGroup rg_main;
    private HomeFragment mHomeFragment;//主页
    private TechnologyFragment mTechnologyFragment;//科技
    private SearchFragment mSearchFragment;//搜索
    private SettingFragment mSettingFragment;//设置
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initListener();
    }

    private void initListener() {
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        mFragment = mHomeFragment;
                        break;
                    case R.id.rb_objects:
                        mFragment = mTechnologyFragment;
                        break;
                    case R.id.rb_search:
                        mFragment = mSearchFragment;
                        break;
                    case R.id.rb_setting:
                        mFragment = mSettingFragment;
                        break;
                }
                //实现fragment的切换
                switchFragment(mFragment);
            }
        });
        //设置默认选中的fragment
        rg_main.check(R.id.rb_home);
    }

    //实现fragment的切换
    private void switchFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.fl_content, fragment).commit();
    }

    private void initData() {
        mHomeFragment = new HomeFragment(MainActivity.this);
        mTechnologyFragment = new TechnologyFragment(MainActivity.this);
        mSearchFragment = new SearchFragment(MainActivity.this);
        mSettingFragment = new SettingFragment(MainActivity.this);
    }

    private void initView() {
        rg_main = (RadioGroup) findViewById(R.id.rg_main);
    }

    private long exitTime = 0;

//    /**
//     * 连续按两次退出
//     *
//     * @param keyCode
//     * @param event
//     * @return
//     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if (System.currentTimeMillis() - exitTime > 2000) {
//                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
//                exitTime = System.currentTimeMillis();
//            } else {
//                finish();
//                System.exit(0);
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

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
//                    CacheUtils.isGrantExternalRW(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                break;
        }
    }

    //点击返回键返回桌面而不是退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
