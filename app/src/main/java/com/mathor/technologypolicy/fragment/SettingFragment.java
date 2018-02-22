package com.mathor.technologypolicy.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.activity.AboutActivity;
import com.mathor.technologypolicy.activity.InteractionActivity;
import com.mathor.technologypolicy.activity.TuiSongActivity;
import com.mathor.technologypolicy.db.DataBaseAdapter;
import com.mathor.technologypolicy.domain.UserInfo;
import com.mathor.technologypolicy.utils.CacheDataManager;
import com.mathor.technologypolicy.utils.CacheUtils;
import com.mathor.technologypolicy.utils.MyUtils;
import com.mathor.technologypolicy.view.RoundCornersImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import cn.jpush.android.api.JPushInterface;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Author: mathor
 * Date : on 2017/11/14 10:47
 */

public class SettingFragment extends Fragment implements View.OnClickListener {

    private static final int LOGIN_SUCCESS = 0x986;
    private static final String IS_LOGIN = "is_login";
    private DataBaseAdapter mDataBaseAdapter;//数据库操作类

    private Context mContext;//上下文

    @ViewInject(R.id.tv_setting_interaction)
    private TextView tv_setting_interaction;//互动

    @ViewInject(R.id.ll_setting_cache)
    private LinearLayout ll_setting_cache;//清除缓存

    @ViewInject(R.id.tv_setting_cache)
    private TextView tv_setting_cache;//缓存大小

    @ViewInject(R.id.tv_setting_about)
    private TextView tv_setting_about;//关于

    @ViewInject(R.id.rciv_icon)
    private RoundCornersImageView rciv_icon;//头像

    @ViewInject(R.id.tv_user_name)
    private TextView tv_user_name;//昵称

    @ViewInject(R.id.ll_setting_inform)
    private LinearLayout ll_setting_inform;

    @ViewInject(R.id.btn_setting_exit)
    private Button btn_setting_exit;//退出登录

    @ViewInject(R.id.tv_setting_tuisong)
    private TextView tv_setting_tuisong;//推送定制

    /**
     * 本地图片返回码
     */
    public static final int RESULT_IMAGE = 100;
    /**
     * 本地图片类型
     */
    public static final String IMAGE_TYPE = "image/*";

    public static String TEMP_IMAGE_PATH;//拍照保存的头像部分路径
    /**
     * 相机的返回码
     */
    public static final int RESULT_CAMERA = 200;

    //獲取系統版本
    int currentapiVersion = android.os.Build.VERSION.SDK_INT;

    /**
     * 保存照片的文件
     */
    private File tempFile;

    private boolean islogin = false;//是否登录

