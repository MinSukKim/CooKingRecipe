package com.example.girln.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment basicFragment = new basic();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_create();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, basicFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public void button_create(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tbTitle = (TextView) findViewById(R.id.toolbarTitle);
        tbTitle.setText("Recipe");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String str) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Intent intent = new Intent(getApplicationContext(), recommend.class);
                intent.putExtra("searchText", str);
                startActivity(intent);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onSearchRequested() {
        Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_LONG).show();
        return super.onSearchRequested();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment profileFragment= new profile();
        Fragment activityFragment= new activity();
        Fragment settingFragment = new setting();
        Fragment sharedFragment = new shared();
        Fragment recipeFragment = new recipeList();

        int id = item.getItemId();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_profile) {
            transaction.replace(R.id.container, profileFragment);
        } else if (id == R.id.nav_recipe) {
            transaction.replace(R.id.container, recipeFragment);
//
        } else if (id == R.id.nav_activity) {
            transaction.replace(R.id.container, activityFragment);
        } else if (id == R.id.nav_setting) {
            transaction.replace(R.id.container, settingFragment);
        } else if (id == R.id.nav_logout) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(main.this, Login.class));
            } else {
                Toast.makeText(main.this, "You are a guest..", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_shared) {
            transaction.replace(R.id.container, sharedFragment);
        }

        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

}

