package com.example.girln.recipeapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.request.target.Target;
import com.example.girln.recipeapp.models.CommentModel;
import com.example.girln.recipeapp.models.CookingIngredientModel;
import com.example.girln.recipeapp.models.CookingStepsModel;
import com.example.girln.recipeapp.models.CookingTagsModel;
import com.example.girln.recipeapp.models.RecipeModel;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.api.GoogleApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.girln.recipeapp.UserRights.needsToNotOwn;
import static com.example.girln.recipeapp.UserRights.needsToOwn;
import static java.lang.Math.round;

public class detailedRecipeView extends AppCompatActivity {
    private FirebaseDatabase mData;
    private String recipeID;
    RecipeModel recipe = new RecipeModel();
    private FirebaseAuth mUser;
    FirebaseStorage storage;
    StorageReference storageReference;
    RatingBar ratingBar;
    EditText commentField;

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
        ratingBar = findViewById(R.id.ratingBar);
        commentField = findViewById(R.id.commentTextField);
        getRecipe(recipeID);
    }

    void afterCreate() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tbTitle = findViewById(R.id.toolbarTitle);
        tbTitle.setText(recipe.getRecipeName());

        LinearLayout pictureLL = findViewById(R.id.imageShow);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        for (String picturesURL : recipe.getCookingPictures()) {
//            System.out.println(picturesURL);
            ImageView pic = new ImageView(this);
            GlideApp.with(this)
                    .load(picturesURL)
                    .override(Target.SIZE_ORIGINAL, 300)
                    .into(pic);
            pic.setLayoutParams(lparams);
            pictureLL.addView(pic);
        }

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(recipe.getRecipeName());

        LinearLayout ingredientList = findViewById(R.id.ingredientList);
        populatingIngredient(lparams, ingredientList);

        LinearLayout stepList = findViewById(R.id.stepList);
        populatingSteps(lparams, stepList);

        LinearLayout tagList = findViewById(R.id.tagList);
        populatingTags(lparams, tagList);

        LinearLayout commentLL = findViewById(R.id.CommentLayout);
        ArrayList<CommentModel> comments = getComments(recipeID, commentLL);

        //to prevent people from rating their own recipes
        Button ratingBtn=findViewById(R.id.ratingButton);
        needsToNotOwn(ratingBtn,mUser,recipe.getUserID());


    }

    private void populatingComments(LinearLayout commentLL, ArrayList<CommentModel> comments) {
        for (CommentModel commentModel : comments) {
            TextView commentText = new TextView(this);
            commentText.setText(commentModel.getUName()+" :"+commentModel.getComment());
            commentText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            commentLL.addView(commentText);
        }
    }

    private ArrayList<CommentModel> getComments(String recipeID, final LinearLayout commentLL) {
        final ArrayList<CommentModel> comments = new ArrayList<>();
        mData.getReference().child("Comments").child(recipeID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot comment :
                        dataSnapshot.getChildren()) {
                    CommentModel com = comment.getValue(CommentModel.class);
                    comments.add(com);
                }


                populatingComments(commentLL, comments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException());
            }
        });
        return comments;
    }

    private void populatingTags(LinearLayout.LayoutParams lparams, LinearLayout tagList) {
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

    private void populatingSteps(LinearLayout.LayoutParams lparams, LinearLayout stepList) {
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
    }

    private void populatingIngredient(LinearLayout.LayoutParams lparams, LinearLayout ingredientList) {
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
    }

    public void rateRecipe(View v) {
        if (mUser.getUid() != null) {
            mData.getReference().child("Ratings").child(recipeID).child(mUser.getUid()).setValue(ratingBar.getRating());
            mData.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map ratedata = (HashMap) dataSnapshot.child("Ratings").child(recipeID).getValue();
                    double sum = 0;
                    for(Object val : ratedata.values()){
                        sum = sum + Double.parseDouble(val.toString());
                    }
                    mData.getReference().child("Recipes").child(recipeID).child("rating").setValue(sum/ratedata.size());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("TAG", "Failed to read value in detailedRecipeView_rateRecipe.", databaseError.toException());
                }
            });
        }
        finish();
    }

    public void commentRecipe(View v) {
        CommentModel commentModel = new CommentModel();
        if (mUser.getUid() != null) {
            if(mUser.getCurrentUser()!=null)
            if(mUser.getCurrentUser().getDisplayName()!=null) {
                commentModel= new CommentModel(commentField.getText().toString(), mUser.getUid(), mUser.getCurrentUser().getDisplayName());
            }else

            { commentModel = new CommentModel(commentField.getText().toString(), mUser.getUid(),"anonymous");}
            mData.getReference().child("Comments").child(recipeID).push().setValue(commentModel);
            finish();
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