    private String mPhone;//本机电话号码

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_SUCCESS:
                    btn_setting_exit.setVisibility(View.VISIBLE);
                    tv_setting_tuisong.setVisibility(View.VISIBLE);
                    JPushInterface.resumePush(mContext);//启动推送
                    UserInfo userInfo = mDataBaseAdapter.findByPhone_num(mPhone);
                    tv_user_name.setText(userInfo.getName());
                    if (userInfo.getIcon_path() != null) {

                        rciv_icon.setImageBitmap(BitmapFactory.decodeFile(userInfo.getIcon_path()));
                    }
                    break;
            }
            return true;
        }
    });

    public SettingFragment() {
    }

    @SuppressLint("ValidFragment")
    public SettingFragment(Context context) {

        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.e("TAG", "onCreateView");

        View view = View.inflate(mContext, R.layout.fragment_setting, null);
        x.view().inject(SettingFragment.this, view);

        mDataBaseAdapter = new DataBaseAdapter(mContext);
        btn_setting_exit.setVisibility(View.GONE);
        tv_setting_tuisong.setVisibility(View.GONE);

        //判断是否登录
        islogin = CacheUtils.getBoolean(mContext, IS_LOGIN);
        if (islogin) {
            mPhone = CacheUtils.getString(mContext, IS_LOGIN);
            if (mPhone != null) {
                if (mDataBaseAdapter.findByPhone_num(mPhone).getIcon_path() != null) {
                    rciv_icon.setImageBitmap(BitmapFactory.decodeFile(mDataBaseAdapter.findByPhone_num(mPhone).getIcon_path()));
                }
                tv_user_name.setText(mDataBaseAdapter.findByPhone_num(mPhone).getName());
                btn_setting_exit.setVisibility(View.VISIBLE);
                tv_setting_tuisong.setVisibility(View.VISIBLE);
            }
        }

        rciv_icon.setRadius(200, 200);//设置圆形头像

        TEMP_IMAGE_PATH = getFilePath(mContext, "/iconPic");

        //设置监听
        setListener();

        try {
            MyUtils.getPermission(getActivity());
            String cacheSize = CacheDataManager.getTotalCacheSize(mContext);//获取下载文件大小
            tv_setting_cache.setText(cacheSize + "MB");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * @param context 上下文对象
     * @param dir     存储目录
     * @return
     */
    public static String getFilePath(Context context, String dir) {
        String directoryPath = "";
        //判断SD卡是否可用
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            directoryPath = context.getExternalFilesDir(dir).getAbsolutePath();

        } else {
            //没内存卡就存机身内存
            directoryPath = context.getFilesDir() + dir;

        }
        File file = new File(directoryPath);
        if (!file.exists()) {//判断文件目录是否存在
            file.mkdirs();
        }
//        Log.e("TAG","filePath====>" + directoryPath);
        return directoryPath;
    }

    private void setListener() {
        rciv_icon.setOnClickListener(this);
        tv_user_name.setOnClickListener(this);
        tv_setting_interaction.setOnClickListener(this);
        ll_setting_cache.setOnClickListener(this);
        tv_setting_about.setOnClickListener(this);
        btn_setting_exit.setOnClickListener(this);
        tv_setting_tuisong.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {

        switch (v.getId()) {
            case R.id.tv_setting_interaction:
                Intent intent = new Intent(mContext, InteractionActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.ll_setting_cache:
                CacheDataManager.clearAllCache();
                try {
                    MyUtils.getPermission(getActivity());
                    String cacheSize = CacheDataManager.getTotalCacheSize(mContext);
                    tv_setting_cache.setText(cacheSize + "MB");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (tv_setting_cache.getText().toString().equals("0MB")) {
                    Toast.makeText(mContext, "已清除.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_setting_about:
                Intent intent1 = new Intent(mContext, AboutActivity.class);
                mContext.startActivity(intent1);
                break;
            case R.id.rciv_icon:
                if (islogin) {

                    showDialogCustom();
                } else {

                    //权限验证（根据官网提供的实例）
                    if (Build.VERSION.SDK_INT >= 23) {
                        int readPhone = mContext.checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
                        int receiveSms = mContext.checkSelfPermission(Manifest.permission.RECEIVE_SMS);
                        int readSms = mContext.checkSelfPermission(Manifest.permission.READ_SMS);
                        int readContacts = mContext.checkSelfPermission(Manifest.permission.READ_CONTACTS);
                        int readSdcard = mContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

                        int requestCode = 0;
                        ArrayList<String> permissions = new ArrayList<String>();
                        if (readPhone != PackageManager.PERMISSION_GRANTED) {
                            requestCode |= 1 << 0;
                            permissions.add(Manifest.permission.READ_PHONE_STATE);
                        }
                        if (receiveSms != PackageManager.PERMISSION_GRANTED) {
                            requestCode |= 1 << 1;
                            permissions.add(Manifest.permission.RECEIVE_SMS);
                        }
                        if (readSms != PackageManager.PERMISSION_GRANTED) {
                            requestCode |= 1 << 2;
                            permissions.add(Manifest.permission.READ_SMS);
                        }
                        if (readContacts != PackageManager.PERMISSION_GRANTED) {
                            requestCode |= 1 << 3;
                            permissions.add(Manifest.permission.READ_CONTACTS);
                        }
                        if (readSdcard != PackageManager.PERMISSION_GRANTED) {
                            requestCode |= 1 << 4;
                            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                        }
                        if (requestCode > 0) {
                            String[] permission = new String[permissions.size()];
                            this.requestPermissions(permissions.toArray(permission), requestCode);
                            return;
                        }
                    }
                    Login();
                }
                break;
            case R.id.tv_user_name:
                if (islogin) {
                    updateUserName();
                } else {
                    Login();
                }
                break;
            case R.id.btn_setting_exit:
                islogin = false;
                CacheUtils.putBoolean(mContext, IS_LOGIN, islogin);
                rciv_icon.setImageResource(R.mipmap.iv_icon2);
                tv_user_name.setText("点击登录");
                btn_setting_exit.setVisibility(View.GONE);
                tv_setting_tuisong.setVisibility(View.GONE);
                JPushInterface.stopPush(mContext);//停止推送
                break;
            case R.id.tv_setting_tuisong:
                Intent intent2 = new Intent(mContext, TuiSongActivity.class);
                intent2.putExtra("phone", mPhone);
                startActivity(intent2);
                break;
        }
    }

    //修改昵称
    private void updateUserName() {
        final View view = View.inflate(mContext, R.layout.user_name_view, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et_user_name = view.findViewById(R.id.et_user_name);
                        String user_name = et_user_name.getText().toString().trim();
                        if (TextUtils.isEmpty(user_name)) {
                            Toast.makeText(mContext, "昵称为空！", Toast.LENGTH_SHORT).show();
                            updateUserName();
                        } else {
                            tv_user_name.setText(user_name);
                            UserInfo userInfo = mDataBaseAdapter.findByPhone_num(mPhone);
                            if (user_name != null) {
                                userInfo.setName(user_name);
                            }
                            mDataBaseAdapter.updateByPhone_num(userInfo);
                            Toast.makeText(mContext, "修改成功！", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("取消", null);
        builder.show();
    }

    //手机验证
    private void Login() {
        //打开注册界面
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {// 事件调用监听类

            @Override
            public void afterEvent(int event, int result, Object data) {//短信SDK操作回调
                super.afterEvent(event, result, data);

                //解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {// 如果状态为完成状态,表示注册成功
                    // 获取Data中的数据
                    HashMap<String, Object> dataMaps = (HashMap<String, Object>) data;

                    // 获取手机号所在国家信息
                    String country = (String) dataMaps.get("country");
                    // 获取收到验证码的手机号码
                    mPhone = (String) dataMaps.get("phone");

                    // 提交信息到mob注册
                    submitInfo(country, mPhone);

                    islogin = true;
                    CacheUtils.putBoolean(mContext, IS_LOGIN, islogin);
                    CacheUtils.putString(mContext, IS_LOGIN, mPhone);
                    mHandler.sendEmptyMessage(LOGIN_SUCCESS);
                }

            }
        });

        // 第三步：显示注册界面
        registerPage.show(mContext);
    }

    //提交信息
    private void submitInfo(String country, String phone) {
        UserInfo userInfo = mDataBaseAdapter.findByPhone_num(phone);
        if (userInfo != null) {
            SMSSDK.submitUserInfo(userInfo.getId() + "", userInfo.getName(), null, country, phone);
        } else {

            Random r = new Random();
            String uid = Math.abs(r.nextInt()) + "";
            String nickName = "user_" + uid;
            UserInfo userInfo1 = new UserInfo();
            userInfo1.setName(nickName);
            userInfo1.setPhone_num(phone);
            mDataBaseAdapter.add(userInfo1);
            SMSSDK.submitUserInfo(uid, nickName, null, country, phone);// 提交用户信息，在监听中返回
        }
    }

    /**
     * 显示弹框选择头像
     */
    private void showDialogCustom() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("选择：")
                .setItems(new CharSequence[]{"本地图册", "相机拍照"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //安卓6.0以上动态获取sdcard读写权限
                        MyUtils.getPermission(getActivity());
                        switch (which) {
                            case 0://本地图册
//                                Toast.makeText(PuzzleMain.this, "本地图册", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_TYPE);
                                startActivityForResult(intent, RESULT_IMAGE);
                                break;
                            case 1://相机拍照
//                                Toast.makeText(PuzzleMain.this, "相机拍照", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                tempFile = new File(TEMP_IMAGE_PATH + "/tp_icon_" + mPhone + ".png");
                                if (currentapiVersion < 24) {
                                    // 从文件中创建uri
                                    Uri uri = Uri.fromFile(tempFile);
                                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                } else {
                                    //兼容android7.0 使用共享文件的形式
                                    ContentValues contentValues = new ContentValues(1);
                                    contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
                                    Uri uri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                }
                                startActivityForResult(intent1, RESULT_CAMERA);
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_IMAGE && data != null) {
                //相册
                Cursor cursor = mContext.getContentResolver().query(data.getData(), null, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex("_data"));
                UserInfo userInfo = mDataBaseAdapter.findByPhone_num(mPhone);
                userInfo.setIcon_path(imagePath);
                mDataBaseAdapter.updateByPhone_num(userInfo);
                rciv_icon.setImageBitmap(BitmapFactory.decodeFile(imagePath));

            } else if (requestCode == RESULT_CAMERA) {
                //相机
                UserInfo userInfo = mDataBaseAdapter.findByPhone_num(mPhone);
                userInfo.setIcon_path(TEMP_IMAGE_PATH + "/tp_icon_" + mPhone + ".png");
                mDataBaseAdapter.updateByPhone_num(userInfo);
                rciv_icon.setImageBitmap(BitmapFactory.decodeFile(TEMP_IMAGE_PATH + "/tp_icon_" + mPhone + ".png"));
            }
            Toast.makeText(mContext, "修改成功！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();//反注册代码，避免内存泄漏
    }
}
