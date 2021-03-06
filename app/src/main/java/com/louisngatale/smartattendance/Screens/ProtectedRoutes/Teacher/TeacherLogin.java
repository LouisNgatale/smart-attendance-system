package com.louisngatale.smartattendance.Screens.ProtectedRoutes.Teacher;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.louisngatale.smartattendance.R;
import com.louisngatale.smartattendance.Screens.Login;
import com.louisngatale.smartattendance.Screens.ProtectedRoutes.Student.HomeActivity;

public class TeacherLogin extends AppCompatActivity {
    private static final String TAG = "Home";
    EditText password,email;
    Button signUp;
    FirebaseAuth mAuth;
    CircularProgressIndicator progressIndicator;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);
        password = findViewById(R.id.teacherPasswordValue);
        email = findViewById(R.id.teacherEmailValue);
        signUp = findViewById(R.id.login);
        progressIndicator = findViewById(R.id.loginLoading);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(v -> {
            progressIndicator.setVisibility(View.VISIBLE);
            String email = this.email.getText().toString();
            String pwd = password.getText().toString();

            // TODO: Handle on wrong password stop loading the progress bar
            mAuth.signInWithEmailAndPassword(email,pwd).addOnSuccessListener(authResult -> {
                String uid = authResult.getUser().getUid();

                DocumentReference docRef = db.collection("users").document(uid);

                docRef.get().addOnCompleteListener(task -> {

                    Toast.makeText(this, "User signed In", Toast.LENGTH_SHORT).show();
                    if (task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();

                        if (document.exists()){
                            String fullName = document.getString("fullName");
                            String role = document.getString("role");
                            Log.d(TAG, "onCreate: " + role);

                            if (role.equals("Teacher")){
                                Intent intent = new Intent(TeacherLogin.this, TeacherHome.class);
                                progressIndicator.setVisibility(View.GONE);
                                intent.putExtra("fullName", fullName);
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(TeacherLogin.this, HomeActivity.class);
                                progressIndicator.setVisibility(View.GONE);
                                startActivity(intent);
                            }
                        }else {
                            Toast.makeText(this, "Records not found!", Toast.LENGTH_SHORT).show();
                            progressIndicator.setVisibility(View.GONE);
                        }
                    }else {
                        Toast.makeText(this, "Failed retrieving records!", Toast.LENGTH_SHORT).show();
                        progressIndicator.setVisibility(View.GONE);
                    }
                });

            }).addOnFailureListener(e -> {
                progressIndicator.setVisibility(View.GONE);
                Toast.makeText(TeacherLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (null != currentUser){
            Intent loginIntent = new Intent(TeacherLogin.this, HomeActivity.class);
            startActivity(loginIntent);
        }
    }
}