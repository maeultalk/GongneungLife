package com.maeultalk.gongneunglife.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.adapter.SectionsPagerAdapter;
import com.maeultalk.gongneunglife.request.LoadMyPlaceRequest;
import com.maeultalk.gongneunglife.request.MyOffRequest;
import com.maeultalk.gongneunglife.request.MyOnRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaceActivity extends AppCompatActivity {

    private String placeCode;
    private String placeName;
    private boolean write;

    private String contentId;
    private boolean scrap;

    private FloatingActionButton fab;

    boolean my = false;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        // 액션바 타이틀 스팟이름으로 설정
        Intent intent = getIntent();
        placeCode = intent.getStringExtra("place_code");
        placeName = intent.getStringExtra("place_name");
        write = intent.getBooleanExtra("write", false);
        setTitle(placeName);

        // 액션바 아이템 셋팅


        contentId = intent.getStringExtra("content_id");

        scrap = intent.getBooleanExtra("scrap", false);

        if(write) {

            // 글쓰기 액티비티 호출
            Handler hd = new Handler();
            hd.postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent_write = new Intent(getApplicationContext(), AddContentActivity2.class);
                    intent_write.putExtra("place_code", placeCode);
                    intent_write.putExtra("place_name", placeName);
                    startActivity(intent_write);
                }

            }, 250);


        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddContentActivity2.class);
                intent.putExtra("place_code", placeCode);
                intent.putExtra("place_name", placeName);
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(placeName);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        if(TextUtils.isEmpty(contentId)) {
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fab, placeCode, placeName);
        } else {
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fab, placeCode, placeName, contentId, scrap);
        }



        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(i == 0) {
                    fab.show();
                } else if(i == 1) {
                    fab.hide();
                } else {
                    fab.hide();
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

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
                    my = jsonResponse.getBoolean("my");
                    invalidateOptionsMenu();
                } catch (JSONException e) {

                }

            }
        };
        LoadMyPlaceRequest loadMyPlaceRequest = new LoadMyPlaceRequest(placeCode, user, responseListener);
        RequestQueue queue = Volley.newRequestQueue(PlaceActivity.this);
        queue.add(loadMyPlaceRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_place,menu);

        menu.findItem(R.id.action_favorite).getIcon().mutate();
        menu.findItem(R.id.action_favorite).getIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_favorite:
//                Toast.makeText(this, "첫번째", Toast.LENGTH_SHORT).show();
                if(my) {
                    SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                final JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    /*((NormalHolder) holder).imageView1022.setImageResource(R.drawable.folder_open);
                                    ((NormalHolder) holder).textView_save.setTextColor(Color.parseColor("#808080"));*/
                                    my = false;
                                    invalidateOptionsMenu();
                                } else {
                                    Toast.makeText(PlaceActivity.this, "처리 실패", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                            }

                        }
                    };
                    MyOffRequest myOffRequest = new MyOffRequest(pref.getString("email", ""), placeCode, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(PlaceActivity.this);
                    queue.add(myOffRequest);
                } else {
                    SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                final JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    /*((NormalHolder) holder).imageView1022.setImageResource(R.drawable.fold
                                    er_closed);
                                    ((NormalHolder) holder).textView_save.setTextColor(Color.parseColor("#ffc107"));*/
                                    my = true;
                                    invalidateOptionsMenu();
                                } else {
                                    Toast.makeText(PlaceActivity.this, "처리 실패", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                            }

                        }
                    };
                    MyOnRequest myOnRequest = new MyOnRequest(pref.getString("email", ""), placeCode, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(PlaceActivity.this);
                    queue.add(myOnRequest);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(my) {
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_star_black_24dp);
            menu.findItem(R.id.action_favorite).getIcon().mutate();
            menu.findItem(R.id.action_favorite).getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        } else {
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_star_border_black_24dp);
            menu.findItem(R.id.action_favorite).getIcon().mutate();
            menu.findItem(R.id.action_favorite).getIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        return super.onPrepareOptionsMenu(menu);

    }
}
