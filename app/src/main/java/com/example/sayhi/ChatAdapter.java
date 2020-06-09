package com.example.sayhi;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<ChatItem> chatItems;

    public ChatAdapter(Context context, ArrayList<ChatItem> chatItems) {
        this.context = context;
        this.chatItems = chatItems;
    }



    @Override
    public int getCount() {
        return chatItems.size();
    }

    @Override
    public Object getItem(int position) {
        return chatItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = View.inflate(context, R.layout.chat_view, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.chatIcon);

        TextView textView = (TextView) convertView.findViewById(R.id.chatName);

        ChatItem chatItem = chatItems.get(position);


        imageView.setImageResource(chatItem.getPersonImage());

        textView.setText(chatItem.getPersonName());


        return convertView;
    }
}
