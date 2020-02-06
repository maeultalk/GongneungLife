package com.maeultalk.gongneunglife.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.test.NetworkActivity;

public class SettingsActivity extends AppCompatActivity {

    int count = 0;

    String email;
    String nick;
    String admin;

    TextView textView_nick;
    TextView textView_email;
    TextView textView_admin;

    Button button_address;
    Button button_network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("설정");

        Button button_test = (Button) findViewById(R.id.button_test);
        button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setCancelable(false);
                builder.setTitle("로그아웃");
                builder.setMessage("로그아웃 하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.clear();
                                editor.commit();

                                setResult(500);
                                finish();
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();

            }
        });

        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        email = pref.getString("email", "");
        nick = pref.getString("nick", "");
        admin = pref.getString("admin", "");

        button_address = (Button) findViewById(R.id.button_address);
        button_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
                startActivity(intent);
            }
        });

        textView_nick = (TextView) findViewById(R.id.textView_nick);
        textView_nick.setText(nick);
        textView_email = (TextView) findViewById(R.id.textView_email);
        textView_email.setText(email);
        textView_admin = (TextView) findViewById(R.id.textView_admin);
        if(admin.equals("true")) {
            textView_admin.setText("관리자");
            button_address.setVisibility(View.VISIBLE);
        } else {
            textView_admin.setVisibility(View.GONE);
            button_address.setVisibility(View.GONE);
        }

        button_network = (Button) findViewById(R.id.button_network);
        button_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NetworkActivity.class);
                startActivity(intent);
            }
        });
        if(admin.equals("true")) {
            button_network.setVisibility(View.VISIBLE);
        } else {
            button_network.setVisibility(View.GONE);
        }

        View view = (View) findViewById(R.id.view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(count==10) {
//                    startActivity(new Intent(SettingsActivity.this, InboxAdminActivity.class));
                    Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
