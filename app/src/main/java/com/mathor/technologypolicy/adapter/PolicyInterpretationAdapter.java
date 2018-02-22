package com.mathor.technologypolicy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.domain.PolicyInterpretation;

import java.util.ArrayList;

/**
 * Author: mathor
 * Date : on 2017/11/10 16:34
 * 政策解读适配器
 */

public class PolicyInterpretationAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<PolicyInterpretation> mPolicyInterpretations;

    public PolicyInterpretationAdapter(Context context, ArrayList<PolicyInterpretation> policyInterpretations) {

        this.mContext = context;
        this.mPolicyInterpretations = policyInterpretations;
    }

    @Override
    public int getCount() {
        return mPolicyInterpretations.size() == 0 ? 0 : mPolicyInterpretations.size();
    }

    @Override
    public Object getItem(int position) {
        return mPolicyInterpretations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.policyinterpretation_pager_detail_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_policy_interpretation_title = convertView.findViewById(R.id.tv_policy_interpretation_title);
            viewHolder.tv_policy_interpretation_date = convertView.findViewById(R.id.tv_policy_interpretation_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PolicyInterpretation policyInterpretation = mPolicyInterpretations.get(position);
        viewHolder.tv_policy_interpretation_title.setText(policyInterpretation.getTitle());
        viewHolder.tv_policy_interpretation_date.setText(policyInterpretation.getDate());
        return convertView;
    }

    class ViewHolder {
        TextView tv_policy_interpretation_title;
        TextView tv_policy_interpretation_date;
    }
}
