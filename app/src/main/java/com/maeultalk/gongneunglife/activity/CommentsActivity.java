package com.maeultalk.gongneunglife.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.adapter.CommentsAdapter;
import com.maeultalk.gongneunglife.model.CommentModel;
import com.maeultalk.gongneunglife.model.Content;
import com.maeultalk.gongneunglife.request.AddCommentRequest;
import com.maeultalk.gongneunglife.request.LoadCommentsRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.maeultalk.gongneunglife.activity.CollectActivity.contentsInCollect;
import static com.maeultalk.gongneunglife.fragment.mainActivity.HomeFragment.contentsInHome;
import static com.maeultalk.gongneunglife.fragment.placeActivity.TimeLineFragment.contentsInPlace;

public class CommentsActivity extends AppCompatActivity {

    LinearLayout layout;

    ArrayList<CommentModel> comments = new ArrayList<>();
    ListView listView;
//    ArrayAdapter<String> adapter;
    CommentsAdapter commentsAdapter;
    EditText editText_comment;
    Button button_comment;

    String id;
    String content;

    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("댓글");

        // 게시물 번호
        Intent intent = getIntent();
        //Toast.makeText(getApplicationContext(), intent.getStringExtra("id"), Toast.LENGTH_SHORT).show();

        layout = (LinearLayout) findViewById(R.id.layout);

        listView = (ListView) findViewById(R.id.listView_comments) ;

        id = intent.getStringExtra("id");
        content = intent.getStringExtra("content");
        setTitle(content);

        from = intent.getStringExtra("from");

        getComments(id);

        editText_comment = (EditText) findViewById(R.id.editText_comment);
        editText_comment.setOnFocusChangeListener(new View.OnFocusChangeListener()
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
                            try{
                                listView.setSelection(commentsAdapter.getCount() - 1);
                            } catch (Exception e) {

                            }
                        }
                    }, 100);
                }
            }
        });

        button_comment = (Button) findViewById(R.id.button_comment);
        button_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);

                if(!(editText_comment.getText().toString().trim().equals(""))) {
                    // 댓글이 있을때,
                    button_comment.setEnabled(false);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                final JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if(success) {
                                    editText_comment.setText("");
                                    button_comment.setEnabled(true);
                                    getComments(id);

                                    // TODO: 18/07/2019 메인 게시물에 댓글 카운트 증가
                                    if(from.equals("home")) {
                                        for (int i = 0; i < contentsInHome.size(); i++) {
                                            Content c = contentsInHome.get(i);
                                            if (c.getId().equals(id)) {
                                                contentsInHome.get(i).setComments(String.valueOf(Integer.valueOf(c.getComments()) + 1));
                                                break;
                                            }
                                        }
                                    } else if(from.equals("place")) {
                                        for (int i = 0; i < contentsInPlace.size(); i++) {
                                            Content c = contentsInPlace.get(i);
                                            if (c.getId().equals(id)) {
                                                contentsInPlace.get(i).setComments(String.valueOf(Integer.valueOf(c.getComments()) + 1));
                                                break;
                                            }
                                        }
                                    } else if(from.equals("collect")) {
                                        for (int i = 0; i < contentsInCollect.size(); i++) {
                                            Content c = contentsInCollect.get(i);
                                            if (c.getId().equals(id)) {
                                                contentsInCollect.get(i).setComments(String.valueOf(Integer.valueOf(c.getComments()) + 1));
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "처리실패", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                            }

                        }
                    };
                    AddCommentRequest addCommentRequest = new AddCommentRequest(pref.getString("email", ""), id, editText_comment.getText().toString(), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(addCommentRequest);
                } else {
                    // 댓글이 없을때,
                    Toast.makeText(getApplicationContext(), "댓글을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    void getComments(String ContentId) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonResponse.get("comments");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<CommentModel>>(){}.getType();
                    comments = gson.fromJson(jsonArray.toString(), listType);

                    String[] array = new String[comments.size()];
                    for(int i = 0; i< comments.size() ; i++) {
                        array[i] = comments.get(i).getComment();
                    }

                    commentsAdapter = new CommentsAdapter(CommentsActivity.this, id, comments);
//                    adapter = new ArrayAdapter<String>(CommentsActivity.this, android.R.layout.simple_list_item_1, array);

                    listView.setAdapter(commentsAdapter);
                    listView.setSelection(commentsAdapter.getCount() - 1);

                } catch (JSONException e) {

                }

            }
        };
        LoadCommentsRequest loadCommentsRequest = new LoadCommentsRequest(ContentId, responseListener);
        RequestQueue queue = Volley.newRequestQueue(CommentsActivity.this);
        queue.add(loadCommentsRequest);

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
