package com.example.sayhi;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequest extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ArrayList<Information> arrayList;
    FirebaseUser firebaseUser;
    FirebaseRecyclerAdapter<Information, FriendRequestViewHolder> adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.friend_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arrayList = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = view.findViewById(R.id.friend_request_recyclerView);
        recyclerView.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User")
                .child(firebaseUser.getUid()).child("friend request received");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChildren()){
                    TextView alpha = getActivity().findViewById(R.id.alpha);
                    alpha.setText("No new Friend request");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.keepSynced(true);

        FirebaseRecyclerOptions<Information> options = new FirebaseRecyclerOptions.Builder<Information>()
                .setQuery(databaseReference, Information.class)
                .build();

       adapter = new FirebaseRecyclerAdapter<Information, FriendRequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(FriendRequestViewHolder friendRequestViewHolder, int i, final Information information) {

                friendRequestViewHolder.userName.setText(information.getName());
                friendRequestViewHolder.userEmail.setText(information.getEmail());
                if (information.verified.equals("yes"))
                   friendRequestViewHolder.verified.setVisibility(View.VISIBLE);
                else
                   friendRequestViewHolder.verified.setVisibility(View.GONE);
               friendRequestViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(view.getContext(), DisplayUserInfoToOthers.class);
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
            public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card, parent, false);
                return new FriendRequestViewHolder(view);
            }

       };
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    public static class FriendRequestViewHolder extends RecyclerView.ViewHolder{

        TextView userName, userEmail, userStatus;
        CircleImageView userImage;
        ImageView verified;



        public FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.search_profile_image);
            userName = itemView.findViewById(R.id.name_search_card);
            userEmail = itemView.findViewById(R.id.email_search_card);
            userStatus = itemView.findViewById(R.id.status_search_card);
            verified = itemView.findViewById(R.id.verified_search_card);


        }


    }



    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
