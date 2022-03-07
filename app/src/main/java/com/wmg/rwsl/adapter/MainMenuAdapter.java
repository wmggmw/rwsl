package com.wmg.rwsl.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wmg.rwsl.R;
import com.wmg.rwsl.data.MainMenuData;

import java.util.ArrayList;
import java.util.List;

public class MainMenuAdapter extends RecyclerView.Adapter {
    private final List<MainMenuData> mData = new ArrayList<>();

    public void setData(List<MainMenuData> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainMenuVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MainMenuData data = mData.get(position);
        MainMenuVH vh = (MainMenuVH) holder;
        vh.textView.setText(data.title);
        vh.itemView.setOnClickListener(data.onClickListener);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MainMenuVH extends RecyclerView.ViewHolder {
        public TextView textView;

        public MainMenuVH(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_item_main_menu);
        }
    }
}
