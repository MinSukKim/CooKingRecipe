package com.example.girln.recipeapp.models;

public class CookingTagsModel {
    private String tagName;

    public CookingTagsModel(CookingTagsModel i) {
        tagName=i.getTagName();
    }

    public CookingTagsModel(String s) {
        tagName=s;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
