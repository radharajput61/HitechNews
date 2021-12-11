package com.app.hitechnews.Activity.Profile.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.app.hitechnews.Adapter.MyPostListAdapter;
import com.app.hitechnews.Model.VideoItem;
import com.app.hitechnews.R;
import com.app.hitechnews.Util.API;
import com.app.hitechnews.Util.MyPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MyPostFragment extends Fragment {

    MyPreferences myPreferences;
    String UserId;
    RecyclerView video_recyclerview;
    List<VideoItem> videoItems = new ArrayList<>();
    MyPostListAdapter myPostListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_my_post, container, false);
        myPreferences=new MyPreferences(getActivity());
        UserId=myPreferences.getUserId();
        video_recyclerview=root.findViewById(R.id.video_recyclerview);
        video_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(),3));

        getPostVideoData();
        return root;
    }

    private void getPostVideoData()
    {

        final JSONObject obj = new JSONObject();
        try
        {
            obj.put("UserId", UserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("hgsdfad",obj.toString());

        final RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, API.MyPostList, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("response",response.toString());
                try {
                    JSONArray array=response.getJSONArray("Response");
                    Log.v("adfghd",""+array);

                    if(array.length()>0)
                    {
                        for (int i=0;i<array.length();i++)
                        {
                            Log.v("hvjfg",array.toString());
                            JSONObject object=array.getJSONObject(i);
                            String PostId=object.getString("PostId");//
                            String Title=object.getString("Title");
                            String Description=object.getString("Description");//
                            String Location=object.getString("Location");//
                            String PostDate=object.getString("PostDate");//
                            String PostRelTo=object.getString("PostRelTo");//
                            String TotalLike=object.getString("TotalLike");//
                            String TotalComment=object.getString("TotalComment");//
                            String TotalView=object.getString("TotalView");//
                            String VideoLink=object.getString("VideoLink");//

                            videoItems.add(new VideoItem(PostId,Title,Description,Location,PostDate,PostRelTo,TotalComment,TotalLike,TotalView,""+VideoLink,"","","","","",""));
                        }
                        myPostListAdapter =new MyPostListAdapter(getContext(),videoItems);
                        video_recyclerview.setAdapter(myPostListAdapter);
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
}