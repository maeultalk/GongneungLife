package com.maeultalk.gongneunglife.test;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.maeultalk.gongneunglife.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ArrayTestActivity extends AppCompatActivity {

    EditText editText_subject;
    EditText editText_content;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_array_test);

        editText_subject = (EditText) findViewById(R.id.editText_subject);
        editText_content = (EditText) findViewById(R.id.editText_content);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String subject = editText_subject.getText().toString();
                String content = editText_content.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                Toast.makeText(getApplicationContext(), "처리성공", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "처리실패", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

                        }

                    }
                };
                /*ArrayTestRequest arrayTestRequest = new ArrayTestRequest(subject, content, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(arrayTestRequest);*/

            }
        });

    }
}