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
import com.maeultalk.gongneunglife.model.Place;

import java.util.ArrayList;

public class SearchAdapter extends BaseAdapter{

    private static class ViewHolder {

        public TextView mText;
        public TextView mNotice;

    }

    private Context mContext = null;
    private ArrayList<Place> mListData = new ArrayList<Place>();

    public SearchAdapter(Context mContext, ArrayList<Place> mListData) {
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
            convertView = inflater.inflate(R.layout.item_search, null);

            holder.mText = (TextView) convertView.findViewById(R.id.tv_text);
            holder.mNotice = (TextView) convertView.findViewById(R.id.tv_notice);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Place mData = mListData.get(position);

        holder.mText.setText(mData.getName());
        if(SearchSpotActivity.places.get(position).getType().equals("whole")) {
            holder.mNotice.setVisibility(View.VISIBLE);
            holder.mText.setTypeface(null, Typeface.BOLD);
            holder.mText.setPadding(0,0,0,0);
        } else {
            holder.mNotice.setVisibility(View.GONE);
            holder.mText.setTypeface(null, Typeface.NORMAL);
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            int size = Math.round(8 * dm.density);
//            holder.mText.setPadding(size, 0, 0,0);
        }

        // TODO: 08/04/2019 convertView.setOnClickListener 

        return convertView;
    }
}