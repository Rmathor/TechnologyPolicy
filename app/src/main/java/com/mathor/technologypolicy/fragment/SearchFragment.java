package com.mathor.technologypolicy.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mathor.technologypolicy.Constants;
import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.activity.SearchDetailActivity;
import com.mathor.technologypolicy.adapter.SearchAdapter;
import com.mathor.technologypolicy.domain.Search;
import com.mathor.technologypolicy.view.CustomSwipeRL;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Author: mathor
 * Date : on 2017/11/12 9:52
 * 搜索fragment
 */
public class SearchFragment extends Fragment implements View.OnClickListener {

    private Context mContext;

    @ViewInject(R.id.et_search_fragment)
    private EditText et_search_fragment;//输入框

    @ViewInject(R.id.ib_search_fragment)
    private ImageButton ib_search_fragment;//搜索按钮

    @ViewInject(R.id.tv_search_total)
    private TextView tv_search_total;//搜索结果总数

    @ViewInject(R.id.lv_search_fragment)
    private ListView lv_search_fragment;//ListView

    @ViewInject(R.id.customSRL_search_fragment)
    private CustomSwipeRL customSRL_search_fragment;//刷新控件

    @ViewInject(R.id.rl_connect_error)
    private RelativeLayout rl_connect_error;//搜索超时

    @ViewInject(R.id.ll_search_hot_word)
    private LinearLayout ll_search_hot_word;//热搜

    @ViewInject(R.id.tv_search_1)
    private TextView tv_search_1;//热搜词一

    @ViewInject(R.id.tv_search_2)
    private TextView tv_search_2;

    @ViewInject(R.id.tv_search_3)
    private TextView tv_search_3;

    @ViewInject(R.id.tv_search_4)
    private TextView tv_search_4;

    private ArrayList<Search.Data> mDataList;//搜索结果数据集合
    private SearchAdapter mSearchAdapter;//适配器
    private int pageIndex = 0;//当前页数
    private String mInputText;//输入的关键字
    private InputMethodManager mInputMethodManager;//软键盘控制者

    public SearchFragment() {
    }

    @SuppressLint("ValidFragment")
    public SearchFragment(Context context) {

        this.mContext = context;
        mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.e("TAG", "onCreate");
    }

