package com.maeultalk.gongneunglife.fragment.mainActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.RecyclerItem;
import com.maeultalk.gongneunglife.activity.CollectActivity;
import com.maeultalk.gongneunglife.adapter.RecyclerViewAdapter;
import com.maeultalk.gongneunglife.model.Collect;
import com.maeultalk.gongneunglife.util.RecyclerViewDecoration;
import com.maeultalk.gongneunglife.activity.AddPlaceActivity;
import com.maeultalk.gongneunglife.activity.SearchSpotActivity;
import com.maeultalk.gongneunglife.activity.SettingsActivity;
import com.maeultalk.gongneunglife.model.Content;
import com.maeultalk.gongneunglife.request.LoadContentsRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    FloatingActionButton fab;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private String[] names = {"Charlie","Andrew","Han","Liz","Thomas","Sky","Andy","Lee","Park"};
    private RecyclerView.Adapter adapter;
    private ArrayList<RecyclerItem> mItems = new ArrayList<>();

    public static ArrayList<Content> contentsInHome = new ArrayList<>();
    public static ArrayList<Collect> collects = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public void move() {
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onResume() {
        super.onResume();

//        loadData();
        updateUI();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchSpotActivity.class);
                intent.putExtra("SearchSpotActivity", "write");
                startActivity(intent);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter = null;
                updateUI();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        // 다양한 LayoutManager 가 있습니다. 원하시는 방법을 선택해주세요.
        // 지그재그형의 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        // 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        // 가로 또는 세로 스크롤 목록 형식
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new RecyclerViewDecoration(getActivity(), 12));

//        loadData();

        return view;
    }

    private void loadData() {

        SharedPreferences pref = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        String user = pref.getString("email", "");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonResponse.get("contents");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Content>>(){}.getType();
                    contentsInHome = gson.fromJson(jsonArray.toString(), listType);
//                    Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();

                    final JSONObject jsonResponse2 = new JSONObject(response);
                    JSONArray jsonArray2 = (JSONArray) jsonResponse2.get("collects");
                    Gson gson2 = new Gson();
                    Type listType2 = new TypeToken<ArrayList<Collect>>(){}.getType();
                    collects = gson2.fromJson(jsonArray2.toString(), listType2);

//                    updateUI();
                    setRecyclerView();
                } catch (JSONException e) {

                }

            }
        };

        LoadContentsRequest loadContentsRequest = new LoadContentsRequest(user, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(loadContentsRequest);

    }

    private void updateUI() {
        if(adapter == null){
//            setRecyclerView();
            loadData();
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    private void setRecyclerView(){

//        loadData();

        // 각 Item 들이 RecyclerView 의 전체 크기를 변경하지 않는 다면
        // setHasFixedSize() 함수를 사용해서 성능을 개선할 수 있습니다.
        // 변경될 가능성이 있다면 false 로 , 없다면 true를 설정해주세요.
        recyclerView.setHasFixedSize(true);

        // RecyclerView에 Adapter를 설정해줍니다.
        adapter = new RecyclerViewAdapter(getActivity(), HomeFragment.this, true, contentsInHome);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setRefreshing(false);

//        setData();
    }

    private void setData(){
        mItems.clear();
        // RecyclerView 에 들어갈 데이터를 추가합니다.
        for(String name : names){
            mItems.add(new RecyclerItem(name));
            mItems.add(new RecyclerItem(name));
        }
        // 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행합니다.
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);

//        MenuItem searchMenuItem = menu.findItem( R.id.action_search );
//        searchMenuItem.expandActionView();

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivityForResult(intent, 400);
            return true;
        } else if (id == R.id.action_refresh) {
//            Toast.makeText(getActivity(), "준비중입니다.", Toast.LENGTH_SHORT).show();

//            swipeRefreshLayout.setRefreshing(true);
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
            adapter = null;
            updateUI();

            /*final AppCompatDialog progressDialog;
            progressDialog = new AppCompatDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.progress_loading);
            progressDialog.show();

            SharedPreferences pref = getActivity().getSharedPreferences("user", MODE_PRIVATE);
            String user = pref.getString("email", "");

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        final JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = (JSONArray) jsonResponse.get("contents");
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<Content>>(){}.getType();
                        contentsInHome = gson.fromJson(jsonArray.toString(), listType);
//                    Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();

//                        updateUI();

                        // RecyclerView에 Adapter를 설정해줍니다.
                        adapter = new RecyclerViewAdapter(getActivity(), true, contentsInHome);
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {

                    }
                    progressDialog.dismiss();
                }
            };

            LoadContentsRequest loadContentsRequest = new LoadContentsRequest(user, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(loadContentsRequest);*/

            return true;
        } else if (id == R.id.action_add_place) {
            Intent intent = new Intent(getActivity(), AddPlaceActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_search) {
            Intent intent = new Intent(getActivity(), SearchSpotActivity.class);
            intent.putExtra("SearchSpotActivity", "search");
            startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
