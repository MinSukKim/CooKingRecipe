package com.example.girln.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyRecipe extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private TextView tvTotal;
    private DatabaseReference mData;
    private ArrayList<item_recipe> item_recipeArrayList = new ArrayList<>();
    private List RList;
    private Map tmp;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        TextView tbTitle = (TextView) findViewById(R.id.toolbarTitle);
        tbTitle.setText("Recipe");

        tbTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vies) {
                tomain();
            }
        });

        tvTotal = (TextView) findViewById(R.id.tvTotal);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mData = FirebaseDatabase.getInstance().getReference();

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object RList = dataSnapshot.child("Recipes").getValue();
                List<String> keyList = new ArrayList<String>();
                tmp = (HashMap) RList;
                keyList.addAll(tmp.keySet());
                for (String key : keyList) {
                    Map n = (HashMap) tmp.get(key);

                    String uid = (String) n.get("userID");
                    String title = (String) n.get("recipeName");
                    ArrayList pic = (ArrayList) n.get("cookingPictures");
                    ArrayList inge = (ArrayList) n.get("cookingIngredients");
                    ArrayList steps = (ArrayList) n.get("cookingSteps");
                    ArrayList tags = (ArrayList) n.get("cookingTags");

                    if (user != null) {
                        for (UserInfo rUser : user.getProviderData()) {
                            if (uid.equals(rUser.getUid())) {
                                item_recipeArrayList.add(new item_recipe(uid, pic, title, 0.0, inge, steps, tags, key,true));
                            }
                        }
                    }
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

    public void tomain() {
        startActivity(new Intent(this, main.class));
    }

}
