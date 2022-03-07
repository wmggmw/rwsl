package com.wmg.rwsl.openglTest.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wmg.rwsl.R;
import com.wmg.rwsl.openglTest.glsurfaceview.previewrenderer.GLSurfaceViewPreviewRendererActivity;
import com.wmg.rwsl.openglTest.glsurfaceview.simplepreview.SimplePreviewWithGLSurfaceViewActivity;
import com.wmg.rwsl.openglTest.nosurfaceview.NoSurfaceViewActivity;
import com.wmg.rwsl.openglTest.surfaceview.previewrenderer.SurfaceViewPreviewRendererActivity;
import com.wmg.rwsl.openglTest.surfaceview.simplepreview.SimplePreviewWithSurfaceViewActivity;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private final List<String> mDatas = new ArrayList<>();

    public MenuAdapter(Context context) {
        mContext = context;
        mDatas.add("Camera Preview with SurfaceView");
        mDatas.add("Camera Preview with GLSurfaceView");
        mDatas.add("Camera Filter with SurfaceView");
        mDatas.add("Camera Filter with GLSurfaceView");
        mDatas.add("Bitmap Filter with OpenGL");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_opengl_test_main_menu, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        LViewHolder vh = (LViewHolder) viewHolder;
        vh.mTvMenu.setText(mDatas.get(i));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class LViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvMenu;

        public LViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvMenu = itemView.findViewById(R.id.tv_menu);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext == null) {
                        return;
                    }
                    int position = getAdapterPosition();
                    Intent intent;
                    switch (position) {
                        case 0:
                            intent = new Intent(mContext, SimplePreviewWithSurfaceViewActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent(mContext, SimplePreviewWithGLSurfaceViewActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 2:
                            intent = new Intent(mContext, SurfaceViewPreviewRendererActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 3:
                            intent = new Intent(mContext, GLSurfaceViewPreviewRendererActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 4:
                            intent = new Intent(mContext, NoSurfaceViewActivity.class);
                            mContext.startActivity(intent);
                            break;
                    }
                }
            });
        }
    }
}
