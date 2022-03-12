package com.example.recipeproject.UI.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.recipeproject.DataAccess.DataAccess;
import com.example.recipeproject.InterfaceGetData.getUserCallback;
import com.example.recipeproject.R;
import com.example.recipeproject.model.User;
import com.example.recipeproject.utils.FirestoreHelper;
import com.example.recipeproject.utils.PermissionHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateUserProfileActivity extends AbstractActivity {

    private static final int PICKER_REQUEST_CODE = 101;
    private CircleImageView userAvatar;
    private ImageButton btnAddPhoto;
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextDescription;

    private Toolbar toolbar;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);

        // Initialize toolbar on top of screen
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Initialize other components
        userAvatar = findViewById(R.id.imageView5);
        editTextUsername = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextDescription = findViewById(R.id.editTextDescription);

        // set on click events for button inside toolbar
        Button saveProfileBtn = findViewById(R.id.saveProfileButton);
        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserProfile();
            }
        });

        ImageButton backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Let user choose image in local storage
        btnAddPhoto = findViewById(R.id.btnAddPhoto);
        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] PERMISSIONS = {
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                // Check user permission
                if(PermissionHelper.hasPermissions(UpdateUserProfileActivity.this, PERMISSIONS)){
                    showImagePicker();
                }else{
                    ActivityCompat.requestPermissions(UpdateUserProfileActivity.this, PERMISSIONS, PICKER_REQUEST_CODE);
                }
            }
        });

        DataAccess.getUserById(new getUserCallback() {
            @Override
            public void onResponse(User user) {
                editTextEmail.setText(user.getEmail());
                editTextUsername.setText(user.getName());
                Picasso.with(UpdateUserProfileActivity.this)
                        .load(user.getImage_link())
                        .error(R.drawable.placeholder_avatar_foreground)
                        .resize(84,84)
                        .centerCrop()
                        .into(userAvatar);
                //userAvatar.setImageURI(Uri.parse(user.getImage_link()));
            }
        }, firebaseUser.getUid());
    }

    private void showImagePicker() {
        Matisse.from(UpdateUserProfileActivity.this)
                .choose(MimeType.ofImage())
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider"))
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new PicassoEngine())
                .forResult(PICKER_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == PICKER_REQUEST_CODE && data != null){
                imageUri = Matisse.obtainResult(data).get(0);
                userAvatar.setImageURI(imageUri);
            }
        }
    }

    private void updateUserProfile() {
        //TODO: add actual logic to upload photo storage and update user record
        String userId = firebaseUser.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user").child(userId);

        /*reference.child("image_link").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    String iUrl = String.valueOf(task.getResult().getValue());
                    if (iUrl != null){
                        Log.d("firebase", iUrl);
                        FirestoreHelper.deleteFile(String.valueOf(task.getResult().getValue()));
                    }
                } else {
                    Log.e("firebase", "Error getting data", task.getException());
                }
            }
        });*/

        reference.child("image_link").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue()!=null){
                    FirestoreHelper.deleteFile(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String downloadUrl = FirestoreHelper.uploadToStorage(UpdateUserProfileActivity.this, imageUri);

        Log.d("Firebase storage","Get download url: "+ downloadUrl);


        reference.child("email").setValue(editTextEmail.getText().toString());
        reference.child("name").setValue(editTextUsername.getText().toString());
        reference.child("description").setValue(editTextDescription.getText().toString());
        reference.child("image_link").setValue(downloadUrl);

        startActivity(new Intent(UpdateUserProfileActivity.this, ProfileActivity.class));
    }



    /**
     * Callback that handles the status of the permissions request.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PICKER_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                            UpdateUserProfileActivity.this,
                            "Permission granted! Please click on pick a file once again.",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Toast.makeText(
                            UpdateUserProfileActivity.this,
                            "Permission denied to read your External storage",
                            Toast.LENGTH_SHORT
                    ).show();
                }
                return;
            }
        }
    }
}