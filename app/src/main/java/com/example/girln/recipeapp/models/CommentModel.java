package com.example.girln.recipeapp.models;

public class CommentModel {
    private String comment;
    private String UID;
    //String parentID;

    public CommentModel(String comment, String UID) {
        this.comment = comment;
        this.UID = UID;
    }

    public CommentModel() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
