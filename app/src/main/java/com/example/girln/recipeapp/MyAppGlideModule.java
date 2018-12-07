package com.example.girln.recipeapp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;



@GlideModule
public final class MyAppGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context mcontext, @NonNull Glide glide, @NonNull Registry mregistry) {
        mregistry.append(StorageReference.class, InputStream.class, new FirebaseImageLoader.Factory());
    }

}