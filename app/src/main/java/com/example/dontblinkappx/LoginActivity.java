package com.example.dontblinkappx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        usernameEditText = findViewById(R.id.username_etxt);
        passwordEditText = findViewById(R.id.password_etxt);
        signInButton = findViewById(R.id.btn_sign_in);
        signUpButton = findViewById(R.id.btn_sign_up);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignIn();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement sign up logic or redirect to sign up activity
                Toast.makeText(LoginActivity.this, "Sign up clicked", Toast.LENGTH_SHORT).show();
            }
        });

        getWindow().setNavigationBarColor(
                getResources().getColor(R.color.blue, getTheme())
        );
    }

    /*private void handleSignIn() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.equals("Zain") && password.equals("admin")) {
            // Successful login
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        } else {
            // Login failed
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }*/
    /*private void handleSignIn() {
        final String username = usernameEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Username already exists
                    Toast.makeText(LoginActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // Username doesn't exist, save it to the database
                    DatabaseReference userRef = usersRef.child(username);
                    userRef.child("password").setValue(password);
                    // Successful registration
                    Toast.makeText(LoginActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                    // Proceed with the login
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(LoginActivity.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }*/
    private void handleSignIn() {
        final String username = usernameEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Username exists, check password
                    String savedPassword = dataSnapshot.child("password").getValue(String.class);
                    if (savedPassword.equals(password)) {
                        // Password matches, successful login
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();
                    } else {
                        // Password does not match
                        Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Username doesn't exist, register it
                    DatabaseReference userRef = usersRef.child(username);
                    userRef.child("password").setValue(password);
                    // Successful registration
                    Toast.makeText(LoginActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                    // Proceed with the login
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(LoginActivity.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
