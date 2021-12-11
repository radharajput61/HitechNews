package com.app.hitechnews.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.hitechnews.Model.FollowerModel;
import com.app.hitechnews.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.RRViewHolder> {

    private Context context;
    private List<FollowerModel> arrayList;
    int i=0;

    public FollowerAdapter(Context context, List<FollowerModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_list_item, parent, false);
        return new RRViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RRViewHolder holder, final int position) {
        FollowerModel list=arrayList.get(position);
        holder.tvName.setText(list.getName());
        if(list.getAbout().equalsIgnoreCase("null") || list.getAbout().equalsIgnoreCase(""))
        {
            holder.tvAbout.setText("No Caption");
        }else{
            holder.tvAbout.setText(list.getAbout());
        }

        Picasso.with(context).load(list.getProfilePic()).placeholder(R.drawable.placeholder).into(holder.ivProfilePic);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                context.startActivity(new Intent(context, PostDetailsActivity.class)
//                        .putExtra("PostId", list.getPostId())
//                        .putExtra("Title", list.getTitle())
//                        .putExtra("Description", list.getDescription())
//                        .putExtra("Location", list.getLocation())
//                        .putExtra("Date", list.getPostDate())
//                        .putExtra("RelatedTo", list.getPostRelTo())
//                        .putExtra("TotalComment", list.getTotalComment())
//                        .putExtra("TotalLike", list.getTotalLike())
//                        .putExtra("TotalView", list.getTotalView())
//                        .putExtra("VideoLink", list.getVideoLink())
//                );
//
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RRViewHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvAbout;
        ImageView ivProfilePic;

        public RRViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAbout = itemView.findViewById(R.id.tvAbout);
            tvName = itemView.findViewById(R.id.tvName);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
        }
    }
}
