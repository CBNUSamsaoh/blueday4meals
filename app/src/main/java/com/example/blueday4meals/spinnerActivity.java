package com.example.blueday4meals;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class spinnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrition_main);

        Spinner yearSpinner = findViewById(R.id.yearSpinner);
        Spinner monthSpinner = findViewById(R.id.monthSpinner);
        TextView selectedYearTextView = findViewById(R.id.tv_year);
        TextView selectedMonthTextView = findViewById(R.id.tv_month);

        // 현재 날짜로 각각 연도, 월을 초기화합니다.
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy년");
        String currentYear = yearFormat.format(calendar.getTime());
        selectedMonthTextView.setText(currentYear);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM월");
        String currentMonth = monthFormat.format(calendar.getTime());
        selectedMonthTextView.setText(currentMonth);

        // 원하는 연도,월의 배열 또는 리스트를 생성합니다.
        String[] years = {"2023년", "2024년", "2025년", "2026년", "2027년", "2028년", "2029년", "2030년"};
        String[] months = {"1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"};

        // 연도 어댑터를 생성하고 스피너에 연결합니다.
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        // 월 어댑터를 생성하고 스피너에 연결합니다.
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        // 연도스피너의 선택 이벤트 리스너를 설정합니다.
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 선택된 연도을 가져와서 텍스트뷰에 표시합니다.
                String selectedYear = parent.getItemAtPosition(position).toString();
                selectedYearTextView.setText(selectedYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무것도 선택되지 않았을 때의 처리를 여기에 작성합니다.
            }
        });

        // 월스피너의 선택 이벤트 리스너를 설정합니다.
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 선택된 달을 가져와서 텍스트뷰에 표시합니다.
                String selectedMonth = parent.getItemAtPosition(position).toString();
                selectedMonthTextView.setText(selectedMonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무것도 선택되지 않았을 때의 처리를 여기에 작성합니다.
            }
        });

    }

}
