package com.app.hitechnews.UserDashbord.DynamicTab;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.hitechnews.Model.MainModel;
import com.app.hitechnews.R;
import com.app.hitechnews.UserDashbord.UserHome;

import java.util.List;

public class DynamicFragmentAdapter extends FragmentStatePagerAdapter {

    private Context context;

    private List<MainModel> mainModelList;
    public DynamicFragmentAdapter(@NonNull FragmentManager fm, Context context , List<MainModel> mainModelList) {
        super(fm);
        this.context = context;
        this.mainModelList = mainModelList;
    }


//    @Override
//    public DynamicFragment getItem(int position) {
//
//        Log.e("position",position+"");
//        DynamicFragment frag;
//
////        if (position==0)
////        {
////            MainModel mainModel = mainModelList.get(position);
////            frag=DynamicFragment.newInstance(mainModel.getLabel());
////        }else {
//
//            MainModel mainModel = mainModelList.get(position);
//            frag=DynamicFragment.newInstance(mainModel.getLabel());
//
//
////        }
//
//
//        return  frag;
//
//
//    }
    @NonNull
    @Override
    public DynamicFragment getItem(int position) {
        Log.e("position",position+"");
        MainModel mainModel = mainModelList.get(position);
        return  DynamicFragment.newInstance(mainModel.getTab_name());
    }

    @Override
    public int getCount() {
        return mainModelList.size();
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        MainModel mainModel = mainModelList.get(position);
        TextView tv = (TextView) v.findViewById(R.id.item_name);
        TextView textView = v.findViewById(R.id.item_count);
        if(mainModel.getCount() != 0){
            textView.setVisibility(View.GONE);
            textView.setText(String.valueOf(mainModel.getCount()));
        }
        else{
            textView.setVisibility(View.GONE);
        }
        tv.setText(mainModel.getLabel());
       // ImageView img = (ImageView) v.findViewById(R.id.item_icon);
//        Glide.with(context).load(mainModel.getImage()).placeholder(R.drawable.placeholdre).into(img);

        return v;
    }

}