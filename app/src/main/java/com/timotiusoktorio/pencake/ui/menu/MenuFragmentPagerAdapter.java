package com.timotiusoktorio.pencake.ui.menu;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.timotiusoktorio.pencake.data.model.Category;

import java.util.List;

class MenuFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Category> categories;

    MenuFragmentPagerAdapter(FragmentManager fm, List<Category> categories) {
        super(fm);
        this.categories = categories;
    }

    @Override
    public Fragment getItem(int position) {
        return MenuListFragment.newInstance(categories.get(position).getId());
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position).getName();
    }
}