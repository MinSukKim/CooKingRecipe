package com.example.girln.recipeapp;

import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class UserRights {
    public static boolean isOwner(FirebaseAuth mUser, String UID) {
        return Objects.equals(mUser.getUid(), UID);
    }

    public static boolean isAuthenticated(FirebaseAuth mUser)
    {
        return mUser.getUid()!=null;
    }
    public static void needsToOwn(View v,FirebaseAuth mUser, String UID)
    {
        if(!isOwner(mUser,UID))
            v.setEnabled(false);
        else v.setEnabled(true);
    }
    public static void needsToNotOwn(View v,FirebaseAuth mUser, String UID)
    {
        if(isOwner(mUser,UID))
            v.setEnabled(false);
        else v.setEnabled(true);
    }
}
