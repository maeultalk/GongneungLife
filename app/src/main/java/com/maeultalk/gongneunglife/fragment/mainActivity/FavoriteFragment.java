package com.maeultalk.gongneunglife.fragment.mainActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.RecyclerAdapter;
import com.maeultalk.gongneunglife.RecyclerItem;
import com.maeultalk.gongneunglife.activity.PlaceActivity;
import com.maeultalk.gongneunglife.activity.SettingsActivity;
import com.maeultalk.gongneunglife.adapter.FavoriteAdapter;
import com.maeultalk.gongneunglife.model.Favorite;
import com.maeultalk.gongneunglife.request.LoadFavoriteRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;

    private String[] names = {"Charlie","Andrew","Han","Liz","Thomas","Sky","Andy","Lee","Park"};
    private RecyclerView.Adapter adapter;
    public static ArrayList<Favorite> mItems = new ArrayList<>();

    ListView listView;
    FavoriteAdapter favoriteAdapter;

    View header;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    public static FavoriteFragment newInstance() {
        FavoriteFragment fragment = new FavoriteFragment();
        return fragment;
    }

    public void move() {
        listView.smoothScrollToPosition(0);
    }

    @Override
    public void onResume() {
        super.onResume();

        loadData();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);



//        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        listView = (ListView) view.findViewById(R.id.listView);

        header = getLayoutInflater().inflate(R.layout.header_favorite, null, false);
        listView.addHeaderView(header) ;

//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        mItems.add(new Favorite());
//        favoriteAdapter = new FavoriteAdapter(getActivity(), mItems);
//        listView.setAdapter(favoriteAdapter);

//        setRecyclerView();

        loadData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), PlaceActivity.class);
                intent.putExtra("place_code", mItems.get(i-1).getPlace_code());
                intent.putExtra("place_name", mItems.get(i-1).getPlace_name());
                if(mItems.get(i-1).getType().equals("content")) {
                    intent.putExtra("content_id", mItems.get(i-1).getContent_id());
                    intent.putExtra("scrap", true);
                }
                startActivity(intent);
            }
        });

        return view;
    }

    void loadData() {

        SharedPreferences pref = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        String user = pref.getString("email", "");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonResponse.get("favorites");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Favorite>>(){}.getType();
                    mItems = gson.fromJson(jsonArray.toString(), listType);
                    updateUI();
                    /*InboxAdapter inboxAdapter = new InboxAdapter(getActivity(), inboxItems);
                    listview.setAdapter(inboxAdapter);*/
                } catch (JSONException e) {

                }

            }
        };
        LoadFavoriteRequest loadFavoriteRequest = new LoadFavoriteRequest(user, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(loadFavoriteRequest);

    }

    private void updateUI() {
        if(favoriteAdapter == null){
            setListView();
        }else{
            favoriteAdapter.notifyDataSetChanged();
        }
    }

    private void setListView(){
        favoriteAdapter = new FavoriteAdapter(getActivity(), mItems);
        listView.setAdapter(favoriteAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_scrap, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }

    /*private void setRecyclerView(){
        // 각 Item 들이 RecyclerView 의 전체 크기를 변경하지 않는 다면
        // setHasFixedSize() 함수를 사용해서 성능을 개선할 수 있습니다.
        // 변경될 가능성이 있다면 false 로 , 없다면 true를 설정해주세요.
        recyclerView.setHasFixedSize(true);

        // RecyclerView에 Adapter를 설정해줍니다.
        adapter = new RecyclerAdapter(mItems);
        recyclerView.setAdapter(adapter);

        // 다양한 LayoutManager 가 있습니다. 원하시는 방법을 선택해주세요.
        // 지그재그형의 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        // 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        // 가로 또는 세로 스크롤 목록 형식
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setData();
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
    }*/

}
