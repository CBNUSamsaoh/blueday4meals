package com.example.blueday4meals.Camera;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blueday4meals.MainPages.ChildMainPage;
import com.example.blueday4meals.Function.navigationbar;
import com.example.blueday4meals.NaverMap.NaverMapMain;
import com.example.blueday4meals.Nutrient.NutrientMain;
import com.example.blueday4meals.R;
import com.example.blueday4meals.MainPages.SettingMain;

public class CameraMain extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        String userID = getIntent().getStringExtra("userID");
        Log.d("TAG", "변수 값: " + userID);

        Button btnMain, btnCam, btnNut, btnMap, btnSet;

        btnMain = findViewById(R.id.button1);
        btnMap = findViewById(R.id.button2);
        btnNut = findViewById(R.id.button3);
        btnCam = findViewById(R.id.button4);
        btnSet = findViewById(R.id.button5);

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(CameraMain.this, ChildMainPage.class, userID);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(CameraMain.this, NaverMapMain.class, userID);
            }
        });

        btnNut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(CameraMain.this, NutrientMain.class, userID);
            }
        });

        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(CameraMain.this, CameraMain.class, userID);
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(CameraMain.this, SettingMain.class, userID);
            }
        });
    }
}