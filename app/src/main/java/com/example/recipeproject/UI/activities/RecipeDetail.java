package com.example.recipeproject.UI.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeproject.DataAccess.DataAccess;
import com.example.recipeproject.InterfaceGetData.getRecipeCallback;
import com.example.recipeproject.InterfaceGetData.getUserCallback;
import com.example.recipeproject.R;
import com.example.recipeproject.Repsentation.StepAdapter;
import com.example.recipeproject.model.Recipe;
import com.example.recipeproject.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecipeDetail extends AbstractActivity {
    private Context mContext;
    private Activity mActivity;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    //private Recipe recipe;
    private String recipeId;
    ImageButton editBtn, deleteBtn;
    TextView recipeName;
    ImageView image;
    ImageView avatar1;
    ImageView avatar2;
    TextView description;
    TextView userName1;
    TextView userName2;
    TextView date;
    SimpleDateFormat formatter;
    ListView listIngredients;
    RecyclerView recyclerSteps;
    ImageButton backButton;
    TextView portion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        mContext = getApplicationContext();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar2);
//        setSupportActionBar(myToolbar);
//        if (getSupportActionBar() != null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//        }
        backButton = findViewById(R.id.detail_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        formatter = new SimpleDateFormat("d MMM yyyy");
        image = findViewById(R.id.detail_recipe_img);
        recipeName = findViewById(R.id.detail_recipe_name);
        avatar1 = findViewById(R.id.detail_avatar_img1);
        avatar2 = findViewById(R.id.detail_avatar_img2);
        description = findViewById(R.id.detail_description);
        userName1 = findViewById(R.id.detail_username1);
        userName2 = findViewById(R.id.detail_username2);
        date = findViewById(R.id.detail_date);
        listIngredients = findViewById(R.id.detail_list_ingredients);
        recyclerSteps = findViewById(R.id.detail_recycler_steps);
        portion = findViewById(R.id.detail_portion);
        Bundle b = getIntent().getExtras();
        recipeId = ""; // or other values
        if(b != null)
            recipeId = b.getString("recipeId");

        DataAccess.getRecipeById(new getRecipeCallback() {
            @Override
            public void onResponse(Recipe recipe) {
                DataAccess.getUserById(new getUserCallback() {
                    @Override
                    public void onResponse(User user) {
                        recipe.setUser(user);
                        userName1.setText(user.getName());
                        userName2.setText(user.getName());
                        Picasso.with(mContext).load(user.getImage_link()).into(avatar1);
                        Picasso.with(mContext).load(user.getImage_link()).into(avatar2);
                    }
                }, recipe.getUserID());
                if(recipe.getThumbnail()!= null && !recipe.getThumbnail().isEmpty()) Picasso.with(mContext).load(recipe.getThumbnail()).into(image);
                recipeName.setText(recipe.getName());
                description.setText(recipe.getDescription());
                date.setText("On " + formatter.format(recipe.getDate()));
                ArrayList<String> ingredients = recipe.getIngredients();
                ArrayAdapter<String> ingredientAdapter = new ArrayAdapter<String>(
                        mContext,
                        R.layout.ingredient_item,
                        R.id.ingredient_name,
                        ingredients
                        );
                listIngredients.setAdapter(ingredientAdapter);
                setListViewHeightBasedOnChildren(listIngredients);

                RecyclerViewLayoutManager
                        = new LinearLayoutManager(
                        getApplicationContext());
                StepAdapter stepAdapter = new StepAdapter(recipe.getSteps(), mContext, mActivity);
                LinearLayoutManager verticalLayout
                        = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                recyclerSteps.setLayoutManager(verticalLayout);
                recyclerSteps.setAdapter(stepAdapter);


                //Button controls
                    //Get Logged in user.
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String currentUserId = null;
                if (user != null){
                    currentUserId = user.getUid();
                }
                editBtn = findViewById(R.id.editBtn);
                deleteBtn = findViewById(R.id.deleteBtn);
                if (!recipe.getUserID().equals(currentUserId)){

                    removeBtn();
                }
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showConfirmDelete();
                    }
                });
                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), EditRecipe.class);
                        Bundle b = new Bundle();
                        b.putString("recipeId", recipeId); //Your id
                        intent.putExtras(b); //Put your id to your next Intent
                        startActivity(intent);
                    }
                });
                if(!(recipe.getDuration() == null || recipe.getDuration().isEmpty()))
                    createDurationTextView(recipe.getDuration());
                else removeSeparator();
                if(!(recipe.getPortion() == null || recipe.getPortion().isEmpty()))
                    portion.setText("\uD83D\uDC64" + " "+recipe.getPortion());
                else removePortion();


            }
        }, recipeId);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void showConfirmDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Delete this recipe?")
                .setMessage("Just so you know, this can't be undone.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        DataAccess.deleteRecipeByKey(recipeId);
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent);
                    }})
                .setNegativeButton("Cancel", null);
        builder.create();
        builder.show();
    }

    public void createDurationTextView(String duration){
        ConstraintLayout constraintLayout = findViewById(R.id.detail_constraint_layout);
        View separator_bottom = findViewById(R.id.separator_bottom);
        View separator_top = findViewById(R.id.separator_top);
        float dpRatio = mContext.getResources().getDisplayMetrics().density;
        ConstraintSet set = new ConstraintSet();
        TextView durationText = new TextView(mContext);
        durationText.setId(View.generateViewId());
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        durationText.setLayoutParams(params);
        durationText.setText("\uD83D\uDD52" + " " + duration);
        durationText.setGravity(Gravity.CENTER);
        constraintLayout.addView(durationText);

        set.clone(constraintLayout);
        set.clear(separator_bottom.getId(), ConstraintSet.TOP);
        set.connect(durationText.getId(), ConstraintSet.TOP, separator_top.getId(), ConstraintSet.BOTTOM, (int) (10*dpRatio));
        set.connect(separator_bottom.getId(), ConstraintSet.TOP, durationText.getId(), ConstraintSet.BOTTOM, (int) (10*dpRatio));
        set.applyTo(constraintLayout);
    }
    public void removeSeparator(){
        float dpRatio = mContext.getResources().getDisplayMetrics().density;
        View separator_bottom = findViewById(R.id.separator_bottom);
        View separator_top = findViewById(R.id.separator_top);
        ConstraintLayout constraintLayout = findViewById(R.id.detail_constraint_layout);
        ConstraintSet set = new ConstraintSet();
        set.clone(constraintLayout);
        set.clear(separator_bottom.getId(), ConstraintSet.TOP);
        constraintLayout.removeView(separator_top);
        set.connect(separator_bottom.getId(), ConstraintSet.TOP, description.getId(), ConstraintSet.BOTTOM, (int) (12*dpRatio));
        set.applyTo(constraintLayout);
    }
    public void removePortion(){
        float dpRatio = mContext.getResources().getDisplayMetrics().density;
        ConstraintLayout constraintLayout = findViewById(R.id.detail_constraint_layout);
        ConstraintSet set = new ConstraintSet();
        set.clone(constraintLayout);
        set.clear(listIngredients.getId(), ConstraintSet.TOP);
        constraintLayout.removeView(portion);
        set.connect(listIngredients.getId(), ConstraintSet.TOP, R.id.detail_text_ingredient, ConstraintSet.BOTTOM, (int) (12*dpRatio));
        set.applyTo(constraintLayout);
    }

    public void removeBtn(){
        ConstraintLayout constraintLayout = findViewById(R.id.mainLayout);
        constraintLayout.removeView(deleteBtn);
        constraintLayout.removeView(editBtn);
    }
    }


