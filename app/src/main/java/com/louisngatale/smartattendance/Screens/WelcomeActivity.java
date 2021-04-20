package com.louisngatale.smartattendance.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.louisngatale.smartattendance.R;

public class WelcomeActivity extends AppCompatActivity {

    Button register,signIn;
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
}