package com.app.hitechnews.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.app.hitechnews.Model.BGMusic;
import com.app.hitechnews.R;

import java.util.List;


public class BgMusicAdapter extends RecyclerView.Adapter<BgMusicAdapter.RRViewHolder> {

    private Context context;
    private List<BGMusic> arrayList;

    private final OnItemClickListener listener;
    public static String btnClick;
    public static int myposition;
    public interface OnItemClickListener {
        void onItemClick(BGMusic item);
    }

    public BgMusicAdapter(Context context, List<BGMusic> arrayList, OnItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout._bg_music_list_item, parent, false);

        return new RRViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RRViewHolder holder, final int position) {
        BGMusic list=arrayList.get(position);

        holder.tvNameVL.setText(" "+list.getName());
        holder.bind(arrayList.get(position),listener, position);




    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RRViewHolder extends RecyclerView.ViewHolder{
        TextView tvNameVL,play,add;

        public RRViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameVL = itemView.findViewById(R.id.tvNameVL);

            add = itemView.findViewById(R.id.add);
            play = itemView.findViewById(R.id.play);

        }
        public void bind(final BGMusic item, final OnItemClickListener listener, final int position) {

            add.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    btnClick="add";
                    myposition=position;
                    listener.onItemClick(item);
                    notifyDataSetChanged();
                }
            });
            play.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    btnClick="play";
                    myposition=position;
                    listener.onItemClick(item);
                    notifyDataSetChanged();
                }
            });
        }
    }




}
