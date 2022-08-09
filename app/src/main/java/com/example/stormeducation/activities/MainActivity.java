package com.example.stormeducation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.example.stormeducation.R;
import com.example.stormeducation.fragments.NewsFeedFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public NavController navController;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        navController = Navigation.findNavController(this,R.id.mainframeContainer);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);

        Intent intent = getIntent();
        String data = intent.getStringExtra("fromQuestion");
        if (data!=null && data.contentEquals("1")){

            navController = Navigation.findNavController(this,R.id.mainframeContainer);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
            bottomNavigationView.setSelectedItemId(R.id.newsFragment);

        }
    }
}