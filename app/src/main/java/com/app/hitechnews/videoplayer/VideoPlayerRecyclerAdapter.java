package com.app.hitechnews.videoplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.hitechnews.Model.VideoItem;
import com.app.hitechnews.R;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

public class VideoPlayerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<VideoItem> mediaObjects;
    private RequestManager requestManager;
Context context;

    public VideoPlayerRecyclerAdapter(Context context,ArrayList<VideoItem> mediaObjects, RequestManager requestManager) {
        this.mediaObjects = mediaObjects;
        this.requestManager = requestManager;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VideoPlayerViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_video_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((VideoPlayerViewHolder)viewHolder).onBind(mediaObjects.get(i), requestManager,context);
    }

    @Override
    public int getItemCount() {
        return mediaObjects.size();
    }

}