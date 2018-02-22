package com.mathor.technologypolicy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.domain.CountryPolicy;

import java.util.ArrayList;

/**
 * Author: mathor
 * Date : on 2017/11/5 20:48
 * 国家政策页面适配器
 */

public class CountryPolicyAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<CountryPolicy> mCountryPolicies;

    public CountryPolicyAdapter(Context context, ArrayList<CountryPolicy> countryPolicies) {

        this.mContext = context;
        this.mCountryPolicies = countryPolicies;
    }

    @Override
    public int getCount() {
        if (mCountryPolicies != null && mCountryPolicies.size() > 0) {

            return mCountryPolicies.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mCountryPolicies.get(position);
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

        CountryPolicy countryPolicy = mCountryPolicies.get(position);
        viewHolder.tv_policy_newTitle.setText(countryPolicy.getTitle());
        viewHolder.tv_policy_date.setText(countryPolicy.getDate());
        return convertView;
    }

    class ViewHolder {
        TextView tv_policy_newTitle;
        TextView tv_policy_date;
    }
}
