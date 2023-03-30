package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.parse.ParseUser;

public class SocialMediaActivity extends AppCompatActivity {

    private ProgressBar my_progress_bar3;

    private androidx.appcompat.widget.Toolbar toolbar;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        setTitle("Instagram");

        my_progress_bar3 = findViewById(R.id.my_progress_bar3);
        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        viewPager2 = findViewById(R.id.viewPager2);
        tabAdapter = new TabAdapter(this);
        viewPager2.setAdapter(tabAdapter);

        tabLayout = findViewById(R.id.tabLayout);

        String [] tabTitles={"PROFILE","USER","SHARE"};
        new TabLayoutMediator(tabLayout, viewPager2,(myTabLayout, position) ->
                myTabLayout.setText(tabTitles[position])).attach();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:

                my_progress_bar3.setVisibility(View.VISIBLE);
                ParseUser.getCurrentUser();
                ParseUser.logOut();
                finish();
                my_progress_bar3.setVisibility(View.GONE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}