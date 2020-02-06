package com.maeultalk.gongneunglife.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.RecyclerItem;
import com.maeultalk.gongneunglife.activity.AskActivity;

import java.util.ArrayList;

/**
 * Created by charlie on 2017. 4. 24..
 */

public class InboxRecyclerAdapter extends RecyclerView.Adapter {

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    Context mContext;
    ArrayList<RecyclerItem> mItems;

    public InboxRecyclerAdapter(Context context, ArrayList<RecyclerItem> items){
        mContext = context;
        mItems = items;
    }


    // 새로운 뷰 홀더 생성
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view/* = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view,parent,false)*/;
        RecyclerView.ViewHolder holder;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
            holder = new HeaderViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);
            holder = new ItemViewHolder(view);
        }

//        return new ItemViewHolder(view);
        return holder;
    }


    // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        holder.mNameTv.setText(mItems.get(position).getName());

        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.layout_ask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AskActivity.class);
                    mContext.startActivity(intent);
                }
            });
        } else {
            // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.mNameTv.setText(mItems.get(position-1).getName());
        }

    }

    // 데이터 셋의 크기를 리턴해줍니다.
    @Override
    public int getItemCount() {
        return mItems.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout layout_ask;
        HeaderViewHolder(View headerView) {
            super(headerView);
            layout_ask = (LinearLayout) headerView.findViewById(R.id.layout_ask);
        }
    }

    // 커스텀 뷰홀더
// item layout 에 존재하는 위젯들을 바인딩합니다.
    class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView mNameTv;
        public ItemViewHolder(View itemView) {
            super(itemView);
            mNameTv = (TextView) itemView.findViewById(R.id.itemNameTv);
        }
    }
}
