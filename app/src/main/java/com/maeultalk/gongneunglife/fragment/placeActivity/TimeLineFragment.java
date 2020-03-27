package com.maeultalk.gongneunglife.fragment.placeActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.adapter.PlaceRecyclerViewAdapter;
import com.maeultalk.gongneunglife.util.RecyclerViewDecoration;
import com.maeultalk.gongneunglife.model.Content;
import com.maeultalk.gongneunglife.request.LoadPlaceContentsRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class TimeLineFragment extends Fragment {

    FloatingActionButton fab;

    private RecyclerView recyclerView;

    private RecyclerView.Adapter adapter;

    private ArrayList<Content> contents = new ArrayList<>();
    public static ArrayList<Content> contentsInPlace = new ArrayList<>();

    String placeCode;
    String placeName;
    String contentId;
    boolean scrap;

    TextView textView_ready;

    boolean mark = false;

    @Override
    public void onResume() {
        super.onResume();

        loadData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        if(getArguments() != null) {
            placeCode = getArguments().getString("place_code");
            placeName = getArguments().getString("place_name");
            contentId = getArguments().getString("content_id");
            scrap = getArguments().getBoolean("scrap");
        }

//        textView_ready = (TextView) view.findViewById(R.id.textView_ready);
//        textView_ready.setText("\"" + placeName + "\"에 첫 게시글을 남겨보세요.");

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(getActivity(), SearchSpotActivity.class);
                intent.putExtra("SearchSpotActivity", "write");
                startActivity(intent);*/
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        loadData();

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
                    contentsInPlace = gson.fromJson(jsonArray.toString(), listType);
//                    Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();

                    if(!TextUtils.isEmpty(contentId)) {

                        Content content = null;
                        boolean first = false;
//                        for (Content c : contentsInPlace) {
                        for (int i=0; i<contentsInPlace.size(); i++) {
                            Content c = contentsInPlace.get(i);
                            if (c.getId().equals(contentId)) {
                                content = c;
                                if(i==0) {
                                    first = true;
                                } else {
                                    first = false;
                                }
                                break;
                            }
                        }

                        if(!first) {
                            contentsInPlace.add(0, content);
                        }

                        mark = true;

                    } else {
                        mark = false;
                    }

                    updateUI();
                } catch (JSONException e) {

                }

            }
        };

        LoadPlaceContentsRequest loadPlaceContentsRequest = new LoadPlaceContentsRequest(placeCode, user, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(loadPlaceContentsRequest);

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
        adapter = new PlaceRecyclerViewAdapter(getActivity(), false, contents, mark, scrap);
        recyclerView.setAdapter(adapter);

        // 다양한 LayoutManager 가 있습니다. 원하시는 방법을 선택해주세요.
        // 지그재그형의 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        // 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        // 가로 또는 세로 스크롤 목록 형식
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new RecyclerViewDecoration(getActivity(), 12));

//        setData();
    }

}
