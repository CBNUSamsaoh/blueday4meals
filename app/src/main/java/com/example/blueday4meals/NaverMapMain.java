package com.example.blueday4meals;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NaverMapMain extends AppCompatActivity implements NaverMap.OnMapClickListener, OnMapReadyCallback {

    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private NaverMapItem naverMapList;
    private List<NaverMapData> naverMapInfo;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    List<LatLng> lstLatLng = new ArrayList<>();
    Button btnMain, btnCam, btnNut, btnMap, btnSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.naver_map);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        btnMain = findViewById(R.id.button1);
        btnMap = findViewById(R.id.button2);
        btnNut = findViewById(R.id.button3);
        btnCam = findViewById(R.id.button4);
        btnSet = findViewById(R.id.button5);

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NaverMapMain.this, ChildMainPage.class);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NaverMapMain.this, NaverMapMain.class);
            }
        });

        btnNut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NaverMapMain.this, NutrientMain.class);
            }
        });

        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NaverMapMain.this, CameraMain.class);
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NaverMapMain.this, SettingMain.class);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);  // 현재위치 표시
        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE); // 현재위치 표시할 때 권한 확인

        // 클라이언트 객체 생성
        NaverMapApiInterface naverMapApiInterface = NaverMapRequest.getClient().create(NaverMapApiInterface.class);
        // 응답을 받을 콜백 구현
        Call<NaverMapItem> call = naverMapApiInterface.getMapData();
        // 클라이언트 객체가 제공하는 enqueue로 통신에 대한 요청, 응답 처리 방법 명시
        call.enqueue(new Callback<NaverMapItem>() {
            @Override
            public void onResponse(Call<NaverMapItem> call, Response<NaverMapItem> response) { // 통신 성공시
                naverMapList = response.body(); // naverMapList에 요청에 대한 응답 결과 저장
                naverMapInfo = naverMapList.data;

                // 현재 위치 기준으로 마커 표시
                LatLng currentLocation = new LatLng(locationSource.getLastLocation().getLatitude(), locationSource.getLastLocation().getLongitude());
                for (int i = 0; i < naverMapInfo.size(); i++) {
                    NaverMapData mapData = naverMapInfo.get(i);
                    LatLng markerLocation = new LatLng(mapData.getlatitude(), mapData.getlongitude());

                    // 현재 위치와 마커의 거리 계산 -진경
                    double distance = currentLocation.distanceTo(markerLocation);

                    // 일정 범위 내에 있는 마커만 표시
                    if (distance <= 1000) { // 1000m(1km) 이내의 마커만 표시하도록 설정
                        Marker marker = new Marker();
                        marker.setPosition(markerLocation);
                        marker.setWidth(20); // 마커 크기 조절
                        marker.setHeight(20);
                        marker.setMap(naverMap);

                        int finalI = i;
                        marker.setOnClickListener(new Overlay.OnClickListener() {
                            @Override
                            public boolean onClick(@NonNull Overlay overlay) {
                                getClickHandler(finalI);
                                return false;
                            }
                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<NaverMapItem> call, Throwable t) { // 통신 실패시

            }
        });
    }

    //마커 클릭시 가게 정보 제공 (가게명, 전화번호)
    private void getClickHandler(int index) {
        //데이터 불러오기
        if (naverMapInfo != null && index >= 0 && index < naverMapInfo.size()) {
            NaverMapData selectedData = naverMapInfo.get(index);
            String name = selectedData.getfranchisee_name();
            String tel_num = selectedData.gettel_num();

            //정보 text 추가
            DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
            TextView drawerTitle = findViewById(R.id.drawer_title);
            drawerTitle.setText(name + "\n" + tel_num);
            drawerTitle.setClickable(true);
            drawerTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 전화 걸기 Intent 생성
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + tel_num));
                    startActivity(intent);
                }
            });

            drawerLayout.openDrawer(GravityCompat.START);
        }
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
                return;
            } else {
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {

    }
}
