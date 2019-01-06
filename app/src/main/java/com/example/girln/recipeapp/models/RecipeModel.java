package com.example.girln.recipeapp.models;

import java.util.ArrayList;


public class RecipeModel {
    private String recipeName;
    private String userID;
    private Boolean priv;
    private ArrayList<CookingIngredientModel> cookingIngredients;
    private ArrayList<CookingStepsModel> cookingSteps;
    private ArrayList<CookingTagsModel> cookingTags;
    private ArrayList<String> cookingPictures;

    private void init()
    {
        cookingIngredients=new ArrayList<>();
        cookingSteps=new ArrayList<>();
        cookingTags=new ArrayList<>();
        cookingPictures=new ArrayList<>();
    }
    public RecipeModel() {
        init();
    }

    public RecipeModel(RecipeModel recipe) {
        init();
        recipeName = recipe.getRecipeName();
        userID = recipe.getUserID();
        priv = recipe.getPriv();
        for (CookingIngredientModel i :
                getCookingIngredients()) {
            cookingIngredients.add(new CookingIngredientModel(i));
        }
        for (CookingStepsModel i :
                getCookingSteps()) {
            cookingSteps.add(new CookingStepsModel(i));
        }
        for (CookingTagsModel i :
                getCookingTags()) {
            cookingTags.add(new CookingTagsModel(i));
        }
    }

    public RecipeModel(String recipeName, String userID, Boolean priv) {
        init();
        this.recipeName = recipeName;
        this.userID = userID;
        this.priv = priv;
    }
    //ArrayList<CookingTimeModel> cookingTime;


    public ArrayList<String> getCookingPictures() {
        return cookingPictures;
    }

    public void setCookingPictures(ArrayList<String> cookingPictures) {
        this.cookingPictures = cookingPictures;
    }

    public String getRecipeName() { return recipeName; }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Boolean getPriv() {
        return priv;
    }

    public void setPriv(Boolean priv) {
        this.priv = priv;
    }

    public ArrayList<CookingIngredientModel> getCookingIngredients() {
        return cookingIngredients;
    }

    public void setCookingIngredients(ArrayList<CookingIngredientModel> cookingIngredients) {
        this.cookingIngredients = cookingIngredients;
    }

    public ArrayList<CookingStepsModel> getCookingSteps() {
        return cookingSteps;
    }

    public void setCookingSteps(ArrayList<CookingStepsModel> cookingSteps) {
        this.cookingSteps = cookingSteps;
    }

    public ArrayList<CookingTagsModel> getCookingTags() {
        return cookingTags;
    }

    public void setCookingTags(ArrayList<CookingTagsModel> cookingTags) {
        this.cookingTags = cookingTags;
    }

    /*public ArrayList<CookingTimeModel> getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(ArrayList<CookingTimeModel> cookingTime) {
        this.cookingTime = cookingTime;
    }*/
}
