package com.example.recipeproject.UI.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.recipeproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AbstractActivity {

    EditText editTextEmail;
    EditText editTextPassword;
    Button btnLogin;
    TextView textViewForgetPassword;
    TextView textViewRegister;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get reference of all components
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        textViewForgetPassword = findViewById(R.id.forgetPasswordText);
        textViewRegister = findViewById(R.id.textRegister);
        progressBar = findViewById(R.id.progressBarLogin);

        /*if(isLoggedIn){
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
            finish();
        }*/

        progressBar.setVisibility(View.INVISIBLE);

        // Open register when clicking on text view
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Logic for login using firebase
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userEmail = editTextEmail.getText().toString();
                String userPassword = editTextPassword.getText().toString();

                if (userEmail.isEmpty() || userPassword.isEmpty()){
                    Toast.makeText(view.getContext(), "Username and password can't be empty!", Toast.LENGTH_LONG).show();

                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        firebaseUser = firebaseAuth.getCurrentUser();
                                        progressBar.setVisibility(View.VISIBLE);
                                        Intent intent = new Intent(view.getContext(), HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(view.getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
            }
        });

        //TODO: add intent to change password
    }

}
