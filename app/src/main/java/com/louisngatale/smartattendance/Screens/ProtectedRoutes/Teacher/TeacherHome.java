package com.louisngatale.smartattendance.Screens.ProtectedRoutes.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.louisngatale.smartattendance.Screens.ProtectedRoutes.Student.CourseView;
import com.louisngatale.smartattendance.Screens.ProtectedRoutes.Student.HomeActivity;
import com.louisngatale.smartattendance.Screens.WelcomeActivity;

public class TeacherHome extends AppCompatActivity {
    String TAG = "Home";
    FirebaseAuth mAuth;
    ImageView logout;
    TextView username;
    FirebaseFirestore db;
    String course;
    String fullName;
    RecyclerView subjectsRecView,recyclerView;
    CoursesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
        Intent intent = getIntent();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null){
            Intent loginIntent = new Intent(TeacherHome.this, WelcomeActivity.class);
            startActivity(loginIntent);
        }

        if (intent != null){
            Log.d(TAG, "onCreate: Created");
            fullName = intent.getStringExtra("fullName");
        }

        initializeViews();
        retrieveFromFirestore();
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("Started");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Intent loginIntent = new Intent(TeacherHome.this, WelcomeActivity.class);
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

                    Log.d(TAG, "retrieveFromFirestore: " + course);
                    retrieveCourses();
                }
            }
        });
    }

    private void retrieveCourses() {
        //        Create query

        String uid = mAuth.getCurrentUser().getUid();

        Query query =
                db.collection("/users/"+uid+"/Subjects");


        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d(TAG, "onComplete: " + task.getResult().getDocuments().size());
            }
        });

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

            db.collection("users/"+uid+"/Subjects")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            QuerySnapshot snapshot = task.getResult();
                            Log.d(TAG, "onComplete: task");
                            if (snapshot != null){
                                if (!snapshot.isEmpty()) {
                                    snapshot.getDocuments().forEach(item -> {
                                        if (item.getId().equals(id)){
                                            String courseId = String.valueOf(item.get("course"));
                                            Intent viewItem = new Intent(TeacherHome.this, TeacherCourseView.class);
                                            viewItem.putExtra("Id", id.trim());
                                            viewItem.putExtra("Course", courseId);
                                            startActivity(viewItem);
                                        }
                                    });
                                }
                            }
                        }
                    });


        });

        adapter.startListening();
    }

    private void initializeViews() {
        username = findViewById(R.id.fullNameTeacher);
        logout = findViewById(R.id.logout);
        subjectsRecView =findViewById(R.id.subjectsRecViewTeacher);
        logout.setOnClickListener(v ->{
            mAuth.signOut();
            Intent loginIntent = new Intent(TeacherHome.this, WelcomeActivity.class);
            startActivity(loginIntent);
        });
    }

}