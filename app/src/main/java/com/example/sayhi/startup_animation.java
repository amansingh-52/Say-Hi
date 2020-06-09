package com.example.sayhi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class startup_animation extends AppCompatActivity {

    static int DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_animation);
        Animation bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.botttom_animation);
        Animation topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        TextView appName = (TextView) findViewById(R.id.appName);
        ImageView appIcon = (ImageView) findViewById(R.id.appIcon);
        appName.setAnimation(topAnimation);
        appIcon.setAnimation(bottomAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        },DELAY);
    }
}
