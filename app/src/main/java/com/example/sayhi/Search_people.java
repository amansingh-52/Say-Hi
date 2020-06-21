package com.example.sayhi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Search_people extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ArrayList<Information> arrayList;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_people);
        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.searchRecyclerView);
        recyclerView.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        databaseReference.keepSynced(true);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final EditText sText = findViewById(R.id.search_people_editText);
        sText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().equals("")) {
                    search(s.toString());
                } else {
                    search("");
                }
            }
        });
    }

    private void search(String s) {
        final Query query = databaseReference.orderByChild("name").startAt(s).endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    arrayList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        final Information information = dataSnapshot1.getValue(Information.class);
                        arrayList.add(information);
                    }
                    FirebaseRecyclerOptions<Information> options = new FirebaseRecyclerOptions.Builder<Information>()
                            .setQuery(query, Information.class)
                            .build();

                    FirebaseRecyclerAdapter<Information, SearchUserViewHolder> adapter = new FirebaseRecyclerAdapter<Information, SearchUserViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(SearchUserViewHolder searchUserViewHolder, int i, final Information information) {
                            searchUserViewHolder.userName.setText(information.getName());
                            searchUserViewHolder.userEmail.setText(information.getEmail());
                            try {
                                if (information.verified.equals("yes"))
                                    searchUserViewHolder.verified.setVisibility(View.VISIBLE);
                                else
                                    searchUserViewHolder.verified.setVisibility(View.GONE);
                                searchUserViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Search_people.this, DisplayUserInfoToOthers.class);
                                        intent.putExtra("name", information.getName());
                                        intent.putExtra("email", information.getEmail());
                                        intent.putExtra("uid", information.getUid());
                                        intent.putExtra("verified", information.verified);
                                        startActivity(intent);
                                    }
                                });
                            }catch (NullPointerException e){
                                TextView noView = findViewById(R.id.searchNoItem);
                                noView.setVisibility(View.VISIBLE);
                                noView.setText("No Users");
                            }
                        }


                        @NonNull
                        @Override
                        public SearchUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card, parent, false);
                            return new SearchUserViewHolder(view);

                        }
                    };
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                arrayList.clear();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        IsSearching connect = new IsSearching("online");
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference2.child("User").child(firebaseUser.getUid()).child("connect").child("searching").setValue(connect);
        FirebaseRecyclerOptions<Information> options = new FirebaseRecyclerOptions.Builder<Information>()
                .setQuery(databaseReference, Information.class)
                .build();

        FirebaseRecyclerAdapter<Information, SearchUserViewHolder> adapter = new FirebaseRecyclerAdapter<Information, SearchUserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(SearchUserViewHolder searchUserViewHolder, int i, final Information information) {
                searchUserViewHolder.userName.setText(information.getName());
                searchUserViewHolder.userEmail.setText(information.getEmail());
                if (information.verified.equals("yes"))
                    searchUserViewHolder.verified.setVisibility(View.VISIBLE);
                else
                    searchUserViewHolder.verified.setVisibility(View.GONE);
                searchUserViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Search_people.this, DisplayUserInfoToOthers.class);
                        intent.putExtra("name",information.getName());
                        intent.putExtra("email",information.getEmail());
                        intent.putExtra("uid",information.getUid());
                        intent.putExtra("verified",information.verified);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public SearchUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card, parent, false);
                return new SearchUserViewHolder(view);
            }
        };
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public static class SearchUserViewHolder extends RecyclerView.ViewHolder {

        TextView userName, userEmail, userStatus;
        CircleImageView userImage;
        ImageView verified;

        private SearchUserViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.search_profile_image);
            userName = itemView.findViewById(R.id.name_search_card);
            userEmail = itemView.findViewById(R.id.email_search_card);
            userStatus = itemView.findViewById(R.id.status_search_card);
            verified = itemView.findViewById(R.id.verified_search_card);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IsSearching connect = new IsSearching("online");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").child(firebaseUser.getUid()).child("connect").child("searching").setValue(connect);
    }

    @Override
    protected void onStop() {
        IsSearching connect = new IsSearching("offline");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").child(firebaseUser.getUid()).child("connect").child("searching").setValue(connect);
        super.onStop();
    }

    @Override
    protected void onPause() {
        IsSearching connect = new IsSearching("offline");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").child(firebaseUser.getUid()).child("connect").child("searching").setValue(connect);
        super.onPause();
    }
}

