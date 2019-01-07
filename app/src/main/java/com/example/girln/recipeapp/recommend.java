package com.example.girln.recipeapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;


public class recommend extends AppCompatActivity {

    private Fragment resultFragment = new search_result();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        String key = getIntent().getStringExtra("searchText");
        Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 개수
        bundle.putString("key", key); // key , value
        resultFragment.setArguments(bundle);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, resultFragment);
        transaction.commit();

    }
}

