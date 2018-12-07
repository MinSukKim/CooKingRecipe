package com.example.girln.recipeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.girln.recipeapp.models.CookingIngredientModel;
import com.example.girln.recipeapp.models.CookingPicturesURL;
import com.example.girln.recipeapp.models.CookingStepsModel;
import com.example.girln.recipeapp.models.CookingTagsModel;
import com.example.girln.recipeapp.models.IngredientUnit;
import com.example.girln.recipeapp.models.RecipeModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class edit extends Upload {

    private String recipeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        recipeID = i.getStringExtra("recipeID");

        getRecipe(recipeID);
    }

    private void addIngredientField(CookingIngredientModel ingredient) {
        LinearLayout ingredientLL = findViewById(R.id.IngredientsLinearLayout);
        LinearLayout newTag = new LinearLayout(this);
        newTag.setOrientation(LinearLayout.HORIZONTAL);
        newTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        Button deleteIngredientFieldButton = getDeleteIngredientButton(ingredientLL, newTag);

        ingredientTextField(ingredientLL, newTag,ingredient.getIngredientName());
        ingredientUnitBox(ingredientLL, newTag,ingredient.getUnit());
        ingredientAmountTextField(ingredientLL, newTag,ingredient.getAmount());
        newTag.addView(deleteIngredientFieldButton);
        ingredientLL.addView(newTag, ingredientLL.getChildCount() - 1);
    }

    private void addTagField(CookingTagsModel tag) {
        LinearLayout tagLL = findViewById(R.id.TagLinearLayout);
        LinearLayout newTag = new LinearLayout(this);
        newTag.setOrientation(LinearLayout.HORIZONTAL);
        newTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        Button deleteTagFieldButton = getDeleteTagButton(tagLL, newTag);
        tagTextField(tagLL, newTag);
        tagTagBox(tagLL, newTag,tag.getTagName());
        newTag.addView(deleteTagFieldButton);
        tagLL.addView(newTag, tagLL.getChildCount() - 1);
    }

    private void addStepField(CookingStepsModel step) {
        final LinearLayout stepLL = findViewById(R.id.CookingStepLinearLayout);
        final LinearLayout newStep = new LinearLayout(this);
        newStep.setOrientation(LinearLayout.HORIZONTAL);
        newStep.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        Button deleteStepFieldButton = getDeleteStepButton(stepLL, newStep);
        stepTextField(stepLL, newStep,step.getDescription());
        newStep.addView(deleteStepFieldButton);
        stepLL.addView(newStep, stepLL.getChildCount() - 1);
    }

    public RecipeModel getRecipe(final String recipeID) {
        mData.getReference().child("Recipes").child(recipeID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipe = dataSnapshot.getValue(RecipeModel.class);
                if (recipe != null&& recipe!=new RecipeModel()) {
                    for (CookingIngredientModel ingredient : recipe.getCookingIngredients()) {
                        addIngredientField(ingredient);
                    }
                    for (CookingTagsModel tag : recipe.getCookingTags()) {
                        addTagField(tag);
                    }
                    for (CookingStepsModel step : recipe.getCookingSteps()) {
                        addStepField(step);
                    }
                    for (CookingPicturesURL pic : recipe.getCookingPictures()) {
                        picList.add(pic.getPictureURL());
                    }
                    TextView recipeNameView = findViewById(R.id.RecipeNameField);
                    recipeNameView.setText(recipe.getRecipeName());
                    CheckBox privacyCheckBox = findViewById(R.id.PrivacyCheckbox);
                    if(recipe.getPriv()!=null)
                        privacyCheckBox.setChecked(recipe.getPriv());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException());
            }
        });
        return recipe;
    }


    @Override
    public void uploadRecipe(View v) {
        convertRecipe();
        mData.getReference().child("Recipes").child(recipeID).setValue(recipe);
        Toast.makeText(edit.this, "Uploaded Recipe", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void ingredientAmountTextField(LinearLayout tagLL, LinearLayout newTag,double amount) {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        EditText ingredientAmountText = new EditText(this);
        ingredientAmountText.setLayoutParams(lparams);
        ingredientAmountText.setEnabled(true);
        ingredientAmountText.setText(String.valueOf(amount));
        newTag.addView(ingredientAmountText);
        ingredientAmounts.add(ingredientAmountText);
    }

    private void ingredientUnitBox(LinearLayout tagLL, LinearLayout newTag,IngredientUnit ingredientUnit) {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2);
        Spinner unitSpinner = new Spinner(this);
        IngredientUnit[] unitList = IngredientUnit.values();
        ArrayList<String> unitStringList = new ArrayList<>();
        for (IngredientUnit unit : unitList
                ) {
            unitStringList.add(unit.toString());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, unitStringList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(dataAdapter);
        unitSpinner.setLayoutParams(lparams);
        if(ingredientUnit!=null)
        unitSpinner.setSelection(unitStringList.indexOf(ingredientUnit.toString()));
        newTag.addView(unitSpinner);
        ingredientUnits.add(unitSpinner);
    }

    private void ingredientTextField(LinearLayout tagLL, LinearLayout newTag,String ingredientName) {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 4);
        EditText ingredientText = new EditText(this);
        ingredientText.setLayoutParams(lparams);
        ingredientText.setText(ingredientName);
        newTag.addView(ingredientText);
        ingredientNames.add(ingredientText);
    }

    private void stepTextField(LinearLayout tagLL, LinearLayout newTag,String stepDescription) {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        TextView stepNumberText = new TextView(this);
        stepNumberText.setLayoutParams(lparams);
        stepNumberText.setText("Step " + (tagLL.getChildCount() - 1));
        newTag.addView(stepNumberText);
        LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 3);
        EditText stepText = new EditText(this);
        stepText.setLayoutParams(lparams2);
        stepText.setText(stepDescription);
        newTag.addView(stepText);
        stepDescriptions.add(stepText);
    }

    private void tagTagBox(LinearLayout tagLL, LinearLayout newTag,String tagsModel) {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 3);
        Spinner tagSpinner = new Spinner(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, tagList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(dataAdapter);
        tagSpinner.setLayoutParams(lparams);
        System.out.println(tagsModel);
        tagSpinner.setSelection(tagList.indexOf(tagsModel));
        newTag.addView(tagSpinner);
        tagNames.add(tagSpinner);
    }

    private void tagTextField(LinearLayout tagLL, LinearLayout newTag) {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        TextView tagText = new TextView(this);
        tagText.setLayoutParams(lparams);
        tagText.setText("Tag " + (tagLL.getChildCount() - 1));
        newTag.addView(tagText);
    }

}
