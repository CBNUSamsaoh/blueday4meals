package com.example.blueday4meals;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private EditText et_id, et_pass;
    private RadioButton rb_child, rb_parents;
    private CustomButtonOutLineView btn_login, btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        et_id = findViewById(R.id.et_id);
        et_id.setFilters(new InputFilter[] {idFilter});
        et_pass = findViewById(R.id.et_pass);
        et_pass.setFilters(new InputFilter[] {pwdFilter});
        rb_child = findViewById(R.id.rb_child);
        rb_parents = findViewById(R.id.rb_parents);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);


        // 회원가입 버튼을 클릭 시 수행
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterType.class);
                startActivity(intent);
            }
        });

        // 로그인 버튼을 클릭 시 수행
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 현재 입력되어있는 값을 가져온다.
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();

                //아동 로그인
                Response.Listener<String> c_responseListener = null;
                if (rb_child.isChecked()) {
                    c_responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                // TODO : 인코딩 문제때문에 한글 DB인 경우 로그인 불가
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) { // 로그인에 성공한 경우
                                    String userID = jsonObject.getString("userID");
                                    String userPass = jsonObject.getString("userPassword");

                                    Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, ChildMainPage.class);//Register대신 메인화면.class로 이동시키면 됨
                                    intent.putExtra("userID", userID);
                                    intent.putExtra("userPass", userPass);
                                    startActivity(intent);
                                } else { // 로그인에 실패한 경우
                                    Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                }

                //부모 로그인
                Response.Listener<String> p_responseListener = null;
                if (rb_parents.isChecked()) {
                    p_responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                // TODO : 인코딩 문제때문에 한글 DB인 경우 로그인 불가
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) { // 로그인에 성공한 경우
                                    String userID = jsonObject.getString("userID");
                                    String userPass = jsonObject.getString("userPassword");

                                    Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, ParentMainPage.class);//Register대신 메인화면.class로 이동시키면 됨
                                    intent.putExtra("userID", userID);
                                    intent.putExtra("userPass", userPass);
                                    startActivity(intent);
                                } else { // 로그인에 실패한 경우
                                    Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                }

                LoginRequest loginRequest = new LoginRequest(userID, userPass, c_responseListener);
                ParentsLoginRequest parentsLoginRequest = new ParentsLoginRequest(userID, userPass, p_responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
                queue.add(parentsLoginRequest);
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