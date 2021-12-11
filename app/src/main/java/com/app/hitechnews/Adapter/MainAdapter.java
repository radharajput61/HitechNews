package com.app.hitechnews.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.app.hitechnews.Fragment.TabFragment;
import com.app.hitechnews.Model.MainModel;
import com.app.hitechnews.R;
import com.app.hitechnews.UserDashbord.DynamicTab.DynamicFragment;

import java.util.List;

public class MainAdapter extends FragmentPagerAdapter {
    private Context context;
    private int noOfTabs;
    private List<MainModel> mainModelList;
    public MainAdapter(@NonNull FragmentManager fm, Context context, int noOfTabs, List<MainModel> mainModelList) {
        super(fm);
        this.context = context;
        this.noOfTabs = noOfTabs;
        this.mainModelList = mainModelList;
    }

    @NonNull
    @Override
    public DynamicFragment getItem(int position) {
        Log.v("jsdfgj",""+position);
        MainModel mainModel = mainModelList.get(position);
        return  DynamicFragment.newInstance(mainModel.getTab_name());
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        MainModel mainModel = mainModelList.get(position);
        TextView tv = (TextView) v.findViewById(R.id.item_name);
        tv.setText(mainModel.getLabel());
        return v;
    }
}