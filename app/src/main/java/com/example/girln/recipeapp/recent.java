package com.example.girln.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

public class recent extends AppCompatActivity {

    RecyclerView mRV;
    RecyclerView.LayoutManager mLM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        TextView tbTitle = (TextView) findViewById(R.id.toolbarTitle);
        tbTitle.setText("Recent Recipe");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
