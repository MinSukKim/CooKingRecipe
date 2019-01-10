package com.example.girln.recipeapp;

import android.content.Context;
import android.content.Intent;
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

import com.example.girln.recipeapp.models.CookingTagsModel;
import com.example.girln.recipeapp.models.RecipeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.girln.recipeapp.UserRights.needsToOwn;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> recipeIdList;
    private RecipeModel recipe;
    private Context context;
    private FirebaseAuth mUser;
    private FirebaseDatabase mData = FirebaseDatabase.getInstance();

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
        mUser= FirebaseAuth.getInstance();
        recipe = getRecipe(recipeID, myViewHolder);
    }

    public void afterCreate(MyViewHolder viewHolder, final String recipeID) {

        viewHolder.tvtitle.setText(recipe.getRecipeName());

        ArrayList<String> tem = recipe.getCookingPictures();
        if (!tem.isEmpty()) {
            GlideApp.with(context)
                    .load(tem.get(0))
                    .into(viewHolder.ivPicture);
        }
        if(recipe.getRating() != null){
            double score = recipe.getRating();
            if(score!=0){
                viewHolder.tvRate.setRating((float) score);
            }
        }
        String str = "";
        for (CookingTagsModel tag : recipe.getCookingTags()) {
            str = str+"#"+tag.getTagName()+"  ";
        }
        if(str!="")
            viewHolder.tvtags.setText(str);

        viewHolder.tvtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, detailedRecipeView.class);
                intent.putExtra("recipeID", recipeID);
                context.startActivity(intent);
            }
        });

        needsToOwn(viewHolder.deleteBtn,mUser,recipe.getUserID());
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mData.getReference().child("Recipes").child(recipeID).removeValue();
                mData.getReference().child("Comments").child(recipeID).removeValue();
                mData.getReference().child("Ratings").child(recipeID).removeValue();
            }
        });

        needsToOwn(viewHolder.editBtn,mUser,recipe.getUserID());
        viewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, edit.class);
                intent.putExtra("recipeID", recipeID);
                context.startActivity(intent);
            }
        });

    }


    private RecipeModel getRecipe(final String recipeID, final MyViewHolder myViewHolder) {
        mData.getReference().child("Recipes").child(recipeID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipe = dataSnapshot.getValue(RecipeModel.class);
                afterCreate(myViewHolder, recipeID);
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
