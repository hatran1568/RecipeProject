package com.example.recipeproject.InterfaceGetData;

import com.example.recipeproject.model.Recipe;
import com.example.recipeproject.model.User;

import java.util.ArrayList;

public interface FirebaseCallback {
    void onResponse(ArrayList<Recipe> recipes);

}
