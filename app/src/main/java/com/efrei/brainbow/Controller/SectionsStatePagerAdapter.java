package com.efrei.brainbow.Controller;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class SectionsStatePagerAdapter extends FragmentStatePagerAdapter {

    private final ArrayList<Fragment> fragments = new ArrayList<>();

    public SectionsStatePagerAdapter(FragmentManager fm){
        super(fm);
    }

    public void addFragment(Fragment fragment){
        fragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
