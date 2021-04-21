package com.louisngatale.smartattendance.Screens.ProtectedRoutes.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.louisngatale.smartattendance.R;
import com.louisngatale.smartattendance.Screens.RegisterActivity;

import java.util.ArrayList;
import java.util.List;

public class CourseView extends AppCompatActivity {
    private static final String TAG = "Home";
    String id;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    String course;
    Button scan;
    TextView percentage, totalAttended, totalSessions;
    int count = 0,days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_view);
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        percentage = findViewById(R.id.percentage);
        totalAttended = findViewById(R.id.totalAttended);
        totalSessions = findViewById(R.id.totalSessions);
        scan = findViewById(R.id.scanId);

        Intent intent = getIntent();
        if (null != intent){
            id = intent.getStringExtra("Id");
            course = intent.getStringExtra("Course");
        }

        db.collection("classes/"+course+"/Subjects/"+id+"/Attendance")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot snapshot = task.getResult();
                        if (snapshot != null){
                            if (snapshot.isEmpty()){
                                totalAttended.setText("0");
                                totalSessions.setText("0");
                                percentage.setText("0");
                            }else {
                                snapshot.getDocuments().forEach(item -> {
                                    String result = String.valueOf(item.get(uid));
                                    if (result.equals("true")){
                                        count++;
                                    }
                                    days++;
                                    Log.d(TAG, "onComplete: " + count);
                                });
                                int perCent = (count / days) * 100;
                                totalAttended.setText(String.valueOf(count));
                                totalSessions.setText(String.valueOf(days));
                                percentage.setText(String.valueOf(perCent));

                            }
                        }else {
                                Log.d(TAG, "onComplete: No attendance yet");
                        }
                    }
                });

        scan.setOnClickListener(v -> {
            Intent intent1 = new Intent(CourseView.this,ScanAttendance.class);
            startActivity(intent1);
        });
    }
}