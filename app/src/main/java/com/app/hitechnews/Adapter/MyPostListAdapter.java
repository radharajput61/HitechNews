package com.app.hitechnews.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.hitechnews.Activity.Post.PostDetailsActivity;
import com.app.hitechnews.Model.VideoItem;
import com.app.hitechnews.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MyPostListAdapter extends RecyclerView.Adapter<MyPostListAdapter.RRViewHolder> {

    private Context context;
    private List<VideoItem> arrayList;
    int i=0;

    public MyPostListAdapter(Context context, List<VideoItem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);
        return new RRViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RRViewHolder holder, final int position) {
        VideoItem list=arrayList.get(position);
        holder.tvDate.setText(list.getPostDate());
        holder.tvTitle.setText(list.getTitle());
        Picasso.with(context).load(list.getVideoLink()).placeholder(R.drawable.placeholder_image).into(holder.ivThumb);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra("PostId", list.getPostId())
                        .putExtra("Title", list.getTitle())
                        .putExtra("Description", list.getDescription())
                        .putExtra("Location", list.getLocation())
                        .putExtra("Date", list.getPostDate())
                        .putExtra("RelatedTo", list.getPostRelTo())
                        .putExtra("TotalComment", list.getTotalComment())
                        .putExtra("TotalLike", list.getTotalLike())
                        .putExtra("TotalView", list.getTotalView())
                        .putExtra("VideoLink", list.getVideoLink());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RRViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate,tvTitle;
        ImageView ivThumb;

        public RRViewHolder(@NonNull View itemView) {
            super(itemView);

            ivThumb = itemView.findViewById(R.id.ivThumb);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
