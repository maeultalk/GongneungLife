package com.maeultalk.gongneunglife.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.activity.SearchSpotActivity;
import com.maeultalk.gongneunglife.model.Content;
import com.maeultalk.gongneunglife.model.Place;

import java.util.ArrayList;

public class SearchContentsAdapter extends BaseAdapter{

    private static class ViewHolder {

        public TextView textView_place;
        public TextView textView_content;

    }

    private Context mContext = null;
    private ArrayList<Content> mListData = new ArrayList<Content>();

    public SearchContentsAdapter(Context mContext, ArrayList<Content> mListData) {
        super();
        this.mContext = mContext;
        this.mListData = mListData;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_search_content, null);

            holder.textView_place = (TextView) convertView.findViewById(R.id.textView_place);
            holder.textView_content = (TextView) convertView.findViewById(R.id.textView_content);

            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        Content mData = mListData.get(position);

        holder.textView_place.setText(mData.getPlace_name());
        holder.textView_content.setText(mData.getContent());

        // TODO: 08/04/2019 convertView.setOnClickListener 

        return convertView;
    }
}