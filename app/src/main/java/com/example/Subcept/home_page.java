package com.example.Subcept;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.Subcept.adapter.SubceptViewPagerAdapter;
import com.example.Subcept.model.survey_Data;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class home_page extends AppCompatActivity {

    SubceptViewPagerAdapter subceptViewPagerAdapter;
    TabLayout tabLayout;
    ViewPager subceptViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        List<survey_Data> survey_data_List = new ArrayList<>();
        survey_data_List.add(new survey_Data("Welcome To Subcept ", "Easiest way to manage your subscriptions!",R.drawable.organizer));  // NOt sure if thar 0 is uspposed o go there
        survey_data_List.add(new survey_Data("Welcome To Subcept ", "Easiest way to manage your subscriptions!",R.drawable.organizer));
    }

    private void setSubceptViewPagerAdapter(List<survey_Data> survey_data_List){

        subceptViewPager = findViewById(R.id.screenPager);
        tabLayout = findViewById(R.id.tabIndicator);
        subceptViewPagerAdapter = new SubceptViewPagerAdapter(this, survey_data_List);
        subceptViewPager.setAdapter(subceptViewPagerAdapter);
        tabLayout.setupWithViewPager(subceptViewPager);

    }

}
