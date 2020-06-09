package com.example.sayhi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SettingScreen extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setting_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextView userName = view.findViewById(R.id.userNameDisplaySetting);
        final TextView userEmail = view.findViewById(R.id.UserEmailDisplaySetting);
        final TextView userStatus = view.findViewById(R.id.UserStatusDisplaySetting);
        final TextView postCount = view.findViewById(R.id.NoOfPostSetting);
        final TextView followingCount = view.findViewById(R.id.NoOfFollowingSetting);
        final TextView followersCount = view.findViewById(R.id.NoOfFollowersSetting);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.keepSynced(true);
        DatabaseReference mDatabaseRef = databaseReference.child(firebaseUser.getUid());
        mDatabaseRef.keepSynced(true);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("name"))
                     userName.setText(dataSnapshot.child("name").getValue().toString());
                if(dataSnapshot.hasChild("email"))
                     userEmail.setText(dataSnapshot.child("email").getValue().toString());
                if(dataSnapshot.hasChild("status"))
                      userStatus.setText(dataSnapshot.child("status").getValue().toString());
                if(dataSnapshot.hasChild("post_no"))
                      postCount.setText(dataSnapshot.child("post_no").getValue().toString());
                if(dataSnapshot.hasChild("following_count"))
                       followingCount.setText(dataSnapshot.child("following_count").getValue().toString());
                if(dataSnapshot.hasChild("followers_count"))
                       followersCount.setText(dataSnapshot.child("followers_count").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        CardView logOutButton = view.findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(view.getContext(),signIn.class);
                startActivity(intent);
            }
        });

    }
}
