package com.louisngatale.smartattendance.Screens;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.louisngatale.smartattendance.R;
import com.louisngatale.smartattendance.Screens.ProtectedRoutes.Student.HomeActivity;

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    EditText password,email;
    Button signUp;
    FirebaseAuth mAuth;
    CircularProgressIndicator progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        password = findViewById(R.id.passwordValueLogin);
        email = findViewById(R.id.emailValueLogin);
        signUp = findViewById(R.id.login);
        progressIndicator = findViewById(R.id.loginLoading);


        signUp.setOnClickListener(v -> {
            progressIndicator.setVisibility(View.VISIBLE);
            String email = this.email.getText().toString();
            String pwd = password.getText().toString();
            mAuth.signInWithEmailAndPassword(email,pwd).addOnSuccessListener(authResult -> {
                Intent intent = new Intent(Login.this, HomeActivity.class);
                progressIndicator.setVisibility(View.GONE);
                startActivity(intent);
            }).addOnFailureListener(e -> Toast.makeText(Login.this, "Couldn't log you in", Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (null != currentUser){
            Intent loginIntent = new Intent(Login.this, HomeActivity.class);
            startActivity(loginIntent);
        }
    }
}