package com.example.recipeproject.UI.activities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.recipeproject.DataAccess.DataAccess;
import com.example.recipeproject.InterfaceGetData.FirebaseCallback;
import com.example.recipeproject.NotLogInError;
import com.example.recipeproject.R;
import com.example.recipeproject.Repsentation.Adapter;
import com.example.recipeproject.listener.SelectListener;
import com.example.recipeproject.model.Recipe;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class HomeActivity extends AbstractActivity implements SelectListener {
    private Context mContext;
    private Activity mActivity;
    RecyclerView recyclerView;

    // Array list for recycler view data source
    ArrayList<Recipe> source = new ArrayList<>();

    // Layout Manager
    RecyclerView.LayoutManager RecyclerViewLayoutManager;

    // adapter class object
    Adapter adapter;

    // Linear Layout Manager
    LinearLayoutManager HorizontalLayout;

    View ChildView;
    int RecyclerViewItemPosition;
    TextView textView;

    BottomNavigationView bottomNavigationView;
    int pastVisibleItems, visibleItemCount, totalItemCount;
    Boolean loading = false;
    //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_add:
                        // TODO: add intent to add recipe
                        if(isLoggedIn){
                            startActivity(new Intent(getApplicationContext(), AddRecipeActivity.class));
                            overridePendingTransition(0,0);
                        }
                        else{
                            startActivity(new Intent(getApplicationContext(), NotLogInError.class));
                            overridePendingTransition(0,0);
                        }
                        return true;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
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


        /// new home page
        mActivity = HomeActivity.this;
        mContext = getApplicationContext();
        textView = findViewById(R.id.newest);
        recyclerView
                = (RecyclerView) findViewById(
                R.id.recyclerviewNewest);
        RecyclerViewLayoutManager
                = new LinearLayoutManager(
                getApplicationContext());
        // Set LayoutManager on Recycler View
        recyclerView.setLayoutManager(
                RecyclerViewLayoutManager);
        // calling constructor of adapter
        // with source list as a parameter

        adapter = new Adapter(source,mContext,mActivity,HomeActivity.this);
        // Set Horizontal Layout Manager
        // for Recycler view
        HorizontalLayout
                = new LinearLayoutManager(
                HomeActivity.this,
                LinearLayoutManager.VERTICAL,
                false);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2);
        recyclerView.setLayoutManager(HorizontalLayout);
        // Set adapter on recycler view
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0)
                {
                    visibleItemCount = HorizontalLayout.getChildCount();
                    totalItemCount = HorizontalLayout.getItemCount();
                    pastVisibleItems = HorizontalLayout.findFirstVisibleItemPosition();

                    if (!loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = true;
                            loadMore();
                        }
                    }
                }
            }
        });
        DataAccess.getRecipesByPage("",5, new FirebaseCallback() {
            @Override
            public void onResponse(ArrayList<Recipe> recipes) {
                adapter.notifyDataSetChanged();
            }


        }, source);
    }

    public void loadMore(){
        Recipe recipe = source.get(source.size()-1);
        DataAccess.getRecipesByPage(recipe.getKey(), 5, new FirebaseCallback() {
            @Override
            public void onResponse(ArrayList<Recipe> recipes) {
                adapter.notifyDataSetChanged();
                loading = false;
            }
        }, source);
    }
    @Override
    public void onItemClick(Recipe recipe) {

        Intent intent = new Intent(getApplicationContext(), RecipeDetail.class);
        Bundle b = new Bundle();
        b.putString("recipeId", recipe.getKey()); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
    }


}