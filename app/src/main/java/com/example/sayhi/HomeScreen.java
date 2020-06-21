package com.example.sayhi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class HomeScreen extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_screen,container,false);

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout = view.findViewById(R.id.HomeScreenTabLayout);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeScreenFragmentLayout, new HomeScreenChats()).commit();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0)  {
                  getActivity(). getSupportFragmentManager().beginTransaction().replace(R.id.homeScreenFragmentLayout,new HomeScreenChats()).commit();
                }
                else if(tab.getPosition() == 1){
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeScreenFragmentLayout, new HomeScreenViewFriendList()).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FloatingActionButton floatingActionButton = view.findViewById(R.id.contacts);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(),"Clicked!",Toast.LENGTH_LONG).show();
            }
        });

    }
}
