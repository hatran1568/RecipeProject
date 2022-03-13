package com.example.recipeproject.UI.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.recipeproject.AddRecipeActivity;
import com.example.recipeproject.DataAccess.DataAccess;
import com.example.recipeproject.InterfaceGetData.FirebaseCallback;
import com.example.recipeproject.R;
import com.example.recipeproject.Repsentation.Adapter;
import com.example.recipeproject.listener.SelectListener;
import com.example.recipeproject.model.Recipe;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class SearchActivity extends AbstractActivity implements SelectListener {
    private Context mContext;
    private Activity mActivity;
    RecyclerView recyclerView;

    // Array list for recycler view data source
    ArrayList<Recipe> source;

    //SearchView
    private SearchView mSearchView;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_search);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_add:
                        // TODO: add intent to add recipe
                        startActivity(new Intent(getApplicationContext(), AddRecipeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_profile:
                        // TODO: add intent to search activity
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        mActivity = SearchActivity.this;
        mContext = getApplicationContext();
        textView = findViewById(R.id.newest);
        mSearchView = findViewById(R.id.searchView);
        DataAccess.getRecipesOnChange(new FirebaseCallback() {
            @Override
            public void onResponse(ArrayList<Recipe> recipes) {

                mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        ArrayList<Recipe> searchedRecipes = new ArrayList<>();
                        for (Recipe object : recipes)
                            if (object.getName().toUpperCase().contains(newText.toUpperCase()))
                                searchedRecipes.add(object);
                        //Searched Recycler View
                        GetDataToRecyclerView(searchedRecipes);
                        return true;
                    }
                });
                //Initial Recycler View
                GetDataToRecyclerView(recipes);

            }


        });
    }

    protected void GetDataToRecyclerView(ArrayList<Recipe> recipes){
        //Recycler View
        recyclerView
                = (RecyclerView) findViewById(
                R.id.recyclerviewNewest);
        RecyclerViewLayoutManager
                = new LinearLayoutManager(
                getApplicationContext());
        // Set LayoutManager on Recycler View using GridLayout
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2);

        // calling constructor of adapter
        // with source list as a parameter

        adapter = new Adapter(recipes,mContext,mActivity,SearchActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        // Set adapter on recycler view
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onItemClick(Recipe recipe) {
        Intent intent = new Intent(getApplicationContext(), RecipeDetail.class);
        Bundle b = new Bundle();
        b.putInt("recipeId", recipe.getId()); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
    }
}