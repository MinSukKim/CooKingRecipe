package com.example.girln.recipeapp;

import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class item_recipe {

    public ArrayList cookingPictures;
    public String title;
    public double rate;
    public boolean priv = false;
    public String Author;
    public List<String> cookingTags = new ArrayList<>();
    public String key;
    public String UserID = null;
    public ArrayList cookingIngredients;
    public ArrayList cookingSteps;

    private item_recipe() {
        //Default constructor
    }

    public item_recipe(String uid, ArrayList cookingPictures, String title, double rate, ArrayList cookingIngredients, ArrayList cookingSteps, ArrayList tags, String key) {
        this.UserID = uid;
        this.cookingPictures = cookingPictures;
        this.title = title;
        this.rate = rate;
        this.cookingIngredients = cookingIngredients;
        this.cookingSteps = cookingSteps;
        this.cookingTags = tags;
//        getTags(tags);

        this.key = key;
//        System.out.println(this.cookingTags.size());
        System.out.println(cookingTags);
        System.out.println(cookingIngredients);
        System.out.println(cookingSteps);
    }

    //    public void getTags(ArrayList tags) {
//        int num = tags.size();
//        for (int i = 0; i < num; i++) {
//            String item = tags.get(i).toString().split("tagName=")[1];
//            System.out.println(item);
//            this.cookingTags.add(item);
//        }
//    }
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return Author;
    }

    public double getRate() {
        return rate;
    }
//    public ArrayList<> getTags(){
//        return tags;
//    }
//    public ArrayList<> getIngredients(){
//        return ingredients;
//    }
//    public ArrayList<> getSteps(){
//        return steps;
//    }
}
