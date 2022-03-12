package com.example.recipeproject.UI.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import com.example.recipeproject.DataAccess.DataAccess;
import com.example.recipeproject.InterfaceGetData.getRecipeCallback;
import com.example.recipeproject.InterfaceGetData.getUserCallback;
import com.example.recipeproject.R;
import com.example.recipeproject.model.Recipe;
import com.example.recipeproject.model.User;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class RecipeDetail extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;

    //private Recipe recipe;
    private String recipeId = "recipe2";
    TextView recipeName;
    ImageView image;
    ImageView avatar1;
    ImageView avatar2;
    TextView description;
    TextView userName1;
    TextView userName2;
    TextView date;
    SimpleDateFormat formatter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        mContext = getApplicationContext();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        //setSupportActionBar(myToolbar);
        formatter = new SimpleDateFormat("d MMM yyyy");
        image = findViewById(R.id.detail_recipe_img);
        recipeName = findViewById(R.id.detail_recipe_name);
        avatar1 = findViewById(R.id.detail_avatar_img1);
        avatar2 = findViewById(R.id.detail_avatar_img2);
        description = findViewById(R.id.detail_description);
        userName1 = findViewById(R.id.detail_username1);
        userName2 = findViewById(R.id.detail_username2);
        date = findViewById(R.id.detail_date);
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
                Picasso.with(mContext).load(recipe.getThumbnail()).into(image);
                recipeName.setText(recipe.getName());
                description.setText(recipe.getDescription());
                date.setText("On " + formatter.format(recipe.getDate()));
            }
        }, recipeId);
    }
}