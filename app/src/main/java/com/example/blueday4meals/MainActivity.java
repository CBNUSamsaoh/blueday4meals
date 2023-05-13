package com.example.blueday4meals;

import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import net.daum.mf.map.api.MapView;

public class MainActivity extends AppCompatActivity {

    MapView mapView;
    RelativeLayout mapViewContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapViewContainer = (RelativeLayout)findViewById(R.id.map_view);
        mapView = new MapView(this);
        mapViewContainer.addView(mapView);
    }
}