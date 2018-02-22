package com.mathor.technologypolicy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.github.chrisbanes.photoview.PhotoView;
import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.utils.BitmapCache;

/**
 * 显示大图activity
 */
public class ShowBigPictureActivity extends Activity {

    private PhotoView pv_bigPicture;//图片载体
    private String mPictureUrl;//传过来的图片路径

    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;
    private ImageLoader.ImageListener mImageListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_big_picture);

        pv_bigPicture = (PhotoView) findViewById(R.id.pv_bigPicture);
        mPictureUrl = getIntent().getStringExtra("picture_url");
        if (!TextUtils.isEmpty(mPictureUrl)) {
            mRequestQueue = Volley.newRequestQueue(ShowBigPictureActivity.this);
            mImageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
            mImageListener = ImageLoader.getImageListener(pv_bigPicture, R.mipmap.picture_loading, R.mipmap.picture_error);
            mImageLoader.get(mPictureUrl, mImageListener);
        }
    }
}
