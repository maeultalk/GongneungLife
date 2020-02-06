package com.maeultalk.gongneunglife.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.util.RecyclerViewDecoration;
import com.maeultalk.gongneunglife.adapter.Inbox2Adapter;
import com.maeultalk.gongneunglife.model.Inbox2;
import com.maeultalk.gongneunglife.request.LoadInbox2Request;
import com.maeultalk.gongneunglife.request.SendAsk;
import com.maeultalk.gongneunglife.request.SendNewAsk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AnswerActivity2 extends AppCompatActivity {

    LinearLayout layout_map;
    LinearLayout layout_shame;
    LinearLayout layout_good;

    EditText editText;
    Button button;

    String id;
    String subject;

    RecyclerView recyclerView;
    Inbox2Adapter adapter;

    public static ArrayList<Inbox2> inbox2s = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer2);
//        setTitle("공릉동에 신발세탁소 위치 알려주세요~");

//        setLayout();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        subject = intent.getStringExtra("subject");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        if(!TextUtils.isEmpty(id)) {
            setTitle(subject);
            loadData(id);
        } else {
            setTitle("궁금한 공릉동 정보를 요청해주세요.");
        }

        editText = (EditText) findViewById(R.id.editText);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean hasFocus)
            {
                if(hasFocus)
                {
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try {
                                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                            } catch (Exception e) {

                            }
                        }
                    }, 100);
                }
            }
        });
        /*editText.requestFocus();



        //키보드 보이게 하는 부분

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


*/
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editText.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "보내실 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    button.setEnabled(false);
                    send(editText.getText().toString());
                }
            }
        });

    }

    private void send(String msg) {

        if(!TextUtils.isEmpty(id)) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        final JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success) {

                            editText.setText("");
                            button.setEnabled(true);

//                            String newId = jsonResponse.getString("id");
                            loadData(id);
//                            id = newId;

                        } else {
                            Toast.makeText(getApplicationContext(), "처리실패", Toast.LENGTH_SHORT).show();
                            button.setEnabled(true);
                        }
                    } catch (JSONException e) {
                        button.setEnabled(true);
                    }
                }
            };
            SendAsk sendAsk = new SendAsk(id, msg, responseListener);
            RequestQueue queue = Volley.newRequestQueue(AnswerActivity2.this);
            queue.add(sendAsk);
        } else {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        final JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success) {

                            editText.setText("");
                            button.setEnabled(true);

                            String newId = jsonResponse.getString("id");
                            loadData(newId);
                            id = newId;

                        } else {
                            Toast.makeText(getApplicationContext(), "처리실패", Toast.LENGTH_SHORT).show();
                            button.setEnabled(true);
                        }
                    } catch (JSONException e) {
                        button.setEnabled(true);
                    }

                }
            };
            SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
            String user = pref.getString("email", "");
            SendNewAsk sendNewAsk = new SendNewAsk(user, msg, responseListener);
            RequestQueue queue = Volley.newRequestQueue(AnswerActivity2.this);
            queue.add(sendNewAsk);
        }

    }

    private void loadData(String id) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonResponse.get("inbox2");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Inbox2>>(){}.getType();
                    inbox2s = gson.fromJson(jsonArray.toString(), listType);
                    updateUI();

                    recyclerView.post(new Runnable() {

                        @Override

                        public void run() {

                            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);

                        }

                    });
                } catch (JSONException e) {

                }

            }
        };
        LoadInbox2Request loadInbox2Request = new LoadInbox2Request(id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(AnswerActivity2.this);
        queue.add(loadInbox2Request);

    }

    private void updateUI() {
        if(adapter == null){
            setRecyclerView();
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    private void setRecyclerView(){
        // 각 Item 들이 RecyclerView 의 전체 크기를 변경하지 않는 다면
        // setHasFixedSize() 함수를 사용해서 성능을 개선할 수 있습니다.
        // 변경될 가능성이 있다면 false 로 , 없다면 true를 설정해주세요.
        recyclerView.setHasFixedSize(true);

        // RecyclerView에 Adapter를 설정해줍니다.
        adapter = new Inbox2Adapter(AnswerActivity2.this, inbox2s);
        recyclerView.setAdapter(adapter);

        // 다양한 LayoutManager 가 있습니다. 원하시는 방법을 선택해주세요.
        // 지그재그형의 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        // 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        // 가로 또는 세로 스크롤 목록 형식
        recyclerView.setLayoutManager(new LinearLayoutManager(AnswerActivity2.this));
        recyclerView.addItemDecoration(new RecyclerViewDecoration(AnswerActivity2.this, 12));

//        setData();
    }

    void setLayout() {
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

                final AlertDialog.Builder alt_bld = new AlertDialog.Builder(AnswerActivity2.this);

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

                final AlertDialog.Builder alt_bld = new AlertDialog.Builder(AnswerActivity2.this);

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
