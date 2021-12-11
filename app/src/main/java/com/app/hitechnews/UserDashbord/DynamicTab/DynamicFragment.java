package com.app.hitechnews.UserDashbord.DynamicTab;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.hitechnews.Adapter.VideosAdapter;
import com.app.hitechnews.Fragment.TabFragment;
import com.app.hitechnews.MainActivity;
import com.app.hitechnews.Model.FollowerModel;
import com.app.hitechnews.Model.VideoItem;
import com.app.hitechnews.R;
import com.app.hitechnews.UserDashbord.UserHome;
import com.app.hitechnews.Util.API;
import com.app.hitechnews.Util.MyPreferences;
import com.app.hitechnews.videoplayer.Resources;
import com.app.hitechnews.videoplayer.VerticalSpacingItemDecorator;
import com.app.hitechnews.videoplayer.VideoPlayerRecyclerAdapter;
import com.app.hitechnews.videoplayer.VideoPlayerRecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DynamicFragment extends Fragment {

    ArrayList<VideoItem> videoItems = new ArrayList<>();
    ViewPager2 videosViewPager;
    MyPreferences myPreferences;
    String UserId;
    List<FollowerModel> followerModels = new ArrayList<>();
    private VideoPlayerRecyclerView mRecyclerView;
    VideoItem videoItem;
     String CategoryId="";

    public static DynamicFragment newInstance(String name) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putString("tabName",name);
        fragment.setArguments(args);
        return fragment;
    }

    // adding the layout with inflater
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic, container, false);
        videosViewPager = view.findViewById(R.id.viewPagerVideos);
        myPreferences=new MyPreferences(getActivity());
        UserId=myPreferences.getUserId();
        mRecyclerView = view.findViewById(R.id.recycler_view);
        CategoryId = getArguments().getString("tabName");
        Log.v("fjgsdjhdsf",CategoryId);

        getFollowerData();
        getPostVideoData();





        return view;

    }


    private void getFollowerData()
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
        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, API.MyFollowerList, obj, new Response.Listener<JSONObject>() {
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
                            String AboutUs=object.getString("AboutUs");//
                            String FollowerId=object.getString("FollowerId");
                            String Name=object.getString("Name");//
                            String ProfilePic=object.getString("ProfilePic");//
                            followerModels.add(new FollowerModel(AboutUs,FollowerId,Name,"http://abdekhegabharat.sigmasoftwares.co"+ProfilePic));
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
            obj.put("Cityname", "Lucknow");
            obj.put("Location", "");
            obj.put("CategoryId", CategoryId);
            obj.put("UserId", UserId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("getPostVideoData",obj.toString());

        final RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, API.RandomPostList, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("nsdfghdsf",response.toString());
                try {
                    JSONArray array=response.getJSONArray("Response");
                    Log.v("nsdfghdsf",""+array);

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
                            String Thumbnail="http://abdekhegabharat.sigmasoftwares.co/"+object.getString("Thumbnail");//
                            String FollowerId=object.getString("FollowerId");//
                            String PostedBy=object.getString("PostedBy");//
                            String PostedImg=object.getString("PostedImg");//videoDesc
                            String MyLike=object.getString("MyLike");

//                            videoItems.add(new VideoItem(PostId,Title,Description,Location,PostDate,PostRelTo,TotalComment,TotalLike,TotalView,"http://abdekhegabharat.sigmasoftwares.co"+VideoLink,FollowerId,PostedBy,PostedImg,"1"));

                            boolean isAvail=false;
                            for (int j = 0; j < followerModels.size(); j++)
                            {
                                Log.v("dshgsdja",j+"-"+followerModels.get(j).getFollowerId());
                                Log.v("dsfjk",i+"-"+FollowerId);
                                if(followerModels.get(j).getFollowerId().equalsIgnoreCase(FollowerId))
                                {
                                    isAvail=true;
                                    break;
                                }
                            }
                            if(isAvail)
                            {
                                videoItems.add(new VideoItem(PostId,Title,Description,Location,PostDate,PostRelTo,TotalComment,TotalLike,TotalView,""+VideoLink,Thumbnail,FollowerId,PostedBy,PostedImg,"1",MyLike));
                                Resources.MEDIA_OBJECTS = new VideoItem[]{
                                        new VideoItem(PostId, Title, Description, Location, PostDate, PostRelTo, TotalComment, TotalLike, TotalView, "" + VideoLink,Thumbnail, FollowerId, PostedBy, PostedImg, "1",MyLike)
                                };
                            }else{
                                videoItems.add(new VideoItem(PostId,Title,Description,Location,PostDate,PostRelTo,TotalComment,TotalLike,TotalView,""+VideoLink,Thumbnail,FollowerId,PostedBy,PostedImg,"0",MyLike));
                                Resources.MEDIA_OBJECTS = new VideoItem[]{
                                        new VideoItem(PostId, Title, Description, Location, PostDate, PostRelTo, TotalComment, TotalLike, TotalView, "" + VideoLink,Thumbnail, FollowerId, PostedBy, PostedImg, "0",MyLike)
                                };
                            }
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        mRecyclerView.setLayoutManager(layoutManager);
                        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
                        mRecyclerView.addItemDecoration(itemDecorator);
                        ArrayList<VideoItem> mediaObjects = new ArrayList<VideoItem>(Arrays.asList(Resources.MEDIA_OBJECTS));
                        mRecyclerView.setMediaObjects(videoItems);
                        VideoPlayerRecyclerAdapter adapter = new VideoPlayerRecyclerAdapter(getActivity(),videoItems, initGlide());
                        mRecyclerView.setAdapter(adapter);

                        videosViewPager.setAdapter(new VideosAdapter(getActivity(),videoItems, new VideosAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(VideoItem item) {
                                Log.v("jdshksd",""+item.getPostId());
                                ((MainActivity)getActivity()).showchooser();
//                                Uri vidurl=Uri.parse("http://publicapp.thelive18.com/PostVideos/App_637644742541473917.MP4");
//                                shareVideoWhatsApp(vidurl);
//                                download();
                            }
                        }));
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

    public void shareVideoWhatsApp(Uri uri1) {
        Uri uri = uri1;
        Intent videoshare = new Intent(Intent.ACTION_SEND);
        videoshare.setType("*/*");
        videoshare.setPackage("com.whatsapp");
        videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        videoshare.putExtra(Intent.EXTRA_STREAM,uri);
        startActivity(videoshare);

    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);

//        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
//        mRecyclerView.setLayoutManager(manager);
        ArrayList<VideoItem> mediaObjects = new ArrayList<VideoItem>(Arrays.asList(Resources.MEDIA_OBJECTS));
        mRecyclerView.setMediaObjects(mediaObjects);
        VideoPlayerRecyclerAdapter adapter = new VideoPlayerRecyclerAdapter(getActivity(),mediaObjects, initGlide());
        mRecyclerView.setAdapter(adapter);
    }

    private RequestManager initGlide(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background);

        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }


    @Override
    public void onDestroy() {
        if(mRecyclerView!=null)
            mRecyclerView.releasePlayer();
        super.onDestroy();
    }


    @Override
    public void onResume() {//
        mRecyclerView.setVolumeOn();
        super.onResume();
    }


    @Override
    public void onStop() {
        mRecyclerView.setVolumeOff();
        super.onStop();
    }

}