package com.mathor.technologypolicy.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Author: mathor
 * Date : on 2017/11/10 19:48
 */

 public class BitmapCache implements ImageLoader.ImageCache{

    private LruCache<String,Bitmap> mLruCache;

    public BitmapCache() {
        int maxSize = 10 * 1024 * 1024;
        mLruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }
    @Override
    public Bitmap getBitmap(String s) {
        return mLruCache.get(s);
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        mLruCache.put(s,bitmap);
    }
}
