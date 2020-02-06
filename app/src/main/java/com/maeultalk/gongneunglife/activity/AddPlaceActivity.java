package com.maeultalk.gongneunglife.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.request.AddPlaceRequest;
import com.maeultalk.gongneunglife.request.AddPlaceTestRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class AddPlaceActivity extends AppCompatActivity {

    Button button;
    EditText editText;
    EditText editText2;
    ImageView imageView4;

    String placeCode;
    String placeName;

    String admin;

    LinearLayout layout;
    EditText editText_url;
    EditText editText_latitude;
    EditText editText_longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        setTitle("새로운 장소 만들기");

        editText = (EditText) findViewById(R.id.editText);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if(textView.getId()==R.id.editText && i== EditorInfo.IME_ACTION_DONE) {
//                    doConfirm();
                }

                return false;
            }
        });

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(editText.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "장소명을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    doConfirm();
                }

            }
        });

        layout = (LinearLayout) findViewById(R.id.layout_admin);

        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        admin = pref.getString("admin", "");

        if(admin.equals("true")) {
            layout.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.GONE);
        }

        editText_url = (EditText) findViewById(R.id.editText_url);
        editText_latitude = (EditText) findViewById(R.id.editText_latitude);
        editText_longitude = (EditText) findViewById(R.id.editText_longitude);

    }

    /*void doConfirm2() {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    final JSONObject jsonResponse = new JSONObject(response);
                    String code = jsonResponse.getString("code");
                    String name = jsonResponse.getString("name");
                    String type = jsonResponse.getString("type");
                    String nmap = jsonResponse.getString("nmap");
                    String latitude = jsonResponse.getString("latitude");
                    String longitude = jsonResponse.getString("longitude");
                    String tel = jsonResponse.getString("tel");
                    String image = jsonResponse.getString("image");

                    Toast.makeText(getApplicationContext(), code + "\n" + name + "\n" + type + "\n" + nmap + "\n" + latitude + "\n" + longitude + "\n" + tel + "\n" + image, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }

            }
        };

        AddPlaceTestRequest addPlaceRequest = new AddPlaceTestRequest("placeCode", "placeName", "url", "latitude", "longitude", "tel", "image", responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(addPlaceRequest);

    }*/

    void doConfirm() {
        button.setEnabled(false);

        placeName = editText.getText().toString().trim();

        AlertDialog.Builder builder = new AlertDialog.Builder(AddPlaceActivity.this);
        builder.setCancelable(false);
        builder.setTitle(placeName);
        builder.setMessage("장소를 등록하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        button.setEnabled(true);

                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//        spot_code = "place_" + simpleDateFormat.format(date) + "_" + new Random().nextInt(10000);
                        placeCode = "place_" + simpleDateFormat.format(date) + "_" + numberGen(4, 1);

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                try {
                                    final JSONObject jsonResponse = new JSONObject(response);
                                    String success = jsonResponse.getString("response");
                                    if(success.equals("success")) {

                                        //

                                        Toast.makeText(getApplicationContext(), "새로운 장소가 등록되었습니다.", Toast.LENGTH_SHORT).show();

                        /*Intent intent = new Intent(AddSpotActivity.this, AddContentActivity.class);
                        intent.putExtra("spot_name", spot_name);
                        intent.putExtra("spot_code", spot_code);
                        startActivity(intent);*/

                                        finish();

                                        // 만든 장소로 이동
                                        Intent intent = new Intent(AddPlaceActivity.this, PlaceActivity.class);
                                        intent.putExtra("place_code", placeCode);
                                        intent.putExtra("place_name", placeName);
                                        startActivity(intent);

                                    } else if (success.equals("overlap")) {

                                        final String code_overlap = jsonResponse.getString("code");
                                        final String name_overlap = jsonResponse.getString("name");

                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddPlaceActivity.this);
                                        builder.setCancelable(false);
                                        builder.setTitle(placeName);
                                        builder.setMessage("이미 존재하는 장소입니다.");
                        /*builder.setPositiveButton("예",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                                    }
                                });*/
                                        builder.setNeutralButton("방문하기",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        button.setEnabled(true);
                                                        Intent intent = new Intent(AddPlaceActivity.this, PlaceActivity.class);
                                                        intent.putExtra("place_code", code_overlap);
                                                        intent.putExtra("place_name", name_overlap);
                                                        startActivity(intent);
                                                    }
                                                });
                                        builder.setNegativeButton("확인",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        button.setEnabled(true);
                                                    }
                                                });
                        /*builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }
                        });*/
                                        builder.show();

                                        // 다이얼로그(그래도 만드시겠습니까?, 방문하시겠습니까?, 다른 이름으로 만들기(취소))

                                        // 그래도 만들시겠습니까 하면 무조건 만드는 api 호출

                                    } else {
                                        String error = jsonResponse.getString("error");
                                        String fail = jsonResponse.getString("response");
                                        Toast.makeText(getApplicationContext(), "처리실패" + error + fail, Toast.LENGTH_SHORT).show();
                                        button.setEnabled(true);
                                        // TODO: 04/04/2019 콘솔에 에러 찍어주기
                                    }
                                } catch (JSONException e) {
                                    button.setEnabled(true);
                                }

                            }
                        };

                        String url = "";
                        String latitude = "";
                        String longitude = "";
                        String tel = "";
                        String image = "";
                        if(!TextUtils.isEmpty(editText_url.getText().toString())) {
                            url = editText_url.getText().toString();
                        }
                        if(!TextUtils.isEmpty(editText_latitude.getText().toString())) {
                            latitude = editText_latitude.getText().toString();
                        }
                        if(!TextUtils.isEmpty(editText_longitude.getText().toString())) {
                            longitude = editText_longitude.getText().toString();
                        }

                        AddPlaceRequest addPlaceRequest = new AddPlaceRequest(placeCode, placeName, url, latitude, longitude, tel, image, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        queue.add(addPlaceRequest);

                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        button.setEnabled(true);
                    }
                });
                        /*builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }
                        });*/
        builder.show();

    }

    /**
     * 전달된 파라미터에 맞게 난수를 생성한다
     * @param len : 생성할 난수의 길이
     * @param dupCd : 중복 허용 여부 (1: 중복허용, 2:중복제거)
     *
     * Created by 닢향
     * http://niphyang.tistory.com
     */
    public static String numberGen(int len, int dupCd ) {

        Random rand = new Random();
        String numStr = ""; //난수가 저장될 변수

        for(int i=0;i<len;i++) {

            //0~9 까지 난수 생성
            String ran = Integer.toString(rand.nextInt(10));

            if(dupCd==1) {
                //중복 허용시 numStr에 append
                numStr += ran;
            }else if(dupCd==2) {
                //중복을 허용하지 않을시 중복된 값이 있는지 검사한다
                if(!numStr.contains(ran)) {
                    //중복된 값이 없으면 numStr에 append
                    numStr += ran;
                }else {
                    //생성된 난수가 중복되면 루틴을 다시 실행한다
                    i-=1;
                }
            }
        }
        return numStr;
    }

}
