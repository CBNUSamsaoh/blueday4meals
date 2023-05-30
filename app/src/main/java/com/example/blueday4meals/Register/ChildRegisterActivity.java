package com.example.blueday4meals.Register;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.blueday4meals.Login.LoginActivity;
import com.example.blueday4meals.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.regex.Pattern;

public class ChildRegisterActivity extends AppCompatActivity {

    private EditText et_cid, et_cpwd, et_cpwdR;
    private int day = 0, month = 0, year = 0, gender = 0, okay = 0;
    private boolean validate = false;
    private AlertDialog dialog;
    private String[] city, citys;
    private Button btn_idcheck, btn_cRegion1, btn_cRegion2, btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 액티비티 시작시 처음으로 실행되는 생명주기!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_child);

        // 아이디 값 찾아주기
        et_cid = findViewById(R.id.cid);
        et_cid.setFilters(new InputFilter[] {idFilter});
        et_cpwd = findViewById(R.id.cpwd);
        et_cpwd.setFilters(new InputFilter[] {pwdFilter});
        et_cpwdR = findViewById(R.id.cpwdR); // 비밀번호 확인용

        Intent intent = getIntent();
        String PorC = intent.getStringExtra("PorC");

        btn_idcheck = findViewById(R.id.idcheck);
        btn_idcheck.setOnClickListener(new View.OnClickListener() {//id중복체크
            @Override
            public void onClick(View view) {
                String userID = et_cid.getText().toString();
                if (validate) {
                    return;
                }
                if (userID.length() < 5) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChildRegisterActivity.this);
                    dialog = builder.setMessage("아이디는 5자 이상이어야 합니다")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ChildRegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                et_cid.setEnabled(false);
                                validate = true;
                                btn_idcheck.setText("확인");
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ChildRegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                CidValidateRequest validateRequest = new CidValidateRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ChildRegisterActivity.this);
                queue.add(validateRequest);

            }
        });

        // 생년월일 변경 시 수행
        DatePicker dps_cBirth = findViewById(R.id.cBirth);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
//        DatePicker.OnDateChangedListener onDateChangedListener = null;
//        dps_cBirth.init(year, month, day, onDateChangedListener);
        dps_cBirth.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int yy, int mm, int dd) {
                year = yy;
                month = mm;
                day = dd;
            }
        });

        // 성별 라디오버튼 클릭시 수행
        RadioGroup rbg_cGender = findViewById(R.id.cGender);
        rbg_cGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if(checkedId == R.id.male){
                    gender = 0;  //남
                } else {
                    gender = 1;  //여
                }
            }
        });

        btn_cRegion1 = findViewById(R.id.cRegion1);
        btn_cRegion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCity();
            }
        });

        btn_cRegion2 = findViewById(R.id.cRegion2);
        btn_cRegion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_cRegion1.getText().toString().equals("충청북도")){
                    showDialogCitys("Chungbuk");
                } else if (btn_cRegion1.getText().toString().equals("도/특별시/광역시")){
                    Toast.makeText(getApplicationContext(), "도/특별시/광역시를 먼저 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 회원가입 버튼 클릭 시 수행
        btn_signup = findViewById(R.id.signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 현재 입력되어있는 값을 가져온다.
                String id = et_cid.getText().toString();
                String pwd = et_cpwd.getText().toString();
                String pwdR = et_cpwdR.getText().toString();

                // 생년월일 xxxx-xx-xx 형식으로 저장
                String birth = Integer.toString(year);
                if(Integer.toString(month+1).length() == 1) {
                    birth += "-0" + Integer.toString(month+1);
                } else {
                    birth += "-" + Integer.toString(month+1);
                }
                if(Integer.toString(day).length() == 1) {
                    birth += "-0" + Integer.toString(day);
                } else {
                    birth += "-" + Integer.toString(day);
                }

                String region = btn_cRegion1.getText().toString() + ";" + btn_cRegion2.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (id.length() >= 5) {
                                if (pwd.length() >= 8) {
                                    if (validate) {
                                        if (pwd.equals(pwdR)) {
                                            if (success) {
                                                Toast.makeText(getApplicationContext(), "회원 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                                okay = 1;
                                                Intent intent = new Intent(ChildRegisterActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(getApplicationContext(), "회원 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("오류");
                        }

                    }
                };

                if (id.length() >= 5) {
                    if (pwd.length() >= 8) {
                        if (validate) {
                            if (pwd.equals(pwdR)) {
                                if  (btn_cRegion1.getText().toString().equals("도/특별시/광역시") || btn_cRegion2.getText().toString().equals("시/군/구")) {
                                    okay = 0;
                                    Toast.makeText(getApplicationContext(), "지역을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                                }else{
                                    okay = 1;
                                }
                            } else {
                                okay = 0;
                                Toast.makeText(getApplicationContext(), "비밀번호 확인이 틀립니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            okay = 0;
                            Toast.makeText(getApplicationContext(), "아이디 중복확인을 해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        okay = 0;
                        Toast.makeText(getApplicationContext(), "비밀번호는 8자 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    okay = 0;
                    Toast.makeText(getApplicationContext(), "아이디는 5자 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
                }

                if(okay == 1){
                    // 서버로 Volley를 이용해서 요청을 함.
                    ChildRegisterRequest registerRequest = new ChildRegisterRequest(id, pwd, birth, gender, region, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(ChildRegisterActivity.this);
                    queue.add(registerRequest);
                }

            }
        });

    }

    protected InputFilter idFilter= new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");

            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

    protected InputFilter pwdFilter= new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Pattern ps = Pattern.compile("^[a-zA-Z0-9\\d`~!@#$%^&*()-_=+]+$");

            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

    public void showDialogCity() {
        citys = getResources().getStringArray(R.array.Citydo);
        AlertDialog.Builder builder = new AlertDialog.Builder(ChildRegisterActivity.this);
        builder.setTitle("지역을 선택하세요");

        builder.setItems(citys, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                btn_cRegion1.setText(citys[i]);
                Toast.makeText(getApplicationContext(), citys[i] + "를 선택했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showDialogCitys(String names) {
        int resourceId = getResources().getIdentifier(names,"array",getPackageName());
        city = getResources().getStringArray(resourceId);

        AlertDialog.Builder builderc = new AlertDialog.Builder(ChildRegisterActivity.this);
        builderc.setTitle("지역을 선택하세요");

        builderc.setItems(city, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                btn_cRegion2.setText(city[i]);
                Toast.makeText(getApplicationContext(), city[i] + "를 선택했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builderc.create();
        alertDialog.show();
    }
}