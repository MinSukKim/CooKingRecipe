package com.example.girln.recipeapp;


// Recipe Upload page

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.girln.recipeapp.models.CookingIngredientModel;
import com.example.girln.recipeapp.models.CookingStepsModel;
import com.example.girln.recipeapp.models.CookingTagsModel;
import com.example.girln.recipeapp.models.IngredientUnit;
import com.example.girln.recipeapp.models.RecipeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Upload extends AppCompatActivity {
    private FirebaseDatabase mData;
    private FirebaseAuth mUser;
    RecipeModel recipe;
    ArrayList<String> tagList = new ArrayList<>();

    ArrayList<EditText> ingredientNames = new ArrayList<>();
    ArrayList<Spinner> ingredientUnits = new ArrayList<>();
    ArrayList<EditText> ingredientAmounts = new ArrayList<>();

    ArrayList<EditText> stepDescriptions = new ArrayList<>();

    ArrayList<Spinner> tagNames = new ArrayList<>();
    long num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
      recipe = new RecipeModel();
      mData = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance();
        mData.getReference().child("Tags").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if(dataSnapshot.exists()){
                  Iterable<DataSnapshot> tags = dataSnapshot.getChildren();
            for (DataSnapshot tag : tags) {
                   tagList.add(Objects.requireNonNull(tag.getValue()).toString());
             }}
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
          });

    }

    public RecipeModel convertRecipe() {
        recipe = new RecipeModel();
        recipe.setUserID(mUser.getUid());
        TextView recipeNameView = findViewById(R.id.RecipeNameField);
        recipe.setRecipeName(recipeNameView.getText().toString());
        CheckBox privacyCheckBox = findViewById(R.id.PrivacyCheckbox);
        recipe.setPriv(privacyCheckBox.isChecked());
        int i = 0;
        for (EditText ingredientName : ingredientNames) {
            recipe.getCookingIngredients().add(new CookingIngredientModel(ingredientName.getText().toString(), ingredientUnits.get(i).getSelectedItem().toString(), ingredientAmounts.get(i).getText().toString()));
            i++;
        }
        for (EditText step : stepDescriptions) {
            recipe.getCookingSteps().add(new CookingStepsModel(step.getText().toString()));
        }
        for (Spinner tag : tagNames) {
            recipe.getCookingTags().add(new CookingTagsModel(tag.getSelectedItem().toString()));
        }

        return recipe;
    }

    public void uploadRecipe(View v) {
        convertRecipe();
        mData.getReference().child("Recipes").push().setValue(recipe);
        //todo go back to main and display message
    }

    public void addTagField(View v) {
        LinearLayout tagLL = findViewById(R.id.TagLinearLayout);
        LinearLayout newTag = new LinearLayout(this);
        newTag.setOrientation(LinearLayout.HORIZONTAL);
        newTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tagTextField(tagLL, newTag);
        tagTagBox(tagLL, newTag);
        tagLL.addView(newTag, tagLL.getChildCount() - 1);
    }

    public void addIngredient(View v) {
        LinearLayout ingredientLL = findViewById(R.id.IngredientsLinearLayout);
        LinearLayout newTag = new LinearLayout(this);
        newTag.setOrientation(LinearLayout.HORIZONTAL);
        newTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        ingredientTextField(ingredientLL, newTag);
        ingredientUnitBox(ingredientLL, newTag);
        ingredientAmountTextField(ingredientLL, newTag);
        ingredientLL.addView(newTag, ingredientLL.getChildCount() - 1);
    }

    public void addStep(View v) {
        LinearLayout stepLL = findViewById(R.id.CookingStepLinearLayout);
        LinearLayout newStep = new LinearLayout(this);
        newStep.setOrientation(LinearLayout.HORIZONTAL);
        newStep.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        stepTextField(stepLL, newStep);
        stepLL.addView(newStep, stepLL.getChildCount() - 1);
    }


    private void ingredientAmountTextField(LinearLayout tagLL, LinearLayout newTag) {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        EditText ingredientAmountText = new EditText(this);
        ingredientAmountText.setLayoutParams(lparams);
        ingredientAmountText.setEnabled(true);
        newTag.addView(ingredientAmountText);
        ingredientAmounts.add(ingredientAmountText);
    }

    private void ingredientUnitBox(LinearLayout tagLL, LinearLayout newTag) {
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
        newTag.addView(unitSpinner);
        ingredientUnits.add(unitSpinner);
    }

    private void ingredientTextField(LinearLayout tagLL, LinearLayout newTag) {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 4);
        EditText ingredientText = new EditText(this);
        ingredientText.setLayoutParams(lparams);
        newTag.addView(ingredientText);
        ingredientNames.add(ingredientText);
    }

    private void stepTextField(LinearLayout tagLL, LinearLayout newTag) {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        TextView stepNumberText = new TextView(this);
        stepNumberText.setLayoutParams(lparams);
        stepNumberText.setText("Step " + (tagLL.getChildCount() - 1));
        newTag.addView(stepNumberText);
        LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 3);
        EditText stepText = new EditText(this);
        stepText.setLayoutParams(lparams2);
        newTag.addView(stepText);
        stepDescriptions.add(stepText);
    }

    private void tagTagBox(LinearLayout tagLL, LinearLayout newTag) {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 3);
        Spinner tagSpinner = new Spinner(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, tagList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(dataAdapter);
        tagSpinner.setLayoutParams(lparams);
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
