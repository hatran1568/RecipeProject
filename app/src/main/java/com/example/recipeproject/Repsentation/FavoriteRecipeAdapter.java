package com.example.recipeproject.Repsentation;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;

import com.example.recipeproject.DataAccess.DataAccess;
import com.example.recipeproject.InterfaceGetData.getUserCallback;
import com.example.recipeproject.R;
import com.example.recipeproject.UI.activities.ProfileActivity;
import com.example.recipeproject.listener.SelectListener;
import com.example.recipeproject.model.Recipe;
import com.example.recipeproject.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteRecipeAdapter extends RecyclerView.Adapter<FavoriteRecipeAdapter.MyView> {
    private ArrayList<Recipe> list;
    private Context mContext;
    private Activity mActivity;
    private SelectListener listener;
    DatabaseReference databaseRef, favRef, favListRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public class MyView extends RecyclerView.ViewHolder {
        TextView userName;
        TextView recipeName;
        ImageView recipeImg;
        TextView recipeDescription;
        CardView card;
        ImageView avatar;

        ImageView favBtn;


        public MyView(View view) {
            super(view);
            userName = view.findViewById(R.id.userName1);
            recipeName = view.findViewById(R.id.recipeName1);
            recipeImg = view.findViewById(R.id.recipeImg1);
            recipeDescription = view.findViewById(R.id.description1);
            card = view.findViewById(R.id.card1);
            avatar = view.findViewById(R.id.avatar);
            favBtn = view.findViewById(R.id.favBtn);
        }

        //Check or uncheck the Favorite button
        public void favoriteChecker(Recipe recipe) {
            favBtn = itemView.findViewById(R.id.favBtn);
            favBtn.setImageResource(R.drawable.favorite_on);

        }
    }

    public FavoriteRecipeAdapter(ArrayList<Recipe> horizontalist, Context mContext, Activity mActivity, SelectListener listener) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.list = horizontalist;
        this.listener = listener;
        setHasStableIds(true);
    }

    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent,
                                     int viewType) {
        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_cardview_double,
                        parent,
                        false);

        //Get the path to user in db
        favRef = database.getReference("user");
        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder,
                                 int position) {

        //Get Logged in user.
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Recipe recipe = list.get(position);
        final String recipeKey = recipe.getKey();

        holder.recipeName.setText(recipe.getName());
        DataAccess.getUserById(new getUserCallback() {
            @Override
            public void onResponse(User user) {
                holder.userName.setText(user.getName());
                Picasso.with(mContext).load(user.getImage_link()).into(holder.avatar);
            }
        }, recipe.getUserID());

        holder.favoriteChecker(recipe);
        String finalCurrentUserId = currentUserId;
        holder.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        favRef.child(finalCurrentUserId).child("favorites").child(recipeKey).removeValue();
                        mContext.startActivity(new Intent(mContext, ProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                notifyDataSetChanged();
            }
        });


        holder.recipeDescription.setText(recipe.getDescription());
        if (list.get(position).getThumbnail() != null && !list.get(position).getThumbnail().isEmpty())
            Picasso.with(mContext).load(list.get(position).getThumbnail()).into(holder.recipeImg);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
