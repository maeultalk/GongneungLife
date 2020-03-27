package com.maeultalk.gongneunglife.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.maeultalk.gongneunglife.R;

public class AnswerActivity extends AppCompatActivity {

    LinearLayout layout_map;
    LinearLayout layout_shame;
    LinearLayout layout_good;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        setTitle("공릉동에 신발세탁소 위치 알려주세요~");

        layout_map = (LinearLayout) findViewById(R.id.layout_map);
        layout_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:37.5,127.0"));
                startActivity(intent);
            }
        });

        layout_good = (LinearLayout) findViewById(R.id.layout_good);
        layout_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText et = new EditText(getApplicationContext());

                final AlertDialog.Builder alt_bld = new AlertDialog.Builder(AnswerActivity.this);

                alt_bld.setTitle("유용한 정보에요")

                        .setMessage("유용하셨다면 공릉코인(공돈) 기부를 원하는만큼 해주실수 있나요? 저희에게 큰 힘이 됩니다.")

//                .setIcon(R.drawable.check_dialog_64)

                        .setCancelable(false)

                        .setView(et)

                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                String value = et.getText().toString();

//                        user_name.setText(value);

                            }

                        });

                AlertDialog alert = alt_bld.create();

                alert.show();

            }
        });

        layout_shame = (LinearLayout) findViewById(R.id.layout_shame);
        layout_shame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText et = new EditText(getApplicationContext());

                final AlertDialog.Builder alt_bld = new AlertDialog.Builder(AnswerActivity.this);

                alt_bld.setTitle("아쉬운 정보에요")

                        .setMessage("어떤 부분이 아쉬운지, 어떤 정보가 더 있으면 좋을지 알려주세요. 저희에게 큰 힘이 됩니다.")

//                .setIcon(R.drawable.check_dialog_64)

                        .setCancelable(false)

                        .setView(et)

                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                String value = et.getText().toString();

//                        user_name.setText(value);

                            }

                        });

                AlertDialog alert = alt_bld.create();

                alert.show();

            }
        });

    }
}
