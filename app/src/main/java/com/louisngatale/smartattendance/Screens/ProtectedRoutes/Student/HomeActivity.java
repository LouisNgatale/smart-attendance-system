package com.louisngatale.smartattendance.Screens.ProtectedRoutes.Student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.louisngatale.smartattendance.Adapter.CoursesAdapter;
import com.louisngatale.smartattendance.Data.Courses;
import com.louisngatale.smartattendance.R;
import com.louisngatale.smartattendance.Screens.WelcomeActivity;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "Home";
    FirebaseAuth mAuth;
    ImageView logout;
    TextView username;
    FirebaseFirestore db;
    String course;
    RecyclerView subjectsRecView,recyclerView;
    CoursesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        If user is not authenticated send to welcome activity
        if (currentUser == null){
            Intent loginIntent = new Intent(HomeActivity.this, WelcomeActivity.class);
            startActivity(loginIntent);
        }

        initializeViews();
        retrieveFromFirestore();

    }

    private void initializeViews() {
        username = findViewById(R.id.fullName);
        logout = findViewById(R.id.logout);
        subjectsRecView =(RecyclerView) findViewById(R.id.subjectsRecView);
        logout.setOnClickListener(v ->{
            mAuth.signOut();
            Intent loginIntent = new Intent(HomeActivity.this, WelcomeActivity.class);
            startActivity(loginIntent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Intent loginIntent = new Intent(HomeActivity.this, WelcomeActivity.class);
            startActivity(loginIntent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void retrieveFromFirestore() {
        String uid = mAuth.getCurrentUser().getUid();
        DocumentReference docRef = db.collection("users").document(uid);

        docRef.get().addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               DocumentSnapshot document = task.getResult();
               if (document.exists()){
                   String fullName = document.getString("fullName");
                   username.setText(fullName);
                   course = document.getString("course");
                   retrieveCourses();
               }
           }else {
               Log.d(TAG, "setUserName: FAILED GETTING DOCUMENT");
           }
        });
    }

    private void retrieveCourses() {
        //        Create query

        Query query =
                db.collection("/classes/"+course+"/Subjects");

        //        Configure the adapter options
        FirestoreRecyclerOptions<Courses> options =
                new FirestoreRecyclerOptions.Builder<Courses>()
                        .setQuery(query,Courses.class)
                        .build();

        adapter = new CoursesAdapter(options, this);

        subjectsRecView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        subjectsRecView.setAdapter(adapter);
        subjectsRecView.setNestedScrollingEnabled(false);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Intent viewItem = new Intent(this, SubjectView.class);
            viewItem.putExtra("Id", id);
            startActivity(viewItem);
        });

        adapter.startListening();
    }
}