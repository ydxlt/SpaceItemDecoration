package com.lt.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by admin on 2018/5/2.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyHolder> {

    private Context mContext;
    private String[] mTitle;

    public CardAdapter(Context context, String[] title) {
        mContext = context;
        mTitle = title;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.textview,parent,false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        ((TextView)holder.itemView).setText(mTitle[position]);
    }

    @Override
    public int getItemCount() {
        return mTitle.length;
    }

    class MyHolder extends RecyclerView.ViewHolder{

        public MyHolder(View itemView) {
            super(itemView);
        }
    }
}
