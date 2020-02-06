package com.maeultalk.gongneunglife.fragment.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maeultalk.gongneunglife.adapter.InboxAdapter;
import com.maeultalk.gongneunglife.activity.AnswerActivity2;
import com.maeultalk.gongneunglife.model.InboxItem;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.activity.AddPlaceActivity;
import com.maeultalk.gongneunglife.activity.AskActivity;
import com.maeultalk.gongneunglife.activity.SettingsActivity;
import com.maeultalk.gongneunglife.request.ClearBadgeCountRequest;
import com.maeultalk.gongneunglife.request.LoadInboxRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class InboxFragment2Copy extends Fragment {

    ListView listview;
    LinearLayout layout_ask;

    InboxAdapter inboxAdapter;

    FloatingActionButton fab;

    EditText editText;
    View header;

    public static ArrayList<InboxItem> inboxItems = new ArrayList<>();

    private ListViewAdapter mAdapter = null;

    public InboxFragment2Copy() {
        // Required empty public constructor
    }

    public static InboxFragment2Copy newInstance() {
        InboxFragment2Copy fragment = new InboxFragment2Copy();
        return fragment;
    }

    public void move() {
        listview.smoothScrollToPosition(0);
    }

    @Override
    public void onResume() {
        super.onResume();

        editText.requestFocus();

        loadData();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            //do when hidden
        } else {
            //do when show
            editText.requestFocus();
        }
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
                Intent intent = new Intent(getActivity(), AnswerActivity2.class);
                startActivity(intent);
            }
        });

        listview = (ListView)view.findViewById(R.id.listView);

        header = getLayoutInflater().inflate(R.layout.header, null, false);
        SharedPreferences pref = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        String nick = pref.getString("nick", "");
        TextView textView_nick = (TextView) header.findViewById(R.id.textView_nick);
        textView_nick.setText(nick);
        editText = (EditText) header.findViewById(R.id.editText);
        editText.requestFocus();
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AnswerActivity2.class);
                startActivity(intent);
            }
        });

        /*editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getActivity(), AnswerActivity2.class);
                startActivity(intent);
                return true;

                *//*switch (event.getAction()) {
                    *//**//*case ACTION_MOVE :
                        editText3.
                        return false;*//**//*
                    case ACTION_DOWN:
                        header.dispatchTouchEvent(event);
                        //Toast.makeText(getApplicationContext(), "다운", Toast.LENGTH_SHORT).show();
                        return true;
                    case ACTION_UP:
                        header.dispatchTouchEvent(event);
                        //Toast.makeText(getApplicationContext(), "업", Toast.LENGTH_SHORT).show();
                        return false;
                    default:
                        return false;
                }*//*

            }
        });*/
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AnswerActivity2.class);
                startActivity(intent);
            }
        });

        listview.addHeaderView(header) ;


        // TODO: 22/05/2019 서버에서 데이터 가져오기
        loadData();





        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                //클릭한 아이템의 문자열을 가져옴
//                String selected_item = (String)adapterView.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), AnswerActivity2.class);
                intent.putExtra("subject", inboxItems.get(position-1).getSubject());
                intent.putExtra("id", inboxItems.get(position-1).getId());
                startActivity(intent);

                // TODO: 13/06/2019 뱃지 카운트 0으로
                // 뱃지 카운트 0으로
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                };
                ClearBadgeCountRequest clearBadgeCountRequest = new ClearBadgeCountRequest(inboxItems.get(position-1).getId(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(clearBadgeCountRequest);

            }
        });

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

    void loadData() {

        SharedPreferences pref = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        String user = pref.getString("email", "");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonResponse.get("inbox");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<InboxItem>>(){}.getType();
                    inboxItems = gson.fromJson(jsonArray.toString(), listType);
                    updateUI();
                    /*InboxAdapter inboxAdapter = new InboxAdapter(getActivity(), inboxItems);
                    listview.setAdapter(inboxAdapter);*/
                } catch (JSONException e) {

                }

            }
        };
        LoadInboxRequest loadInboxRequest = new LoadInboxRequest(user, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(loadInboxRequest);

    }

    private void updateUI() {
        if(inboxAdapter == null){
            setListView();
        }else{
            inboxAdapter.notifyDataSetChanged();
        }
    }

    private void setListView(){
        inboxAdapter = new InboxAdapter(getActivity(), inboxItems);
        listview.setAdapter(inboxAdapter);
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
            startActivityForResult(intent, 400);
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

//            InboxItem mData = mListData.get(position);

//            holder.mText.setText(mData.getText());
            holder.mDate.setText(inboxItems.get(position-1).getBadge());
            if(inboxItems.get(position-1).getBadge().equals("0")) {
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
