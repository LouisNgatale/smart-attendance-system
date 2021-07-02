package com.louisngatale.smartattendance.Screens.ProtectedRoutes.Teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.louisngatale.smartattendance.Adapter.StudentsAdapter;
import com.louisngatale.smartattendance.Data.Student;
import com.louisngatale.smartattendance.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ViewStudents extends AppCompatActivity {
    private static final String TAG = "StudentsView";
    private String course,id;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RecyclerView students_rec_view;
    private StudentsAdapter adapter;
    private ArrayList<Student> items;
    private HashMap<String,Integer> all_students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);
        students_rec_view = findViewById(R.id.students_rec_view);
        all_students = new HashMap<>();
        db = FirebaseFirestore.getInstance();
        items = new ArrayList<Student>();

        Intent intent = getIntent();

        if (null != intent){
            id = intent.getStringExtra("Id");
            course = intent.getStringExtra("Course");
        }

        getTotalAttended();
    }

    // [START] Get total sessions attended by each student
    private void getTotalAttended() {
        // Get Students

        // Get Attendance

        db.collection("classes/"+course+"/Subjects/"+id+"/Attendance")
            .get()
            .addOnCompleteListener(task -> {
                QuerySnapshot snapshot = task.getResult();

                // CHECK IF THERE ARE VALUES
                if (snapshot != null){
                    // [START #1] Search each active session
                    snapshot.getDocuments().forEach(item -> {

                        // [START #2] Enter attendees collection for current session
                        item.getReference().collection("attendees").get().addOnCompleteListener(task1 -> {
                            // Check if task was successful in getting all attendees
                            if (task1.isSuccessful()){
                                // Get all students who are in the attendance list
                                List<DocumentSnapshot> students = Objects.requireNonNull(task1.getResult()).getDocuments();

                                // [START #3] For each student update attendance count in the hashmap
                                students.forEach(student -> {
                                    String student_id = student.getId();

                                    // Increase attendance count of student in the hashmap
                                    if (all_students.containsKey(student_id))
                                        all_students.put(student_id, all_students.get(student_id) + 1);
                                    else
                                        all_students.put(student_id,1);
                                });
                                // [END #3] For each student update attendance count in the hashmap
                            }
                        }).addOnSuccessListener(queryDocumentSnapshots -> {
                            Log.d(TAG, "Start here first");
                            // Using for-each loop
                            all_students.forEach((k,v) -> {
                                items.add(new Student(k,v));
                            });

                            initiateViews();
                        });
                        // [END #2] Enter attendees collection for current session
                    });
                    // [END #1] Search each active session
                }else {
                    Log.d(TAG, "onComplete: No attendance yet");
                }
            });
    }

    private void initiateViews() {
        adapter = new StudentsAdapter(this, items);

        students_rec_view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        students_rec_view.setNestedScrollingEnabled(false);
        students_rec_view.setAdapter(adapter);

    }
    // [END] Get total sessions attended by each student
}