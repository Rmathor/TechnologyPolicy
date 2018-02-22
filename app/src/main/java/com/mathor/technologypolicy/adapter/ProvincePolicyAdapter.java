package com.mathor.technologypolicy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.domain.ProvincePolicy;

import java.util.ArrayList;

/**
 * Author: mathor
 * Date : on 2017/11/5 20:48
 * 省政策页面的适配器
 */

public class ProvincePolicyAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<ProvincePolicy> mProvincePolicies;

    public ProvincePolicyAdapter(Context context, ArrayList<ProvincePolicy> provincePolicies) {

        this.mContext = context;
        this.mProvincePolicies = provincePolicies;
    }

    @Override
    public int getCount() {
        if (mProvincePolicies != null && mProvincePolicies.size() > 0) {

            return mProvincePolicies.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mProvincePolicies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.policy_pager_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_policy_newTitle = convertView.findViewById(R.id.tv_policy_newTitle);
            viewHolder.tv_policy_date = convertView.findViewById(R.id.tv_policy_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ProvincePolicy provincePolicy = mProvincePolicies.get(position);
        viewHolder.tv_policy_newTitle.setText(provincePolicy.getTitle());
        viewHolder.tv_policy_date.setText(provincePolicy.getDate());
        return convertView;
    }

    class ViewHolder {
        TextView tv_policy_newTitle;
        TextView tv_policy_date;
    }
}
