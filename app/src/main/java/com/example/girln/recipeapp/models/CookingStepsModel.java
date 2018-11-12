package com.example.girln.recipeapp.models;

public class CookingStepsModel {
    private String description;

    public CookingStepsModel(CookingStepsModel i) {
        description=i.getDescription();
    }

    public CookingStepsModel(String s) {
        description=s;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
