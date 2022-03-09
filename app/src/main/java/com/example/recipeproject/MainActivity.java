package com.example.recipeproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.recipeproject.DataAccess.DataAccess;
import com.example.recipeproject.InterfaceGetData.FirebaseCallback;
import com.example.recipeproject.model.Recipe;
import com.example.recipeproject.model.Step;
import com.example.recipeproject.model.User;
import com.google.firebase.database.DataSnapshot;

import java.sql.Date;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         Recipe recipe = new Recipe();
         textView = findViewById(R.id.textView);
         DataAccess.getRecipes(new FirebaseCallback() {
            @Override
            public void onResponse(ArrayList<Recipe> recipes) {
                textView.setText(recipes.get(0).getDescription());


            }


         });





    }

    public void getRecipes(FirebaseCallback callback){

    }


    public void getRecipe(@NonNull DataSnapshot snapshot,Recipe recipe){

        String description = snapshot.child("description").getValue().toString();
        recipe.setDescription(description);
        String duration = snapshot.child("duration").getValue().toString();
        recipe.setDuration(duration);
        String portion = snapshot.child("portion").getValue().toString();
        recipe.setPortion(portion);
        String thumbnail = snapshot.child("thumbnail").getValue().toString();
        recipe.setThumbnail(thumbnail);
        String userId = snapshot.child("userId").getValue().toString();
        recipe.setUserID(userId);
        int id = Integer.parseInt(snapshot.child("id").getValue().toString());
        recipe.setId(id);

        ArrayList<Step> steps = new ArrayList<>();
        for (DataSnapshot dataSnapshot: snapshot.child("steps").getChildren()
        ) {
            Step step = dataSnapshot.getValue(Step.class);
            steps.add(step);
        }
        recipe.setSteps(steps);

        ArrayList<String> ingredients = new ArrayList<>();
        for (DataSnapshot dataSnapshot: snapshot.child("ingredients").getChildren()
        ) {
            ingredients.add(dataSnapshot.getValue().toString());
        }
        recipe.setIngredients(ingredients);
        Date date = Date.valueOf(snapshot.child("date").getValue().toString());
        recipe.setDate(date);

    }
}