package com.example.blueday4meals;

import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class ParentsRegisterActivity extends AppCompatActivity {

    private EditText et_pid, et_ppwd, et_ppwdR;
    private Button btn_idcheck, btn_signup;
    private int okay = 0;
    private boolean validate = false;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 액티비티 시작시 처음으로 실행되는 생명주기!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_parents);

        // 아이디 값 찾아주기
        et_pid = findViewById(R.id.pid);
        et_pid.setFilters(new InputFilter[] {idFilter});
        et_ppwd = findViewById(R.id.ppwd);
        et_ppwd.setFilters(new InputFilter[] {pwdFilter});
        et_ppwdR = findViewById(R.id.ppwdR);

        Intent intent = getIntent();
        String PorC = intent.getStringExtra("PorC");

        btn_idcheck = findViewById(R.id.idcheck);
        btn_idcheck.setOnClickListener(new View.OnClickListener() {//id중복체크
            @Override
            public void onClick(View view) {
                String userID = et_pid.getText().toString();
                if(validate)
                {
                    return;
                }
                if(userID.length() < 5){
                    AlertDialog.Builder builder = new AlertDialog.Builder( ParentsRegisterActivity.this );
                    dialog = builder.setMessage("아이디는 5자 이상이어야 합니다")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse=new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder( ParentsRegisterActivity.this );
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                                et_pid.setEnabled(false);
                                validate = true;
                                btn_idcheck.setText("확인");
                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder( ParentsRegisterActivity.this );
                                dialog = builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                CidValidateRequest validateRequest = new CidValidateRequest(userID,responseListener);
                RequestQueue queue = Volley.newRequestQueue(ParentsRegisterActivity.this);
                queue.add(validateRequest);

            }
        });

        // 회원가입 버튼 클릭 시 수행
        btn_signup = findViewById(R.id.signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 현재 입력되어있는 값을 가져온다.
                String id = et_pid.getText().toString();
                String pwd = et_ppwd.getText().toString();
                String pwdR = et_ppwdR.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if(id.length() >= 5) {
                                if(pwd.length() >= 8) {
                                    if(validate) {
                                        if(pwd.equals(pwdR)) {
                                            if(success) {
                                                Toast.makeText(getApplicationContext(),"회원 등록에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                                                okay = 1;
                                                Intent intent = new Intent(ParentsRegisterActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(getApplicationContext(),"회원 등록에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(),"비밀번호 확인이 틀립니다.",Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(),"아이디 중복확인을 해주세요.",Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(),"비밀번호는 8자 이상이어야 합니다.",Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(),"아이디는 5자 이상이어야 합니다.",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("오류");
                        }

                    }
                };

                if (okay == 1) {
                    // 서버로 Volley를 이용해서 요청을 함.
                    ParentsRegisterRequest registerRequest = new ParentsRegisterRequest(id, pwd, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(ParentsRegisterActivity.this);
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
}
