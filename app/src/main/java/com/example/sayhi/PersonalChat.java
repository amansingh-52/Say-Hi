package com.example.sayhi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalChat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        String personName = intent.getStringExtra("User_Name");
      //  int personPhoto = Integer.parseInt(intent.getStringExtra("User_Profile_Photo"));
        TextView personNameDisplay = findViewById(R.id.personalChatPersonName);
        personNameDisplay.setText(personName);
        CircleImageView personImage = findViewById(R.id.personalChatImage);
      //  if(personPhoto)
      //  personImage.setImageResource(personPhoto);
    }
}
