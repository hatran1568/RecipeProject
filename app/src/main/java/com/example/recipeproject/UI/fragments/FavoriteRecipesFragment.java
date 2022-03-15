package com.example.recipeproject.UI.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.recipeproject.DataAccess.DataAccess;
import com.example.recipeproject.InterfaceGetData.FirebaseCallback;
import com.example.recipeproject.InterfaceGetData.getRecipeCallback;
import com.example.recipeproject.R;
import com.example.recipeproject.Repsentation.Adapter;
import com.example.recipeproject.Repsentation.FavoriteRecipeAdapter;
import com.example.recipeproject.UI.activities.RecipeDetail;
import com.example.recipeproject.listener.SelectListener;
import com.example.recipeproject.model.Recipe;
import com.example.recipeproject.model.Step;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteRecipesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    private ArrayList<Recipe> favoriteRecipes = new ArrayList<>();
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;

    FavoriteRecipeAdapter adapter;

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
        swipeRefreshLayout = rootView.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favorites");
        ArrayList<String> favoriteIds = new ArrayList<>();

        // get data from firebase and populate recyclerView
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child: snapshot.getChildren()){
                    favoriteIds.add(child.getKey());
                }
                for(String id: favoriteIds){
                    DataAccess.getRecipeById(new getRecipeCallback() {
                        @Override
                        public void onResponse(Recipe recipe) {
                            favoriteRecipes.add(recipe);
                        }
                    }, id);
                }
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        GetDataToRecyclerView(favoriteRecipes);
                    }
                });
                //GetDataToRecyclerView(favoriteRecipes);

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        ArrayList<Recipe> searchedRecipes = new ArrayList<>();
                        for (Recipe object : favoriteRecipes)
                            if (object.getName().toUpperCase()
                                    .contains(query.toUpperCase()))
                                searchedRecipes.add(object);
                        //Searched Recycler View

                        GetDataToRecyclerView(searchedRecipes);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        ArrayList<Recipe> searchedRecipes = new ArrayList<>();
                        for (Recipe object : favoriteRecipes)
                            if (object.getName().toUpperCase()
                                    .contains(newText.toUpperCase()))
                                searchedRecipes.add(object);
                        //Searched Recycler View

                        GetDataToRecyclerView(searchedRecipes);
                        return true;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });




        // Inflate the layout for this fragment
        return rootView;
    }


    private void GetDataToRecyclerView(ArrayList<Recipe> recipes) {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);

        FavoriteRecipeAdapter adapter = new FavoriteRecipeAdapter(recipes, getContext(), getActivity(), new SelectListener() {

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

    @Override
    public void onRefresh() {
        GetDataToRecyclerView(favoriteRecipes);
        swipeRefreshLayout.setRefreshing(false);
    }
}