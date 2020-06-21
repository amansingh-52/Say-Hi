package com.example.sayhi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalChat extends AppCompatActivity {

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    @Override
    protected void onStart() {
        super.onStart();
        IsChatting isChatting = new IsChatting("online");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").child(firebaseUser.getUid()).child("connect").child("isChatting").setValue(isChatting);
        Intent intent = getIntent();
        String personName = intent.getStringExtra("User_Name");
      //  int personPhoto = Integer.parseInt(intent.getStringExtra("User_Profile_Photo"));
        TextView personNameDisplay = findViewById(R.id.personalChatPersonName);
        personNameDisplay.setText(personName);
        CircleImageView personImage = findViewById(R.id.personalChatImage);
      //  if(personPhoto)
      //  personImage.setImageResource(personPhoto);
    }

    @Override
    protected void onStop() {
        IsChatting isChatting = new IsChatting("offline");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").child(firebaseUser.getUid()).child("connect").child("isChatting").setValue(isChatting);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IsChatting isChatting = new IsChatting("online");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").child(firebaseUser.getUid()).child("connect").child("isChatting").setValue(isChatting);
    }

    @Override
    protected void onPause() {
        IsChatting isChatting = new IsChatting("offline");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").child(firebaseUser.getUid()).child("connect").child("isChatting").setValue(isChatting);
        super.onPause();
    }
}
