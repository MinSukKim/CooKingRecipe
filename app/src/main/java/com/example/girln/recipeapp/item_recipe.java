package com.example.girln.recipeapp;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class item_recipe implements Serializable {

    public List<String> cookingPictures = new ArrayList<>();
    public String title;
    public double rate;
    public boolean priv = false;
    public String Author;
    public List<String> cookingTags = new ArrayList<>();
    public String key;
    public String UserID = null;
    public List<Map> cookingIngredients = new ArrayList<>();
    public List<Map> cookingSteps = new ArrayList<>();
    public boolean mypage = true;

    public item_recipe() {
        //Default constructor
    }

    public item_recipe(String uid, ArrayList cookingPictures, String title, double rate, ArrayList cookingIngredients, ArrayList cookingSteps, ArrayList tags, String key, boolean mypage) {
        this.UserID = uid;
        this.title = title;
        this.key = key;

        this.rate = rate;
        this.cookingSteps = cookingSteps;
        this.mypage = mypage;

        setTags(tags);
        setIngredients(cookingIngredients);
        setSteps(cookingSteps);
        setPics(cookingPictures);

        System.out.println(this.cookingPictures);
    }

    public void setPics(ArrayList cookingPictures) {
        if (cookingPictures != null) {
            int num = cookingPictures.size();
            for (int i = 0; i < num; i++) {
                String item = cookingPictures.get(i).toString().split("pictureURL=")[1];
                item = item.replaceAll("\\}", "");
                this.cookingPictures.add(item);
            }
        } else {
            this.cookingPictures.add("https://firebasestorage.googleapis.com/v0/b/posd-befe7.appspot.com/o/RageFace.jpg?alt=media&token=4a7074c3-4f54-46fa-b515-8fcd3acc2613");
        }
    }

    public void setTags(ArrayList tags) {
        int num = tags.size();
        for (int i = 0; i < num; i++) {
            String item = tags.get(i).toString().split("tagName")[1];
            item = item.replaceAll("=", "#");
            item = item.replaceAll("\\}", "");
            this.cookingTags.add(item);
        }
    }

    public void setIngredients(ArrayList cookingIngredients) {
        int num = cookingIngredients.size();
        for (int i = 0; i < num; i++) {
            Map item = (HashMap) cookingIngredients.get(i);
            this.cookingIngredients.add(item);
        }
//        for (Object cookingIngredient :
//                cookingIngredients) {
//            Map item = (HashMap) cookingIngredient;
//            this.cookingIngredients.add(item)
//        }

    }

    public void setSteps(ArrayList cookingSteps) {
        int num = cookingSteps.size();
        for (int i = 0; i < num; i++) {
            Map item = (HashMap) cookingSteps.get(i);
            this.cookingSteps.add(item);
        }

    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.Author;
    }

    public double getRate() {
        return this.rate;
    }

    public List<Map> getCookingIngredients() {
        return this.cookingIngredients;
    }

    public List<Map> getCookingSteps() {
        return this.cookingSteps;
    }

    public List<String> getCookingPictures() {
        return cookingPictures;
    }

    public List<String> getCookingTags() {
        return this.cookingTags;
    }

    public String getKey() {
        return this.key;
    }

}
