package com.maeultalk.gongneunglife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

//import com.maeultalk.gongneunglife.fragment.mainActivity.InboxFragment2;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.fragment.mainActivity.InboxFragment2Copy;
import com.maeultalk.gongneunglife.model.InboxItem;

import java.util.ArrayList;

import static com.maeultalk.gongneunglife.fragment.mainActivity.InboxFragment2Copy.inboxItems;

public class InboxAdapter extends BaseAdapter{

    private static class ViewHolder {

        public TextView mText;

        public TextView mDate;
    }

    private Context mContext = null;
//    private ArrayList<InboxItem> mListData = new ArrayList<InboxItem>();

    public InboxAdapter(Context mContext, ArrayList<InboxItem> mListData) {
        super();
        this.mContext = mContext;
//        this.mListData = mListData;
    }

    @Override
    public int getCount() {
        return inboxItems.size();
    }

    @Override
    public Object getItem(int position) {
        return inboxItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*public void addItem(String mTitle, String mDate){
        InboxItem addInfo = null;
        addInfo = new InboxItem();
        addInfo.setSubject(mTitle);
        addInfo.setBadge(mDate);

        mListData.add(addInfo);
    }*/


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

//        InboxItem mData = mListData.get(position);

        holder.mText.setText(inboxItems.get(position).getSubject());
        holder.mDate.setText(inboxItems.get(position).getBadge());
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