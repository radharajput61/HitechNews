package com.app.hitechnews.Activity.Search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.app.hitechnews.Adapter.FollowerSearchAdapter;
import com.app.hitechnews.Adapter.SearchPostListAdapter;
import com.app.hitechnews.Adapter.TopicAdapter;
import com.app.hitechnews.Model.FollowerModel;
import com.app.hitechnews.Model.TopicModel;
import com.app.hitechnews.Model.VideoItem;
import com.app.hitechnews.R;
import com.app.hitechnews.Util.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    List<TopicModel> topicModels = new ArrayList<>();
    List<FollowerModel> followerModels = new ArrayList<>();
    List<VideoItem> videoItems = new ArrayList<>();
    TopicAdapter topicAdapter;
    RecyclerView popular_tag_recyclerview,popular_user_recyclerview,popular_trading_recyclerview;
    FollowerSearchAdapter followerSearchAdapter;
    SearchPostListAdapter searchPostListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_search, container, false);
        popular_tag_recyclerview=root.findViewById(R.id.popular_tag_recyclerview);
        popular_tag_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        popular_user_recyclerview=root.findViewById(R.id.popular_user_recyclerview);
        popular_user_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        popular_trading_recyclerview=root.findViewById(R.id.popular_trading_recyclerview);
        popular_trading_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        getSearchData();
        return root;
    }
    private void getSearchData()
    {
        final RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, API.Tranding_PostFollowerTopicList, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("response",response.toString());
                try {
//                    JSONArray TopicDetails=response.getJSONArray("TopicDetails");
                    JSONArray FollowerDetails=response.getJSONArray("FollowerDetails");
                    JSONArray PostDetails=response.getJSONArray("PostDetails");
                    Log.v("jdfshk",""+FollowerDetails);

//                    if(TopicDetails.length()>0)
//                    {
//                        for (int i=0;i<TopicDetails.length();i++)
//                        {
//                            Log.v("hvjfg",TopicDetails.toString());
//                            JSONObject object=TopicDetails.getJSONObject(i);
//                            String Description=object.getString("Description");//
//                            String TopicId=object.getString("TopicId");
//                            String TopicName=object.getString("TopicName");//
//                            String TopicType=object.getString("TopicType");//
//                            String UserId=object.getString("UserId");//
//
//                            topicModels.add(new TopicModel(Description,TopicId,TopicName,TopicType,UserId));
//                        }
//                        topicAdapter =new TopicAdapter(getActivity(),topicModels);
//                        popular_tag_recyclerview.setAdapter(topicAdapter);
//                    }else{
////                        emptyMsg.setVisibility(View.VISIBLE);
////                        tvNetworkError.setVisibility(View.GONE);
////                        loader.setVisibility(View.GONE);
//                    }
                    if(FollowerDetails.length()>0)
                    {
                        for (int i=0;i<FollowerDetails.length();i++)
                        {
                            Log.v("hvjfg",FollowerDetails.toString());
                            JSONObject object=FollowerDetails.getJSONObject(i);
                            String AboutUs=object.getString("AboutUs");//
                            String FollowerId=object.getString("FollowerId");
                            String Name=object.getString("Name");//
                            String ProfilePic=object.getString("ProfilePic");//

                            followerModels.add(new FollowerModel(AboutUs,FollowerId,Name,"http://abdekhegabharat.sigmasoftwares.co"+ProfilePic));
                        }
                        followerSearchAdapter =new FollowerSearchAdapter(getActivity(),followerModels);
                        popular_user_recyclerview.setAdapter(followerSearchAdapter);
                    }else{
//                        emptyMsg.setVisibility(View.VISIBLE);
//                        tvNetworkError.setVisibility(View.GONE);
//                        loader.setVisibility(View.GONE);
                    }

                    if(PostDetails.length()>0)
                    {
                        for (int i=0;i<PostDetails.length();i++)
                        {
                            Log.v("hvjfg",PostDetails.toString());
                            JSONObject object=PostDetails.getJSONObject(i);
                            String CityName=object.getString("CityName");//
                            String Description=object.getString("Description");
                            String Location=object.getString("Location");//
                            String PostId=object.getString("PostId");//
                            String PostRelTo=object.getString("PostRelTo");//
                            String UserId=object.getString("UserId");//
                            String VideoTitle=object.getString("VideoTitle");//
                            String Videolink=object.getString("Videolink");//

                            videoItems.add(new VideoItem(PostId,VideoTitle,Description,CityName,"",PostRelTo,"","","",""+Videolink,"","","","","1",""));
                        }
                        searchPostListAdapter =new SearchPostListAdapter(getContext(),videoItems);
                        popular_trading_recyclerview.setAdapter(searchPostListAdapter);
                    }else{
//                        emptyMsg.setVisibility(View.VISIBLE);
//                        tvNetworkError.setVisibility(View.GONE);
//                        loader.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
//                    emptyMsg.setVisibility(View.GONE);
//                    tvNetworkError.setVisibility(View.VISIBLE);
//                    loader.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("jgdkjhkj",error.toString());
//                emptyMsg.setVisibility(View.GONE);
//                tvNetworkError.setVisibility(View.VISIBLE);
//                loader.setVisibility(View.GONE);
            }
        });
        requestQueue.add(request);
    }
}