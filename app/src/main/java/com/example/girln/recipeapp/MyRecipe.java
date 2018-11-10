
package com.example.girln.recipeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class MyRecipe extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<item_recipe> item_recipeArrayList = new ArrayList<>();
        item_recipeArrayList.add(new item_recipe(R.drawable.strawberry, "5,000원"));
        item_recipeArrayList.add(new item_recipe(R.drawable.bread, "4,600원"));
        item_recipeArrayList.add(new item_recipe(R.drawable.noodle, "4,000원"));

        MyAdapter myAdapter = new MyAdapter(item_recipeArrayList);

        mRecyclerView.setAdapter(myAdapter);
    }
}