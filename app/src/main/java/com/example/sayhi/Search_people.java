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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

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
    FirebaseRecyclerOptions<Information> options;
    ArrayList<Information> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_people);
        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.searchRecyclerView);
        recyclerView.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        databaseReference.keepSynced(true);

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
                                   .setQuery(query,Information.class)
                                    .build();

                            FirebaseRecyclerAdapter<Information, SearchUserViewHolder> adapter = new FirebaseRecyclerAdapter<Information, SearchUserViewHolder>(options){
                                @Override
                                protected void onBindViewHolder(SearchUserViewHolder searchUserViewHolder, int i, Information information) {
                                    searchUserViewHolder.userName.setText(information.getName());
                                    searchUserViewHolder.userEmail.setText(information.getEmail());
                                    if (information.verified.equals("yes"))
                                        searchUserViewHolder.verified.setVisibility(View.VISIBLE);
                                    else
                                        searchUserViewHolder.verified.setVisibility(View.GONE);
                                }

                                @NonNull
                                @Override
                                public SearchUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card, parent, false);
                                    SearchUserViewHolder viewHolder = new SearchUserViewHolder(view);
                                    return viewHolder;
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
                            /**/
                    }
                });


        }

        @Override
        protected void onStart () {
            super.onStart();

            FirebaseRecyclerOptions<Information> options = new FirebaseRecyclerOptions.Builder<Information>()
                    .setQuery(databaseReference, Information.class)
                    .build();

            FirebaseRecyclerAdapter<Information, SearchUserViewHolder> adapter = new FirebaseRecyclerAdapter<Information, SearchUserViewHolder>(options) {
                @Override
                protected void onBindViewHolder(SearchUserViewHolder searchUserViewHolder, int i, Information information) {
                    searchUserViewHolder.userName.setText(information.getName());
                    searchUserViewHolder.userEmail.setText(information.getEmail());
                    if (information.verified.equals("yes"))
                        searchUserViewHolder.verified.setVisibility(View.VISIBLE);
                    else
                        searchUserViewHolder.verified.setVisibility(View.GONE);
                }

                @NonNull
                @Override
                public SearchUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card, parent, false);
                    SearchUserViewHolder viewHolder = new SearchUserViewHolder(view);
                    return viewHolder;
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

            public SearchUserViewHolder(@NonNull View itemView) {
                super(itemView);

                userImage = itemView.findViewById(R.id.search_profile_image);
                userName = itemView.findViewById(R.id.name_search_card);
                userEmail = itemView.findViewById(R.id.email_search_card);
                userStatus = itemView.findViewById(R.id.status_search_card);
                verified = itemView.findViewById(R.id.verified_search_card);
            }
        }

        @Override
        protected void onStop () {
            /*   adapter.stopListening();/*/
            finish();
            super.onStop();
        }

}

