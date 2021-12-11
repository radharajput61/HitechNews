package com.app.hitechnews.Activity.Follow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.hitechnews.Adapter.FollowerAdapter;
import com.app.hitechnews.MainActivity;
import com.app.hitechnews.Model.FollowerModel;
import com.app.hitechnews.R;
import com.app.hitechnews.Util.API;
import com.app.hitechnews.Util.MyPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FollowingListActivity extends AppCompatActivity {

    MyPreferences myPreferences;
    String UserId;
    RecyclerView following_recyclerview;
    List<FollowerModel> followerModels = new ArrayList<>();
    FollowerAdapter followerAdapter;
    LinearLayout loader;
    TextView emptyMsg;
    ImageView tvBack;
    TextView tvNetworkError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_list);
        emptyMsg= findViewById(R.id.emptyMsg);
        tvBack= findViewById(R.id.tvBack);
        tvNetworkError= findViewById(R.id.tvNetworkError);
        loader=findViewById(R.id.loader);
        myPreferences=new MyPreferences(getApplicationContext());
        UserId=myPreferences.getUserId();
        following_recyclerview=findViewById(R.id.following_recyclerview);
        following_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        getPostVideoData();
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

        final RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, API.MyFollowingList, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("response",response.toString());
                try {
                    JSONArray array=response.getJSONArray("Response");
                    Log.v("adfghd",""+array);

                    if(array.length()>0)
                    {
                        emptyMsg.setVisibility(View.GONE);
                        tvNetworkError.setVisibility(View.GONE);
                        loader.setVisibility(View.GONE);
                        for (int i=0;i<array.length();i++)
                        {
                            Log.v("hvjfg",array.toString());
                            JSONObject object=array.getJSONObject(i);
                            String AboutUs=object.getString("AboutUs");//
                            String FollowerId=object.getString("FollowerId");
                            String Name=object.getString("Name");//
                            String ProfilePic=object.getString("ProfilePic");//

                            followerModels.add(new FollowerModel(AboutUs,FollowerId,Name,"http://abdekhegabharat.sigmasoftwares.co"+ProfilePic));
                        }
                        followerAdapter =new FollowerAdapter(getApplicationContext(),followerModels);
                        following_recyclerview.setAdapter(followerAdapter);
                    }else{
                        emptyMsg.setVisibility(View.VISIBLE);
                        tvNetworkError.setVisibility(View.GONE);
                        loader.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    emptyMsg.setVisibility(View.GONE);
                    tvNetworkError.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("jgdkjhkj",error.toString());
                emptyMsg.setVisibility(View.GONE);
                tvNetworkError.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
            }
        });
        requestQueue.add(request);
    }
}