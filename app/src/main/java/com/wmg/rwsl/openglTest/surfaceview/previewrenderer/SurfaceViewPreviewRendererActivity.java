package com.wmg.rwsl.openglTest.surfaceview.previewrenderer;

import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;


import com.wmg.rwsl.R;
import com.wmg.rwsl.openglTest.surfaceview.previewrenderer.util.EGLSurfaceViewUtils;
import com.wmg.rwsl.openglTest.surfaceview.previewrenderer.util.OpenGLUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class SurfaceViewPreviewRendererActivity extends AppCompatActivity {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private Camera.Parameters mParams;
    private String TAG = "wLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surfaceview_preview_renderer);

        mSurfaceView = findViewById(R.id.surfaceview);

        initSurfaceView();
        initCamera();
        initOpenGL();
    }

    private void initSurfaceView() {
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.i(TAG, "initSurfaceView() surfaceCreated()...");
                EGLSurfaceViewUtils.initEGL(holder.getSurface());
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    private int mPreviewWidth;
    private int mPreviewHeight;
    private void initCamera() {
        if (mCamera == null) {
            mCamera = Camera.open();
        }

        mPreviewWidth = mCamera.getParameters().getPreviewSize().width;
        mPreviewHeight = mCamera.getParameters().getPreviewSize().height;
        int tmp = mPreviewWidth;
        mPreviewWidth = mPreviewHeight;
        mPreviewHeight = tmp;
        Log.i(TAG, "initCamera() " + " width " + mPreviewWidth + " height " + mPreviewHeight);

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

    private int mTextureId = -1;
    private int mGLProgId = -1;
    private SurfaceTexture mSurfaceTexture;
    private int mGLAttribVertex, mGLAttribTextureCoordinate;
    private int mGLUniformTexture, mGLUniformHue;
    private FloatBuffer mGLVertexBuffer, mGLTextureCoordinateBuffer;
    private void initOpenGL() {
        if (mTextureId == -1) {
            mTextureId = OpenGLUtils.createOESTextureID();
            mSurfaceTexture = new SurfaceTexture(mTextureId);
            mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
                @Override
                public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                    drawFrame();
                }
            });
            try {
                mCamera.setPreviewTexture(mSurfaceTexture);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.startPreview();
        }
    }
    private void drawFrame() {
        //创建着色器程序
        if (mGLProgId == -1) {
            mGLProgId = OpenGLUtils.loadProgram(OpenGLUtils.NO_FILTER_VERTEX_SHADER, OpenGLUtils.HUE_FRAGMENT_SHADER);
        }
        GLES20.glViewport(0, 0, mPreviewWidth, mPreviewHeight);
        //获取着色程序中的变量引用
        mGLAttribVertex = GLES20.glGetAttribLocation(mGLProgId, "position");
        mGLAttribTextureCoordinate = GLES20.glGetAttribLocation(mGLProgId, "inputTextureCoordinate");
        mGLUniformTexture = GLES20.glGetUniformLocation(mGLProgId, "inputImageTexture");
        mGLUniformHue = GLES20.glGetUniformLocation(mGLProgId, "hueAdjust");

        //有些值可以在glUseProgram()方法调用之前调用
        //初始化定点坐标
        mGLVertexBuffer = ByteBuffer.allocateDirect(OpenGLUtils.CUBE.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mGLVertexBuffer.put(OpenGLUtils.CUBE).position(0);
        GLES20.glVertexAttribPointer(mGLAttribVertex, 2, GLES20.GL_FLOAT, false, 0, mGLVertexBuffer);
        GLES20.glEnableVertexAttribArray(mGLAttribVertex);
        //初始化纹理坐标
        mGLTextureCoordinateBuffer = ByteBuffer.allocateDirect(OpenGLUtils.TEXTURE_NO_ROTATION.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mGLTextureCoordinateBuffer.put(OpenGLUtils.TEXTURE_ROTATED_90).position(0);
        GLES20.glVertexAttribPointer(mGLAttribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0,
                mGLTextureCoordinateBuffer);
        GLES20.glEnableVertexAttribArray(mGLAttribTextureCoordinate);
        //设置texture值
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureId);
        GLES20.glUniform1i(mGLUniformTexture, 0);

        EGLSurfaceViewUtils.makeCurrent();
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        //开始绘制
        GLES20.glUseProgram(mGLProgId);

        //设置hue值，这条设置不能够移到glUseProgram()方法之前
        GLES20.glUniform1f(mGLUniformHue, 10);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisableVertexAttribArray(mGLAttribVertex);
        GLES20.glDisableVertexAttribArray(mGLAttribTextureCoordinate);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        mSurfaceTexture.updateTexImage();
        EGLSurfaceViewUtils.swapBuffers();
    }
}
