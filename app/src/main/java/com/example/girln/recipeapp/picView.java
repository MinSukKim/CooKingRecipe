package com.example.girln.recipeapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.girln.recipeapp.models.RecipeModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class picView extends AppCompatActivity {
    String recipeID;
    FirebaseDatabase mData;
    FirebaseStorage mStorage;
    private Context context;
    FirebaseAuth mUser;
    RecipeModel recipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_view);
        Intent i = getIntent();
        recipeID = i.getStringExtra("recipeID");
        recipe = new RecipeModel();
        mData = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance();
        mStorage=FirebaseStorage.getInstance();
        getRecipe(recipeID);
        context = this;
    }

    public RecipeModel getRecipe(final String recipeID) {
        mData.getReference().child("Recipes").child(recipeID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipe = dataSnapshot.getValue(RecipeModel.class);
                LinearLayout pictureLinearLayout = findViewById(R.id.pictureLinearLayout1);


                for (final String pic : recipe.getCookingPictures()) {
                    LinearLayout tr = newRow();
                    pictureLinearLayout.addView(tr);
                        ImageView picView = createImageView(pic);

                        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);

                        //todo add clock listener
                        Button deleteButton = new Button(context);
                        deleteButton.setText("Delete");
                        deleteButton.setLayoutParams(lparams);
                        deleteButton.setPadding(20,50,20,50);
                        deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mData.getReference().child("Recipes").child(recipeID).child("cookingPictures").child(String.valueOf(recipe.getCookingPictures().indexOf(pic))).removeValue();
                            StorageReference photoRef = mStorage.getReferenceFromUrl(pic);
                            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // File deleted successfully
                                    Toast.makeText(picView.this, "Delete success", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Uh-oh, an error occurred!
                                    Toast.makeText(picView.this, "Failed deletion", Toast.LENGTH_SHORT).show();
                                }
                            });
                            finish();
                            startActivity(getIntent());
                        }
                    });

                        tr.addView(picView);
                        tr.addView(deleteButton);
                        System.out.println("after");

                }
                TextView PicViewTitle = findViewById(R.id.PicViewTitel);
                PicViewTitle.setText(recipe.getRecipeName());


            }

            private LinearLayout newRow() {
                LinearLayout row = new LinearLayout(context);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,1));
                return row;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException());
            }
        });
        return recipe;
    }

    @NonNull
    private ImageView createImageView(String pic) {
        ImageView picView = new ImageView(context);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 4);
        picView.setLayoutParams(lparams);
        System.out.println("before");
        GlideApp.with(context)
                .load(pic)
                //.centerCrop()
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .transition(DrawableTransitionOptions.withCrossFade()) //Optional
                .skipMemoryCache(true)  //No memory cache
                .diskCacheStrategy(DiskCacheStrategy.NONE)   //No disk cache
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(picView);

        return picView;
    }

    public void closePicView(View v) {
        finish();
    }
}
