package com.mathor.technologypolicy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.domain.Search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Author: mathor
 * Date : on 2017/11/12 14:40
 * 搜索结果列表适配器
 */

public class SearchAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Search.Data> mDatas;

    public SearchAdapter(Context context, ArrayList<Search.Data> datas) {

        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size() == 0 ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.fragment_search_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_search_title = convertView.findViewById(R.id.tv_search_title);
            viewHolder.tv_search_date = convertView.findViewById(R.id.tv_search_date);
            viewHolder.tv_search_type = convertView.findViewById(R.id.tv_search_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Search.Data data = mDatas.get(position);
        viewHolder.tv_search_title.setText(data.getTitle());
        viewHolder.tv_search_date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(data.getDate())));
        viewHolder.tv_search_type.setText(data.getType());
        return convertView;
    }

    class ViewHolder {
        TextView tv_search_title;
        TextView tv_search_date;
        TextView tv_search_type;
    }
}
