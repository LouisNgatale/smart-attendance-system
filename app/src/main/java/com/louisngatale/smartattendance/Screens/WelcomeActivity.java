package com.louisngatale.smartattendance.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.louisngatale.smartattendance.R;

public class WelcomeActivity extends AppCompatActivity {

    Button signIn,Register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        signIn = findViewById(R.id.sign_in);

        signIn.setOnClickListener((View v) -> {
            Intent signInIntent = new Intent(WelcomeActivity.this,StudentSignInActivity.class);
            startActivity(signInIntent);
        });
    }
}