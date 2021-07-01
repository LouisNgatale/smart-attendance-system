package com.louisngatale.smartattendance.Screens.ProtectedRoutes.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.louisngatale.smartattendance.R;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanAttendance extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private FirebaseAuth mAuth;
    Button proceed;
    FirebaseFirestore db;
    String course;
    String id;
    String TAG = "scan";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_attendance);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        if (null != intent){
            id = intent.getStringExtra("Id");
            course = intent.getStringExtra("Course");
            Log.d(TAG, course);

        }

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        proceed = findViewById(R.id.proceed);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Set current session to active
                Map<String, Object> session = new HashMap<>();
                session.put(mAuth.getUid(),true);

                String qrValue = result.getText();


                db.collection("classes/"+course+"/Subjects/"+id+"/Attendance/")
                    .document(qrValue.trim())
                    .collection("attendees")
                    .add(session)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(ScanAttendance.this, "Session registered", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(ScanAttendance.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        }));

        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
