package com.louisngatale.smartattendance.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.louisngatale.smartattendance.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentSignInActivity extends AppCompatActivity {
    private static final String TAG = "CREATED";
    private CodeScanner mCodeScanner;
    String fullName,id,course;
    Button proceed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_in);

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);

        proceed = findViewById(R.id.proceed);

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
                course = getCourse(COURSE_PATTERN);
                proceed.setEnabled(true);

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


        proceed.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: " + fullName + " " + id + " " + course);
        });

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