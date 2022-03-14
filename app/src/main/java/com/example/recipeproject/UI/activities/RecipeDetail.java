package com.example.recipeproject.UI.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeproject.DataAccess.DataAccess;
import com.example.recipeproject.InterfaceGetData.getRecipeCallback;
import com.example.recipeproject.InterfaceGetData.getUserCallback;
import com.example.recipeproject.R;
import com.example.recipeproject.Repsentation.StepAdapter;
import com.example.recipeproject.model.Recipe;
import com.example.recipeproject.model.User;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecipeDetail extends AbstractActivity {
    private Context mContext;
    private Activity mActivity;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    //private Recipe recipe;
    private int recipeId;
    TextView recipeName;
    ImageView image;
    ImageView avatar1;
    ImageView avatar2;
    TextView description;
    TextView userName1;
    TextView userName2;
    TextView date;
    SimpleDateFormat formatter;
    ListView listIngredients;
    RecyclerView recyclerSteps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        mContext = getApplicationContext();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        formatter = new SimpleDateFormat("d MMM yyyy");
        image = findViewById(R.id.detail_recipe_img);
        recipeName = findViewById(R.id.detail_recipe_name);
        avatar1 = findViewById(R.id.detail_avatar_img1);
        avatar2 = findViewById(R.id.detail_avatar_img2);
        description = findViewById(R.id.detail_description);
        userName1 = findViewById(R.id.detail_username1);
        userName2 = findViewById(R.id.detail_username2);
        date = findViewById(R.id.detail_date);
        listIngredients = findViewById(R.id.detail_list_ingredients);
        recyclerSteps = findViewById(R.id.detail_recycler_steps);
        Bundle b = getIntent().getExtras();
        String recipeId = ""; // or other values
        if(b != null)
            recipeId = b.getString("recipeId");

        DataAccess.getRecipeById(new getRecipeCallback() {
            @Override
            public void onResponse(Recipe recipe) {
                DataAccess.getUserById(new getUserCallback() {
                    @Override
                    public void onResponse(User user) {
                        recipe.setUser(user);
                        userName1.setText(user.getName());
                        userName2.setText(user.getName());
                        Picasso.with(mContext).load(user.getImage_link()).into(avatar1);
                        Picasso.with(mContext).load(user.getImage_link()).into(avatar2);
                    }
                }, recipe.getUserID());
                if(recipe.getThumbnail()!= null && !recipe.getThumbnail().isEmpty()) Picasso.with(mContext).load(recipe.getThumbnail()).into(image);
                recipeName.setText(recipe.getName());
                description.setText(recipe.getDescription());
                date.setText("On " + formatter.format(recipe.getDate()));
                ArrayList<String> ingredients = recipe.getIngredients();
                ArrayAdapter<String> ingredientAdapter = new ArrayAdapter<String>(
                        mContext,
                        R.layout.ingredient_item,
                        R.id.ingredient_name,
                        ingredients
                        );
                listIngredients.setAdapter(ingredientAdapter);
                setListViewHeightBasedOnChildren(listIngredients);

                RecyclerViewLayoutManager
                        = new LinearLayoutManager(
                        getApplicationContext());
                StepAdapter stepAdapter = new StepAdapter(recipe.getSteps(), mContext, mActivity);
                LinearLayoutManager verticalLayout
                        = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                recyclerSteps.setLayoutManager(verticalLayout);
                recyclerSteps.setAdapter(stepAdapter);

            }
        }, recipeId);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    }


