package com.example.recipeproject.UI.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.recipeproject.DataAccess.DataAccess;
import com.example.recipeproject.InterfaceGetData.FirebaseCallback;
import com.example.recipeproject.R;
import com.example.recipeproject.Repsentation.Adapter;
import com.example.recipeproject.UI.activities.RecipeDetail;
import com.example.recipeproject.listener.SelectListener;
import com.example.recipeproject.model.Recipe;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteRecipesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Recipe> favoriteRecipes = new ArrayList<>();
    private SearchView searchView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoriteRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteRecipesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteRecipesFragment newInstance(String param1, String param2) {
        FavoriteRecipesFragment fragment = new FavoriteRecipesFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_favorite_recipes, container, false);

        searchView = rootView.findViewById(R.id.searchView);
        recyclerView = rootView.findViewById(R.id.recyclerviewNewest);

        DataAccess.getFavoritesRecipe(new FirebaseCallback() {
            @Override
            public void onResponse(ArrayList<Recipe> recipes) {
                GetDataToRecyclerView(recipes);
            }
        }, FirebaseAuth.getInstance().getCurrentUser().getUid(), null);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                DataAccess.getFavoritesRecipe(new FirebaseCallback() {
                    @Override
                    public void onResponse(ArrayList<Recipe> recipes) {
                        GetDataToRecyclerView(recipes);
                    }
                }, FirebaseAuth.getInstance().getCurrentUser().getUid(), s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return true;
            }
        });

        /*DataAccess.getFavoritesRecipe(new FirebaseCallback() {
            @Override
            public void onResponse(ArrayList<Recipe> recipes) {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        for (Recipe recipe: recipes){

                        }
                        return false;
                    }
                });
            }
        }, FirebaseAuth.getInstance().getCurrentUser().getUid(), null);*/

        // Inflate the layout for this fragment
        return rootView;
    }

    private void GetDataToRecyclerView(ArrayList<Recipe> recipes) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);

        Adapter adapter = new Adapter(recipes, getContext(), getActivity(), new SelectListener() {
            @Override
            public void onItemClick(Recipe recipe) {
                Intent intent = new Intent(getContext(), RecipeDetail.class);
                    Bundle b = new Bundle();
                b.putInt("recipeId", recipe.getId()); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}