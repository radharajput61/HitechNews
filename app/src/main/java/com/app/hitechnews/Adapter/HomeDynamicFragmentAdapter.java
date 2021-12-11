package com.app.hitechnews.Adapter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.hitechnews.Fragment.DynamicHome;
import com.app.hitechnews.UserDashbord.DynamicTab.DynamicFragment;

public class HomeDynamicFragmentAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public HomeDynamicFragmentAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    // get the current item with position number
    @Override
    public Fragment getItem(int position) {
        Bundle b = new Bundle();
        b.putInt("position", position);
        Fragment frag = DynamicHome.newInstance();
        frag.setArguments(b);
        return frag;
    }

    // get total number of tabs
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}