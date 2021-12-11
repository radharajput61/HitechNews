package com.app.hitechnews.UserDashbord;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.hitechnews.Adapter.MainAdapter;
import com.app.hitechnews.Model.MainModel;
import com.app.hitechnews.R;
import com.app.hitechnews.UserDashbord.DynamicTab.DynamicFragmentAdapter;

import com.app.hitechnews.Util.API;
import com.google.android.material.tabs.TabLayout;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserHome extends Fragment {
    DynamicFragmentAdapter mDynamicFragmentAdapter;
     ViewPager viewPager;
    private List<MainModel> mainModels = new ArrayList<>();
    MainAdapter mainAdapter;
    private TabLayout tabLayout;
    MagicIndicator magicIndicator;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user_home, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);
        magicIndicator = (MagicIndicator)view. findViewById(R.id.magic_indicator8);

         CategoryList();
        return view;

    }



    private void CategoryList()
    {
        final RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getActivity());
        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, API.CategoryList, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("response",response.toString());
                try {
                    JSONArray array=response.getJSONArray("CategoryList");
                    Log.v("cat..array",""+array);

                    if(array.length()>0)
                    {
                        mainModels.clear();
                        for (int i=0;i<array.length();i++)
                        {
                            Log.v("jdfhjg",array.toString());
                            JSONObject object=array.getJSONObject(i);
                            String CategoryId=object.getString("CategoryId");//
                            String CategoryName=object.getString("CategoryName");
                            Log.v("jhfsdgjg",CategoryId+","+CategoryName);
                            mainModels.add(new MainModel(CategoryId,CategoryName,"",2));
                        }

                         initMagicIndicator8();
                         mDynamicFragmentAdapter = new DynamicFragmentAdapter(getActivity().getSupportFragmentManager(),getActivity(), mainModels);

                         viewPager.setAdapter(mDynamicFragmentAdapter);


//                        mainAdapter = new MainAdapter(getFragmentManager(),getActivity(),mainModels.size(),mainModels);
//                        viewPager.setAdapter(mainAdapter);
//                        tabLayout.setupWithViewPager(viewPager);
//                        for(int i=0;i<tabLayout.getTabCount();i++){
//                            TabLayout.Tab tab = tabLayout.getTabAt(i);
//                            tab.setCustomView(mainAdapter.getTabView(i));
//                        }
//                        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//                            @Override
//                            public void onTabSelected(TabLayout.Tab tab) {
//                                View view = tab.getCustomView();
//                                if(view != null){
////                                    ImageView imageView = view.findViewById(R.id.item_icon);
////                                    TextView textView = view.findViewById(R.id.item_name);
////                                    textView.setTextColor(getResources().getColor(R.color.white));
////                                    TextView count = view.findViewById(R.id.item_count);
//                                }
//                            }
//
//                            @Override
//                            public void onTabUnselected(TabLayout.Tab tab) {
//                                View view = tab.getCustomView();
//                                if(view != null){
////                                    ImageView imageView = view.findViewById(R.id.item_icon);
////                                    TextView textView = view.findViewById(R.id.item_name);
////                                    textView.setTextColor(getResources().getColor(R.color.black));
////                                    TextView count = view.findViewById(R.id.item_count);
//                                }
//                            }
//
//                            @Override
//                            public void onTabReselected(TabLayout.Tab tab) {
//
//                            }
//                        });

                    }else{

                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.v("jgdkjhkj",error.toString());

            }
        });
        requestQueue.add(request);
    }
    private void initMagicIndicator8() {

        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setScrollPivotX(0.35f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mainModels == null ? 0 : mainModels.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mainModels.get(index).getLabel());
                simplePagerTitleView.setNormalColor(Color.parseColor("#333333"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#e94220"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        viewPager.setCurrentItem(index);
                        Log.e("click",index+" f");
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                WrapPagerIndicator indicator = new WrapPagerIndicator(context);
                indicator.setFillColor(Color.parseColor("#ebe4e3"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }
}