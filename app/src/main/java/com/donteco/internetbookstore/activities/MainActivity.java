package com.donteco.internetbookstore.activities;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.donteco.internetbookstore.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getRidOfTopBar();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        NavController navController = Navigation.findNavController(this, R.id.main_navigation_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

    }

    //On back click it goes to the search fragment
    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.main_navigation_host_fragment).navigateUp();
    }
}
