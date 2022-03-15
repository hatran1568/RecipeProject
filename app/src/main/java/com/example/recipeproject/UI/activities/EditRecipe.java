package com.example.recipeproject.UI.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipeproject.DataAccess.DataAccess;
import com.example.recipeproject.InterfaceGetData.FirebaseStorageCallback;
import com.example.recipeproject.InterfaceGetData.getRecipeCallback;
import com.example.recipeproject.InterfaceGetData.getUserCallback;
import com.example.recipeproject.R;
import com.example.recipeproject.model.Recipe;
import com.example.recipeproject.model.Step;
import com.example.recipeproject.model.User;
import com.example.recipeproject.utils.FirestoreHelper;
import com.example.recipeproject.utils.PermissionHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.runAsync;

public class EditRecipe extends AbstractActivity {
    String recipeId;
    final int PICKER_REQUEST_CODE =100;
    EditText rename;
    Recipe recipe;
    Map<Integer,Uri> URIMapping = new HashMap<>();
    Map<Integer,Boolean> BooleanIMapping = new HashMap<>();
    DateTimeFormatter dtf;
    ImageButton backBtn;
    ArrayList<Integer> stepTextId;
    ArrayList<Integer> imgStepId;
    FirebaseDatabase database;
    EditText RecipeDescription;
    EditText portion;
    EditText duration;
    DatabaseReference myRef;
    ArrayList<Integer> ingreId;
    ArrayList<String> ingredients;
    ArrayList<Step> steps;
    LinearLayout addIngre;
    View addIngreBtn;
    View addStepBtn;
    LinearLayout addStep;
    DatabaseReference recipeRef;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_recipe);

        dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        recipe = new Recipe();
        backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ImageView RecipeThumbnail = findViewById(R.id.RecipeImage);
        RecipeThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] PERMISSIONS = {
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                // Check user permission
                if (PermissionHelper.hasPermissions(EditRecipe.this, PERMISSIONS)) {
                    showImagePicker(R.id.RecipeImage);
                } else {
                    ActivityCompat.requestPermissions(EditRecipe.this, PERMISSIONS, PICKER_REQUEST_CODE);
                }
            }

        });
        Bundle b = getIntent().getExtras();
        recipeId = ""; // or other values
        if(b != null)
            recipeId = b.getString("recipeId");

        rename = findViewById(R.id.RecipeName);

        stepTextId = new ArrayList<>();

        imgStepId = new ArrayList<>();

        RecipeDescription = findViewById(R.id.RecipeDescription);

        portion = findViewById(R.id.ReicpePortion);

        duration = findViewById(R.id.RecipeDuration);

        database = FirebaseDatabase.getInstance();

        myRef = database.getReference().child("test");

        recipeRef = database.getReference().child("recipe");

        ingreId = new ArrayList<>();

        ingredients = new ArrayList<>();

        steps = new ArrayList<>();

        addIngre = findViewById(R.id.AddIngredientsLayout);

        addIngreBtn = findViewById(R.id.AddIngredientBtn);

        addStepBtn = findViewById(R.id.AddStepButton);

        addStep = findViewById(R.id.AddStepLayout);
        BooleanIMapping.put(R.id.RecipeImage,false);
        addIngreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConstraintLayout ctrlayout = new ConstraintLayout(getApplicationContext());
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                int marg = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
                layoutParams.setMargins(0, marg, marg, marg / 2);
                ctrlayout.setLayoutParams(layoutParams);
                EditText newIngre = new EditText(getApplicationContext());
                newIngre.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
                newIngre.setWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics()));
                newIngre.setId(View.generateViewId());
                newIngre.setBackgroundResource(R.drawable.edit_text_border);



                newIngre.setSingleLine(false);
                newIngre.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                newIngre.setMaxLines(3);

                ImageButton btn = new ImageButton(getApplicationContext());
                btn.setBackgroundColor(Color.TRANSPARENT);
                btn.setImageDrawable(getDrawable(R.drawable.ic_baseline_close_24));
                btn.setId(View.generateViewId());
                ingreId.add(newIngre.getId());
                ConstraintSet set = new ConstraintSet();
                ctrlayout.addView(newIngre);
                ctrlayout.addView(btn);
                set.clone(ctrlayout);

                set.constrainWidth(btn.getId(), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics()));
                set.constrainHeight(btn.getId(), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
                set.connect(btn.getId(), ConstraintSet.RIGHT, set.PARENT_ID, ConstraintSet.RIGHT);
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
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                int marg = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
                layoutParams.setMargins(0, marg, marg, marg / 2);
                ctrlayout.setLayoutParams(layoutParams);
                EditText newStep = new EditText(getApplicationContext());
                newStep.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
                newStep.setWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics()));
                newStep.setId(View.generateViewId());
                newStep.setBackgroundResource(R.drawable.edit_text_border);
                newStep.setSingleLine(false);
                //newIngre.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                newStep.setMaxLines(5);
                ImageButton btn = new ImageButton(getApplicationContext());
                btn.setBackgroundColor(Color.TRANSPARENT);
                btn.setImageDrawable(getDrawable(R.drawable.ic_baseline_close_24));
                btn.setId(View.generateViewId());
                ImageView img = new ImageView(getApplicationContext());
                img.setImageDrawable(getDrawable(R.drawable.ic_food));
                img.setId(View.generateViewId());
                BooleanIMapping.put(img.getId(),false);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                stepTextId.add(newStep.getId());
                imgStepId.add(img.getId());
                ConstraintSet set = new ConstraintSet();
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String[] PERMISSIONS = {
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        };
                        // Check user permission
                        if (PermissionHelper.hasPermissions(EditRecipe.this, PERMISSIONS)) {
                            showImagePicker(img.getId());
                        } else {
                            ActivityCompat.requestPermissions(EditRecipe.this, PERMISSIONS, PICKER_REQUEST_CODE);
                        }
                    }
                });
                ctrlayout.addView(newStep);
                ctrlayout.addView(btn);
                ctrlayout.addView(img);
                set.clone(ctrlayout);
                set.constrainHeight(img.getId(), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
                set.constrainWidth(img.getId(), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics()));
                set.connect(img.getId(), ConstraintSet.TOP, newStep.getId(), ConstraintSet.BOTTOM, 15);
                set.connect(img.getId(), ConstraintSet.LEFT, set.PARENT_ID, ConstraintSet.LEFT, 15);

                set.constrainWidth(btn.getId(), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics()));
                set.constrainHeight(btn.getId(), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
                set.connect(btn.getId(), ConstraintSet.RIGHT, set.PARENT_ID, ConstraintSet.RIGHT);
                set.applyTo(ctrlayout);

                addStep.addView(ctrlayout);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view1) {
                        stepTextId.remove(Integer.valueOf(newStep.getId()));
                        imgStepId.remove(Integer.valueOf(img.getId()));
                        addStep.removeView(ctrlayout);
                    }
                });
            }
        });
        Button btnAdd = findViewById(R.id.AddRecipeButton);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"inget "+ String.valueOf(stepTextId.size()));

                if(ingreId.size() ==0|| stepTextId.size()==0||rename.getText().toString().length()==0){
                    Toast.makeText(EditRecipe.this,"Please fill ingredients and steps",Toast.LENGTH_LONG).show();

                   return;
                }
                Thread thread = new Thread(){
                    @Override
                    public void run() {

                        for (Integer index : ingreId) {
                            TextView t = findViewById(index);
                            ingredients.add(t.getText().toString());
                        }
                        String key = recipeId;
                        recipeRef.child(key).child("name").setValue(rename.getText().toString());
                        recipeRef.child(key).child("description").setValue(RecipeDescription.getText().toString());
                        recipeRef.child(key).child("portion").setValue(portion.getText().toString());
                        recipeRef.child(key ).child("duration").setValue(duration.getText().toString());
                        if(BooleanIMapping.get(R.id.RecipeImage)==true) {


                            FirestoreHelper.uploadToStorage(new FirebaseStorageCallback() {
                                @Override
                                public void onResponse(String url) {
                                    recipeRef.child(key).child("thumbnail").setValue(url);
                                    Log.d("Firebase", "Get value of download url: " + url);
                                }
                            }, EditRecipe.this, URIMapping.get(R.id.RecipeImage));
                        }
                        for (int i = 0; i < stepTextId.size(); i++) {
                            Step s = new Step();
                            int index=i;
                            TextView t = findViewById(stepTextId.get(i));
                            recipeRef.child(key).child("steps").child(String.valueOf(index)).child("text").setValue(t.getText().toString());
                            ImageView imageUri = findViewById(imgStepId.get(i));
                            if(BooleanIMapping.get(imgStepId.get(i))==true) {
                                FirestoreHelper.uploadToStorage(new FirebaseStorageCallback() {
                                    @Override
                                    public void onResponse(String url) {
                                        recipeRef.child(key).child("steps").child(String.valueOf(index)).child("image").setValue(url);
                                        Log.d("Firebase", "Get value of download url: " + url);
                                    }
                                }, EditRecipe.this, URIMapping.get(imgStepId.get(i)));
                            }
                            steps.add(s);
                        }






                    }
                };
                thread.run();
                Thread thread1 = new Thread(){
                    @Override
                    public void run() {
                        startActivity(new Intent(EditRecipe.this, ProfileActivity.class));
                    }
                }  ;
                thread1.run();
            }
        });
        DataAccess.getRecipeById(new getRecipeCallback() {
            @Override
            public void onResponse(Recipe recipe) {
                Picasso.with(EditRecipe.this).load(recipe.getThumbnail()).into(RecipeThumbnail);
                rename.setText(recipe.getName());
                RecipeDescription.setText(recipe.getDescription());
                portion.setText(recipe.getPortion());
                duration.setText(recipe.getDuration());
                ArrayList<String> recipeIngre = recipe.getIngredients();
                for (String ing: recipeIngre
                ) {
                    ConstraintLayout ctrlayout = new ConstraintLayout(getApplicationContext());
                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    int marg = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
                    layoutParams.setMargins(0, marg, marg, marg / 2);
                    ctrlayout.setLayoutParams(layoutParams);
                    EditText newIngre = new EditText(getApplicationContext());
                    newIngre.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
                    newIngre.setWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics()));
                    newIngre.setId(View.generateViewId());
                    newIngre.setBackgroundResource(R.drawable.edit_text_border);
                    newIngre.setSingleLine(false);
                    //newIngre.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    newIngre.setMaxLines(3);
                    newIngre.setText(ing);
                    newIngre.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            ingreId.add(newIngre.getId());
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    ImageButton btn = new ImageButton(getApplicationContext());
                    btn.setBackgroundColor(Color.TRANSPARENT);
                    btn.setImageDrawable(getDrawable(R.drawable.ic_baseline_close_24));
                    btn.setId(View.generateViewId());
                    ingreId.add(newIngre.getId());
                    ConstraintSet set = new ConstraintSet();
                    ctrlayout.addView(newIngre);
                    ctrlayout.addView(btn);
                    set.clone(ctrlayout);
                    set.constrainWidth(btn.getId(), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics()));
                    set.constrainHeight(btn.getId(), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
                    set.connect(btn.getId(), ConstraintSet.RIGHT, set.PARENT_ID, ConstraintSet.RIGHT);
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
                ArrayList<Step> steps1 = recipe.getSteps();
                for (Step step : steps1
                     ) {
                    ConstraintLayout ctrlayout = new ConstraintLayout(getApplicationContext());
                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    int marg = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
                    layoutParams.setMargins(0, marg, marg, marg / 2);
                    ctrlayout.setLayoutParams(layoutParams);
                    EditText newStep = new EditText(getApplicationContext());
                    newStep.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
                    newStep.setWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics()));
                    newStep.setId(View.generateViewId());
                    newStep.setBackgroundResource(R.drawable.edit_text_border);
                    newStep.setText(step.getText());
                    newStep.setSingleLine(false);
                    //newIngre.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    newStep.setMaxLines(5);
                    ImageButton btn = new ImageButton(getApplicationContext());
                    btn.setBackgroundColor(Color.TRANSPARENT);
                    btn.setImageDrawable(getDrawable(R.drawable.ic_baseline_close_24));
                    btn.setId(View.generateViewId());
                    ImageView img = new ImageView(getApplicationContext());
                    img.setImageDrawable(getDrawable(R.drawable.ic_food));
                    img.setId(View.generateViewId());
                    if(step.getImage()!=null ){
                        Picasso.with(EditRecipe.this).load(step.getImage()).into(img);
                    }

                    BooleanIMapping.put(img.getId(),false);
                    img.setScaleType(ImageView.ScaleType.FIT_XY);
                    stepTextId.add(newStep.getId());
                    imgStepId.add(img.getId());
                    ConstraintSet set = new ConstraintSet();
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String[] PERMISSIONS = {
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                            };
                            // Check user permission
                            if (PermissionHelper.hasPermissions(EditRecipe.this, PERMISSIONS)) {
                                showImagePicker(img.getId());
                            } else {
                                ActivityCompat.requestPermissions(EditRecipe.this, PERMISSIONS, PICKER_REQUEST_CODE);
                            }
                        }
                    });
                    ctrlayout.addView(newStep);
                    ctrlayout.addView(btn);
                    ctrlayout.addView(img);
                    set.clone(ctrlayout);
                    set.constrainHeight(img.getId(), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
                    set.constrainWidth(img.getId(), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics()));
                    set.connect(img.getId(), ConstraintSet.TOP, newStep.getId(), ConstraintSet.BOTTOM, 15);
                    set.connect(img.getId(), ConstraintSet.LEFT, set.PARENT_ID, ConstraintSet.LEFT, 15);

                    set.constrainWidth(btn.getId(), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics()));
                    set.constrainHeight(btn.getId(), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
                    set.connect(btn.getId(), ConstraintSet.RIGHT, set.PARENT_ID, ConstraintSet.RIGHT);
                    set.applyTo(ctrlayout);

                    addStep.addView(ctrlayout);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view1) {
                            stepTextId.remove(Integer.valueOf(newStep.getId()));
                            imgStepId.remove(Integer.valueOf(img.getId()));
                            addStep.removeView(ctrlayout);
                        }
                    });
                }

            }
        }, recipeId);

            }
    private void showImagePicker(int id) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                id);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant

            // Get the url of the image from data
            Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                // update the preview image in the layout
                ImageView imageView = findViewById(requestCode);
                URIMapping.put(requestCode,selectedImageUri);
                BooleanIMapping.put(requestCode,true);
                imageView.setImageURI(selectedImageUri);


            }
        }

    }




    private class UploadRecipe extends AsyncTask<Void, Void, Void> {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {


            return  null;
        }
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            startActivity(new Intent(EditRecipe.this, ProfileActivity.class));
        }
    }
}


