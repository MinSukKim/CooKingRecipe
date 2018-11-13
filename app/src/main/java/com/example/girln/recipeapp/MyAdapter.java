package com.example.girln.recipeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import static java.lang.Math.round;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<item_recipe> item_recipeArrayList;
    private Context context;

    MyAdapter(ArrayList<item_recipe> item_recipeArrayList) {
        this.item_recipeArrayList = item_recipeArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        context = parent.getContext();
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        Uri myUri = Uri.parse(item_recipeArrayList.get(position).drawabled);

        GlideApp.with(context)
                .load(myUri)
                .into(myViewHolder.ivPicture);

        myViewHolder.tvtitle.setText(item_recipeArrayList.get(position).title);
        myViewHolder.tvRate.setRating(round(item_recipeArrayList.get(position).rate));
        myViewHolder.tvtags.setText(item_recipeArrayList.get(position).tags);


    }

    @Override
    public int getItemCount() {
        return item_recipeArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPicture;
        TextView tvtitle;
        RatingBar tvRate;
        TextView tvtags;

        MyViewHolder(View view) {
            super(view);
            ivPicture = view.findViewById(R.id.iv_picture);
            tvtitle = view.findViewById(R.id.tv_title);
            tvRate = view.findViewById(R.id.tvRate);
            tvtags = view.findViewById(R.id.tv_tags);
        }


    }
}
