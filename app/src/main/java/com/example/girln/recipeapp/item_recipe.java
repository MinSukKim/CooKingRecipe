package com.example.girln.recipeapp;

public class item_recipe {

    public int drawableId;
    public String title;
    public String rate;
    public String tags;

    public item_recipe(int drawableId, String title, String rate, String tags) {
        this.drawableId = drawableId;
        this.title = title;
        this.rate = rate;
        this.tags = tags;
    }
}
