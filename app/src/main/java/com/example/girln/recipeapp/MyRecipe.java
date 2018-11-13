
package com.example.girln.recipeapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRecipe extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private TextView tvTotal;
    private DatabaseReference mData;
    private ArrayList<item_recipe> item_recipeArrayList = new ArrayList<>();
    private List RList;
    private Map tmp;
    private FirebaseUser us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tbTitle = (TextView) findViewById(R.id.toolbarTitle);
        tbTitle.setText("Recipe");

//        us = FirebaseAuth.getInstance().getCurrentUser("email");

        tvTotal = (TextView) findViewById(R.id.tvTotal);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mData = FirebaseDatabase.getInstance().getReference();

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RList = (List) dataSnapshot.child("Recipes").getValue();
                int num = RList.size() - 1;
                int ctn = 0;
                tvTotal.setText("Total: " + num);
                while (num > 0) {
                    tmp = (HashMap) RList.get(num);
                    String val = (String) tmp.get("title");
                    String aut = (String) tmp.get("Author");
                    String img = (String) tmp.get("Pics");
//                    if (aut == )
                    item_recipeArrayList.add(new item_recipe(img, val, 1, "#data"));
                    ctn++;
                    num--;
                }
                MyAdapter myAdapter = new MyAdapter(item_recipeArrayList);
                mRecyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException());
            }
        });
    }
}