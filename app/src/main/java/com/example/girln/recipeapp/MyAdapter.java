package com.example.girln.recipeapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.example.girln.recipeapp.models.RecipeModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> recipeIdList;
    private ArrayList<item_recipe> item_recipeArrayList;
    RecipeModel recipe;
    private Context context;
    private FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();


    MyAdapter(List<String> recipeIDList) {
        this.notifyDataSetChanged();
        this.recipeIdList = recipeIDList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        context = parent.getContext();
        recipe = new RecipeModel();
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        String recipeID = recipeIdList.get(position);
        recipe = getRecipe(recipeID,myViewHolder);

    }
    public void afterCreate(MyViewHolder viewHolder, final String recipeID)
    {
//        System.out.println(recipe);
        viewHolder.tvtitle.setText(recipe.getRecipeName());
        //        System.out.println(title);
//        final int positiont = position;
//        final String key = (item_recipeArrayList.get(position).key);
//        List tag = item_recipeArrayList.get(position).cookingTags;
//        List pics = item_recipeArrayList.get(position).cookingPictures;
//        String pic_url = pics.get(0).toString();
        ArrayList tem = recipe.getCookingPictures();
        System.out.println(tem.get(0));
        StorageReference tmp_imgs = storage.getReference().child("images").child(tem.get(0).toString());
        System.out.println(tmp_imgs);
//
            GlideApp.with(context)
                    .load(tmp_imgs)
                    .into(viewHolder.ivPicture);
//
//
//        final double rate = round(item_recipeArrayList.get(position).rate);
//        final String title = item_recipeArrayList.get(position).title;
//
//        myViewHolder.tvtitle.setText(item_recipeArrayList.get(position).title);
//        viewHolder.tvRate.setRating(recipe.get);
//        viewHolder.tvtags.setText(recipe.getCookingTags());
//
        ((MyViewHolder) viewHolder).tvtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, detailedRecipeView.class);
                intent.putExtra("recipeID", recipeID);
                context.startActivity(intent);
            }
        });

//        ((MyViewHolder) viewHolder).deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fb = FirebaseDatabase.getInstance().getReference().child("Recipes").child(recipeID);
//                fb.removeValue();
//            }
//        });

        ((MyViewHolder) viewHolder).editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,edit.class );
                intent.putExtra("recipe",recipeID);
                context.startActivity(intent);
            }
        });
    }


    private RecipeModel getRecipe(final String recipeID, final MyViewHolder myViewHolder) {
        System.out.println(recipeID);
        firebase.getReference().child("Recipes").child(recipeID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipe = dataSnapshot.getValue(RecipeModel.class);
               afterCreate(myViewHolder,recipeID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException());
            }
        });
        return recipe;
    }

    @Override
    public int getItemCount() {
        return recipeIdList.size();
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
