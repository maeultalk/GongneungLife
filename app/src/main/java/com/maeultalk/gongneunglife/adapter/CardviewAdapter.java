package com.maeultalk.gongneunglife.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.RecyclerAdapter;
import com.maeultalk.gongneunglife.activity.CollectActivity;
import com.maeultalk.gongneunglife.model.Item;

import java.util.List;

import static com.maeultalk.gongneunglife.key.Key.URL_IMAGES;

public class CardviewAdapter extends RecyclerView.Adapter<CardviewAdapter.ViewHolder> {
    Context context;
    List<Item> items;
    int item_layout;

    public CardviewAdapter(Context context, List<Item> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item item = items.get(position);
        if(item.isTest()) {
            Drawable drawable = ContextCompat.getDrawable(context, item.getImage());
            holder.image.setBackground(drawable);
        } else {
            Glide.with(context).load(URL_IMAGES + item.getImageString()).into(holder.image);
        }
        holder.title.setText("#" + item.getTitle());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CollectActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("collect", item.getTitle());
                context.startActivity(intent);
//                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}
