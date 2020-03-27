package com.maeultalk.gongneunglife.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialog;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.request.AddUserRequest;
import com.maeultalk.gongneunglife.request.ConfirmNickRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private String nick;
    private String email;
    private String password;
    private String password2;

    private EditText editText_nick;
    private EditText editText_email;
    private EditText editText_pwd;
    private EditText editText_pwd2;
    private Button button_complete;

    AppCompatDialog progressDialog;

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{8,16}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("회원가입");

        editText_nick = (EditText) findViewById(R.id.editText_nick);
        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_pwd = (EditText) findViewById(R.id.editText_pwd);
        editText_pwd2 = (EditText) findViewById(R.id.editText_pwd2);
        button_complete = (Button) findViewById(R.id.button_complete);

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        button_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new AppCompatDialog(RegisterActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.setContentView(R.layout.progress_loading);
                progressDialog.show();

                button_complete.setEnabled(false);

                nick = editText_nick.getText().toString().trim();
                email = editText_email.getText().toString().trim();
                password = editText_pwd.getText().toString();
                password2 = editText_pwd2.getText().toString();

                if (TextUtils.isEmpty(nick)) {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    button_complete.setEnabled(true);
                } else {

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                final JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if(success) {
                                    if (jsonResponse.getBoolean("overlap")) {
                                        progressDialog.dismiss();
                                        button_complete.setEnabled(true);
                                        Toast.makeText(getApplicationContext(), "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (TextUtils.isEmpty(email)) {
                                            // 이메일 공백
                                            Toast.makeText(RegisterActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                            button_complete.setEnabled(true);
                                        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                            // 이메일 형식 불일치
                                            Toast.makeText(RegisterActivity.this, "잘못된 이메일 형식입니다.", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                            button_complete.setEnabled(true);
                                        } else {
                                            firebaseAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                                    if (task.getResult().getProviders().size() > 0) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(RegisterActivity.this, "이미 가입된 이메일입니다.", Toast.LENGTH_SHORT).show();
                                                        button_complete.setEnabled(true);
                                                    } else {
                                                        if (!PASSWORD_PATTERN.matcher(password).matches()) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(RegisterActivity.this, "잘못된 비밀번호 형식입니다. 비밀번호는 공백을 포함할 수 없으며, 8자리 이상 16자리 이하로만 가능합니다.", Toast.LENGTH_SHORT).show();
                                                            button_complete.setEnabled(true);
                                                        } else if (!password.equals(password2)) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                                            button_complete.setEnabled(true);
                                                        } else {
                                                            firebaseAuth.createUserWithEmailAndPassword(email, password)
                                                                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                                            if (task.isSuccessful()) {
                                                                                // 회원가입 성공
                                                                                firebaseAuth.useAppLanguage(); //해당기기의 언어 설정
                                                                                task.getResult().getUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {

                                                                                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                                                                @Override
                                                                                                public void onResponse(String response) {
                                                                                                    try {
                                                                                                        final JSONObject jsonResponse = new JSONObject(response);
                                                                                                        boolean success = jsonResponse.getBoolean("success");
                                                                                                        if(success) {
                                                                                                            progressDialog.dismiss();
                                                                                                            Toast.makeText(RegisterActivity.this, "인증 메일이 전송되었습니다. 인증완료 후 로그인해 주시기바랍니다.", Toast.LENGTH_SHORT).show();
                                                                                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                                                                            intent.putExtra("email", email);
                                                                                                            intent.putExtra("password", password);
                                                                                                            intent.putExtra("nick", nick);
                                                                                                            startActivity(intent);
                                                                                                            finish();
                                                                                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // 페이드-아웃 애니메이션
                                                                                                        } else {
                                                                                                            progressDialog.dismiss();
                                                                                                            button_complete.setEnabled(true);
                                                                                                            Toast.makeText(getApplicationContext(), "회원정보 저장 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    } catch (JSONException e) {

                                                                                                    }
                                                                                                }
                                                                                            };
                                                                                            AddUserRequest addUserRequest = new AddUserRequest(email, nick, responseListener);
                                                                                            RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                                                                                            queue.add(addUserRequest);

                                                                                        } else {
                                                                                            Toast.makeText(RegisterActivity.this, "인증 메일 전송이 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                                                                            progressDialog.dismiss();
                                                                                            button_complete.setEnabled(true);
                                                                                            // TODO: 02/05/2019 인증 메일 재전송 버튼으로 변경
                                                                                        }
                                                                                    }
                                                                                });
                                                                            } else {
                                                                                // 회원가입 실패
                                                                                progressDialog.dismiss();
                                                                                Toast.makeText(RegisterActivity.this, R.string.failed_signup, Toast.LENGTH_SHORT).show();
                                                                                button_complete.setEnabled(true);
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    button_complete.setEnabled(true);
                                    Toast.makeText(getApplicationContext(), "닉네임 중복확인 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                            }
                        }
                    };
                    ConfirmNickRequest confirmNickRequest = new ConfirmNickRequest(nick, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(confirmNickRequest);

                }

            }
        });

        findViewById(R.id.textView_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // 페이드-아웃 애니메이션
            }
        });

    }
}
