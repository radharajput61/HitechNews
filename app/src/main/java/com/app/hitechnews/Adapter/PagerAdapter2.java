package com.app.hitechnews.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.hitechnews.Activity.Profile.Fragment.BookmarkFragment;
import com.app.hitechnews.Activity.Profile.Fragment.MyPostFragment;


public class PagerAdapter2 extends FragmentStatePagerAdapter {

	int mNumOfTabs;
	public PagerAdapter2(FragmentManager fm, int NumOfTabs) {
		super(fm);
		this.mNumOfTabs = NumOfTabs;
	}
	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:
				MyPostFragment tab0 = new MyPostFragment();
				return tab0;
			case 1:
				BookmarkFragment tab1 = new BookmarkFragment();
				return tab1;
			case 2:
				BookmarkFragment tab2 = new BookmarkFragment();
				return tab2;
			case 3:
				BookmarkFragment tab3 = new BookmarkFragment();
				return tab3;
			default:
				return null;
		}
	}
	@Override
	public int getCount() {
		return mNumOfTabs;
	}
}
