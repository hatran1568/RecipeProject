package com.example.recipeproject.UI.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.recipeproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordActivity extends AbstractActivity {

    EditText editTextEmail, editTextOldPwd, editTextNewPwd, editTextConfirmNewPwd;
    Button btnUpdatePwd;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextOldPwd = findViewById(R.id.editTextOldPassword);
        editTextNewPwd = findViewById(R.id.editTextNewPassword);
        editTextConfirmNewPwd = findViewById(R.id.editTextNewPassword2);
        btnUpdatePwd = findViewById(R.id.btnChangePwd);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        btnUpdatePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String oldPassword = editTextOldPwd.getText().toString();
                String newPassword = editTextNewPwd.getText().toString();
                String confirmPassword = editTextConfirmNewPwd.getText().toString();

                if (email.isEmpty()){
                    editTextEmail.setError("Email is required");
                    return;
                }

                if (oldPassword.isEmpty()){
                    editTextOldPwd.setError("Password is required");
                    return;
                }

                if (newPassword.isEmpty()){
                    editTextNewPwd.setError("New password is required");
                    return;
                }

                if (confirmPassword.isEmpty()){
                    editTextConfirmNewPwd.setError("New password is required");
                    return;
                }

                if (!newPassword.equals(confirmPassword)){
                    Toast.makeText(ChangePasswordActivity.this, "Please recheck new password", Toast.LENGTH_LONG).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                AuthCredential credential = EmailAuthProvider.getCredential(email,oldPassword);
                firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.INVISIBLE);

                        if (task.isSuccessful()){
                            firebaseUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        FirebaseDatabase.getInstance().getReference().child("user").child(firebaseUser.getUid()).child("password").setValue(newPassword);
                                        Toast.makeText(ChangePasswordActivity.this,
                                                "Password updated successfully!", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(ChangePasswordActivity.this,
                                                "Something went wrong. Please try again later", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(ChangePasswordActivity.this,
                                    "Authentication Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                return;
            }
        });
    }


}