package com.mathor.technologypolicy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.domain.Fujian;

import java.util.ArrayList;

/**
 * Author: mathor
 * Date : on 2017/12/7 15:20
 * 附件的适配器
 */

public class FuJianAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Fujian> mFujian;

    public FuJianAdapter(Context context, ArrayList<Fujian> fujian) {

        this.mContext = context;
        this.mFujian = fujian;
    }

    @Override
    public int getCount() {
        return mFujian.size() == 0 ? 0 : mFujian.size();
    }

    @Override
    public Object getItem(int position) {
        return mFujian.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.fujian_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_fujian_title = convertView.findViewById(R.id.tv_fujian_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String title = mFujian.get(position).getTitle();
        viewHolder.tv_fujian_title.setText(title);
        return convertView;
    }

    class ViewHolder {
        TextView tv_fujian_title;
    }
}
