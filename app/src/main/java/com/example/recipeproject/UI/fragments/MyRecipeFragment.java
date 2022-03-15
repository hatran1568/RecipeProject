package com.example.recipeproject.UI.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.recipeproject.DataAccess.DataAccess;
import com.example.recipeproject.InterfaceGetData.FirebaseCallback;
import com.example.recipeproject.R;
import com.example.recipeproject.Repsentation.Adapter;
import com.example.recipeproject.Repsentation.FavoriteRecipeAdapter;
import com.example.recipeproject.UI.activities.RecipeDetail;
import com.example.recipeproject.listener.SelectListener;
import com.example.recipeproject.model.Recipe;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRecipeFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyRecipeFragment newInstance(String param1, String param2) {
        MyRecipeFragment fragment = new MyRecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_recipe, container, false);

        searchView = rootView.findViewById(R.id.searchView);
        recyclerView = rootView.findViewById(R.id.recyclerviewNewest);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DataAccess.getRecipesOnChange(new FirebaseCallback() {
            @Override
            public void onResponse(ArrayList<Recipe> recipes) {
                ArrayList<Recipe> myRecipes = new ArrayList<>();
                for(Recipe recipe: recipes){
                    if (recipe.getUserID().equals(uid)){
                        myRecipes.add(recipe);
                    }
                }
                GetDataToRecyclerView(myRecipes);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        ArrayList<Recipe> searchedRecipe = new ArrayList<>();
                        for(Recipe recipe: myRecipes){
                            if (recipe.getName().contains(s)){
                                searchedRecipe.add(recipe);
                            }
                        }
                        GetDataToRecyclerView(searchedRecipe);
                        return true;
                    }
                });
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private void GetDataToRecyclerView(ArrayList<Recipe> recipes) {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);

        Adapter adapter = new Adapter(recipes, getContext(), getActivity(), new SelectListener() {

            @Override
            public void onItemClick(Recipe recipe) {
                Intent intent = new Intent(getContext(), RecipeDetail.class);
                Bundle b = new Bundle();
                b.putString("recipeId", recipe.getKey()); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}