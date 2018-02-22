package com.mathor.technologypolicy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.utils.BitmapCache;
import com.mathor.technologypolicy.view.SingleRequestQueue;

import java.util.ArrayList;

/**
 * Author: mathor
 * Date : on 2017/12/7 11:47
 */

public class MyListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mUrls;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private ImageLoader.ImageListener mImageListener;

    public MyListAdapter(Context context, ArrayList<String> urls){

        this.mContext = context;
        this.mUrls = urls;
    }

    @Override
    public int getCount() {
        return mUrls.size() == 0?0:mUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return mUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.province_policy_detail_item,null);
            viewHolder = new ViewHolder();
            viewHolder.iv_picture = convertView.findViewById(R.id.pv_picture);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String url = mUrls.get(position);

        mRequestQueue = SingleRequestQueue.getRequestQueue(mContext);
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
        mImageListener = ImageLoader.getImageListener(viewHolder.iv_picture, R.mipmap.picture_loading, R.mipmap.picture_error);
        mImageLoader.get(url, mImageListener);
        return convertView;
    }

    class ViewHolder{
        ImageView iv_picture;
    }
}
