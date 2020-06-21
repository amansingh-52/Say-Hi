package com.example.sayhi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class HomeScreenChats extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_screen_chats,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListView listView = view.findViewById(R.id.home_screen_listView);
        ArrayList<ChatItem> chatItems = SetChat.getList();
        final ChatAdapter chatAdapter = new ChatAdapter(view.getContext(),chatItems);
        listView.setAdapter(chatAdapter);

        final ArrayList<ChatItem> finalChatItems = chatItems;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), PersonalChat.class);
                intent.putExtra("User_Name",finalChatItems.get(position).getPersonName().toString());
                intent.putExtra("User_Profile_Photo",Integer.toString(finalChatItems.get(position).getPersonImage()));
                startActivity(intent);
            }
        });
    }
}
