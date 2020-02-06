package com.maeultalk.gongneunglife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maeultalk.gongneunglife.activity.ListviewStudyActivity;
import com.maeultalk.gongneunglife.activity.MainActivity;

public class CustomAdapterListview extends BaseAdapter {
    private String[] itemValues;
    private LayoutInflater inflater;
    public ViewHolder holder=null;
    Context context;
    public CustomAdapterListview(Context context, String[] values)
    {
        this.context=context;
        this.itemValues=values;
        this.inflater=LayoutInflater.from(context);
    }
    public int getCount() {
        return itemValues.length;
    }
    @Override
    public Object getItem(int position)
    {
        return position;
    }
    @Override
    public long getItemId(int position)
    {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView==null)
        {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_for_listview, null);

//            convertView=inflater.inflate(R.layout.layout_for_listview,null);
            holder=new ViewHolder();
            holder.tickMark=(ImageView) convertView.findViewById(R.id.tick_mark);
            holder.itemDataView=(TextView)convertView.findViewById(R.id.items);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)convertView.getTag();
        }
        holder.itemDataView.setText(itemValues[position]);
        if(ListviewStudyActivity.tickMarkVisibileListPosition[position]==Boolean.TRUE)
            /*Everytime adapter check whether the imageview at particular position have to be made visible or not.
             */
        {
            holder.tickMark.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.tickMark.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }
    static class ViewHolder
    {
        TextView itemDataView;
        ImageView tickMark;
    }
}