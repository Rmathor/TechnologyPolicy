package com.mathor.technologypolicy.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mathor.technologypolicy.R;
import com.mathor.technologypolicy.base.BasePager;
import com.mathor.technologypolicy.pager.PolicyInterpretationPager;
import com.mathor.technologypolicy.pager.TechnologyPager;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Author: mathor
 * Date : on 2017/11/3 18:09
 * 科技fragment
 */

public class TechnologyFragment extends Fragment {

    private Context mContext;

    @ViewInject(R.id.tabPageIndicator)
    private TabPageIndicator mTabPageIndicator;

    @ViewInject(R.id.viewPager)
    private ViewPager mViewPager;

    //标题
    private String[] pagerTitle = new String[]{"科技", "政策解读"};

    //页面
    private ArrayList<BasePager> mPagers = new ArrayList<>();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewPager.setAdapter(new TechnologyPagerAdapter());//设置适配器
        mTabPageIndicator.setViewPager(mViewPager);//绑定viewpager
    }

    public TechnologyFragment() {
    }

    @SuppressLint("ValidFragment")
    public TechnologyFragment(Context context) {
        this.mContext = context;

        mPagers.add(new TechnologyPager(mContext));//科技页面
        mPagers.add(new PolicyInterpretationPager(mContext));//政策解读页面
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(mContext, R.layout.fragment_technology,null);
        x.view().inject(TechnologyFragment.this,view);
        return view;
    }

    private class TechnologyPagerAdapter extends PagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            return pagerTitle[position];
        }

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPagers.get(position);
            View rootView = pager.rootView;
            pager.initData();//初始化数据
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
