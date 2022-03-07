package com.wmg.rwsl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wmg.rwsl.adapter.MainMenuAdapter;
import com.wmg.rwsl.animationTest.AnimationTestMainActivity;
import com.wmg.rwsl.data.MainMenuData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRv;
    private MainMenuAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRv = findViewById(R.id.rv_main);
        mAdapter = new MainMenuAdapter();

        initRv();
    }

    void initRv() {
        List<MainMenuData> list = new ArrayList<>();
        MainMenuData data = new MainMenuData();
        data.title = "动画";
        data.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AnimationTestMainActivity.class));
            }
        };
        list.add(data);
        mAdapter.setData(list);
        mRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mRv.setAdapter(mAdapter);
    }
}