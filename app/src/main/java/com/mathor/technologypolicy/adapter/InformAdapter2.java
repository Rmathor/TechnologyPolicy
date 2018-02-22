package com.mathor.technologypolicy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.domain.Inform2;

import java.util.ArrayList;

/**
 * Author: mathor
 * Date : on 2017/11/7 11:12
 */

public class InformAdapter2 extends BaseAdapter{

    private Context mContext;
    private ArrayList<Inform2> mInform2s;

    public InformAdapter2(Context context, ArrayList<Inform2> inform2s) {

        this.mContext = context;
        this.mInform2s = inform2s;
    }

    @Override
    public int getCount() {
        if (mInform2s != null && mInform2s.size() > 0) {

            return mInform2s.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mInform2s.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.inform_pager_detail_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_inform_newTitle = convertView.findViewById(R.id.tv_inform_newTitle);
            viewHolder.tv_inform_date = convertView.findViewById(R.id.tv_inform_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Inform2 inform2 = mInform2s.get(position);
        viewHolder.tv_inform_newTitle.setText(inform2.getTitle());
        viewHolder.tv_inform_date.setText(inform2.getDate());
        return convertView;
    }

    class ViewHolder {
        TextView tv_inform_newTitle;
        TextView tv_inform_date;
    }
}
