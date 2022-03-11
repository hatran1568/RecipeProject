package com.example.recipeproject.UI.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.recipeproject.DataAccess.DataAccess;
import com.example.recipeproject.InterfaceGetData.getUserCallback;
import com.example.recipeproject.R;
import com.example.recipeproject.model.User;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateUserProfileActivity extends AbstractActivity {

    private static final int OPEN_IMAGE_REQUEST_CODE = 100;
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
                if(hasPermissions(UpdateUserProfileActivity.this, PERMISSIONS)){
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
                userAvatar.setImageURI(Uri.parse(user.getImage_link()));
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
                String res = Matisse.obtainResult(data).get(0).toString();

                editTextDescription.setText(res);
                userAvatar.setImageURI(imageUri);
            }
        }
    }

    private void updateUserProfile() {
        //TODO: add actual logic to upload photo storage and update user record

        startActivity(new Intent(UpdateUserProfileActivity.this, ProfileActivity.class));
    }

    /**
     * Helper method that verifies whether the permissions of a given array are granted or not.
     *
     * @param context
     * @param permissions
     * @return {Boolean}
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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