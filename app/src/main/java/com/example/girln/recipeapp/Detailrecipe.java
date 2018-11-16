package com.example.girln.recipeapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import static java.lang.Math.round;

public class Detailrecipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailrecipe);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String title = bundle.getString("title");
        String tmp = bundle.getString("img");
        Uri myUri = Uri.parse(tmp);
        double rate = bundle.getDouble("rate");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tbTitle = (TextView) findViewById(R.id.toolbarTitle);
        tbTitle.setText(title);

        ImageView img = (ImageView) findViewById(R.id.imageView2);
        GlideApp.with(this)
                .load(myUri)
                .into(img);


        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(round(rate));

        TextView tvIngredients = (TextView) findViewById(R.id.tvIngredients);
//        tvIngredients.setText();

        TextView tvSteps = (TextView) findViewById(R.id.tvSteps);
//        tvSteps.setText();

        TextView tvTags = (TextView) findViewById(R.id.tvTags);
//        tvTags.setText();
    }
}
