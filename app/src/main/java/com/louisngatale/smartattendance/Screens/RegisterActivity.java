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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.louisngatale.smartattendance.R;
import com.louisngatale.smartattendance.Screens.ProtectedRoutes.Student.HomeActivity;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "Register";
    EditText password,confirmPassword,email;
    Button signUp;
    String fullName, id, course;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    CircularProgressIndicator progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signUp = findViewById(R.id.login);
        password = findViewById(R.id.passwordValue);
        confirmPassword = findViewById(R.id.confirmPasswordValue);
        email = findViewById(R.id.emailValue);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressIndicator = findViewById(R.id.registerLoading);

        Intent intent = getIntent();
        if (null != intent){
            fullName = intent.getStringExtra("Full Name");
            id = intent.getStringExtra("Id");
            course = intent.getStringExtra("Course");
        }

        signUp.setOnClickListener(v ->{
            String pwd = password.getText().toString();
            String pwd2 = confirmPassword.getText().toString();
            if (pwd.equals(pwd2)){
                String email = this.email.getText().toString();
                if (!email.isEmpty() && !pwd.isEmpty()){
                    progressIndicator.setVisibility(View.VISIBLE);
                    createUser(pwd, email);
                }
            }else {
                confirmPassword.setError("Password does not match");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (null != currentUser){
            Intent loginIntent = new Intent(RegisterActivity.this, HomeActivity.class);
            startActivity(loginIntent);
        }
    }

    private void createUser(String pwd, String email) {
        mAuth.createUserWithEmailAndPassword(email,pwd).addOnSuccessListener(authResult -> {
            String uid = authResult.getUser().getUid();

            createNewUser(authResult, uid);
            AddToDatabase(authResult);

        }).addOnFailureListener(failureResult -> {
            Log.d(TAG, "onCreate: " + failureResult.getMessage());
        });
    }

    private void createNewUser(AuthResult authResult, String uid) {
        Map<String, Object> student = new HashMap<>();
        student.put("UID", authResult.getUser().getUid());
        student.put("schoolId", id);
        student.put("fullName", fullName);
        student.put("course", course);
        db.collection("users")
                .document(uid)
                .set(student)
                .addOnSuccessListener(success -> {
                }).addOnFailureListener(failure -> {
                    progressIndicator.setVisibility(View.GONE);
            Toast.makeText(this, "There was an error creating user", Toast.LENGTH_SHORT).show();
        });
    }

    private void AddToDatabase(AuthResult authResult) {
        Map<String, Object> student = new HashMap<>();
        String uid = authResult.getUser().getUid();
        student.put("UID", uid);
        db.collection("classes/"+course+"/Students")
            .document(uid)
            .set(student)
            .addOnSuccessListener(success -> {
                Intent homeIntent = new Intent(RegisterActivity.this, HomeActivity.class);
                progressIndicator.setVisibility(View.GONE);
                startActivity(homeIntent);
            }).addOnFailureListener(failure -> {
                progressIndicator.setVisibility(View.GONE);
        Toast.makeText(this, "There was an error creating user", Toast.LENGTH_SHORT).show();
        });
    }
}