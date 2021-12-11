package com.app.hitechnews.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.hitechnews.Model.TopicModel;
import com.app.hitechnews.R;

import java.util.List;


public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.RRViewHolder> {

    private Context context;
    private List<TopicModel> arrayList;
    int i=0;

    public TopicAdapter(Context context, List<TopicModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_list_item, parent, false);
        return new RRViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RRViewHolder holder, final int position) {
        TopicModel list=arrayList.get(position);
        holder.tvTopic.setText(list.getTopicName());



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
        TextView tvTopic;

        public RRViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTopic = itemView.findViewById(R.id.tvTopic);
        }
    }
}
