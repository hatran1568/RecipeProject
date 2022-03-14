package com.example.recipeproject.Repsentation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.recipeproject.UI.activities.RecipeDetail;
import com.example.recipeproject.listener.SelectListener;
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
        DataAccess.getUserById(new getUserCallback() {
            @Override
            public void onResponse(User user) {
                holder.userName.setText(user.getName());
                Picasso.with(holder.avatar.getContext()).load(user.getImage_link()).into(holder.avatar);
            }
        },model.getUserID());
        holder.recipeName.setText(model.getName());
        holder.recipeDescription.setText(model.getDescription());
        if (model.getThumbnail() != null && !model.getThumbnail().isEmpty())
            Picasso.with(holder.avatar.getContext()).load(model.getThumbnail()).into(holder.recipeImg);
        holder.card.setOnClickListener(holder.onClick(););
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
        return new FavoriteRecipeAdapter.ViewHolder(itemView, new MyClickListener() {
            @Override
            public void onSelect(int p) {
                Intent intent = new Intent((itemView.getContext()), RecipeDetail.class);
                Bundle b = new Bundle();
                b.putInt("recipeId", recipe.getId()); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                itemView.getContext().startActivity(intent);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MyClickListener listener;

        TextView userName;
        TextView recipeName;
        ImageView recipeImg;
        TextView recipeDescription;
        CardView card;
        ImageView avatar;

        public ViewHolder(@NonNull View itemView, MyClickListener listener) {
            super(itemView);
            userName= itemView.findViewById(R.id.userName1);
            recipeName = itemView.findViewById(R.id.recipeName1);
            recipeImg = itemView.findViewById(R.id.recipeImg1);
            recipeDescription = itemView.findViewById(R.id.description1);
            card = itemView.findViewById(R.id.card1);
            avatar = itemView.findViewById(R.id.avatar);
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface MyClickListener {
        void onSelect(int p);
    }
}


