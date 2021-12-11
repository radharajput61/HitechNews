package com.app.hitechnews.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.hitechnews.Adapter.HomeDynamicFragmentAdapter;
import com.app.hitechnews.Adapter.VideosAdapter;
import com.app.hitechnews.MainActivity;
import com.app.hitechnews.Model.FollowerModel;
import com.app.hitechnews.Model.VideoItem;
import com.app.hitechnews.R;
import com.app.hitechnews.Util.API;
import com.app.hitechnews.Util.MyPreferences;
import com.app.hitechnews.videoplayer.Resources;
import com.app.hitechnews.videoplayer.VerticalSpacingItemDecorator;
import com.app.hitechnews.videoplayer.VideoPlayerRecyclerAdapter;
import com.app.hitechnews.videoplayer.VideoPlayerRecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment{

    private ViewPager viewPager;
    private TabLayout mTabLayout;
    String name[]={"Top News","Resent News","Tranding","All News"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // initialise the layout
        viewPager =view. findViewById(R.id.viewpager);
        mTabLayout =view. findViewById(R.id.tabs);

        // setOffscreenPageLimit means number
        // of tabs to be shown in one page

        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // setCurrentItem as the tab position
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
       // setDynamicFragmentToTabLayout();
        return view;
    }
  /*  private void setDynamicFragmentToTabLayout() {
        // here we have given 10 as the tab number
        // you can give any number here
        for (int i = 0; i < name.length; i++) {
            // set the tab name as "Page: " + i
            mTabLayout.addTab(mTabLayout.newTab().setText(name[i]));
        }
        HomeDynamicFragmentAdapter mDynamicFragmentAdapter = new HomeDynamicFragmentAdapter(getActivity().getSupportFragmentManager(), mTabLayout.getTabCount());

        // set the adapter
        viewPager.setAdapter(mDynamicFragmentAdapter);

        // set the current item as 0 (when app opens for first time)
        viewPager.setCurrentItem(0);
    }*/


}