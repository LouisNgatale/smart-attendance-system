package com.louisngatale.smartattendance.Screens.ProtectedRoutes.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.collection.LLRBNode;
import com.google.zxing.WriterException;
import com.louisngatale.smartattendance.R;

import java.util.Random;
import java.util.UUID;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QrCodeGenerator extends AppCompatActivity {
    ImageView qrImage;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_generator);
        qrImage = findViewById(R.id.qrPlaceHolder);
        back = findViewById(R.id.back);

        Intent intent = getIntent();

        if (intent != null) {

            String data = intent.getStringExtra("QrValue");

            QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 500);
            Bitmap qrBits = qrgEncoder.getBitmap();
            qrImage.setImageBitmap(qrBits);
        }

        back.setOnClickListener(v -> {
            finish();
        });
    }
}