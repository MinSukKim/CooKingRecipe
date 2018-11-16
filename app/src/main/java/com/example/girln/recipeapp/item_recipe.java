package com.example.girln.recipeapp;

import android.net.Uri;

import java.util.ArrayList;

public class item_recipe {

    //    public Uri drawabled;
    public String drawabled;
    public String title;
    public double rate;
    public boolean shared;
    public String author;
    public String tags;
    public int key;
//    public ArrayList<tagsModel> tags;
////    public ArrayList<ingredientsModel> ingredients;
////    public ArrayList<StepsModel> Steps;

    private item_recipe() {
        //Default constructor
    }

    public item_recipe(String drawabled, String title, double rate, String tags, int key) {
        this.drawabled = drawabled;
        this.title = title;
        this.rate = rate;
        this.tags = tags;
        this.key = key;

    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
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