    @Override
    public void onStart() {
//        Log.e("TAG", "onStart");
        super.onStart();
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.GET_DATA:
                    String data = (String) msg.obj;
                    Search search = new Gson().fromJson(data, Search.class);//用Gson解析数据
                    if (search != null) {
                        mDataList = search.getData();
                        if (mDataList != null && mDataList.size() > 0) {
                            tv_search_total.setText("共搜索到：" + search.getTotal() + "条数据");
                            mSearchAdapter = new SearchAdapter(mContext, mDataList);
                            lv_search_fragment.setAdapter(mSearchAdapter);//设置适配器
                            customSRL_search_fragment.setRefreshing(false);//关闭刷新
                            customSRL_search_fragment.setEnabled(false);//禁止下拉刷新
                            et_search_fragment.setText("");//清空输入框
                            mInputMethodManager.hideSoftInputFromWindow(et_search_fragment.getWindowToken(), 0);//强制软键盘隐藏
                            Toast.makeText(mContext, "搜索成功.", Toast.LENGTH_SHORT).show();
                        } else {
                            mDataList = new ArrayList<>();
                            mSearchAdapter = new SearchAdapter(mContext, mDataList);
                            lv_search_fragment.setAdapter(mSearchAdapter);//设置适配器
                            customSRL_search_fragment.setRefreshing(false);
                            customSRL_search_fragment.setEnabled(false);
                            tv_search_total.setText("");
                            et_search_fragment.setText("");
                            ll_search_hot_word.setVisibility(View.VISIBLE);
                            Toast.makeText(mContext, "没有匹配的内容.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        tv_search_total.setText("");
                        customSRL_search_fragment.setRefreshing(false);
                        customSRL_search_fragment.setEnabled(false);
                        mInputMethodManager.hideSoftInputFromWindow(et_search_fragment.getWindowToken(), 0);//强制软键盘隐藏
                        rl_connect_error.setVisibility(View.VISIBLE);
                    }

                    break;
                case Constants.GET_DATA_MORE:
                    data = (String) msg.obj;
                    Search search_more = new Gson().fromJson(data, Search.class);//用Gson解析数据
                    if (!search_more.isSuccess()) {
                        customSRL_search_fragment.setLoading(false);
                        Toast.makeText(mContext, "没有更多了.", Toast.LENGTH_SHORT).show();
                    } else {
                        mDataList.addAll(search_more.getData());
                        mSearchAdapter.notifyDataSetChanged();
                        customSRL_search_fragment.setLoading(false);
                        Toast.makeText(mContext, "已加载更多.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return true;
        }
    });

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Log.e("TAG", "onCreateView");
        final View view = View.inflate(mContext, R.layout.fragment_search, null);
        x.view().inject(SearchFragment.this, view);

        lv_search_fragment.setDivider(new ColorDrawable(Color.GRAY));
        lv_search_fragment.setDividerHeight(1);

        et_search_fragment.setFocusable(true);//设置可以获取焦点
        et_search_fragment.setFocusableInTouchMode(true);
        et_search_fragment.requestFocus();//请求焦点
        Timer timer = new Timer();//必须延迟显示软键盘，否则无法显示
        timer.schedule(new TimerTask() { //让软键盘延时弹出，以更好的加载Activity
            public void run() {
                mInputMethodManager.showSoftInput(et_search_fragment, 0);
            }
        }, 100);

        lv_search_fragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = mDataList.get(position).getUrl();
                Intent intent = new Intent(mContext, SearchDetailActivity.class);
                intent.putExtra(SearchDetailActivity.SEARCH_DETAIL_URL, url);
                mContext.startActivity(intent);
            }
        });

        //为热搜添加点击事件
        tv_search_1.setOnClickListener(this);
        tv_search_2.setOnClickListener(this);
        tv_search_3.setOnClickListener(this);
        tv_search_4.setOnClickListener(this);

        customSRL_search_fragment.setEnabled(false);//关闭刷新

        customSRL_search_fragment.setOnLoadListener(new CustomSwipeRL.OnLoadListener() {
            @Override
            public void onLoad() {

                getMoreData(mInputText);
            }
        });

        ib_search_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rl_connect_error.isEnabled()) {
                    rl_connect_error.setVisibility(View.GONE);
                }

                mInputText = et_search_fragment.getText().toString().trim();
                if (TextUtils.isEmpty(mInputText)) {
                    Toast.makeText(mContext, "关键字为空！", Toast.LENGTH_SHORT).show();
                } else {
                    customSRL_search_fragment.post(new Runnable() {
                        @Override
                        public void run() {
                            customSRL_search_fragment.setEnabled(true);
//                            customSRL_search_fragment.setLoading(false);
                            customSRL_search_fragment.setRefreshing(true);
                        }
                    });
                    ll_search_hot_word.setVisibility(View.GONE);//隐藏热搜
                    getDataFromNet(mInputText);
                }
            }
        });
        return view;
    }

    private void getMoreData(final String key) {

        pageIndex++;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<>();//post请求的参数
                params.put("clsid", "1010,1401,1710,1060,1556");
                params.put("page", pageIndex + "");//当前页数
                params.put("key", key);//关键字
                params.put("limit", "10");//搜索数量
                String result = sendPostMessage(params, "utf-8");
                result = decode(result);
                String[] strings = result.split("<font color='red'>");
                StringBuffer sb = new StringBuffer();
                for (String s : strings) {
                    sb.append(s);
                }
                result = String.valueOf(sb);
                strings = result.split("<\\\\/font>");
//                System.out.println(strings.length);
                StringBuffer sb2 = new StringBuffer();
                for (String s : strings) {
                    sb2.append(s);
                }
                result = String.valueOf(sb2);
                Message msg = Message.obtain();
                msg.what = Constants.GET_DATA_MORE;
                msg.obj = result;
                mHandler.sendMessageDelayed(msg, 1000);
            }
        }).start();
    }

    private void getDataFromNet(final String key) {
        pageIndex = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<>();
                params.put("clsid", "1010,1401,1710,1060,1556");
                params.put("page", pageIndex + "");
                params.put("key", key);
                params.put("limit", "10");
                String result = sendPostMessage(params, "utf-8");
                result = decode(result);
                String[] strings = result.split("<font color='red'>");
                StringBuffer sb = new StringBuffer();
                for (String s : strings) {
                    sb.append(s);
                }
                result = String.valueOf(sb);
                strings = result.split("<\\\\/font>");
//                System.out.println(strings.length);
                StringBuffer sb2 = new StringBuffer();
                for (String s : strings) {
                    sb2.append(s);
                }
                result = String.valueOf(sb2);
                Message msg = Message.obtain();
                msg.what = Constants.GET_DATA;
                msg.obj = result;
                mHandler.sendMessageDelayed(msg, 1000);
            }
        }).start();
    }

