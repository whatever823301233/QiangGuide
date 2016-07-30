package com.qiang.qiangguide.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.qiang.qiangguide.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiang on 2016/7/20.
 */
public class FragmentPagerStateAdapter extends FragmentStatePagerAdapter {

    private List<BaseFragment> fragmentBases = new ArrayList<>();

    public List<BaseFragment> getFragmentBases() {
        return fragmentBases;
    }

    public void setFragmentBases(List<BaseFragment> fragmentBases) {
        this.fragmentBases = fragmentBases;
    }

    public FragmentPagerStateAdapter(FragmentManager fm) {
        super(fm);
    }

    public FragmentPagerStateAdapter(FragmentManager fm, ArrayList<BaseFragment> fragmentBases) {
        super(fm);
        this.fragmentBases = fragmentBases;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragmentBases.get(arg0);
    }

    @Override
    public int getCount() {
        return fragmentBases.size();
    }

}
