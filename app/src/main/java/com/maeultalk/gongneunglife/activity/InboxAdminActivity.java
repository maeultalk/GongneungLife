package com.maeultalk.gongneunglife.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.adapter.UserAdapter;
import com.maeultalk.gongneunglife.model.User;
import com.maeultalk.gongneunglife.request.LoadUserRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class InboxAdminActivity extends AppCompatActivity {

    ListView listView;
    UserAdapter adapter;
    ArrayList<User> users = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_admin);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                /*Intent intent = new Intent(MainActivity.this, InboxActivity.class);
                intent.putExtra("email", users.get(position).getEmail());
                startActivity(intent);*/

            }
        });

//        loadData();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonResponse.get("users");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<User>>() {}.getType();
                    users = gson.fromJson(jsonArray.toString(), listType);
                    updateUI();
                } catch (JSONException e) {

                }

            }
        };
        LoadUserRequest testRequest = new LoadUserRequest("", responseListener);
        RequestQueue queue = Volley.newRequestQueue(InboxAdminActivity.this);
        queue.add(testRequest);

    }

    private void loadData() {

        /*Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonResponse.get("users");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<User>>(){}.getType();
                    users = gson.fromJson(jsonArray.toString(), listType);
                    updateUI();
                    *//*InboxAdapter inboxAdapter = new InboxAdapter(getActivity(), inboxItems);
                    listview.setAdapter(inboxAdapter);*//*
                } catch (JSONException e) {

                }

            }
        };*/
        LoadUserRequest loadUserRequest = new LoadUserRequest("asdf", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonResponse.get("users");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<User>>(){}.getType();
                    users = gson.fromJson(jsonArray.toString(), listType);
                    updateUI();
                    /*InboxAdapter inboxAdapter = new InboxAdapter(getActivity(), inboxItems);
                    listview.setAdapter(inboxAdapter);*/
                } catch (JSONException e) {

                }

            }
        });
        RequestQueue queue = Volley.newRequestQueue(InboxAdminActivity.this);
        queue.add(loadUserRequest);

    }

    private void updateUI() {
        if(adapter == null){
            setListView();
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    private void setListView(){
        adapter = new UserAdapter(InboxAdminActivity.this, users);
        listView.setAdapter(adapter);
    }

}
