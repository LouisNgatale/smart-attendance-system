package com.louisngatale.smartattendance.Screens.ProtectedRoutes.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.louisngatale.smartattendance.R;
import com.louisngatale.smartattendance.Screens.WelcomeActivity;

import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "Home";
    FirebaseAuth mAuth;
    ImageView logout;
    TextView username;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        initializeViews();

//        If user is not authenticated send to welcome activity
        if (currentUser == null){
            Intent loginIntent = new Intent(HomeActivity.this, WelcomeActivity.class);
            startActivity(loginIntent);
        }

    }

    private void initializeViews() {
        username = findViewById(R.id.fullName);
        logout = findViewById(R.id.logout);

        setUserName();
    }

    private void setUserName() {
        String uid = mAuth.getCurrentUser().getUid();
        DocumentReference docRef = db.collection("users").document(uid);

        docRef.get().addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               DocumentSnapshot document = task.getResult();
               if (document.exists()){
                   String fullName = document.getString("fullName");
                   username.setText(fullName);
               }
           }else {
               Log.d(TAG, "setUserName: FAILED GETTING DOCUMENT");
           }
        });
    }
}