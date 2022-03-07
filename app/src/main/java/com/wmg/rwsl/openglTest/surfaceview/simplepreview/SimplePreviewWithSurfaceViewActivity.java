package com.wmg.rwsl.openglTest.surfaceview.simplepreview;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import com.wmg.rwsl.R;

import java.io.IOException;

public class SimplePreviewWithSurfaceViewActivity extends AppCompatActivity {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private Camera.Parameters mParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_preview_with_surfaceview);

        mSurfaceView = findViewById(R.id.surfaceview);

        initSurfaceView();
        initCamera();
    }

    private void initSurfaceView() {
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    mCamera.setPreviewDisplay(holder);
                    mCamera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    private void initCamera() {
        if (mCamera == null) {
            mCamera = Camera.open();
        }
        try {
            mParams = mCamera.getParameters();
            mParams.setPictureFormat(PixelFormat.JPEG);//设置拍照后存储的图片格式
            mCamera.setDisplayOrientation(90);
            //设置摄像头为持续自动聚焦模式
            mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            mCamera.setParameters(mParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
