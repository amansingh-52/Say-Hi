package com.example.sayhi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayUserInfoToOthers extends AppCompatActivity {

    TextView name, email, activityText;
    ImageView verified;
    FirebaseUser firebaseUser;
    CardView activityButton;
    DatabaseReference databaseReference;
    Information information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_info_to_others);
        information = new Information();
        activityButton = findViewById(R.id.ActivityButtonDisplay);
        activityText = findViewById(R.id.ActivityText);
        final CardView deleteButton = findViewById(R.id.deleteFriendButton);
        final Intent intent = getIntent();
        information.uid = intent.getStringExtra("uid");
        information.name = intent.getStringExtra("name");
        information.email = intent.getStringExtra("email");
        information.verified = intent.getStringExtra("verified");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User")
                .child(firebaseUser.getUid()).child("friends");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(intent.getStringExtra("uid")) && !firebaseUser.getUid()
                        .equals(intent.getStringExtra("uid"))) {
                    activityText.setText("Message");
                    deleteButton.setVisibility(View.GONE);
                } else if (firebaseUser.getUid().equals(intent.getStringExtra("uid")))
                    activityText.setText("Edit profile");
                else {
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
                    databaseReference1.child("User").child(firebaseUser.getUid()).child("friend request received")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(intent.getStringExtra("uid"))) {
                                        activityText.setText("Accept");
                                        activityButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                acceptFriendRequest(information, information.uid);
                                            }
                                        });

                                        deleteButton.setVisibility(View.VISIBLE);
                                        deleteButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                clear(information.uid);
                                            }
                                        });
                                    } else {
                                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
                                        databaseReference1.child("User").child(firebaseUser.getUid()).child("sent request")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.hasChild(intent.getStringExtra("uid"))) {
                                                            activityText.setText("Friend request sent");
                                                            final CardView deleteButton = findViewById(R.id.deleteFriendButton);
                                                            deleteButton.setVisibility(View.VISIBLE);
                                                            TextView deleteText = findViewById(R.id.deleteText);
                                                            deleteText.setText("Cancel");
                                                            deleteButton.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    cancel(information.uid);
                                                                }
                                                            });
                                                        } else {
                                                            activityText.setText("Send friend request");
                                                            deleteButton.setVisibility(View.GONE);
                                                            activityButton.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    sendFriendRequest(information, information.uid);
                                                                }
                                                            });
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        name = findViewById(R.id.name_person_display);
        email = findViewById(R.id.person_email_display);
        name.setText(intent.getStringExtra("name"));
        verified = findViewById(R.id.person_verified_display);
        email.setText(intent.getStringExtra("email"));
        if (intent.getStringExtra("verified").equals("yes")) {
            verified.setVisibility(View.VISIBLE);
        } else
            verified.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IsViewingProfile connect = new IsViewingProfile("online");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").child(firebaseUser.getUid()).child("connect").child("viewing").setValue(connect);
    }

    @Override
    protected void onStop() {
        IsViewingProfile connect = new IsViewingProfile("offline");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").child(firebaseUser.getUid()).child("connect").child("viewing").setValue(connect);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IsViewingProfile connect = new IsViewingProfile("online");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").child(firebaseUser.getUid()).child("connect").child("viewing").setValue(connect);
    }

    @Override
    protected void onPause() {
        IsViewingProfile connect = new IsViewingProfile("offline");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").child(firebaseUser.getUid()).child("connect").child("viewing").setValue(connect);
        super.onPause();
    }

    void sendFriendRequest(final Information information, final String uid) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").child(firebaseUser.getUid()).child("sent request")
                .child(uid).setValue(information).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child("User").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Information information1 = new Information();
                        information1.name = dataSnapshot.child("name").getValue().toString();
                        information1.email = dataSnapshot.child("email").getValue().toString();
                        information1.uid = dataSnapshot.child("uid").getValue().toString();
                        information1.verified = dataSnapshot.child("verified").getValue().toString();
                        databaseReference.child("User").child(uid).child("friend request received")
                                .child(firebaseUser.getUid()).
                                setValue(information1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    void acceptFriendRequest(final Information information, final String uid) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.child(firebaseUser.getUid()).child("friends").child(uid)
                .setValue(information).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Information information1 = new Information();
                        information1.name = dataSnapshot.child("name").getValue().toString();
                        information1.email = dataSnapshot.child("email").getValue().toString();
                        information1.uid = dataSnapshot.child("uid").getValue().toString();
                        information1.verified = dataSnapshot.child("verified").getValue().toString();
                        databaseReference.child(uid).child("friends")
                                .child(firebaseUser.getUid()).
                                setValue(information1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                clear(uid);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    void clear(final String uid) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Information information1 = new Information();
                information1.name = dataSnapshot.child("name").getValue().toString();
                information1.email = dataSnapshot.child("email").getValue().toString();
                information1.uid = dataSnapshot.child("uid").getValue().toString();
                information1.verified = dataSnapshot.child("verified").getValue().toString();
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("User");
                databaseReference1.child(uid).child("sent request").child(information1.uid).setValue(null);
                databaseReference1.child(information1.uid).child("friend request received").child(uid).setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void cancel(final String uid) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Information information1 = new Information();
                information1.name = dataSnapshot.child("name").getValue().toString();
                information1.email = dataSnapshot.child("email").getValue().toString();
                information1.uid = dataSnapshot.child("uid").getValue().toString();
                information1.verified = dataSnapshot.child("verified").getValue().toString();
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("User");
                databaseReference1.child(information1.uid).child("sent request").child(uid).setValue(null);
                databaseReference1.child(uid).child("friend request received").child(information1.uid).setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}