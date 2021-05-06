package com.louisngatale.smartattendance.Screens.ProtectedRoutes.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.firebase.auth.FirebaseAuth;
import com.louisngatale.smartattendance.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanAttendance extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private FirebaseAuth mAuth;
    Button proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_attendance);
        mAuth = FirebaseAuth.getInstance();

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        proceed = findViewById(R.id.proceed);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(new Runnable() {
            @Override
            public void run() {

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
