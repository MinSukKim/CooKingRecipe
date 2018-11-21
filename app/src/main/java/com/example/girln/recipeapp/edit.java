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

public class edit extends AppCompatActivity {
    private FirebaseDatabase mData;
    private FirebaseAuth mUser;
    RecipeModel recipe;
    ArrayList<String> tagList = new ArrayList<>();

    ArrayList<EditText> ingredientNames = new ArrayList<>();
    ArrayList<Spinner> ingredientUnits = new ArrayList<>();
    ArrayList<EditText> ingredientAmounts = new ArrayList<>();

    ArrayList<EditText> stepDescriptions = new ArrayList<>();

    ArrayList<Spinner> tagNames = new ArrayList<>();
    ArrayList<String> picList = new ArrayList<>();

    FirebaseStorage storage;
    StorageReference storageReference;
    private ImageView imageView;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;
    private String recipeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Intent i = getIntent();
        recipeID = i.getStringExtra("recipeID");
        recipe = new RecipeModel();
        mData = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        imageView = (ImageView) findViewById(R.id.imageView2);
        mData.getReference().child("Tags").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> tags = dataSnapshot.getChildren();
                    for (DataSnapshot tag : tags) {
                        tagList.add(Objects.requireNonNull(tag.getValue()).toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        for (String s : picList) {
            recipe.getCookingPictures().add(new CookingPicturesURL(s));
        }

        return recipe;
    }

    public void chooseImage(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadImage(View v) {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            //stores the pic in image/userID path
            final StorageReference ref = storageReference.child("images/" + mUser.getUid() + "/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(edit.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            picList.add(ref.getDownloadUrl().toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(edit.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded Picture " + (int) progress + "%");
                        }
                    });
        }
    }

    //todo only accessible if image is uploaded /upload only doable once
    public void uploadRecipe(View v) {
        convertRecipe();
        mData.getReference().child("Recipes").child(recipeID).setValue(recipe);
        Toast.makeText(edit.this, "Uploaded Recipe", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void addTagField(View v) {
        LinearLayout tagLL = findViewById(R.id.TagLinearLayout);
        LinearLayout newTag = new LinearLayout(this);
        newTag.setOrientation(LinearLayout.HORIZONTAL);
        newTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        Button deleteTagFieldButton = getDeleteTagButton(tagLL, newTag);

        tagTextField(tagLL, newTag);
        tagTagBox(tagLL, newTag);

        newTag.addView(deleteTagFieldButton);

        tagLL.addView(newTag, tagLL.getChildCount() - 1);
    }
    @NonNull
    private Button getDeleteTagButton(final LinearLayout tagLL, final LinearLayout newTag) {
        Button deleteTagFieldButton=new Button(this);
        deleteTagFieldButton.setText("Delete");
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        deleteTagFieldButton.setLayoutParams(lparams);
        deleteTagFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagNames.remove(tagLL.indexOfChild(newTag)-1);
                tagLL.removeView(newTag);
            }
        });
        return deleteTagFieldButton;
    }

    public void addIngredientField(View v) {
        LinearLayout ingredientLL = findViewById(R.id.IngredientsLinearLayout);
        LinearLayout newTag = new LinearLayout(this);
        newTag.setOrientation(LinearLayout.HORIZONTAL);
        newTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        Button deleteIngredientFieldButton = getDeleteIngredientButton(ingredientLL, newTag);


        ingredientTextField(ingredientLL, newTag);
        ingredientUnitBox(ingredientLL, newTag);
        ingredientAmountTextField(ingredientLL, newTag);

        newTag.addView(deleteIngredientFieldButton);

        ingredientLL.addView(newTag, ingredientLL.getChildCount() - 1);
    }

    @NonNull
    private Button getDeleteIngredientButton(final LinearLayout ingredientLL, final LinearLayout newTag) {
        Button deleteIngredientFieldButton=new Button(this);
        deleteIngredientFieldButton.setText("Delete");
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        deleteIngredientFieldButton.setLayoutParams(lparams);
        deleteIngredientFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientNames.remove(ingredientLL.indexOfChild(newTag)-1);
                ingredientAmounts.remove(ingredientLL.indexOfChild(newTag)-1);
                ingredientUnits.remove(ingredientLL.indexOfChild(newTag)-1);
                ingredientLL.removeView(newTag);
            }
        });
        return deleteIngredientFieldButton;
    }
    public void addStepField(View v) {
        final LinearLayout stepLL = findViewById(R.id.CookingStepLinearLayout);
        final LinearLayout newStep = new LinearLayout(this);
        newStep.setOrientation(LinearLayout.HORIZONTAL);
        newStep.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        Button deleteStepFieldButton = getDeleteStepButton(stepLL, newStep);

        stepTextField(stepLL, newStep);
        newStep.addView(deleteStepFieldButton);

        stepLL.addView(newStep, stepLL.getChildCount() - 1);
    }
    @NonNull
    private Button getDeleteStepButton(final LinearLayout stepLL, final LinearLayout newStep) {
        Button deleteStepFieldButton=new Button(this);
        deleteStepFieldButton.setText("Delete");
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        deleteStepFieldButton.setLayoutParams(lparams);

        deleteStepFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepDescriptions.remove(stepLL.indexOfChild(newStep)-1);
                stepLL.removeView(newStep);
            }
        });
        return deleteStepFieldButton;
    }

    private void ingredientAmountTextField(LinearLayout tagLL, LinearLayout newTag) {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        EditText ingredientAmountText = new EditText(this);
        ingredientAmountText.setLayoutParams(lparams);
        ingredientAmountText.setEnabled(true);
        newTag.addView(ingredientAmountText);
        ingredientAmounts.add(ingredientAmountText);
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

    private void ingredientTextField(LinearLayout tagLL, LinearLayout newTag) {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 4);
        EditText ingredientText = new EditText(this);
        ingredientText.setLayoutParams(lparams);
        newTag.addView(ingredientText);
        ingredientNames.add(ingredientText);
    }
    private void ingredientTextField(LinearLayout tagLL, LinearLayout newTag,String ingredientName) {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 4);
        EditText ingredientText = new EditText(this);
        ingredientText.setLayoutParams(lparams);
        ingredientText.setText(ingredientName);
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
