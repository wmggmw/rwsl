package com.wmg.rwsl.openglTest.glsurfaceview.previewrenderer;

import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wmg.rwsl.R;

public class GLSurfaceViewPreviewRendererActivity extends AppCompatActivity {
    private GLSurfaceView mGLSurfaceView;
    private SurfaceTexture mSurfaceTexture;

    private String TAG = "wLog";
    private SimpleRenderer mSimpleRenderer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glsurfaceview_preview_renderer);

        mGLSurfaceView = findViewById(R.id.glsurfaceview);
        mSimpleRenderer = new SimpleRenderer(mGLSurfaceView);

        initGLSurfaceView();
    }

    private void initGLSurfaceView() {
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(mSimpleRenderer);
    }
}
