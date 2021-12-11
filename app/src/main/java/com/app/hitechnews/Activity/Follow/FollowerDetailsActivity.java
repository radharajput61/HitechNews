package com.app.hitechnews.Activity.Follow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FollowerDetailsActivity extends AppCompatActivity {

    String UserId;
    RecyclerView post_recyclerview;
    List<VideoItem> videoItems = new ArrayList<>();
    MyPostListAdapter myPostListAdapter;
    TextView tvName,tvAbout;
    ImageView tvProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_details);
        UserId=getIntent().getStringExtra("UserId");
        post_recyclerview=findViewById(R.id.post_recyclerview);
        tvName=findViewById(R.id.tvName);
        tvAbout=findViewById(R.id.tvAbout);
        tvProfilePic=findViewById(R.id.tvProfilePic);
        post_recyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        getFollowerDetailsData();
        getPostVideoData();
    }

    private void getFollowerDetailsData()
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
        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, API.FollowerDetails, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("response",response.toString());
                try {

                    boolean status=response.getBoolean("Status");
                    if(status)
                    {
                        String AboutUs=response.getString("AboutUs");
                        String Name=response.getString("Name");
                        String ProfilePic=response.getString("ProfilePic");
                        tvName.setText(Name);
                        if(AboutUs.equalsIgnoreCase("") || AboutUs.equalsIgnoreCase("null"))
                            tvAbout.setText("No Caption");
                        else
                            tvAbout.setText(AboutUs);

                        try {
                            Picasso.with(getApplicationContext()).load(ProfilePic).placeholder(R.drawable.placeholder).into(tvProfilePic);

                        } catch (Exception e) {
                        }
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

                            videoItems.add(new VideoItem(PostId,Title,Description,Location,PostDate,PostRelTo,TotalComment,TotalLike,TotalView,VideoLink,"","","","","",""));
                        }
                        myPostListAdapter =new MyPostListAdapter(getApplicationContext(),videoItems);
                        post_recyclerview.setAdapter(myPostListAdapter);
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