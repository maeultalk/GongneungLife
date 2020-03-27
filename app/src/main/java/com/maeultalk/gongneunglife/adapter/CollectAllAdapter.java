package com.maeultalk.gongneunglife.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.model.CollectAllItem;

import java.util.ArrayList;

public class CollectAllAdapter extends RecyclerView.Adapter<CollectAllAdapter.ViewHolder> {

    Context context;
    ArrayList<CollectAllItem> items;

    public CollectAllAdapter(Context context, ArrayList<CollectAllItem> items) {
        this.context = context;
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardview;
        TextView textView;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            cardview = (CardView) view.findViewById(R.id.cardView);
            textView = (TextView) view.findViewById(R.id.textView);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public CollectAllAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_collect_all, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.textView.setText(items.get(position).getItem());

        if(position==0) {
            holder.imageView.setImageResource(R.drawable.don);
        }
        if(position==1) {
            holder.imageView.setImageResource(R.drawable.ball);
        }
        if(position==2) {
            holder.imageView.setImageResource(R.drawable.jang);
        }
        if(position==3) {
            holder.imageView.setImageResource(R.drawable.busi);
        }
        if(position==4) {
            holder.imageView.setImageResource(R.drawable.jok);
        }
        if(position==5) {
            holder.imageView.setImageResource(R.drawable.noodle);
        }
        if(position==6) {
            holder.imageView.setImageResource(R.drawable.shoes2);
        }
        if(position==7) {
            holder.imageView.setImageResource(R.drawable.bob);
        }

    }

}
