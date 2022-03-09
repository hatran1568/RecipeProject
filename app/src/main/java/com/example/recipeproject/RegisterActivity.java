package com.example.recipeproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeproject.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

public class RegisterActivity extends AbstractActivity {

    TextView editTextEmail;
    TextView editTextPassword;
    TextView editTextName;
    TextView editTextPasswordReenter;

    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextPasswordReenter = findViewById(R.id.editTextPasswordReenter);
        btnRegister = findViewById(R.id.btnRegister);

        // Register logic
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String rePassword = editTextPasswordReenter.getText().toString().trim();
                String name = editTextName.getText().toString().trim();

                // Validation
                if (name.isEmpty()){
                    editTextName.setError("Name is required");
                    editTextName.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editTextEmail.setError("Email is invalid");
                    editTextEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()){
                    editTextPassword.setError("Password is required");
                    editTextPassword.requestFocus();
                    return;
                }

                if (!password.equals(rePassword)){
                    editTextPasswordReenter.setError("Password not matches");
                    editTextPasswordReenter.requestFocus();
                    return;
                }

                // Actual register logic
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user = new User();
                            user.setEmail(email);
                            user.setName(name);
                            user.setPassword(password);

                            // Add user to database with node is uid
                            FirebaseDatabase.getInstance().getReference("user")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(view.getContext(), "User has been registered successfully!", Toast.LENGTH_LONG).show();

                                        // Redirect to login screen
                                        startActivity(new Intent(view.getContext(), LoginActivity.class));
                                    } else {
                                        Toast.makeText(view.getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(view.getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
