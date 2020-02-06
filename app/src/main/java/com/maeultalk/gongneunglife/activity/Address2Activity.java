package com.maeultalk.gongneunglife.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.request.GetPlaceRequest;
import com.maeultalk.gongneunglife.request.SetPlaceInfoRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Address2Activity extends AppCompatActivity {

    String code;
    String name;

    String url;
    String latitude;
    String longitude;
    String image;
    String tel;

    EditText editText_url;
    EditText editText_latitude;
    EditText editText_longitude;
    EditText editText_image;
    EditText editText_tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address2);

        Intent intent = getIntent();
        code = intent.getStringExtra("place_code");
        name = intent.getStringExtra("place_name");

        setTitle(name);

        editText_url = (EditText) findViewById(R.id.editText_url);
        editText_latitude = (EditText) findViewById(R.id.editText_latitude);
        editText_longitude = (EditText) findViewById(R.id.editText_longitude);
        editText_image = (EditText) findViewById(R.id.editText_image);
        editText_tel = (EditText) findViewById(R.id.editText_tel);

        load();

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set();
            }
        });

    }

    void set() {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                finish();
            }
        };
        SetPlaceInfoRequest setPlaceInfoRequest = new SetPlaceInfoRequest(code, editText_url.getText().toString(), editText_latitude.getText().toString(), editText_longitude.getText().toString(), editText_image.getText().toString(), editText_tel.getText().toString(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(Address2Activity.this);
        queue.add(setPlaceInfoRequest);

    }

    void load() {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success) {
                        url = jsonResponse.getString("nmap");
                        latitude = jsonResponse.getString("latitude");
                        longitude = jsonResponse.getString("longitude");
                        image = jsonResponse.getString("image");
                        tel = jsonResponse.getString("tel");

                        editText_url.setText(url);
                        editText_latitude.setText(latitude);
                        editText_longitude.setText(longitude);
                        editText_image.setText(image);
                        editText_tel.setText(tel);
                    }
                } catch (JSONException e) {

                }

            }
        };
        GetPlaceRequest getPlaceRequest = new GetPlaceRequest(code, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Address2Activity.this);
        queue.add(getPlaceRequest);

    }
}
