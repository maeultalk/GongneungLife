package com.maeultalk.gongneunglife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.adapter.SearchAdapter;
import com.maeultalk.gongneunglife.model.Place;
import com.maeultalk.gongneunglife.request.SearchPlacesRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SearchSpotActivity extends AppCompatActivity {

    public static ArrayList<Place> places = new ArrayList<>();
    ListView listView;

    CollapsingToolbarLayout mCollapsingToolbarLayout;

    String flowFrom;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_spot);
        setTitle("");

        Intent intent = getIntent();
        flowFrom = intent.getStringExtra("SearchSpotActivity");

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbarLayout.setTitleEnabled(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView)findViewById(R.id.listView);

        View header = getLayoutInflater().inflate(R.layout.header_search, null, false) ;
        Button headerBtn = (Button) header.findViewById(R.id.button);
        headerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPlaceActivity.class);
                startActivity(intent);
//                finish();
            }
        });
        /*header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPlaceActivity.class);
                startActivity(intent);
            }
        });*/
        listView.addHeaderView(header) ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_search_spot, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.expandActionView();
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                finish();
                overridePendingTransition(0, 0);
                return true;
            }
        });
        searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(false);
        View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        v.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
        int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView magImage = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        magImage.setImageDrawable(null);
//        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        if(flowFrom.equals("search")) {
            searchView.setQueryHint("장소 검색");
        } else {
            searchView.setQueryHint("어느 장소에 글을 남기시겠습니까?");
        }
//        searchView.setIconified(false);
        searchView.requestFocus();
        //searchView.requestFocusFromTouch();
        getSpots("");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//                getSpots(s);

                Intent intent = new Intent(SearchSpotActivity.this, SearchResultActivity.class);
                intent.putExtra("search_result", s);
//                startActivity(intent);
                startActivityForResult(intent, 100);
                overridePendingTransition(0, 0);

                //todo setSpotsToListView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                getSpots(s);
                return false;
            }
        });

        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    // 게시물 데이터 로딩 및 자료화
    void getSpots(String s) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonResponse.get("places");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Place>>(){}.getType();
                    places = gson.fromJson(jsonArray.toString(), listType);

                    String[] array = new String[places.size()];
                    for(int i = 0; i< places.size() ; i++) {
                        array[i] = places.get(i).getName();
                    }

                    /*ArrayAdapter<String> adapter;
                    adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, array);*/

                    SearchAdapter searchAdapter = new SearchAdapter(getApplicationContext(), places);

                    listView.setAdapter(searchAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(SearchSpotActivity.this, PlaceActivity.class);
                            intent.putExtra("place_code", places.get(i-1).getCode());
                            intent.putExtra("place_name", places.get(i-1).getName());
                            if(!flowFrom.equals("search")) {
                                intent.putExtra("write", true);
                            }
                            startActivity(intent);
                        }
                    });
                } catch (JSONException e) {

                }

            }
        };
        SearchPlacesRequest searchPlacesRequest = new SearchPlacesRequest(s, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(searchPlacesRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==100) {
            finish();
        }
        if(resultCode==200) {
            searchView.setQuery("", false);
        }
    }
}
