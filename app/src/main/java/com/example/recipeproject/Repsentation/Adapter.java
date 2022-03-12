package com.example.recipeproject.Repsentation;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;


import com.example.recipeproject.DataAccess.DataAccess;
import com.example.recipeproject.InterfaceGetData.getUserCallback;
import com.example.recipeproject.R;
import com.example.recipeproject.listener.SelectListener;
import com.example.recipeproject.model.Recipe;
import com.example.recipeproject.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter  extends RecyclerView.Adapter<Adapter.MyView> {
    private ArrayList<Recipe> list;
    private Context mContext;
    private Activity mActivity;
    private SelectListener listener;


    public class MyView extends  RecyclerView.ViewHolder {
        TextView userName;
        TextView recipeName;
        ImageView recipeImg;
        TextView recipeDescription;
        CardView card;
        Context context;
        ImageView avatar;
        public  MyView(View view){
            super(view);
           userName= view.findViewById(R.id.userName1);
           recipeName = view.findViewById(R.id.recipeName1);
           recipeImg = view.findViewById(R.id.recipeImg1);
           recipeDescription = view.findViewById(R.id.description1);
           card = view.findViewById(R.id.card1);
           avatar = view.findViewById(R.id.avatar);
        }


    }
    public Adapter( ArrayList<Recipe> horizontalist,Context mContext,Activity mActivity,SelectListener listener){
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.list = horizontalist;
        this.listener=listener;
    }
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent,
                                     int viewType)
    {

        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_cardview_double,
                        parent,
                        false);

        // return itemView
        return new MyView(itemView);
    }
    @Override
    public void onBindViewHolder(final MyView holder,
                                 int position)
    {
        Recipe recipe = list.get(position);

       holder.recipeName.setText(recipe.getName());
        DataAccess.getUserById(new getUserCallback() {
            @Override
            public void onResponse(User user) {
                holder.userName.setText(user.getName());
                Picasso.with(mContext).load(user.getImage_link()).into(holder.avatar);
            }
        },recipe.getUserID());

       holder.recipeDescription.setText(recipe.getDescription());
       Picasso.with(mContext).load(list.get(position).getThumbnail()).into(holder.recipeImg);
       holder.card.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               listener.onItemClick(recipe);
           }
       });
    }
    @Override
    public int getItemCount()
    {
        return list.size();
    }
}
