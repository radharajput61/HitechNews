package com.app.hitechnews.videoplayer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.hitechnews.Activity.Follow.FollowerDetailsActivity;
import com.app.hitechnews.Activity.Home.CommentActivity;
import com.app.hitechnews.Model.VideoItem;
import com.app.hitechnews.R;
import com.app.hitechnews.Util.API;
import com.app.hitechnews.Util.MyPreferences;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.exoplayer2.C;

import org.json.JSONException;
import org.json.JSONObject;

public class VideoPlayerViewHolder extends RecyclerView.ViewHolder {

    FrameLayout media_container;
    TextView title, tvPostedBy, tvDescription, tvTime, tvLocation, tvLike, tvFollow, tvComment;
    ImageView thumbnail, volumeControl, iv_dp, share, ivLike, ivComment, iv_vid,dis_like;
    ProgressBar progressBar;
    View parent;
    RequestManager requestManager;
    MyPreferences myPreferences;
    LinearLayout like_share;
    Context context;
    private RelativeLayout ll_header;
    public VideoPlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        parent = itemView;
        myPreferences = new MyPreferences(itemView.getContext());
        media_container = itemView.findViewById(R.id.media_container);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        //title = itemView.findViewById(R.id.title);
        progressBar = itemView.findViewById(R.id.progressBar);
        volumeControl = itemView.findViewById(R.id.volume_control);
        tvPostedBy = itemView.findViewById(R.id.tvPostedBy);
        iv_dp = itemView.findViewById(R.id.iv_dp);
        tvDescription = itemView.findViewById(R.id.tvDescription);
        tvTime = itemView.findViewById(R.id.tvTime);
        tvLocation = itemView.findViewById(R.id.tvLocation);
        tvLike = itemView.findViewById(R.id.tvLike);
        tvFollow = itemView.findViewById(R.id.tvFollow);
        tvComment = itemView.findViewById(R.id.tvComment);
        share = itemView.findViewById(R.id.share);
        ll_header = itemView.findViewById(R.id.ll_header);
        ivLike = itemView.findViewById(R.id.ivLike);
        ivComment = itemView.findViewById(R.id.ivComment);
        iv_vid = itemView.findViewById(R.id.iv_vid);
        dis_like = itemView.findViewById(R.id.dis_like);
        like_share = itemView.findViewById(R.id.like_share);

    }

        public void onBind(VideoItem mediaObject, RequestManager requestManager, Context context) {
        this.context=context;
        this.requestManager = requestManager;
        parent.setTag(this);
        tvPostedBy.setText(mediaObject.getPostedByName());
        tvDescription.setText(mediaObject.getTitle()+"\n\n"+mediaObject.getDescription());
//        tvTime.setText(mediaObject.getTi);
        tvLocation.setText("" + mediaObject.getLocation());
        tvTime.setText(mediaObject.getTotalView()+" view | "+mediaObject.getLocation()+" | " + mediaObject.getPostDate());
        tvLike.setText("" + mediaObject.getTotalLike());
        tvComment.setText("" + mediaObject.getTotalComment());

        if ( myPreferences.getUsertype().equals("Reporter"))
        {
            like_share.setVisibility(View.GONE);
            tvFollow.setVisibility(View.GONE);
        }

        if(mediaObject.getFollowerId().equalsIgnoreCase(myPreferences.getUserId()))
        {
            tvFollow.setVisibility(View.GONE);
        }else{
            if(mediaObject.getIsFollowed().equalsIgnoreCase("1"))
            {
                tvFollow.setText("Followed");
                tvFollow.setTextColor(this.itemView.getResources().getColor(R.color.green));
            }
            else{
                tvFollow.setText("Follow");
            }

        }

        try
        {
            Glide.with(itemView.getContext())
                    .load(mediaObject.getPostedByImage())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(iv_dp);
        }
        catch (Exception e)
        {
            
        }

//        ll_header.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                itemView.getContext().startActivity(new Intent(itemView.getContext(), FollowerDetailsActivity.class)
//                        .putExtra("UserId", mediaObject.getFollowerId())
//                );
//            }
//        });

//        tvFollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setFollowData(mediaObject.getFollowerId(),myPreferences.getUserId());
//            }
//        });

        Log.e("url",mediaObject.getVideoLink());
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_SUBJECT,"Demo Video");
                //intent.setAction(Intent.ACTION_SEND);
                intent.setType("video/mp4");
                intent.putExtra(Intent.EXTRA_STREAM,"http://techslides.com/demos/sample-videos/small.mp4");
                // intent.putExtra(Intent.EXTRA_TEXT, "Watch this video to get a good complement from your friends.");
                try {
                    context.startActivity(Intent.createChooser(intent,"Upload video via:"));

                } catch (android.content.ActivityNotFoundException ex) {
                       Log.e("errorrrrr",ex.toString());
                }

