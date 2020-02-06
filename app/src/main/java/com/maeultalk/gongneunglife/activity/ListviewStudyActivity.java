package com.maeultalk.gongneunglife.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.maeultalk.gongneunglife.CustomAdapterListview;
import com.maeultalk.gongneunglife.R;

public class ListviewStudyActivity extends AppCompatActivity {

    public String[] itemValueArray = {"item 1", "item 2", "item 3", "item 4", "item 5", "item 6", "item 7", "item 8", "item 9", "item 10", "item 11", "item 12", "item 13", "item 14", "item 15", "item 16", "item 17", "item 18", "item 19", "item 20"};
    public static boolean[] tickMarkVisibileListPosition = new boolean[20];
    //Allocating size for the Boolean set all element to Boolean.false by default. It is defined static, so that the same variable can be accessed from adapter class without creating object for MainActivity.Java.
    public ListView listviewMain;
    public int listPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_study);
        listviewMain = (ListView) findViewById(R.id.listview_for_layout);

        CustomAdapterListview myadapter = new CustomAdapterListview(getApplicationContext(), itemValueArray);
        listviewMain.setAdapter(myadapter);

        listviewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPosition = position - listviewMain.getFirstVisiblePosition();
                if (listviewMain.getChildAt(listPosition).findViewById(R.id.tick_mark).getVisibility() == View.INVISIBLE) {
                    listviewMain.getChildAt(listPosition).findViewById(R.id.tick_mark).setVisibility(View.VISIBLE);
                    tickMarkVisibileListPosition[position] = Boolean.TRUE;
                } else {
                    listviewMain.getChildAt(listPosition).findViewById(R.id.tick_mark).setVisibility(View.INVISIBLE);
                    tickMarkVisibileListPosition[position] = Boolean.FALSE;
                }
                listviewMain.getChildAt(listPosition).setSelected(true);
            }
        });
    }
}