//    public static String decodeUnicode(final String dataStr) {
//        int start = 0;
//        int end = 0;
//        final StringBuffer buffer = new StringBuffer();
//        while (start > -1) {
//            end = dataStr.indexOf("\\u", start + 2);
//            String charStr = "";
//            if (end == -1) {
//                charStr = dataStr.substring(start + 2, dataStr.length());
//            } else {
//                charStr = dataStr.substring(start + 2, end);
//            }
//            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
//            buffer.append(new Character(letter).toString());
//            start = end;
//        }
//        return buffer.toString();
//    }

    /**
     * 将Unicode编码的汉字转化为utf_8类型
     *
     * @param in
     * @return
     */
    public static String decode(String in) {
        try {
            return decode(in.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return in;
    }

    private static String decode(char[] in) throws Exception {
        int off = 0;
        char c;
        char[] out = new char[in.length];
        int outLen = 0;
        while (off < in.length) {
            c = in[off++];
            if (c == '\\') {
                if (in.length > off) { // 是否有下一个字符
                    c = in[off++]; // 取出下一个字符
                } else {
                    out[outLen++] = '\\'; // 末字符为'\'，返回
                    break;
                }
                if (c == 'u') { // 如果是"\\u"
                    int value = 0;
                    if (in.length > off + 4) { // 判断"\\u"后边是否有四个字符
                        boolean isUnicode = true;
                        for (int i = 0; i < 4; i++) { // 遍历四个字符
                            c = in[off++];
                            switch (c) {
                                case '0':
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                                    value = (value << 4) + c - '0';
                                    break;
                                case 'a':
                                case 'b':
                                case 'c':
                                case 'd':
                                case 'e':
                                case 'f':
                                    value = (value << 4) + 10 + c - 'a';
                                    break;
                                case 'A':
                                case 'B':
                                case 'C':
                                case 'D':
                                case 'E':
                                case 'F':
                                    value = (value << 4) + 10 + c - 'A';
                                    break;
                                default:
                                    isUnicode = false; // 判断是否为unicode码
                            }
                        }
                        if (isUnicode) { // 是unicode码转换为字符
                            out[outLen++] = (char) value;
                        } else { // 不是unicode码把"\\uXXXX"填入返回值
                            off = off - 4;
                            out[outLen++] = '\\';
                            out[outLen++] = 'u';
                            out[outLen++] = in[off++];
                        }
                    } else { // 不够四个字符则把"\\u"放入返回结果并继续
                        out[outLen++] = '\\';
                        out[outLen++] = 'u';
                        continue;
                    }
                } else {
                    switch (c) { // 判断"\\"后边是否接特殊字符，回车，tab一类的
                        case 't':
                            c = '\t';
                            out[outLen++] = c;
                            break;
                        case 'r':
                            c = '\r';
                            out[outLen++] = c;
                            break;
                        case 'n':
                            c = '\n';
                            out[outLen++] = c;
                            break;
                        case 'f':
                            c = '\f';
                            out[outLen++] = c;
                            break;
                        default:
                            out[outLen++] = '\\';
                            out[outLen++] = c;
                            break;
                    }
                }
            } else {
                out[outLen++] = (char) c;
            }
        }
        return new String(out, 0, outLen);
    }

    /**
     * 将unicode的汉字码转换成utf-8格式的汉字
     * @param unicode
     * @return
     */
//    public static String unicodeToString(String unicode) {
//
//        String str = unicode.replace("0x", "\\");
//
//        StringBuffer string = new StringBuffer();
//        String[] hex = str.split("\\\\u");
//        for (int i = 1; i < hex.length; i++) {
//            int data = Integer.parseInt(hex[i], 16);
//            string.append((char) data);
//        }
//        return string.toString();
//    }

    /**
     * 发送post请求
     *
     * @param params 填写的url的参数
     * @param encode 字节编码
     * @return json字符串
     */
    public static String sendPostMessage(Map<String, String> params, String encode) {
        URL url = null;
        StringBuffer buffer = new StringBuffer();
        try {//把请求的主体写入正文！！
            url = new URL("http://search.gdstc.gov.cn/search/");
            if (params != null && !params.isEmpty()) {
                //迭代器
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    buffer.append(entry.getKey()).append("=").
                            append(URLEncoder.encode(entry.getValue(), encode)).
                            append("&");
                }
            }
//            System.out.println(buffer.toString());
            //删除最后一个字符&，多了一个;主体设置完毕
            buffer.deleteCharAt(buffer.length() - 1);
            byte[] mydata = buffer.toString().getBytes();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setDoInput(true);//表示从服务器获取数据
            connection.setDoOutput(true);//表示向服务器写数据
            //获得上传信息的字节大小以及长度

            connection.setRequestMethod("POST");
            //是否使用缓存
            connection.setUseCaches(false);
            //表示设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            connection.setRequestProperty("Content-Length", String.valueOf(mydata.length));
            connection.connect();   //连接，不写也可以。。？？有待了解

            //获得输出流，向服务器输出数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(mydata, 0, mydata.length);
            //获得服务器响应的结果和状态码
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return changeInputStream(connection.getInputStream(), encode);

            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将一个输入流转换成字符串
     *
     * @param inputStream
     * @param encode
     * @return
     */
    private static String changeInputStream(InputStream inputStream, String encode) {
        //通常叫做内存流，写在内存中的
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result = "";
        if (inputStream != null) {
            try {
                while ((len = inputStream.read(data)) != -1) {
                    data.toString();

                    outputStream.write(data, 0, len);
                }
                //result是在服务器端设置的doPost函数中的
                result = new String(outputStream.toByteArray(), encode);
                outputStream.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search_1:
                getHotWordData(tv_search_1.getText().toString().trim());
                break;
            case R.id.tv_search_2:
                getHotWordData(tv_search_2.getText().toString().trim());
                break;
            case R.id.tv_search_3:
                getHotWordData(tv_search_3.getText().toString().trim());
                break;
            case R.id.tv_search_4:
                getHotWordData(tv_search_4.getText().toString().trim());
                break;
        }
    }

    /**
     * 搜索热搜内容
     *
     * @param key
     */
    private void getHotWordData(String key) {
        ll_search_hot_word.setVisibility(View.GONE);//隐藏热搜
        customSRL_search_fragment.setRefreshing(true);
        et_search_fragment.setText(key);
        mInputText = key;
        getDataFromNet(mInputText);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.e("TAG", "onDestroy");
    }
}
