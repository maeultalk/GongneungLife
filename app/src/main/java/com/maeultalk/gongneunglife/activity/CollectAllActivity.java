package com.maeultalk.gongneunglife.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.adapter.CollectAllAdapter;
import com.maeultalk.gongneunglife.model.CollectAllItem;
import com.maeultalk.gongneunglife.util.GridSpacingItemDecoration;
import com.maeultalk.gongneunglife.util.Util;

import java.util.ArrayList;

public class CollectAllActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CollectAllAdapter adapter;
    ArrayList<CollectAllItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_all);
        setTitle("테마별 정보");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(2, new Util().convertDpToPx(CollectAllActivity.this, 16), true);
        recyclerView.addItemDecoration(gridSpacingItemDecoration);

        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        items = new ArrayList<>();

        /*for (int i = 0; i < 15; i++) {
            items.add(new CollectAllItem("족발"));
        }*/

        adapter = new CollectAllAdapter(CollectAllActivity.this, items);
        recyclerView.setAdapter(adapter);
    }
}
