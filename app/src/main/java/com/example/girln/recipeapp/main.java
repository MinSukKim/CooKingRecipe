package com.example.girln.recipeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

public class main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment basicFragment;
    private Fragment profileFragment;
    private Fragment myRecipeFragment;
    private Fragment activityFragment;
    private Fragment settingFragment;
    private Fragment sharedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        basicFragment = new basic();
        profileFragment = new profile();
        myRecipeFragment = new myRecipe();
        activityFragment = new activity();
        settingFragment = new setting();
        sharedFragment = new shared();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, basicFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        Toast.makeText(main.this,"hmm",Toast.LENGTH_SHORT).show();
//        View header = navigationView.getHeaderView(0);
//
//        TextView s_name = (TextView)header.findViewById(R.id.name);
//        TextView s_email = (TextView)header.findViewById(R.id.email);
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user != null) {
//            for (UserInfo profile : user.getProviderData()) {
//
//                String name = profile.getDisplayName();
//                String email = profile.getEmail();
//                s_name.setText(name);
//                s_email.setText(email);
//            }
//        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            transaction.replace(R.id.container, profileFragment);
        } else if (id == R.id.nav_recipe) {
            transaction.replace(R.id.container, myRecipeFragment);
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
                Toast.makeText(main.this, "No user.", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_shared) {
            transaction.replace(R.id.container, sharedFragment);
        }
//        } else if (id == R.id.nav_message) {
//            transaction.replace(R.id.container, profileFragment);
//        }

        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

}

