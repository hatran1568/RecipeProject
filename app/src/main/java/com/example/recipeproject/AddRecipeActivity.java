package com.example.recipeproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipeproject.UI.activities.AbstractActivity;
import com.example.recipeproject.model.Recipe;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AddRecipeActivity extends AbstractActivity {
    EditText rename;
    Recipe recipe;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        recipe = new Recipe();
         rename= findViewById(R.id.RecipeName);
        TextView RecipeDescription = findViewById(R.id.RecipeDescription);
        TextView portion = findViewById(R.id.ReicpePortion);
        TextView duration = findViewById(R.id.RecipeDuration);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("test");
        ArrayList<Integer> ingreId = new ArrayList<>();
        ArrayList<String> ingredients = new ArrayList<>();
        setContentView(R.layout.activity_add_recipe);
        LinearLayout addIngre = findViewById(R.id.AddIngredientsLayout);
        View addIngreBtn =findViewById(R.id.AddIngredientBtn);
        View addStepBtn = findViewById(R.id.AddStepButton);
        LinearLayout addStep = findViewById(R.id.AddStepLayout);
        addIngreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConstraintLayout ctrlayout = new ConstraintLayout(getApplicationContext());
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
                int marg= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,getResources().getDisplayMetrics());
                layoutParams.setMargins(0, marg, marg, marg/2);
                ctrlayout.setLayoutParams(layoutParams);
                EditText newIngre = new EditText(getApplicationContext());
                newIngre.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,getResources().getDisplayMetrics()));
                newIngre.setWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics()));
                newIngre.setId(View.generateViewId());
                newIngre.setBackgroundResource(R.drawable.edit_text_border);
                ImageButton btn = new ImageButton(getApplicationContext());
                btn.setBackgroundColor(Color.TRANSPARENT);
                btn.setImageDrawable(getDrawable(R.drawable.ic_baseline_close_24));
                btn.setId(View.generateViewId());
                ingreId.add(newIngre.getId());
                ConstraintSet set = new ConstraintSet();
                ctrlayout.addView(newIngre);
                ctrlayout.addView(btn);
                set.clone(ctrlayout);
                set.constrainWidth(btn.getId(),(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,56,getResources().getDisplayMetrics()));
                set.constrainHeight(btn.getId(),(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,getResources().getDisplayMetrics()));
                set.connect(btn.getId(),ConstraintSet.RIGHT,set.PARENT_ID,ConstraintSet.RIGHT) ;
                set.applyTo(ctrlayout);
                addIngre.addView(ctrlayout);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view1) {
                        ingreId.remove(Integer.valueOf(newIngre.getId()));
                        addIngre.removeView(ctrlayout);

                    }
                });

            }
        });

        addStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout ctrlayout = new ConstraintLayout(getApplicationContext());
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
                int marg= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,getResources().getDisplayMetrics());
                layoutParams.setMargins(0, marg, marg, marg/2);
                ctrlayout.setLayoutParams(layoutParams);
                EditText newStep = new EditText(getApplicationContext());
                newStep.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,getResources().getDisplayMetrics()));
                newStep.setWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics()));
                newStep.setId(View.generateViewId());
                newStep.setBackgroundResource(R.drawable.edit_text_border);
                ImageButton btn = new ImageButton(getApplicationContext());
                btn.setBackgroundColor(Color.TRANSPARENT);
                btn.setImageDrawable(getDrawable(R.drawable.ic_baseline_close_24));
                btn.setId(View.generateViewId());
                ImageView img = new ImageView(getApplicationContext());
                img.setImageDrawable(getDrawable(R.drawable.ic_food));
                img.setId(View.generateViewId());
                ConstraintSet set = new ConstraintSet();
                ctrlayout.addView(newStep);
                ctrlayout.addView(btn);
                ctrlayout.addView(img);
                set.clone(ctrlayout);
                set.constrainHeight(img.getId(),(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,getResources().getDisplayMetrics()));
                set.constrainWidth(img.getId(),(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,180,getResources().getDisplayMetrics()));
                set.connect(img.getId(),ConstraintSet.TOP,newStep.getId(),ConstraintSet.BOTTOM,15);
                set.connect(img.getId(),ConstraintSet.LEFT,set.PARENT_ID,ConstraintSet.LEFT,15);

                set.constrainWidth(btn.getId(),(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,56,getResources().getDisplayMetrics()));
                set.constrainHeight(btn.getId(),(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,getResources().getDisplayMetrics()));
                set.connect(btn.getId(),ConstraintSet.RIGHT,set.PARENT_ID,ConstraintSet.RIGHT) ;
                set.applyTo(ctrlayout);

                addStep.addView(ctrlayout);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view1) {
                        addStep.removeView(ctrlayout);
                    }
                });
            }
        });
        View btnAdd = findViewById(R.id.AddRecipeButton);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Integer index: ingreId) {
                    TextView t = findViewById(index);
                    ingredients.add(t.getText().toString());
                }
                recipe.setName(rename.getText().toString());
                recipe.setDescription(RecipeDescription.getText().toString());
                recipe.setPortion(portion.getText().toString());
                recipe.setDuration(duration.getText().toString());
                String strDate = dtf.format(LocalDate.now());
                recipe.setDate(Date.valueOf(strDate));
                recipe.setIngredients(ingredients);
                String key = myRef.push().getKey();
                myRef.child(key).setValue(recipe);

            }
        });

    }


}