package com.example.sayhi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserInformation extends Fragment {

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.just, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Information information = new Information();
        final TextView userName = view.findViewById(R.id.userNameDisplaySetting);
        final TextView userEmail = view.findViewById(R.id.UserEmailDisplaySetting);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User")
                .child(firebaseUser.getUid());
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                information.name = dataSnapshot.child("name").getValue().toString();
                information.email = dataSnapshot.child("email").getValue().toString();
                 if (!information.name.isEmpty()) {
                    userName.setText(information.name);
                    userEmail.setText(information.email);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
