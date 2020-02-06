package com.maeultalk.gongneunglife.fragment.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.maeultalk.gongneunglife.adapter.InboxAdapter;
import com.maeultalk.gongneunglife.model.InboxItem;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.activity.AddPlaceActivity;
import com.maeultalk.gongneunglife.activity.AnswerActivity;
import com.maeultalk.gongneunglife.activity.AskActivity;
import com.maeultalk.gongneunglife.activity.SettingsActivity;

import java.util.ArrayList;

public class InboxFragment2 extends Fragment {

    ListView listview;
    LinearLayout layout_ask;

    FloatingActionButton fab;

    public static ArrayList<InboxItem> inboxItems = new ArrayList<>();

    private ListViewAdapter mAdapter = null;

    public InboxFragment2() {
        // Required empty public constructor
    }

    public static InboxFragment2 newInstance() {
        InboxFragment2 fragment = new InboxFragment2();
        return fragment;
    }

    public void move() {
        listview.smoothScrollToPosition(0);
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
        View view = inflater.inflate(R.layout.fragment_inbox2, container, false);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AskActivity.class);
                startActivity(intent);
            }
        });

        listview = (ListView)view.findViewById(R.id.listView);

        View header = getLayoutInflater().inflate(R.layout.header, null, false) ;
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AskActivity.class);
                startActivity(intent);
            }
        });

        listview.addHeaderView(header) ;

        for (int i=0; i<4; i++) {
            inboxItems.add(new InboxItem("'user'님에게 꼭맞는 동네정보를 추천해드립니다.", "1"));
            inboxItems.add(new InboxItem("공릉동에 SC제일은행 있나요?", "999+"));
            inboxItems.add(new InboxItem("도깨비 시장 근처 분위기 좋은 술집 있나요?도깨비 시장 근처 분위기 좋은 술집 있나요?도깨비 시장 근처 분위기 좋은 술집 있나요?도깨비 시장 근처 분위기 좋은 술집 있나요?", "0"));
            inboxItems.add(new InboxItem("도깨비 시장 근처 분위기 좋은 술집 있나요?도깨비 시장 근처 분위기 좋은 술집 있나요?도깨비 시장 근처 분위기 좋은 술집 있나요?도깨비 시장 근처 분위기 좋은 술집 있나요?", "2"));
//            mAdapter.addItem("공릉동에 SC제일은행 있나요?", "999+");
//            mAdapter.addItem("도깨비 시장 근처 분위기 좋은 술집 있나요?", "0");
//            mAdapter.addItem("도깨비 시장 근처 분위기 좋은 술집 있나요?도깨비 시장 근처 분위기 좋은 술집 있나요?도깨비 시장 근처 분위기 좋은 술집 있나요?도깨비 시장 근처 분위기 좋은 술집 있나요?", "2");
        }


        InboxAdapter inboxAdapter = new InboxAdapter(getActivity(), inboxItems);
        /*for (int i=0; i<4; i++) {
            inboxAdapter.addItem("'user'님에게 꼭맞는 동네정보를 추천해드립니다.", "1");
            inboxAdapter.addItem("공릉동에 SC제일은행 있나요?", "999+");
            inboxAdapter.addItem("도깨비 시장 근처 분위기 좋은 술집 있나요?", "0");
            inboxAdapter.addItem("도깨비 시장 근처 분위기 좋은 술집 있나요?도깨비 시장 근처 분위기 좋은 술집 있나요?도깨비 시장 근처 분위기 좋은 술집 있나요?도깨비 시장 근처 분위기 좋은 술집 있나요?", "2");
        }*/



        listview.setAdapter(inboxAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                //클릭한 아이템의 문자열을 가져옴
//                String selected_item = (String)adapterView.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), AnswerActivity.class);
                startActivity(intent);

            }
        });

        /*mAdapter = new ListViewAdapter(getActivity());
        listview.setAdapter(mAdapter);

        for (int i=0; i<4; i++) {
            mAdapter.addItem("'user'님에게 꼭맞는 동네정보를 추천해드립니다.", "1");
            mAdapter.addItem("공릉동에 SC제일은행 있나요?", "999+");
            mAdapter.addItem("도깨비 시장 근처 분위기 좋은 술집 있나요?", "0");
            mAdapter.addItem("도깨비 시장 근처 분위기 좋은 술집 있나요?도깨비 시장 근처 분위기 좋은 술집 있나요?도깨비 시장 근처 분위기 좋은 술집 있나요?도깨비 시장 근처 분위기 좋은 술집 있나요?", "2");
        }*/


