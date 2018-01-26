package com.zhouwei.helloapt.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zhouwei.helloapt.R;
import com.zhouwei.helloapt.adapter.RecyclerViewAdapter;
import com.zhouwei.helloapt.bean.Data;

public class TestRecyclerViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_recycler_view);

        layoutManager = new LinearLayoutManager(TestRecyclerViewActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(Data.datas);
        recyclerView.setAdapter(adapter);
    }
}
