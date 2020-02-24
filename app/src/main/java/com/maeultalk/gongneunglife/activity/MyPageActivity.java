package com.maeultalk.gongneunglife.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.maeultalk.gongneunglife.R;

public class MyPageActivity extends AppCompatActivity {

    TextView textView_nick;
    TextView textView_email;

    String nick;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        setTitle("마이페이지");

        setLayout();

        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        email = pref.getString("email", "");
        nick = pref.getString("nick", "");
        textView_nick.setText(nick);
        textView_email.setText(email);

    }

    private void setLayout() {
        textView_nick = (TextView) findViewById(R.id.textView_nick);
        textView_email = (TextView) findViewById(R.id.textView_email);
    }
}
