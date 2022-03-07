package com.wmg.rwsl.openglTest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wmg.rwsl.R;
import com.wmg.rwsl.openglTest.adapter.MenuAdapter;

public class OpenGLTestMainActivity extends AppCompatActivity {
    private RecyclerView mRvMenu;
    private MenuAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opengl_test_main);

        mRvMenu = findViewById(R.id.rv_menu);
        mAdapter = new MenuAdapter(this);
        mRvMenu.setLayoutManager(new LinearLayoutManager(this));
        mRvMenu.setAdapter(mAdapter);
    }
}
