package com.app.hitechnews.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hb.xvideoplayer.MxVideoPlayerWidget;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder>{
    private List<VideoItem> mVideoItems;
    private final OnItemClickListener listener;
    private Context context;
    MyPreferences myPreferences;
    String UserId;

    public interface OnItemClickListener {
        void onItemClick(VideoItem item);
    }
    public VideosAdapter(Context context,List<VideoItem> videoItems,OnItemClickListener listener) {
        this.context = context;
        this.mVideoItems = videoItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        if (mVideoItems.get(position) != null) {
            myPreferences=new MyPreferences(context);
            UserId=myPreferences.getUserId();
            holder.setVideoData(mVideoItems.get(position),listener);
        }
    }

    @Override
    public int getItemCount() {
        return mVideoItems.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        VideoView mVideoView;
        ImageView iv_vid,ivLike;
        TextView tvDescription, tvLocation,tvTime,tvLike,tvComment,tvPostedBy,tvFollow;
        ProgressBar mProgressBar;
        CircleImageView iv_dp;
        ImageView pause,ivComment;
        RelativeLayout ll_header;
        MxVideoPlayerWidget list_video_player;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            pause = (ImageView) itemView.findViewById(R.id.pause);
            ivLike = itemView.findViewById(R.id.ivLike);
            iv_vid = itemView.findViewById(R.id.iv_vid);
            mVideoView = itemView.findViewById(R.id.vid);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvLike = itemView.findViewById(R.id.tvLike);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvPostedBy = itemView.findViewById(R.id.tvPostedBy);
            tvFollow = itemView.findViewById(R.id.tvFollow);
            mProgressBar = itemView.findViewById(R.id.progress_bar);
            iv_dp = itemView.findViewById(R.id.iv_dp);
            ivComment = itemView.findViewById(R.id.ivComment);
            ll_header = itemView.findViewById(R.id.ll_header);
            list_video_player = itemView.findViewById(R.id.list_video_player);

        }

        void setVideoData(VideoItem videoItem,final OnItemClickListener listener) {
            mProgressBar.setVisibility(View.VISIBLE);
            mVideoView.setVideoURI(Uri.parse(videoItem.getVideoLink()));
//            list_video_player.startPlay(""+videoItem.getVideoLink(),MxVideoPlayer.SCREEN_LAYOUT_NORMAL,"Demo");
            tvDescription.setText("" + videoItem.getDescription());
            tvLocation.setText("" + videoItem.getLocation());
            tvTime.setText(videoItem.getTotalView()+" view | "+videoItem.getLocation()+" | " + videoItem.getPostDate());
            tvLike.setText("" + videoItem.getTotalLike());
            tvComment.setText("" + videoItem.getTotalComment());
            tvPostedBy.setText(videoItem.getPostedByName());
            if(videoItem.getFollowerId().equalsIgnoreCase(UserId))
            {
                tvFollow.setVisibility(View.GONE);
            }else{
                if(videoItem.getIsFollowed().equalsIgnoreCase("1"))
                {
                    tvFollow.setText("Followed");
                    tvFollow.setTextColor(context.getResources().getColor(R.color.green));
                }else{
                    tvFollow.setText("Follow");
                }

            }

            try {
                Picasso.with(context).load(videoItem.getPostedByImage()).placeholder(R.drawable.placeholder).into(iv_dp);

            } catch (Exception e) {
            }
            iv_vid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(videoItem);
                }
            });
            ll_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, FollowerDetailsActivity.class)
                            .putExtra("UserId", videoItem.getFollowerId())
                    );
                }
            });
            ivComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, CommentActivity.class)
                            .putExtra("PostId", videoItem.getPostId())
                            .putExtra("Title", videoItem.getTitle())
                            .putExtra("Description", videoItem.getDescription())
                            .putExtra("Location", videoItem.getLocation())
                            .putExtra("Date", videoItem.getPostDate())
                            .putExtra("RelatedTo", videoItem.getPostRelTo())
                            .putExtra("TotalComment", videoItem.getTotalComment())
                            .putExtra("TotalLike", videoItem.getTotalLike())
                            .putExtra("TotalView", videoItem.getTotalView())
                            .putExtra("VideoLink", videoItem.getVideoLink())
                    );
                }
            });
            pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mVideoView.isPlaying()) {
                        mVideoView.pause();
                        pause.setImageResource(R.drawable.ic_baseline_play_circle_filled_241);
                    } else {
                        mVideoView.start();
                        pause.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                    }
                }
            });
            tvFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFollowData(videoItem.getFollowerId(),UserId);
                }
            });
            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLikeData("1",videoItem.getPostId(),UserId,tvLike);
                }
            });
            Log.v("videoItem.videoURL", "" + videoItem.getVideoLink());
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mProgressBar.setVisibility(View.GONE);
                    mp.start();
                    pause.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                    float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                    float screenRatio = mVideoView.getWidth() / (float) mVideoView.getHeight();
                    float scale = videoRatio / screenRatio;
                    if (scale >= 1f) {
                        mVideoView.setScaleX(scale);
                    } else {
                        mVideoView.setScaleY(1f / scale);
                    }
                }
            });
            mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
//                    mp.start();
                    pause.setImageResource(R.drawable.ic_baseline_play_circle_filled_241);
                }
            });
        }
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
        final RequestQueue requestQueue= Volley.newRequestQueue(context);

        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, API.LikePost, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("response",response.toString());
                try {
                    boolean status=response.getBoolean("Status");

                    String Msg=response.getString("Message");
                    if(status)
                    {
                        tvLike.setText(""+(Integer.parseInt(tvLike.getText().toString())+1));
                        Toast.makeText(context, "Liked", Toast.LENGTH_LONG).show();
                    }else{

                        Toast.makeText(context, ""+Msg, Toast.LENGTH_LONG).show();

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
        final RequestQueue requestQueue= Volley.newRequestQueue(context);

        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, API.DoFollow, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("response",response.toString());
                try {
                    boolean status=response.getBoolean("Status");

                    String Msg=response.getString("Message");
                    if(status)
                    {

                        Toast.makeText(context, "Followed", Toast.LENGTH_LONG).show();
                    }else{

                        Toast.makeText(context, ""+Msg, Toast.LENGTH_LONG).show();

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
