package com.maeultalk.gongneunglife.activity;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.adapter.SearchContentsAdapter;
import com.maeultalk.gongneunglife.model.Content;
import com.maeultalk.gongneunglife.request.SearchContentsRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {

    CollapsingToolbarLayout mCollapsingToolbarLayout;

    ListView listView;
    public static ArrayList<Content> contents = new ArrayList<>();

    String search;

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(100);
        finish();
        overridePendingTransition(0, 0);
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //replaces the default 'Back' button action
        if(keyCode==KeyEvent.KEYCODE_BACK)   {
// something here
            setResult(100);
            finish();
            overridePendingTransition(0, 0);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        search = intent.getStringExtra("search_result");

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbarLayout.setTitleEnabled(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setTitle("검색결과 : " + search);

        listView = (ListView)findViewById(R.id.listView);

        doSearch(search);

    }

    // 게시물 데이터 로딩 및 자료화
    void doSearch(String s) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonResponse.get("contents");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Content>>(){}.getType();
                    contents = gson.fromJson(jsonArray.toString(), listType);

                    SearchContentsAdapter searchContentsAdapter = new SearchContentsAdapter(getApplicationContext(), contents);

                    listView.setAdapter(searchContentsAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(SearchResultActivity.this, PlaceActivity.class);
                            intent.putExtra("place_code", contents.get(i).getPlace_code());
                            intent.putExtra("place_name", contents.get(i).getPlace_name());
                            intent.putExtra("content_id", contents.get(i).getId());
                            startActivity(intent);
                        }
                    });
                } catch (JSONException e) {

                }

            }
        };
        SearchContentsRequest searchContentsRequest = new SearchContentsRequest(s, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(searchContentsRequest);

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
        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setIconifiedByDefault(false);
        View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        v.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
        int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView magImage = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
//        magImage.setImageDrawable(null);
//        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        /*if(flowFrom.equals("search")) {
            searchView.setQueryHint("장소 검색");
        } else {
            searchView.setQueryHint("어느 장소에 글을 남기시겠습니까?");
        }*/
//        searchView.setIconified(false);
//        searchView.requestFocus();
        //searchView.requestFocusFromTouch();
//        getSpots("");
        searchView.setQuery(search, false);
        searchView.clearFocus();
        /*searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        /*searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    finish();
                }
            }
        });*/
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
//                Toast.makeText(SearchResultActivity.this, "onMenuItemActionExpand called", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
//                Toast.makeText(SearchResultActivity.this, "onMenutItemActionCollapse called", Toast.LENGTH_SHORT).show();
                setResult(100);
                finish();
                overridePendingTransition(0, 0);
                return true;
            }
        });
        /*searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setResult(100);
                finish();
                overridePendingTransition(0, 0);
                return false;
            }
        });*/
        // Get the search close button image view
        ImageView closeButton = (ImageView)searchView.findViewById(R.id.search_close_btn);
        // Set on click listener
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(200);
                finish();
                overridePendingTransition(0, 0);
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    finish();
                    overridePendingTransition(0, 0);
                }
            }
        });

//        searchView.onActionViewCollapsed();
//        searchView.setFocusable(true);
        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//                getSpots(s);

                Intent intent = new Intent(SearchResultActivity.this, SearchResultActivity.class);
                intent.putExtra("search_result", s);
                startActivity(intent);
                overridePendingTransition(0, 0);

                //todo setSpotsToListView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
//                getSpots(s);
                return false;
            }
        });*/

        return true;
        //return super.onCreateOptionsMenu(menu);
    }

}
