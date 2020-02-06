package com.maeultalk.gongneunglife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.model.User;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter{

    private static class ViewHolder {
        public TextView textView_nick;
        public TextView textView_email;
    }

    private Context mContext = null;
    private ArrayList<User> users = new ArrayList<User>();

    public UserAdapter(Context mContext, ArrayList<User> users) {
        super();
        this.mContext = mContext;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
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
            convertView = inflater.inflate(R.layout.item_user, null);

            holder.textView_nick = (TextView) convertView.findViewById(R.id.textView_nick);
            holder.textView_email = (TextView) convertView.findViewById(R.id.textView_email);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

//        InboxItem mData = mListData.get(position);

        holder.textView_nick.setText(users.get(position).getNick());
        holder.textView_email.setText(users.get(position).getEmail());

        return convertView;
    }
}