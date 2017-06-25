package com.parot.mtecgwa_jr.myrecipes.Adapter;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mtecgwa-jr on 6/1/17.
 */

public class RecipeFragmentAdapter extends FragmentPagerAdapter {

    private final ArrayList<android.support.v4.app.Fragment> fragmentIst = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();

    public RecipeFragmentAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return fragmentIst.get(position);
    }

    @Override
    public int getCount() {
        return fragmentIst.size();
    }

    public void addFragments(android.support.v4.app.Fragment fragment, String title) {
        fragmentIst.add(fragment);
        fragmentTitle.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }
}
