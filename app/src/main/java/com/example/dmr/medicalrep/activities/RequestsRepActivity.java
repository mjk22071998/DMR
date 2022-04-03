package com.example.dmr.medicalrep.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.dmr.medicalrep.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class RequestsRepActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_rep);
        tabLayout=findViewById(R.id.tabs);
        viewPager=findViewById(R.id.view_pager);
        TabLayoutMediator tabLayoutMediator= new TabLayoutMediator(tabLayout,viewPager, (tab, position)->{
            if (position==0)
                tab.setText("Sent Requests");
            else if (position==1)
                tab.setText("Accepted Requests");
            else
                tab.setText("Rejected Requests");
        });
        tabLayoutMediator.attach();
    }
}