package com.louisngatale.smartattendance.Screens.ProtectedRoutes.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.louisngatale.smartattendance.R;

public class SubjectView extends AppCompatActivity {
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_view);

        Intent intent = getIntent();
        if (null != intent){
            id = intent.getStringExtra("Id");
        }
    }
}