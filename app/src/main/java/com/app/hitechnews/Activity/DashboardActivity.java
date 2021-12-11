package com.app.hitechnews.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hitechnews.Adapter.MainAdapter;
import com.app.hitechnews.Model.MainModel;
import com.app.hitechnews.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private ViewPager viewPager;
    MainAdapter mainAdapter;
    private TabLayout tabLayout;
    private List<MainModel> mainModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        mainModels.add(new MainModel("https://www.welovedogs.jp/movie/video_001.mp4", "All", "", 2));
        mainModels.add(new MainModel("https://www.welovedogs.jp/movie/video_001.mp4", "1", "", 2));
        mainModels.add(new MainModel("https://www.welovedogs.jp/movie/video_001.mp4", "3", "", 2));
        mainModels.add(new MainModel("https://www.welovedogs.jp/movie/video_001.mp4", "2", "", 2));
        mainModels.add(new MainModel("https://www.welovedogs.jp/movie/video_001.mp4", "4", "", 2));


        mainAdapter = new MainAdapter(getSupportFragmentManager(), DashboardActivity.this, mainModels.size(), mainModels);
        viewPager.setAdapter(mainAdapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(mainAdapter.getTabView(i));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (view != null) {
                  //  ImageView imageView = view.findViewById(R.id.item_icon);
                    TextView textView = view.findViewById(R.id.item_name);
                    textView.setTextColor(getResources().getColor(R.color.white));
                    TextView count = view.findViewById(R.id.item_count);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (view != null) {
                 //   ImageView imageView = view.findViewById(R.id.item_icon);
                    TextView textView = view.findViewById(R.id.item_name);
                    textView.setTextColor(getResources().getColor(R.color.black));
                    TextView count = view.findViewById(R.id.item_count);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}