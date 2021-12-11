package com.app.hitechnews.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.hitechnews.Model.CommentModel;
import com.app.hitechnews.R;

import java.util.List;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.RRViewHolder> {

    private Context context;
    private List<CommentModel> arrayList;

    public CommentAdapter(Context context, List<CommentModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        return new RRViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RRViewHolder holder, final int position) {
        CommentModel list=arrayList.get(position);
        holder.tvName.setText(list.getName());
        holder.tvComment.setText(list.getComment());

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RRViewHolder extends RecyclerView.ViewHolder{
        TextView tvComment,tvName;

        public RRViewHolder(@NonNull View itemView) {
            super(itemView);

            tvComment = itemView.findViewById(R.id.tvComment);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
