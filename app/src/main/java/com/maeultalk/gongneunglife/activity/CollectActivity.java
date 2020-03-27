package com.maeultalk.gongneunglife.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.adapter.CollectRecyclerViewAdapter;
import com.maeultalk.gongneunglife.model.Content;
import com.maeultalk.gongneunglife.request.LoadCollectContentsRequest;
import com.maeultalk.gongneunglife.util.RecyclerViewDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CollectActivity extends AppCompatActivity {

    CollapsingToolbarLayout mCollapsingToolbarLayout;

    private RecyclerView recyclerView;

    private RecyclerView.Adapter adapter;

    public static ArrayList<Content> contentsInCollect= new ArrayList<>();

    String title;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbarLayout.setTitleEnabled(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        title = intent.getStringExtra("collect");
        getSupportActionBar().setTitle("#" + title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        loadData();

    }

    private void loadData() {

        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        String user = pref.getString("email", "");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonResponse.get("contents");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Content>>(){}.getType();
                    contentsInCollect = gson.fromJson(jsonArray.toString(), listType);
//                    Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();

                    updateUI();
                } catch (JSONException e) {

                }

            }
        };

        LoadCollectContentsRequest loadContentsRequest = new LoadCollectContentsRequest(user, title, responseListener);
        RequestQueue queue = Volley.newRequestQueue(CollectActivity.this);
        queue.add(loadContentsRequest);

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
        adapter = new CollectRecyclerViewAdapter(CollectActivity.this, true, contentsInCollect);
        recyclerView.setAdapter(adapter);

        // 다양한 LayoutManager 가 있습니다. 원하시는 방법을 선택해주세요.
        // 지그재그형의 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        // 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        // 가로 또는 세로 스크롤 목록 형식
        recyclerView.setLayoutManager(new LinearLayoutManager(CollectActivity.this));
        recyclerView.addItemDecoration(new RecyclerViewDecoration(CollectActivity.this, 12));

//        setData();
    }

}
