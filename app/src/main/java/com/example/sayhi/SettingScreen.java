package com.example.sayhi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class SettingScreen extends Fragment {

    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setting_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.tabLayout);
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.setting_frameLayout, new UserInformation()).commit();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                    if(tab.getPosition()==0){
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.setting_frameLayout, new FriendRequest()).commit();
                    }
                    else if(tab.getPosition()==1){
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.setting_frameLayout, new UserInformation()).commit();
                    }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
