package com.example.recipeproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.recipeproject.model.Recipe;
import com.example.recipeproject.model.Step;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recipe").child("recipe1");

        Recipe recipe = new Recipe();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String description = snapshot.child("description").getValue().toString();
                recipe.setDescription(description);
                String duration = snapshot.child("duration").getValue().toString();
                recipe.setDuration(duration);
                String portion = snapshot.child("portion").getValue().toString();
                recipe.setPortion(portion);
                String thumbnail = snapshot.child("thumbnail").getValue().toString();
                recipe.setThumbnail(thumbnail);
                int userId = Integer.parseInt(snapshot.child("userId").getValue().toString());
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

                textView.setText(steps.get(0).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}