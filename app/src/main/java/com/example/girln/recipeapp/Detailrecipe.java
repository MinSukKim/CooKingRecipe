package com.example.girln.recipeapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import static java.lang.Math.round;

public class Detailrecipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailrecipe);
        Intent intent = getIntent();
        item_recipe tmp = (item_recipe) intent.getSerializableExtra("Object");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tbTitle = (TextView) findViewById(R.id.toolbarTitle);
        tbTitle.setText(tmp.getTitle());
        List img_list = tmp.getCookingPictures();
        ImageView img = (ImageView) findViewById(R.id.imageView2);
        GlideApp.with(this)
                .load(img_list.get(0))
                .into(img);


        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(tmp.getTitle());

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(round(tmp.getRate()));

        TextView tvIngredients = (TextView) findViewById(R.id.tvIngredients);
        tvIngredients.setText(tmp.getCookingIngredients().toString());

        TextView tvSteps = (TextView) findViewById(R.id.tvSteps);
        tvSteps.setText(tmp.getCookingSteps().toString());

        TextView tvTags = (TextView) findViewById(R.id.tvTags);
        tvTags.setText(tmp.getCookingTags().toString());
    }
}
