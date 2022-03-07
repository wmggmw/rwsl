package com.wmg.rwsl.animationTest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wmg.rwsl.R;
import com.wmg.rwsl.adapter.MainMenuAdapter;
import com.wmg.rwsl.adapter.SpacesItemDecoration;
import com.wmg.rwsl.animationTest.test1.AnimationTest01Activity;
import com.wmg.rwsl.data.MainMenuData;

import java.util.ArrayList;
import java.util.List;

public class AnimationTestMainActivity extends AppCompatActivity {
    private RecyclerView mRv;
    private MainMenuAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_test_main);
        mRv = findViewById(R.id.rv_animation_test_main);
        mAdapter = new MainMenuAdapter();

        initRv();
    }

    void initRv() {
        List<MainMenuData> list = new ArrayList<>();
        MainMenuData data = new MainMenuData();
        data.title = "动画测试1";
        data.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnimationTestMainActivity.this, AnimationTest01Activity.class));
            }
        };
        list.add(data);
        data = new MainMenuData();
        data.title = "动画测试2";
        data.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnimationTestMainActivity.this, AnimationTest01Activity.class));
            }
        };
        list.add(data);
        mAdapter.setData(list);
        mRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mRv.addItemDecoration(new SpacesItemDecoration(12));
        mRv.setAdapter(mAdapter);
    }
}
