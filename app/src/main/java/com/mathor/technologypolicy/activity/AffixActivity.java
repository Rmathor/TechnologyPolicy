package com.mathor.technologypolicy.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.mathor.technologypolicy.Constants;
import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.utils.MyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 附件pdf加载页面
 */
public class AffixActivity extends Activity {

    public static final String AFFIX_URL = AffixActivity.class.getSimpleName();
    private static final int GET_AFFIX = 0x246;

    private OkHttpClient okHttpClient;

    private PDFView pdfView;//PDF加载视图
    private LinearLayout ll_affix;
    private ProgressBar pb_affix;

    private int cur_progress = 0;//当前进度

    private String mUrl;
    private String mPath;//文件保存地址

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case GET_AFFIX:
                    cur_progress = msg.arg1;
                    pb_affix.setProgress(cur_progress);
                    if (cur_progress == 100) {
                        displayFromFile(getFile());
                    }
                    break;
            }
            return true;
        }
    });

    //android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    //获取文件
    @NonNull
    private File getFile() {
//        Log.e("TAG", mPath);
        return new File(mPath, mUrl.substring(mUrl.lastIndexOf("/") + 1));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_affix);
        initView();
        mUrl = getIntent().getStringExtra(AFFIX_URL);
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            mPath = getExternalFilesDir("/tpdownload").getAbsolutePath();
        } else {
            mPath = getFilesDir().getAbsolutePath() + "/tpdownload";
        }
//        Log.e("TAG", mPath);
        initData();
    }

    private void initData() {

        MyUtils.getPermission(AffixActivity.this);//获取sdcard权限

        File file = getFile();
        if (file.exists()) {
            displayFromFile(file);//显示PDF文件
        } else {
            downLoadFile();//下载文件
        }
    }

    /**
     * 下载文件
     */
    private void downLoadFile() {

        okHttpClient = new OkHttpClient();
        Request request;
        if (mUrl.startsWith("http")) {
            request = new Request.Builder().url(mUrl).build();
        } else {
            request = new Request.Builder().url(Constants.BASEURL + mUrl).build();
        }
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(AFFIX_URL, e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();//转化为字节流
                    long total = response.body().contentLength();//文件大小

                    File file2 = new File(mPath);//创建文件夹
                    if (!file2.exists()) {
                        file2.mkdir();
                    }

                    File file = new File(mPath, mUrl.substring(mUrl.lastIndexOf("/") + 1));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);//进度
                        Message msg = handler.obtainMessage();
                        msg.what = GET_AFFIX;
                        msg.arg1 = progress;
                        handler.sendMessage(msg);
                    }
                    fos.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 显示附件内容
     *
     * @param file 附件名称
     */
    private void displayFromFile(File file) {

        ll_affix.setVisibility(View.GONE);
        if (mUrl.substring(mUrl.lastIndexOf(".") + 1).equals("pdf")) {//判断是否为pdf
//            System.out.println(file.getName());
            pdfView.setVisibility(View.VISIBLE);

            pdfView.fromFile(file)   //设置pdf文件地址
                    //是否允许翻页，默认是允许翻页
                    .enableSwipe(true)
                    //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                    .swipeHorizontal(true)
                    //能够双击
                    .enableDoubletap(true)
                    //设置默认显示第0页
                    .defaultPage(0)
                    //允许在当前页面上绘制一些内容，通常在屏幕中间可见。
//                .onDraw(onDrawListener)
//                // 允许在每一页上单独绘制一个页面。只调用可见页面
//                .onDrawAll(onDrawListener)
                    //设置加载监听
//                .onLoad(new OnLoadCompleteListener() {
//                    @Override
//                    public void loadComplete(int nbPages) {
//                    }
//                })
//                //设置翻页监听
//                .onPageChange(new OnPageChangeListener() {
//
//                    @Override
//                    public void onPageChanged(int page, int pageCount) {
//                    }
//                })
                    //设置页面滑动监听
//                .onPageScroll(onPageScrollListener)
                    //设置加载失败监听
                    .onError(new OnErrorListener() {
                        @Override
                        public void onError(Throwable t) {
//                        Toast.makeText(AffixActivity.this, "加载失败.", Toast.LENGTH_SHORT).show();
                            downLoadFile();
                        }
                    })
                    // 首次提交文档后调用。
//                .onRender(onRenderListener)
                    // 渲染风格（就像注释，颜色或表单）
                    .enableAnnotationRendering(true)
//                .password(null)
//                .scrollHandle(null)
                    // 改善低分辨率屏幕上的渲染
                    .enableAntialiasing(true)
                    // 页面间的间距。定义间距颜色，设置背景视图
//                .spacing(0)
                    .load();

        } else if (mUrl.substring(mUrl.lastIndexOf(".") + 1).contains("doc") || mUrl.substring(mUrl.lastIndexOf(".") + 1).contains("xls")) {//判断是否为word||excel
            Intent intent = getWordFileIntent(getFile().getPath());//调用本地word解析器
            try {
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Toast.makeText(AffixActivity.this, "找不到可以打开该文件的程序,请确定您的手机已安装了可以打开word或Excel的软件.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void initView() {
        pdfView = findViewById(R.id.pdfView);
        ll_affix = findViewById(R.id.ll_affix);
        pb_affix = findViewById(R.id.pb_affix);
    }
}
