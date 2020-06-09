package com.example.sayhi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null) {
           Intent intent = new Intent(MainActivity.this,signUp.class);
           startActivity(intent);
        }
        else {
            setContentView(R.layout.activity_main);
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
            bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeScreen()).commit();
            ImageView searchButton = findViewById(R.id.search_button_toolbar);
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,Search_people.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                    }
                    else{
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener  = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.home_bottom :
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeScreen()).commit();
                    break;
                case R.id.feed_Bottom :
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FeedScreen()).commit();
                    break;

                case R.id.setting_bottom :
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SettingScreen()).commit();
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Connect connect = new Connect("online");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").child(firebaseUser.getUid()).child("connect").setValue(connect);
    }


    @Override
    protected void onStop() {
        Connect connect = new Connect("offline");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").child(firebaseUser.getUid()).child("connect").setValue(connect);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Connect connect = new Connect("online");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").child(firebaseUser.getUid()).child("connect").setValue(connect);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
