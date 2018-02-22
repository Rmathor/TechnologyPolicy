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
import com.mathor.technologypolicy.pager.CountryPolicyPager;
import com.mathor.technologypolicy.pager.InformPager2;
import com.mathor.technologypolicy.pager.ProvincePolicyPager;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Author: mathor
 * Date : on 2017/11/3 16:23
 * 首页fragment
 */

public class HomeFragment extends Fragment {

    private Context mContext;

    @ViewInject(R.id.tabPageIndicator)
    private TabPageIndicator mTabPageIndicator;

    @ViewInject(R.id.viewPager)
    private ViewPager mViewPager;

    //标题
    private String[] pagerTitle = new String[]{"通知", "国家政策", "省政策"};

    //页面
    private ArrayList<BasePager> mPagers = new ArrayList<>();

    public HomeFragment() {
    }

    @SuppressLint("ValidFragment")
    public HomeFragment(Context context) {
        this.mContext = context;

        mPagers.add(new InformPager2(mContext));//通知页面
        mPagers.add(new CountryPolicyPager(mContext));//国家政策页面
        mPagers.add(new ProvincePolicyPager(mContext));//省政策页面

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(mContext, R.layout.fragmnet_home, null);
        x.view().inject(HomeFragment.this, view);//视图与fragment关联
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewPager.setAdapter(new HomePagerAdapter());//设置适配器
        mTabPageIndicator.setViewPager(mViewPager);//绑定viewpager
    }

    //viewpager的适配器
    class HomePagerAdapter extends PagerAdapter {

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
