package com.louisngatale.smartattendance.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.louisngatale.smartattendance.R;
import com.louisngatale.smartattendance.Screens.ProtectedRoutes.Student.HomeActivity;
import com.louisngatale.smartattendance.Screens.ProtectedRoutes.Teacher.TeacherLogin;

import java.util.Objects;

public class WelcomeActivity extends AppCompatActivity {
    Button register,signIn;
    FirebaseAuth mAuth;
    TextView lecture_sign_in;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        register = findViewById(R.id.register);
        signIn = findViewById(R.id.sign_in);
        lecture_sign_in = findViewById(R.id.lecture_sign_in);

        register.setOnClickListener((View v) -> {
            checkCameraPermissions();

        });

        lecture_sign_in.setOnClickListener(v -> {
            Intent signInIntent = new Intent(WelcomeActivity.this, TeacherLogin.class);
            startActivity(signInIntent);
        });

        signIn.setOnClickListener(v -> {
            Intent signInIntent = new Intent(WelcomeActivity.this,Login.class);
            startActivity(signInIntent);
        });
    }

    private void checkCameraPermissions() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }else {
            openRegistration();
        }
    }

    private void openRegistration() {
        Intent signInIntent = new Intent(WelcomeActivity.this,StudentSignInActivity.class);
        startActivity(signInIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
                openRegistration();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
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