package com.louisngatale.smartattendance.Screens.ProtectedRoutes.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.louisngatale.smartattendance.R;
import com.louisngatale.smartattendance.Screens.ProtectedRoutes.Student.CourseView;
import com.louisngatale.smartattendance.Screens.ProtectedRoutes.Student.ScanAttendance;

public class TeacherCourseView extends AppCompatActivity {
    private static final String TAG = "Home";
    String id;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    String course;
    Button scan;
    TextView totalStudents, currentSessions, totalSessions;
    int current = 0,students = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_course_view);
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        totalStudents = findViewById(R.id.totalStudents);
        currentSessions = findViewById(R.id.currentSessions);
        totalSessions = findViewById(R.id.totalSessions);
        scan = findViewById(R.id.scanId);

        Intent intent = getIntent();
        if (null != intent){
            id = intent.getStringExtra("Id");
            course = intent.getStringExtra("Course");
        }

//        Getting total current sessions
        Log.d(TAG, "onCreate: " + course);
        db.collection("classes/"+course+"/Subjects/"+id+"/Attendance")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot snapshot = task.getResult();
                        Log.d(TAG, "onComplete: task");
                        if (snapshot != null){
                            if (snapshot.isEmpty()){
                                currentSessions.setText("0");
                            }else {
                                snapshot.getDocuments().forEach(item -> {

                                    current++;
                                });
                                currentSessions.setText(String.valueOf(current));
                                Log.d(TAG, "onComplete: current " + current);
                            }
                        }
                    }
                });

        //        Getting total students
        db.collection("classes/"+course+"/Students")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot snapshot = task.getResult();
                        Log.d(TAG, "onComplete: task");

                        if (snapshot != null){
                            if (snapshot.isEmpty()){
                                currentSessions.setText("0");
                            }else {
                                snapshot.getDocuments().forEach(item -> {
                                    students++;
                                });
                                Log.d(TAG, "onComplete: students " + students);

                                totalStudents.setText(String.valueOf(students));
                            }
                        }
                    }
                });

        scan.setOnClickListener(v -> {
            Intent intent1 = new Intent(TeacherCourseView.this, ScanAttendance.class);
            startActivity(intent1);
        });

    }
}