package com.example.blueday4meals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterType extends AppCompatActivity {

    CustomButtonOutLineView btnchild, btnparents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_type);
        btnchild = (CustomButtonOutLineView) findViewById(R.id.child);
        btnparents = (CustomButtonOutLineView) findViewById(R.id.parent);

        btnchild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterType.this, ChildRegisterActivity.class);
                intent.putExtra("PorC",1);
                startActivity(intent);
            }
        });

        btnparents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterType.this, ParentsRegisterActivity.class);
                intent.putExtra("PorC",0);
                startActivity(intent);
            }
        });
    }
}

