package com.example.girln.recipeapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<item_recipe> item_recipeArrayList;

    MyAdapter(ArrayList<item_recipe> item_recipeArrayList) {
        this.item_recipeArrayList = item_recipeArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.ivPicture.setImageResource(item_recipeArrayList.get(position).drawableId);
        myViewHolder.tvtitle.setText(item_recipeArrayList.get(position).title);
        myViewHolder.tvrate.setText(item_recipeArrayList.get(position).rate);
        myViewHolder.tvtags.setText(item_recipeArrayList.get(position).tags);
    }

    @Override
    public int getItemCount() {
        return item_recipeArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPicture;
        TextView tvtitle;
        TextView tvrate;
        TextView tvtags;

        MyViewHolder(View view) {
            super(view);
            ivPicture = view.findViewById(R.id.iv_picture);
            tvtitle = view.findViewById(R.id.tv_title);
            tvrate = view.findViewById(R.id.tv_rate);
            tvtags = view.findViewById(R.id.tv_tags);
        }
    }
}
