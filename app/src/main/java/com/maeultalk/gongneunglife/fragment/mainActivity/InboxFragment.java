package com.maeultalk.gongneunglife.fragment.mainActivity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.maeultalk.gongneunglife.adapter.InboxRecyclerAdapter;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.RecyclerItem;

import java.util.ArrayList;

public class InboxFragment extends Fragment {

    LinearLayout layout_ask;

    private RecyclerView recyclerView;

    private String[] names = {"공릉동에 SC제일은행 있나요?","도깨비 시장 근처 분위기 좋은 술집 있나요?","Han","Liz","Thomas","Sky","Andy","Lee","Park"};
    private RecyclerView.Adapter adapter;
    private ArrayList<RecyclerItem> mItems = new ArrayList<>();

    public InboxFragment() {
        // Required empty public constructor
    }

    public static InboxFragment newInstance() {
        InboxFragment fragment = new InboxFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        /*layout_ask = (LinearLayout) view.findViewById(R.id.layout_ask);
        layout_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AskActivity.class);
                startActivity(intent);
            }
        });*/

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        setRecyclerView();

        return view;
    }

    private void setRecyclerView(){
        // 각 Item 들이 RecyclerView 의 전체 크기를 변경하지 않는 다면
        // setHasFixedSize() 함수를 사용해서 성능을 개선할 수 있습니다.
        // 변경될 가능성이 있다면 false 로 , 없다면 true를 설정해주세요.
        recyclerView.setHasFixedSize(true);

        // RecyclerView에 Adapter를 설정해줍니다.
        adapter = new InboxRecyclerAdapter(getActivity(), mItems);
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
    }

}
