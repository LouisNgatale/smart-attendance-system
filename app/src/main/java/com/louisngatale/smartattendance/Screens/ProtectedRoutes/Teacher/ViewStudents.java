package com.louisngatale.smartattendance.Screens.ProtectedRoutes.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.louisngatale.smartattendance.Adapter.CoursesAdapter;
import com.louisngatale.smartattendance.Adapter.StudentsAdapter;
import com.louisngatale.smartattendance.Data.Courses;
import com.louisngatale.smartattendance.Data.Students;
import com.louisngatale.smartattendance.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewStudents extends AppCompatActivity {
    private static final String TAG = "StudentsView";
    private String course,id;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RecyclerView students_rec_view;
    private ArrayList<String> student_ids  = new ArrayList<>();
    private Query query;
    private Map<String, String> students;
    private StudentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);
        students_rec_view = findViewById(R.id.students_rec_view);

        Intent intent = getIntent();

        if (null != intent){
            id = intent.getStringExtra("Id");
            course = intent.getStringExtra("Course");
        }

        // Getting total students
        db.collection("classes/"+course+"/Students")
            .get()
            .addOnCompleteListener(task -> {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null){
                    snapshot.getDocuments().forEach(item -> {
                        student_ids.add(item.getId());
                    });

                    // Create query
                    query = db.collection("users").whereIn("UID",student_ids);
                }
            });

        query.get().addOnCompleteListener(task -> Log.d(TAG, "onComplete: " + task.getResult().getDocuments().size()));

        // Configure the adapter options
        FirestoreRecyclerOptions<Students> options =
                new FirestoreRecyclerOptions.Builder<Students>()
                        .setQuery(query,Students.class)
                        .build();

        adapter = new StudentsAdapter(options, this);

        students_rec_view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        students_rec_view.setAdapter(adapter);
        students_rec_view.setNestedScrollingEnabled(false);
    }
}