package com.creative.share.apps.aamalnaa.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;
    private List<String> title;

    public MyPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
        title = new ArrayList<>();


    }

    public void addFragment(List<Fragment> fragmentList)
    {
        this.fragmentList.addAll(fragmentList);
    }

    public void addTitle(List<String> title)
    {
        this.title.addAll(title);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }
}
