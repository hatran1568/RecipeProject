package com.example.recipeproject.DataAccess;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.recipeproject.InterfaceGetData.FirebaseCallback;
import com.example.recipeproject.InterfaceGetData.getRecipeCallback;
import com.example.recipeproject.InterfaceGetData.getUserCallback;
import com.example.recipeproject.model.Recipe;
import com.example.recipeproject.model.Step;
import com.example.recipeproject.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.util.ArrayList;

public class DataAccess {

    public static void getRecipesOnChange(FirebaseCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recipe");
        ArrayList<Recipe> recipes = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot postSnapshot) {

                for (DataSnapshot snapshot : postSnapshot.getChildren()) {
                    Recipe recipe = new Recipe();
                    String name = snapshot.child("name").getValue().toString();
                    recipe.setName(name);
                    String description = snapshot.hasChild("description") ? snapshot.child("description").getValue().toString() : "";
                    recipe.setDescription(description);
                    String duration = snapshot.hasChild("duration") ? snapshot.child("duration").getValue().toString() :"";
                    recipe.setDuration(duration);
                    String portion = snapshot.hasChild("portion")? snapshot.child("portion").getValue().toString():"";
                    recipe.setPortion(portion);
                    String thumbnail = snapshot.hasChild("thumbnail")? snapshot.child("thumbnail").getValue().toString():"";
                    recipe.setThumbnail(thumbnail);
                    Object userId = snapshot.child("userId").getValue();
                    if(userId == null)
                        userId = snapshot.child("userID").getValue();

                    recipe.setUserID(userId.toString());
                    String key = snapshot.getKey();
                    recipe.setKey(key);

                    int id = Integer.parseInt(snapshot.child("id").getValue().toString());
                    recipe.setId(id);

                    ArrayList<Step> steps = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.child("steps").getChildren()
                    ) {
                        Step step = dataSnapshot.getValue(Step.class);
                        steps.add(step);
                    }
                    recipe.setSteps(steps);

                    ArrayList<String> ingredients = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.child("ingredients").getChildren()
                    ) {
                        ingredients.add(dataSnapshot.getValue().toString());
                    }
                    recipe.setIngredients(ingredients);
                    Object date = snapshot.child("date").getValue();
                    if(date == null)
                        date =snapshot.child("strdate").getValue();
                    recipe.setDate(Date.valueOf(date.toString()));
                    recipes.add(recipe);
                }

                callback.onResponse(recipes);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getRecipeById(getRecipeCallback callback, int id){
        Recipe recipe = new Recipe();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference recipeRef = database.getReference("recipe");
        recipeRef.orderByChild("id").equalTo(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String name = snapshot.child("name").getValue().toString();
                recipe.setName(name);
                String description = snapshot.hasChild("description") ? snapshot.child("description").getValue().toString() : "";
                recipe.setDescription(description);
                String duration = snapshot.hasChild("duration") ? snapshot.child("duration").getValue().toString() :"";
                recipe.setDuration(duration);
                String portion = snapshot.hasChild("portion")? snapshot.child("portion").getValue().toString():"";
                recipe.setPortion(portion);
                String thumbnail = snapshot.hasChild("thumbnail")? snapshot.child("thumbnail").getValue().toString():"";
                recipe.setThumbnail(thumbnail);
                Object userId = snapshot.child("userId").getValue();
                if(userId == null)
                    userId = snapshot.child("userID").getValue();

                recipe.setUserID(userId.toString());

                String key = snapshot.getKey();
                recipe.setKey(key);

                int id = Integer.parseInt(snapshot.child("id").getValue().toString());
                recipe.setId(id);

                ArrayList<Step> steps = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.child("steps").getChildren()
                ) {
                    Step step = dataSnapshot.getValue(Step.class);
                    steps.add(step);
                }
                recipe.setSteps(steps);

                ArrayList<String> ingredients = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.child("ingredients").getChildren()
                ) {
                    String ingredient = dataSnapshot.getValue().toString();
                    ingredients.add(ingredient);
                }
                recipe.setIngredients(ingredients);
                Object date = snapshot.child("date").getValue();
                if(date == null)
                    date =snapshot.child("strdate").getValue();
                recipe.setDate(Date.valueOf(date.toString()));
                callback.onResponse(recipe);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getUserById(getUserCallback callback, String id){
        String uid = id;
        User user = new User();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("user");
        userRef.orderByChild("id").equalTo(uid).addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String name = snapshot.child("name").getValue().toString();
                user.setName(name);
                String email = snapshot.child("email").getValue().toString();
                user.setEmail(email);
                String password = snapshot.child("password").getValue().toString();
                user.setId(id);
                user.setPassword(password);
                String imageLink = snapshot.hasChild("image_link") ? snapshot.child("image_link").getValue().toString() : null;
                user.setImage_link(imageLink);
                callback.onResponse(user);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }



        });
    }
    public static void getRecipe(FirebaseCallback callback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recipe").child("recipe1");
        Recipe recipe = new Recipe();
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>(){


            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot= task.getResult();
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
                    String key = snapshot.getKey();
                    recipe.setKey(key);

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

                } else {

                }
            }
        });




    }
    public static void getRecipes(FirebaseCallback callback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recipe");
        ArrayList<Recipe>  recipes = new ArrayList<>();
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>(){


            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot postSnapshot = task.getResult();
                    for ( DataSnapshot snapshot: postSnapshot.getChildren()   ) {
                        Recipe recipe = new Recipe();
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
                        String key = snapshot.getKey();
                        recipe.setKey(key);

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
                        recipes.add(recipe);

                    }
                    callback.onResponse(recipes);

                } else {

                }
            }
        });
    }
    public static void getRecipesSearch(FirebaseCallback callback, String searchKey){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recipe");
        ArrayList<Recipe>  recipes = new ArrayList<>();
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>(){


            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot postSnapshot = task.getResult();
                    for ( DataSnapshot snapshot: postSnapshot.getChildren()   ) {
                        Recipe recipe = new Recipe();
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
                        String key = snapshot.getKey();
                        recipe.setKey(key);

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
                        if (recipe.getName().startsWith(searchKey))
                            recipes.add(recipe);

                    }
                    callback.onResponse(recipes);

                } else {

                }
            }
        });
    }

    public static void getFavoritesRecipe(FirebaseCallback callback, String userId, String searchKey){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user").child(userId).child("favorites");
        ArrayList<Recipe> recipes = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("recipe").child(child.getKey());
                    Recipe recipe = new Recipe();
                    myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>(){
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                DataSnapshot snapshot= task.getResult();
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
                                String key = snapshot.getKey();
                                recipe.setKey(key);

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

                            } else {

                            }
                        }
                    });
                    recipes.add(recipe);
                }
                callback.onResponse(recipes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
