package com.example.sayhi;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class SayHi_Offline extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
