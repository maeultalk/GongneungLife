package com.maeultalk.gongneunglife.test;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maeultalk.gongneunglife.R;

import java.util.ArrayList;

public class YoutubeRecyclerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_recycler_view);

        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        ArrayList<YoutubeList> list = new ArrayList<>();
        /*for (int i=0; i<1; i++) {
//            list.add(String.format("TEXT %d", i)) ;
            list.add(new YoutubeList(String.format("TEXT %d", i), "FTK-mZRjaIs")) ;
        }*/
        list.add(new YoutubeList(String.format("TEXT 1"), "FTK-mZRjaIs")) ;
        list.add(new YoutubeList(String.format("TEXT 2"), "spk7tCFT_s8")) ;
        list.add(new YoutubeList(String.format("TEXT 3"), "R6qL9Bjgeqk")) ;
        list.add(new YoutubeList(String.format("TEXT 4"), "_Dwi_WgZbWE")) ;
        list.add(new YoutubeList(String.format("TEXT 5"), "arTzBrLAUjs")) ;

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.recycler1) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        SimpleTextAdapter adapter = new SimpleTextAdapter(YoutubeRecyclerViewActivity.this, list) ;
        recyclerView.setAdapter(adapter) ;

    }
}
