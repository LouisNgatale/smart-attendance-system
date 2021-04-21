package com.louisngatale.smartattendance.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.louisngatale.smartattendance.R;
import com.louisngatale.smartattendance.Screens.ProtectedRoutes.Student.HomeActivity;

public class WelcomeActivity extends AppCompatActivity {
    Button register,signIn;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        register = findViewById(R.id.register);
        signIn = findViewById(R.id.sign_in);

        register.setOnClickListener((View v) -> {
            Intent signInIntent = new Intent(WelcomeActivity.this,StudentSignInActivity.class);
            startActivity(signInIntent);
        });

        signIn.setOnClickListener(v -> {
            Intent signInIntent = new Intent(WelcomeActivity.this,Login.class);
            startActivity(signInIntent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

        if (null != mAuth.getCurrentUser()){
            Intent homeIntent = new Intent(WelcomeActivity.this, HomeActivity.class);
            startActivity(homeIntent);
        }
    }
}