package com.example.girln.recipeapp.models;

import java.time.LocalDateTime;

public class CommentModel {
    private String comment;
    private String UID;
    private String UName;

    //String parentID;

    public CommentModel(String comment, String UID,String name) {
        this.comment = comment;
        this.UID = UID;
        this.UName=name;

    }

    public CommentModel() {
    }

    public String getUName() {
        return UName;
    }

    public void setUName(String UName) {
        this.UName = UName;
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
