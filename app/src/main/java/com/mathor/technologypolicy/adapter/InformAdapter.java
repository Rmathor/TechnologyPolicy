package com.mathor.technologypolicy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.domain.Inform;

import java.util.List;

/**
 * Author: mathor
 * Date : on 2017/11/5 19:35
 * 通知页面的适配器
 */

public class InformAdapter extends BaseAdapter {

    //上下文
    private Context mContext;
    //存放数据的集合
    private List<Inform.DataBean.RowsBean> mInforms;

    public InformAdapter(Context context, List<Inform.DataBean.RowsBean> informs){
        this.mContext = context;
        this.mInforms = informs;
    }

    @Override
    public int getCount() {
        if (mInforms != null && mInforms.size()>0){

            return mInforms.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mInforms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.inform_pager_detail_item,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_inform_newtitle = convertView.findViewById(R.id.tv_inform_newTitle);
            viewHolder.tv_inform_date = convertView.findViewById(R.id.tv_inform_date);
            viewHolder.tv_inform_comeFrom = convertView.findViewById(R.id.tv_inform_comeFrom);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Inform.DataBean.RowsBean inform = mInforms.get(position);
//        System.out.println("tv_inform_newtitle=="+inform.getNewsTitle());
        viewHolder.tv_inform_newtitle.setText(inform.getNewsTitle());
        viewHolder.tv_inform_date.setText(inform.getIssuDate());
        viewHolder.tv_inform_comeFrom.setText(inform.getComeFrom());
        return convertView;
    }

    class ViewHolder{
        TextView tv_inform_newtitle;//标题
        TextView tv_inform_date;//日期
        TextView tv_inform_comeFrom;//出处
    }
}
