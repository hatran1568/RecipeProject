package com.example.recipeproject.UI.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.recipeproject.DataAccess.DataAccess;
import com.example.recipeproject.InterfaceGetData.FirebaseCallback;
import com.example.recipeproject.R;
import com.example.recipeproject.Repsentation.Adapter;
import com.example.recipeproject.listener.SelectListener;
import com.example.recipeproject.model.Recipe;

import java.util.ArrayList;

public class HomePage extends AbstractActivity implements SelectListener {
    private Context mContext;
    private Activity mActivity;
    RecyclerView recyclerView;

    // Array list for recycler view data source
    ArrayList<Recipe> source;

    // Layout Manager
    RecyclerView.LayoutManager RecyclerViewLayoutManager;

    // adapter class object
    Adapter adapter;

    // Linear Layout Manager
    LinearLayoutManager HorizontalLayout;

    View ChildView;
    int RecyclerViewItemPosition;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_page);

        mActivity = HomePage.this;
        mContext = getApplicationContext();
        textView = findViewById(R.id.newest);
        DataAccess.getRecipesOnChange(new FirebaseCallback() {
            @Override
            public void onResponse(ArrayList<Recipe> recipes) {
                recyclerView
                        = (RecyclerView) findViewById(
                        R.id.recyclerview);
                RecyclerViewLayoutManager
                        = new LinearLayoutManager(
                        getApplicationContext());
                // Set LayoutManager on Recycler View
                recyclerView.setLayoutManager(
                        RecyclerViewLayoutManager);
                // calling constructor of adapter
                // with source list as a parameter

                adapter = new Adapter(recipes,mContext,mActivity,HomePage.this);
                // Set Horizontal Layout Manager
                // for Recycler view
                HorizontalLayout
                        = new LinearLayoutManager(
                        HomePage.this,
                        LinearLayoutManager.HORIZONTAL,
                        false);
                recyclerView.setLayoutManager(HorizontalLayout);
                // Set adapter on recycler view
                recyclerView.setAdapter(adapter);

            }


        });
        //AddItemsToRecyclerViewArrayList();

    }


    @Override
    public void onItemClick(Recipe recipe) {

    }
}