package com.example.googlemap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class slider extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("url");
        ArrayList<String> urls=bundle.getStringArrayList("url");
        ViewPager mPager = findViewById(R.id.vpager);
        mPager.setAdapter(new SlidingAdapter(slider.this, urls));

    }
}