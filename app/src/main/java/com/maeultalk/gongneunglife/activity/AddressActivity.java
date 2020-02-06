package com.maeultalk.gongneunglife.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.adapter.SearchAdapter2;
import com.maeultalk.gongneunglife.model.Place;
import com.maeultalk.gongneunglife.request.SearchPlacesRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AddressActivity extends AppCompatActivity {

    ListView listView;

    public static ArrayList<Place> places = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        listView = (ListView)findViewById(R.id.listView);

        getSpots("");
    }

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

                    SearchAdapter2 searchAdapter = new SearchAdapter2(getApplicationContext(), places);

                    listView.setAdapter(searchAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(AddressActivity.this, Address2Activity.class);
                            intent.putExtra("place_code", places.get(i).getCode());
                            intent.putExtra("place_name", places.get(i).getName());
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

}
