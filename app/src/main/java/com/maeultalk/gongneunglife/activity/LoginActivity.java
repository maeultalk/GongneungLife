package com.maeultalk.gongneunglife.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.request.GetUserRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText editText_email;
    EditText editText_pwd;
    Button button_complete;

    // 파이어베이스 인증 객체 생성
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    AppCompatDialog progressDialog;

    Intent intent;

    private String email;
    private String password;
    private String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("로그인");

        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_pwd = (EditText) findViewById(R.id.editText_pwd);
        button_complete = (Button) findViewById(R.id.button_complete);

        intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        nick = intent.getStringExtra("nick");
        editText_email.setText(email);
        editText_pwd.setText(password);

        if(!TextUtils.isEmpty(email)) {
            editText_email.setEnabled(false);
            editText_pwd.setEnabled(false);
        }

        button_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new AppCompatDialog(LoginActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.setContentView(R.layout.progress_loading);
                progressDialog.show();

                button_complete.setEnabled(false);

                if(TextUtils.isEmpty(editText_email.getText().toString().trim())) {
                    progressDialog.dismiss();
                    button_complete.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(editText_pwd.getText().toString().trim())) {
                    progressDialog.dismiss();
                    button_complete.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signInWithEmailAndPassword(editText_email.getText().toString(), editText_pwd.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        user = task.getResult().getUser();
                                        user.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                boolean emailVerified = user.isEmailVerified();
                                                if(emailVerified) {
                                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                final JSONObject jsonResponse = new JSONObject(response);
                                                                boolean success = jsonResponse.getBoolean("success");
                                                                if(success) {
                                                                    progressDialog.dismiss();
                                                                    goToMain(jsonResponse.getString("nick"), jsonResponse.getString("admin"));
                                                                } else {
                                                                    progressDialog.dismiss();
                                                                    button_complete.setEnabled(true);
                                                                    Toast.makeText(getApplicationContext(), "회원정보를 가져오지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            } catch (JSONException e) {

                                                            }
                                                        }
                                                    };
                                                    GetUserRequest getUserRequest = new GetUserRequest(user.getEmail(), responseListener);
                                                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                                                    queue.add(getUserRequest);
                                                } else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(LoginActivity.this, "이메일 인증 후 이용해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                                                    button_complete.setEnabled(true);
                                                }
                                            }
                                        });
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "로그인 정보가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                                        button_complete.setEnabled(true);
                                    }
                                }
                            });
                }

            }
        });

        findViewById(R.id.textView_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // 페이드-아웃 애니메이션
            }
        });

    }

    void goToMain(String nick, String admin) {
        Toast.makeText(LoginActivity.this, nick + "님 환영합니다.", Toast.LENGTH_SHORT).show();
        // TODO: 02/05/2019 프리퍼런스 셋팅해주고,
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", user.getEmail());
        editor.putString("nick", nick);
        editor.putString("admin", admin);
        editor.commit();
        // TODO: 02/05/2019 메인액티비티로 넘어가기
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // 페이드-아웃 애니메이션
    }

}
