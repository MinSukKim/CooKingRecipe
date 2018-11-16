package com.example.girln.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class popular extends AppCompatActivity {

    RecyclerView mRV;
    RecyclerView.LayoutManager mLM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        TextView tbTitle = (TextView) findViewById(R.id.toolbarTitle);
        tbTitle.setText("Popular Recipe");

        tbTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomain();
            }
        });

        mRV = findViewById(R.id.recycler_view);
        mRV.setHasFixedSize(true);

        mLM = new LinearLayoutManager(this);
        mRV.setLayoutManager(mLM);
    }

    public void tomain() {
        startActivity(new Intent(this, main.class));
    }
}
