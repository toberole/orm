package com.zhouwei.helloapt.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhouwei.helloapt.R;
import com.zhouwei.helloapt.bean.TestData;

import java.util.List;

/**
 * Created by Charles on 2018/1/25.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    public List<TestData> datas;

    public RecyclerViewAdapter(List<TestData> datas) {
        this.datas = datas;
    }

    @Override
    public int getItemCount() {
        return null != datas ? datas.size() : 0;
    }

    public int count = 0;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        count++;
        // Log.i("AAAA", "count: " + count);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    // 绑定数据
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i("AAAA", "ViewHolder: " + holder.hashCode());

        TestData data = datas.get(position);
        String name = data.name;
        String age = data.age;
        holder.name.setText(name);
        holder.age.setText(age);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView age;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            age = itemView.findViewById(R.id.age);
        }
    }
}