//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.putExtra(Intent.EXTRA_SUBJECT,"Demo Video");
//        // intent.setAction(Intent.ACTION_SEND);
//        intent.setType("video/mp4");
//        intent.putExtra(Intent.EXTRA_STREAM, "http://techslides.com/demos/sample-videos/small.mp4");
//        intent.putExtra(Intent.EXTRA_TEXT, "Watch this video to get a good complement from your friends.");
//
//        try {
//            itemView.getContext().  startActivity(Intent.createChooser(intent,"Upload video via:"));
//        } catch (android.content.ActivityNotFoundException ex) {
//        }


            }
        });
        Log.e("getMyLike",mediaObject.getMyLike());
if (mediaObject.getMyLike().equals("0"))
{
    ivLike.setVisibility(View.VISIBLE);
    dis_like.setVisibility(View.GONE);

}else {

    dis_like.setVisibility(View.VISIBLE);
    ivLike.setVisibility(View.GONE);
}

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dis_like.setVisibility(View.VISIBLE);
                ivLike.setVisibility(View.GONE);
                setLikeData("1",mediaObject.getPostId(),myPreferences.getUserId(),tvLike);

            }
        });

        dis_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ivLike.setVisibility(View.VISIBLE);
                dis_like.setVisibility(View.GONE);
                setLikeData("0",mediaObject.getPostId(),myPreferences.getUserId(),tvLike);

            }
        });

        ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemView.getContext().startActivity(new Intent(itemView.getContext(), CommentActivity.class)
                        .putExtra("PostId", mediaObject.getPostId())
                        .putExtra("Title", mediaObject.getTitle())
                        .putExtra("Description", mediaObject.getDescription())
                        .putExtra("Location", mediaObject.getLocation())
                        .putExtra("Date", mediaObject.getPostDate())
                        .putExtra("RelatedTo", mediaObject.getPostRelTo())
                        .putExtra("TotalComment", mediaObject.getTotalComment())
                        .putExtra("TotalLike", mediaObject.getTotalLike())
                        .putExtra("TotalView", mediaObject.getTotalView())
                        .putExtra("VideoLink", mediaObject.getVideoLink())
                );
            }
        });

        iv_vid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listener.onItemClick(mediaObject);
            }
        });
        
//        this.requestManager
//                .load(mediaObject.getPostedByImage())
//                .placeholder(R.drawable.placeholder)
//                .error(R.drawable.placeholder)
//                .into(iv_dp);
    }

    private void setLikeData(final String LikeStatus, final String PostId,final String UserId,TextView tvLike)
    {
        final JSONObject obj = new JSONObject();
        try
        {
            obj.put("LikeStatus", LikeStatus);
            obj.put("PostId", PostId);
            obj.put("UserId", UserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("sdfuy",obj.toString());
        final RequestQueue requestQueue= Volley.newRequestQueue(itemView.getContext());

        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, API.LikePost, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("response",response.toString());
                try {
                    boolean status=response.getBoolean("Status");

                    String Msg=response.getString("Message");
                    if(status)
                    {
                        if (LikeStatus.equals("0"))
                        {
                            tvLike.setText(""+(Integer.parseInt(tvLike.getText().toString())-1));
                        }else {
                            tvLike.setText(""+(Integer.parseInt(tvLike.getText().toString())+1));
                        }

                      //  Toast.makeText(itemView.getContext(), "Liked", Toast.LENGTH_LONG).show();

                    }else{

                        Toast.makeText(itemView.getContext(), ""+Msg, Toast.LENGTH_LONG).show();

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

    private void setFollowData(final String FollowerId,final String UserId)
    {
        final JSONObject obj = new JSONObject();
        try
        {
            obj.put("FollowerId", FollowerId);
            obj.put("UserId", UserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("sdfuy",obj.toString());
        final RequestQueue requestQueue= Volley.newRequestQueue(itemView.getContext());

        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, API.DoFollow, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("response",response.toString());
                try {
                    boolean status=response.getBoolean("Status");

                    String Msg=response.getString("Message");
                    if(status)
                    {

                        Toast.makeText(itemView.getContext(), "Followed", Toast.LENGTH_LONG).show();
                    }else{

                        Toast.makeText(itemView.getContext(), ""+Msg, Toast.LENGTH_LONG).show();

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