package com.mathor.technologypolicy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.domain.Technology;

import java.util.ArrayList;

/**
 * Author: mathor
 * Date : on 2017/11/9 17:12
 */

public class TechnologyAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Technology> mTechnologies;

    public TechnologyAdapter(Context context, ArrayList<Technology> technologies) {

        this.mContext = context;
        mTechnologies = technologies;
    }

    @Override
    public int getCount() {
        return mTechnologies.size() == 0 ? 0 : mTechnologies.size();
    }

    @Override
    public Object getItem(int position) {
        return mTechnologies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.technology_fragment_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_technology_title = convertView.findViewById(R.id.tv_technology_title);
            viewHolder.tv_technology_date = convertView.findViewById(R.id.tv_technology_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Technology technology = mTechnologies.get(position);
        viewHolder.tv_technology_title.setText(technology.getTitle());
        viewHolder.tv_technology_date.setText(technology.getDate());
        return convertView;
    }

    class ViewHolder {
        TextView tv_technology_title;
        TextView tv_technology_date;
    }
}
