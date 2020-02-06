package com.maeultalk.gongneunglife.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.model.Content;
import com.maeultalk.gongneunglife.model.Favorite;
import com.maeultalk.gongneunglife.request.FavorOffRequest;
import com.maeultalk.gongneunglife.request.FavorOnRequest;
import com.maeultalk.gongneunglife.request.MyOffRequest;
import com.maeultalk.gongneunglife.request.MyOnRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.maeultalk.gongneunglife.fragment.mainActivity.FavoriteFragment.mItems;
import static com.maeultalk.gongneunglife.key.Key.URL_IMAGES;

public class FavoriteAdapter extends BaseAdapter{

    private static class ViewHolder {

        public ImageView imageView_place;

        public TextView textView_place;
        public TextView textView_content;

        public ImageView imageView_favor;
        public ImageView imageView_favor2;

    }

    private Context mContext = null;
//    private ArrayList<Favorite> mListData = new ArrayList<Favorite>();

    public FavoriteAdapter(Context mContext, ArrayList<Favorite> mListData) {
        super();
        this.mContext = mContext;
//        this.mListData = mListData;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_favorite, null);

            holder.imageView_place = (ImageView) convertView.findViewById(R.id.imageView_place);

            holder.textView_place = (TextView) convertView.findViewById(R.id.textView_place);
            holder.textView_content = (TextView) convertView.findViewById(R.id.textView_content);

            holder.imageView_favor = (ImageView) convertView.findViewById(R.id.imageView_favor);
            holder.imageView_favor2 = (ImageView) convertView.findViewById(R.id.imageView_favor2);

            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

//        Favorite mData = mListData.get(position);

        if(!TextUtils.isEmpty(mItems.get(position).getPlace_image())) {
            Glide.with(mContext).load(URL_IMAGES + mItems.get(position).getPlace_image()).into(holder.imageView_place);
        }

        holder.textView_place.setText(mItems.get(position).getPlace_name());
        if(mItems.get(position).getType().equals("content")) {
            holder.textView_content.setText("저장한 게시글: " + mItems.get(position).getContent());
        } else {
            holder.textView_content.setText("최근 게시글: " + mItems.get(position).getContent());
        }

        if(mItems.get(position).getType().equals("content")) {
            holder.imageView_favor.setVisibility(View.VISIBLE);
            holder.imageView_favor2.setVisibility(View.GONE);
        } else {
            holder.imageView_favor.setVisibility(View.GONE);
            holder.imageView_favor2.setVisibility(View.VISIBLE);
        }

        if(mItems.get(position).isFavor()) {
            holder.imageView_favor.setImageResource(R.drawable.folder_closed);
        } else {
            holder.imageView_favor.setImageResource(R.drawable.folder_open);
        }
        holder.imageView_favor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItems.get(position).isFavor()) {
                    SharedPreferences pref = mContext.getSharedPreferences("user", MODE_PRIVATE);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                final JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    holder.imageView_favor.setImageResource(R.drawable.folder_open);
                                    mItems.get(position).setFavor(false);
                                } else {
                                    Toast.makeText(mContext, "처리 실패", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                            }

                        }
                    };
                    FavorOffRequest favorOffRequest = new FavorOffRequest(pref.getString("email", ""), mItems.get(position).getContent_id(), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(mContext);
                    queue.add(favorOffRequest);
                } else {
                    SharedPreferences pref = mContext.getSharedPreferences("user", MODE_PRIVATE);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                final JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    holder.imageView_favor.setImageResource(R.drawable.folder_closed);
                                    mItems.get(position).setFavor(true);
                                } else {
                                    Toast.makeText(mContext, "처리 실패", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                            }

                        }
                    };
                    FavorOnRequest favorOnRequest = new FavorOnRequest(pref.getString("email", ""), mItems.get(position).getPlace_code(), mItems.get(position).getContent_id(), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(mContext);
                    queue.add(favorOnRequest);
                }
            }
        });

        if(mItems.get(position).isMy()) {
            holder.imageView_favor2.setImageResource(R.drawable.ic_star_accent_24dp);
        } else {
            holder.imageView_favor2.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
        holder.imageView_favor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItems.get(position).isMy()) {
                    SharedPreferences pref = mContext.getSharedPreferences("user", MODE_PRIVATE);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                final JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    holder.imageView_favor2.setImageResource(R.drawable.ic_star_border_black_24dp);
                                    mItems.get(position).setMy(false);
                                } else {
                                    Toast.makeText(mContext, "처리 실패", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                            }

                        }
                    };
                    MyOffRequest myOffRequest = new MyOffRequest(pref.getString("email", ""), mItems.get(position).getPlace_code(), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(mContext);
                    queue.add(myOffRequest);
                } else {
                    SharedPreferences pref = mContext.getSharedPreferences("user", MODE_PRIVATE);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                final JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    holder.imageView_favor2.setImageResource(R.drawable.ic_star_accent_24dp);
                                    mItems.get(position).setMy(true);
                                } else {
                                    Toast.makeText(mContext, "처리 실패", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                            }

                        }
                    };
                    MyOnRequest myOnRequest = new MyOnRequest(pref.getString("email", ""), mItems.get(position).getPlace_code(), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(mContext);
                    queue.add(myOnRequest);
                }
            }
        });

        // TODO: 08/04/2019 convertView.setOnClickListener 

        return convertView;
    }
}