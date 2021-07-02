package com.louisngatale.smartattendance.Screens.ProtectedRoutes.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.louisngatale.smartattendance.R;
import com.louisngatale.smartattendance.Screens.ProtectedRoutes.Student.ScanAttendance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanStudentId extends AppCompatActivity {
    Button back;
    TextView feedback;
    private CodeScanner mCodeScanner;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    private String qrValue;
    String fullName,id,course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_studentid);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);

        back = findViewById(R.id.back);
        feedback = findViewById(R.id.feedback);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();

        if (null != intent){
            qrValue = intent.getStringExtra("QrValue").trim();
            course = intent.getStringExtra("Course").trim();
        }

        mCodeScanner.setDecodeCallback(result -> runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                        String ID_PATTERN = "/time:(.*)ID:/gmi";
                String ID_PATTERN = "ID:.\\d+";
//                        String NAME_PATTERN = "ID:.\\d+(.* )verify:";
                String NAME_PATTERN = "ID:.\\d+(.* )Course:";
                String COURSE_PATTERN = "Course:.(.*) :";

                fullName = getName(NAME_PATTERN);
                id = getId(ID_PATTERN);
//                course = getCourse(COURSE_PATTERN);

                findStudent(id);
            }

            private String getId(String ID_PATTERN) {
                Pattern pattern = Pattern.compile(ID_PATTERN, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(result.getText());
                if (matcher.find()) {
                    String group = matcher.group();
                    Pattern pattern1 = Pattern.compile("\\d+", Pattern.CASE_INSENSITIVE);
                    Matcher matcher2 = pattern1.matcher(group);
                    if (matcher2.find()) {
                        return matcher2.group();
                    }
                    return null;
                }
                return null;
            }

            private String getName(String NAME_PATTERN) {
                Pattern pattern = Pattern.compile(NAME_PATTERN, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(result.getText());
                if (matcher.find()) {
                    String studentName = matcher.group(1);
                    String substring = studentName.substring(studentName.indexOf(",") + 2);
                    String substring2 = studentName.substring(0,studentName.indexOf(","));
                    return substring.concat(substring2);
                }
                return null;
            }

            private String getCourse(String COURSE_PATTERN) {
                Pattern pattern = Pattern.compile(COURSE_PATTERN, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(result.getText());
                if (matcher.find()) {
                    String group = matcher.group();
                    return group.substring(group.indexOf(":") + 1, group.lastIndexOf(":") - 1);
                }
                return null;
            }
        }));

        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    private void findStudent(String id) {
        db.collection("users")
                .whereEqualTo("schoolId",id)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        Optional<DocumentSnapshot> first = documents.stream().findFirst();

                        String id1 = first.get().getId();

                        saveStudentSession(id);
                    }
        });
    }

    private void saveStudentSession(String id) {
        // Set current session to active
        Map<String, Object> session = new HashMap<>();
        session.put(id,true);

        db.collection("classes/"+course+"/Subjects/"+id+"/Attendance/")
                .document(qrValue.trim())
                .collection("attendees")
                .document(id)
                .set(session)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(ScanStudentId.this, "Session registered", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(ScanStudentId .this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}