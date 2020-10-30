package com.miekir.common.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Jason on 2018/8/20.
 * 在Fragment里使用ViewPager的适配器
 */

public class TabFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> tabFragments;
    public TabFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.tabFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return tabFragments == null ? null : tabFragments.get(position);
    }

    @Override
    public int getCount() {
        return tabFragments == null ? 0 : tabFragments.size();
    }
}
