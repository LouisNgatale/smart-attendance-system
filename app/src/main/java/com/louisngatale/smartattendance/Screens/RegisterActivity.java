package com.louisngatale.smartattendance.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.louisngatale.smartattendance.R;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "Register";
    EditText password,confirmPassword;
    Button signUp;
    String fullName, id, course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signUp = findViewById(R.id.signUp);
        password = findViewById(R.id.passwordValue);
        confirmPassword = findViewById(R.id.confirmPasswordValue);

        Intent intent = getIntent();
        if (null != intent){
            fullName = intent.getStringExtra("Full Name");
            id = intent.getStringExtra("Id");
            course = intent.getStringExtra("Course");

        }

        signUp.setOnClickListener(v ->{
            if (password.getText().toString().equals(confirmPassword.getText().toString())){
            }else {
                confirmPassword.setError("Password does not match");
            }
        });
    }
}