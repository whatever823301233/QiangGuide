package com.qiang.qiangguide.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.qiang.qiangguide.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiang on 2016/7/20.
 */
public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> fragmentBases = new ArrayList<>();

    public void setFragmentBases(List<BaseFragment> fragmentBases) {
        this.fragmentBases = fragmentBases;
    }

    public BaseFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public BaseFragmentPagerAdapter(FragmentManager fragmentManager, ArrayList<BaseFragment> fragmentBases) {
        super(fragmentManager);
        this.fragmentBases = fragmentBases;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentBases.get(position);
    }

    @Override
    public int getCount() {
        return fragmentBases.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);// TODO: 2016/7/20 不销毁fragment？
    }


}
