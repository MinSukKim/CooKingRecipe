package com.example.girln.recipeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.round;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<item_recipe> item_recipeArrayList;
    private Context context;
    private FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference fb = firebase.getReference();

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

        final String key = (item_recipeArrayList.get(position).key);
//        System.out.println(key);
//        System.out.println(item_recipeArrayList.get(position).cookingPictures);
//        System.out.println(item_recipeArrayList.get(position).title);
        if (item_recipeArrayList.get(position).cookingPictures == null) {
            Uri myUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/posd-befe7.appspot.com/o/RageFace.jpg?alt=media&token=4a7074c3-4f54-46fa-b515-8fcd3acc2613");
            GlideApp.with(context)
                    .load(myUri)
                    .override(160, 80)
                    .into(myViewHolder.ivPicture);
        }
        final double rate = round(item_recipeArrayList.get(position).rate);
        final String title = item_recipeArrayList.get(position).title;
//        final String uri = item_recipeArrayList.get(position).drawabled;
//
        myViewHolder.tvtitle.setText(item_recipeArrayList.get(position).title);
        myViewHolder.tvRate.setRating(round(rate));
//        myViewHolder.tvtags.setText(item_recipeArrayList.get(position).cookingTags);
//
        ((MyViewHolder) holder).tvtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Detailrecipe.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
//                bundle.putString("img", uri);
                bundle.putDouble("rate", rate);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

//        ((MyViewHolder) holder).deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                System.out.println(title);
//                fb = FirebaseDatabase.getInstance().getReference().child("Recipes").child(key);
//                System.out.println(fb);
//                fb.removeValue();
//            }
//        });
//
        ((MyViewHolder) holder).editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Edit Page
//                Intent intent = new Intent(context, Detailrecipe.class);
//                context.startActivity(intent);
            }
        });
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
        Button deleteBtn;
        Button editBtn;

        MyViewHolder(View view) {
            super(view);
            ivPicture = view.findViewById(R.id.iv_picture);
            tvtitle = view.findViewById(R.id.tv_title);
            tvRate = view.findViewById(R.id.tvRate);
            tvtags = view.findViewById(R.id.tv_tags);
            deleteBtn = view.findViewById(R.id.Delete);
            editBtn = view.findViewById(R.id.Edit);
        }


    }
}
