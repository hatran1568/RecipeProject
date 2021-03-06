package com.example.recipeproject.UI.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.recipeproject.DataAccess.DataAccess;
import com.example.recipeproject.InterfaceGetData.FirebaseStorageCallback;
import com.example.recipeproject.InterfaceGetData.getUserCallback;
import com.example.recipeproject.R;
import com.example.recipeproject.model.User;
import com.example.recipeproject.utils.FirestoreHelper;
import com.example.recipeproject.utils.PermissionHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateUserProfileActivity extends AbstractActivity {

    private static final int PICKER_REQUEST_CODE = 101;
    private CircleImageView userAvatar;
    private ImageButton btnAddPhoto;
    private EditText editTextUsername;
    private EditText editTextEmail;
    private ProgressBar progressBar;

    private Toolbar toolbar;
    private Uri imageUri;

    private boolean isImageChanged = false;

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
        editTextEmail.setFocusable(false);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        //editTextDescription = findViewById(R.id.RecipeDescription);

        // set on click events for button inside toolbar
        Button saveProfileBtn = findViewById(R.id.AddRecipeButton);
        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
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

        // set all fields with data of current user
        DataAccess.getUserById(new getUserCallback() {
            @Override
            public void onResponse(User user) {
                editTextEmail.setText(user.getEmail());
                editTextUsername.setText(user.getName());
                Picasso.with(UpdateUserProfileActivity.this)
                        .load(user.getImage_link())
                        .error(R.drawable.placeholder_avatar_foreground)
                        .resize(200,200)
                        .centerCrop()
                        .into(userAvatar);
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
                isImageChanged = true;
            }
        }
    }

    private void updateUserProfile() {
        //TODO: add actual logic to upload photo storage and update user record
        String userId = firebaseUser.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user").child(userId);

        reference.child("email").setValue(editTextEmail.getText().toString());
        reference.child("name").setValue(editTextUsername.getText().toString());
        //reference.child("description").setValue(editTextDescription.getText().toString());

        if (isImageChanged){
            reference.child("image_link").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful() && task.getResult().exists()){
                        String iUrl = String.valueOf(task.getResult().getValue());
                        if (iUrl != null && !iUrl.isEmpty()){
                            Log.d("firebase", iUrl);
                            FirestoreHelper.deleteFile(String.valueOf(task.getResult().getValue()));
                        }
                    } else {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                }
            });
            /*Thread thread = new Thread(){
                @Override
                public void run() {
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user").child(userId);

                    FirestoreHelper.uploadToStorage(new FirebaseStorageCallback() {
                        @Override
                        public void onResponse(String url) {
                            reference.child("image_link").setValue(url);
                            Log.d("Firebase", "Get value of download url: "+ url);
                        }
                    },UpdateUserProfileActivity.this, imageUri);
                }
            };
            //thread.start();
            Thread thread1 = new Thread(){
                @Override
                public void run() {
                    startActivity(new Intent(UpdateUserProfileActivity.this, ProfileActivity.class));
                }
            };
            //thread1.run();
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(thread);
            executor.execute(thread1);*/
            UploadPhoto(this, imageUri);
        } else {
            startActivity(new Intent(UpdateUserProfileActivity.this, ProfileActivity.class));
        }
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

    private void UploadPhoto(Context context, Uri fileUri) {

        FirebaseStorage storageRef = FirebaseStorage.getInstance();
        StorageReference storageReference = storageRef.getReference()
                .child(System.currentTimeMillis() + "." + getFileExtension(context, fileUri));

        storageReference.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user").child(userId);
                        reference.child("image_link").setValue(uri.toString());
                        Log.d("Firebase storage", "Uploaded " + fileUri + " to " + uri.toString());
                        progressBar.setVisibility(View.INVISIBLE);
                        startActivity(new Intent(UpdateUserProfileActivity.this, ProfileActivity.class));
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Upload failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private static String getFileExtension(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}

