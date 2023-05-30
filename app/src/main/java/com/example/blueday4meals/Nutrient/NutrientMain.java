package com.example.blueday4meals.Nutrient;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.blueday4meals.Camera.CameraMain;
import com.example.blueday4meals.ChildMainPage;
import com.example.blueday4meals.Function.navigationbar;
import com.example.blueday4meals.NaverMap.NaverMapMain;
import com.example.blueday4meals.R;
import com.example.blueday4meals.SettingMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NutrientMain extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrition_main);

        String userID = getIntent().getStringExtra("userID");
        int Cal = getIntent().getIntExtra("needCal", 0);
        int Point = getIntent().getIntExtra("Points", 5);

        double Carbon = 0, Fat = 0, Protain = 0;

        Button btnMain, btnCam, btnNut, btnMap, btnSet;

        int age = 0, gender = 0;
        btnMain = findViewById(R.id.button1);
        btnMap = findViewById(R.id.button2);
        btnNut = findViewById(R.id.button3);
        btnCam = findViewById(R.id.button4);
        btnSet = findViewById(R.id.button5);
        TextView textViewResult = findViewById(R.id.textView);

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NutrientMain.this, ChildMainPage.class, userID);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NutrientMain.this, NaverMapMain.class, userID);
            }
        });

        btnNut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NutrientMain.this, NutrientMain.class, userID);
            }
        });

        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NutrientMain.this, CameraMain.class, userID);
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NutrientMain.this, SettingMain.class, userID);
            }
        });

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    textViewResult.setText((CharSequence) jsonObject);
                    boolean success = jsonObject.getBoolean("success");
                    JSONArray data = jsonObject.getJSONArray("data");
                    if (success && data.length() > 0) {
                        JSONObject item = data.getJSONObject(0);
                        String userGender = item.getString("userGender");
                        String userBirth = item.getString("userBirth");

                        Toast.makeText(getApplicationContext(), userBirth, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), userGender, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("오류");
                }
            }
        };

        CalRequest Request = new CalRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(NutrientMain.this);
        queue.add(Request);

        new daily(age, gender);
        new Rating(Cal,Carbon,Protain,Fat);
    }
}