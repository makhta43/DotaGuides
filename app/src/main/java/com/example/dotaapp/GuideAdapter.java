package com.example.dotaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.MyViewHolder>{

    ArrayList<Guide> guideList;
    private OnGuideListener mOnGuideListener;

    public GuideAdapter(ArrayList<Guide> guideList, OnGuideListener onGuideListener) {
        this.guideList = guideList;
        this.mOnGuideListener = onGuideListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guide_items, parent, false);
        return new MyViewHolder(view, mOnGuideListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Guide guide = guideList.get(position);
        //guide.getId();
        holder.title.setText(guide.getTitle());
        holder.username.setText("By: " + guide.getUsername());
    }

    @Override
    public int getItemCount() {
        return guideList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, string, username;
        OnGuideListener onGuideListener;

        public MyViewHolder(@NonNull View itemView, OnGuideListener onGuideListener) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.guideTitle);
            username = (TextView) itemView.findViewById(R.id.guideName);
            this.onGuideListener = onGuideListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onGuideListener.onGuideClick(getAdapterPosition());
        }
    }
    public interface OnGuideListener{
        void onGuideClick(int currentId);
    }
}
