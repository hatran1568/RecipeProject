package com.example.recipeproject.Repsentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeproject.DataAccess.DataAccess;
import com.example.recipeproject.InterfaceGetData.getUserCallback;
import com.example.recipeproject.R;
import com.example.recipeproject.model.Recipe;
import com.example.recipeproject.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FavoriteRecipeAdapter extends FirebaseRecyclerAdapter<Recipe, FavoriteRecipeAdapter.ViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FavoriteRecipeAdapter(@NonNull FirebaseRecyclerOptions<Recipe> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Recipe model) {
        
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_cardview_double,
                        parent,
                        false);

        // return itemView
        return new FavoriteRecipeAdapter.ViewHolder(itemView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView recipeName;
        ImageView recipeImg;
        TextView recipeDescription;
        CardView card;
        Context context;
        ImageView avatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName= itemView.findViewById(R.id.userName1);
            recipeName = itemView.findViewById(R.id.recipeName1);
            recipeImg = itemView.findViewById(R.id.recipeImg1);
            recipeDescription = itemView.findViewById(R.id.description1);
            card = itemView.findViewById(R.id.card1);
            avatar = itemView.findViewById(R.id.avatar);
        }

    }
}