//        inboxItems.add(new InboxItem("'user'님에게 꼭맞는 동네정보를 추천해드립니다.", "1"));


        /*//데이터를 저장하게 되는 리스트
        List<String> list = new ArrayList<>();

        //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, list);

        //리스트뷰의 어댑터를 지정해준다.
        listview.setAdapter(adapter);


        //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                //클릭한 아이템의 문자열을 가져옴
//                String selected_item = (String)adapterView.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), AnswerActivity.class);
                startActivity(intent);

            }
        });

        //리스트뷰에 보여질 아이템을 추가
        list.add("'user'님에게 꼭맞는 동네정보를 추천해드립니다.");
        list.add("공릉동에 SC제일은행 있나요?");
        list.add("도깨비 시장 근처 분위기 좋은 술집 있나요?");
        list.add("공릉동에 SC제일은행 있나요?");
        list.add("도깨비 시장 근처 분위기 좋은 술집 있나요?");
        list.add("공릉동에 SC제일은행 있나요?");
        list.add("도깨비 시장 근처 분위기 좋은 술집 있나요?");
        list.add("공릉동에 SC제일은행 있나요?");
        list.add("도깨비 시장 근처 분위기 좋은 술집 있나요?");
        list.add("공릉동에 SC제일은행 있나요?");
        list.add("도깨비 시장 근처 분위기 좋은 술집 있나요?");
        list.add("공릉동에 SC제일은행 있나요?");
        list.add("도깨비 시장 근처 분위기 좋은 술집 있나요?");
        list.add("공릉동에 SC제일은행 있나요?");
        list.add("도깨비 시장 근처 분위기 좋은 술집 있나요?");
        list.add("공릉동에 SC제일은행 있나요?");
        list.add("도깨비 시장 근처 분위기 좋은 술집 있나요?");
        list.add("공릉동에 SC제일은행 있나요?");
        list.add("도깨비 시장 근처 분위기 좋은 술집 있나요?");
        list.add("공릉동에 SC제일은행 있나요?");
        list.add("도깨비 시장 근처 분위기 좋은 술집 있나요?");
        list.add("공릉동에 SC제일은행 있나요?");
        list.add("도깨비 시장 근처 분위기 좋은 술집 있나요?");*/

        layout_ask = (LinearLayout) view.findViewById(R.id.layout_ask);
        layout_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AskActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    void getData() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_inbox, menu);
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
            startActivity(intent);
            return true;
        } else if (id == R.id.action_add_place) {
            Intent intent = new Intent(getActivity(), AddPlaceActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class ViewHolder {

        public TextView mText;

        public TextView mDate;
    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<InboxItem> mListData = new ArrayList<InboxItem>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItem(String mTitle, String mDate){
            InboxItem addInfo = null;
            addInfo = new InboxItem();
            addInfo.setSubject(mTitle);
            addInfo.setBadge(mDate);

            mListData.add(addInfo);
        }

        public void remove(int position){
            mListData.remove(position);
            dataChange();
        }

        public void dataChange(){
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_inbox, null);

                holder.mText = (TextView) convertView.findViewById(R.id.tv_text);
                holder.mDate = (TextView) convertView.findViewById(R.id.tv_count);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            InboxItem mData = mListData.get(position);

//            holder.mText.setText(mData.getText());
            holder.mDate.setText(mData.getBadge());
            if(inboxItems.get(position).getBadge().equals("0")) {
                holder.mDate.setVisibility(View.INVISIBLE);
            } else {
                holder.mDate.setVisibility(View.VISIBLE);
            }

            /*if(mData.getCount().equals("0")) {
                holder.mDate.setVisibility(View.INVISIBLE);
            } else {
//                holder.mDate.setText(myItem.getCount());
            }*/

            return convertView;
        }
    }

}
