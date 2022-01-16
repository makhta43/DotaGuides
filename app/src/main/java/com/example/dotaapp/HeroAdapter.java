package com.example.dotaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HeroAdapter<context> extends RecyclerView.Adapter<HeroAdapter.myViewHolder> {
    private ArrayList<Hero> heroList;
    private OnHeroListener mOnHeroListener;
    private int currentId;

    public HeroAdapter(ArrayList<Hero> heroList, OnHeroListener onHeroListener){
        this.heroList = heroList;
        this.mOnHeroListener = onHeroListener;
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameText;
        private ImageView image;
        OnHeroListener onHeroListener;

        public myViewHolder(final View view, OnHeroListener onHeroListener){
            super(view);
            nameText = view.findViewById(R.id.nameLabel);
            image = view.findViewById(R.id.portrait);
            this.onHeroListener = onHeroListener;
            itemView.setOnClickListener(this);
        }

        //position isn't used as the position of a hero in the list changes when searched
        //the id associated with the API are also inconsistent therefore I created an atomic int
        //the atomic int is used as a more consistent internal id
        @Override
        public void onClick(View v) {
            for(Hero hero: heroList){
                if(nameText.getText().equals(hero.getLocalized_name())){
                    currentId = hero.getAtomicId();
                }
            }
            onHeroListener.onHeroClick(currentId);
        }
    }
    public interface OnHeroListener{
        void onHeroClick(int position);
    }

    @NonNull
    @Override
    public HeroAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new myViewHolder(itemView, mOnHeroListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroAdapter.myViewHolder holder, int position) {
        String name = heroList.get(position).getLocalized_name();
        String URL = heroList.get(position).getFullImg();
        holder.nameText.setText(name);
        Glide.with(holder.image.getContext()).load(URL).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return heroList.size();
    }

    //method that filters the list of heros when searched
    public void filterList(ArrayList<Hero> filteredList){
        heroList = filteredList;
        notifyDataSetChanged();
    }
}
