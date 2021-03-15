package com.example.line_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.line_app.adapter.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ViewPager vp = findViewById(R.id.viewpager);
        PagerAdapter pA = new PagerAdapter(getSupportFragmentManager());
        vp.setAdapter(pA);

        TabLayout tL = findViewById(R.id.sliding_tabs);
        tL.setupWithViewPager(vp);
    }

}