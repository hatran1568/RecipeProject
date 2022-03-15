package com.example.recipeproject.UI.activities;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.recipeproject.NotLogInError;
import com.example.recipeproject.R;
import com.example.recipeproject.UI.fragments.GuestProfileFragment;
import com.example.recipeproject.UI.fragments.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ProfileActivity extends AbstractActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        // set navigation bar actions
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_add:
                        if(isLoggedIn){
                            startActivity(new Intent(getApplicationContext(), AddRecipeActivity.class));
                            overridePendingTransition(0,0);
                        }
                        else{
                            startActivity(new Intent(getApplicationContext(), NotLogInError.class));
                            overridePendingTransition(0,0);
                        }
                        return true;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_search:
                        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        // Instantiate layout depend on if user is logged in
        if (savedInstanceState == null) {
            if (isLoggedIn){
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.profile_layout, UserProfileFragment.class, null)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.profile_layout, GuestProfileFragment.class, null)
                        .commit();
            }
        }


    }
}