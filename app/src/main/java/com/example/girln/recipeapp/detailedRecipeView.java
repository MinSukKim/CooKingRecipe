package com.example.girln.recipeapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.girln.recipeapp.models.CookingIngredientModel;
import com.example.girln.recipeapp.models.CookingPicturesURL;
import com.example.girln.recipeapp.models.CookingStepsModel;
import com.example.girln.recipeapp.models.CookingTagsModel;
import com.example.girln.recipeapp.models.RecipeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import static java.lang.Math.round;

public class detailedRecipeView extends AppCompatActivity {
    private FirebaseDatabase mData;
    private String recipeID;
    RecipeModel recipe = new RecipeModel();
    private FirebaseAuth mUser;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailrecipe);
        Intent intent = getIntent();
        recipeID = intent.getStringExtra("recipeID");
        mData = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        getRecipe(recipeID);
    }

    void afterCreate() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tbTitle = (TextView) findViewById(R.id.toolbarTitle);
        tbTitle.setText(recipe.getRecipeName());
        LinearLayout pictureLL = findViewById(R.id.imageShow);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        for (CookingPicturesURL picturesURL : recipe.getCookingPictures()) {
            ImageView pic = new ImageView(this);
            /*GlideApp.with(this)
                    .load(picturesURL)
                    .into(pic);*/
            pic.setImageURI(Uri.parse(picturesURL.getPictureURL()));
            pic.setLayoutParams(lparams);
            pictureLL.addView(pic);
        }

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(recipe.getRecipeName());

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        LinearLayout ingredientList = findViewById(R.id.ingredientList);

        for (CookingIngredientModel ingredient : recipe.getCookingIngredients()) {
            LinearLayout singleIngredientLayout = new LinearLayout(this);
            singleIngredientLayout.setOrientation(LinearLayout.HORIZONTAL);
            singleIngredientLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2);
            TextView ingredientName = new TextView(this);
            ingredientName.setText(ingredient.getIngredientName());
            ingredientName.setLayoutParams(lparams2);
            singleIngredientLayout.addView(ingredientName);

            TextView ingredientAmount = new TextView(this);
            ingredientAmount.setText(String.valueOf(ingredient.getAmount()));
            ingredientAmount.setLayoutParams(lparams);
            singleIngredientLayout.addView(ingredientAmount);

            TextView ingredientUnit = new TextView(this);
            ingredientUnit.setText(ingredient.getUnit().toString());
            ingredientUnit.setLayoutParams(lparams);
            singleIngredientLayout.addView(ingredientUnit);

            ingredientList.addView(singleIngredientLayout);
        }

        LinearLayout stepList = findViewById(R.id.stepList);

        int i = 0;
        for (CookingStepsModel step : recipe.getCookingSteps()) {
            LinearLayout singleStepLayout = new LinearLayout(this);
            singleStepLayout.setOrientation(LinearLayout.HORIZONTAL);
            singleStepLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            TextView stepNumber = new TextView(this);
            stepNumber.setText((++i) + " :");
            stepNumber.setLayoutParams(lparams);
            singleStepLayout.addView(stepNumber);

            LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2);
            TextView stepDescription = new TextView(this);
            stepDescription.setText(step.getDescription());
            stepDescription.setLayoutParams(lparams2);
            singleStepLayout.addView(stepDescription);

            stepList.addView(singleStepLayout);
        }

        LinearLayout tagList = findViewById(R.id.tagList);

        for (CookingTagsModel tag : recipe.getCookingTags()) {
            LinearLayout singleTagLayout = new LinearLayout(this);
            singleTagLayout.setOrientation(LinearLayout.HORIZONTAL);
            singleTagLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            TextView tagName = new TextView(this);
            tagName.setText(tag.getTagName());
            tagName.setLayoutParams(lparams);
            singleTagLayout.addView(tagName);

            tagList.addView(singleTagLayout);
        }

    }

    public RecipeModel getRecipe(final String recipeID) {
        mData.getReference().child("Recipes").child(recipeID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipe = dataSnapshot.getValue(RecipeModel.class);
                afterCreate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException());
            }
        });
        return recipe;
    }
}
