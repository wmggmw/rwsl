package com.wmg.rwsl.openglTest.glsurfaceview.previewrenderer;

import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.wmg.rwsl.openglTest.glsurfaceview.previewrenderer.util.OpenGLGLSurfaceViewUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SimpleRenderer implements GLSurfaceView.Renderer {
    private int mTextureId;
    private SurfaceTexture mSurfaceTexture;
    private String TAG = "wLog";

    private GLSurfaceView mGLSurfaceView;
    public SimpleRenderer(GLSurfaceView glSurfaceView) {
        mGLSurfaceView = glSurfaceView;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mTextureId = OpenGLGLSurfaceViewUtils.createOESTextureID();
        mSurfaceTexture = new SurfaceTexture(mTextureId);
        mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                Log.i(TAG, "surfacetexture onFrameAvailable()...");
                mGLSurfaceView.requestRender();
            }
        });
        initCamera();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.i(TAG, "simplerenderer onDrawFrame()...");
        drawFrame();
    }

    private int mGLProgId = -1;
    private int mGLAttribVertex, mGLAttribTextureCoordinate;
    private int mGLUniformTexture, mGLUniformHue;
    private FloatBuffer mGLVertexBuffer, mGLTextureCoordinateBuffer;
    private void drawFrame() {
        //创建着色器程序
        if (mGLProgId == -1) {
            mGLProgId = OpenGLGLSurfaceViewUtils.loadProgram(OpenGLGLSurfaceViewUtils.NO_FILTER_VERTEX_SHADER, OpenGLGLSurfaceViewUtils.HUE_FRAGMENT_SHADER);
        }
        //获取着色程序中的变量引用
        mGLAttribVertex = GLES20.glGetAttribLocation(mGLProgId, "position");
        mGLAttribTextureCoordinate = GLES20.glGetAttribLocation(mGLProgId, "inputTextureCoordinate");
        mGLUniformTexture = GLES20.glGetUniformLocation(mGLProgId, "inputImageTexture");
        mGLUniformHue = GLES20.glGetUniformLocation(mGLProgId, "hueAdjust");

        //有些值可以在glUseProgram()方法调用之前调用
        //初始化定点坐标
        mGLVertexBuffer = ByteBuffer.allocateDirect(OpenGLGLSurfaceViewUtils.CUBE.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mGLVertexBuffer.put(OpenGLGLSurfaceViewUtils.CUBE).position(0);
        GLES20.glVertexAttribPointer(mGLAttribVertex, 2, GLES20.GL_FLOAT, false, 0, mGLVertexBuffer);
        GLES20.glEnableVertexAttribArray(mGLAttribVertex);
        //初始化纹理坐标
        mGLTextureCoordinateBuffer = ByteBuffer.allocateDirect(OpenGLGLSurfaceViewUtils.TEXTURE_NO_ROTATION.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mGLTextureCoordinateBuffer.put(OpenGLGLSurfaceViewUtils.TEXTURE_ROTATED_90).position(0);
        GLES20.glVertexAttribPointer(mGLAttribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0,
                mGLTextureCoordinateBuffer);
        GLES20.glEnableVertexAttribArray(mGLAttribTextureCoordinate);
        //设置texture值
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureId);
        GLES20.glUniform1i(mGLUniformTexture, 0);

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
    }

    private Camera mCamera;
    private Camera.Parameters mParams;

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
        try {
            mCamera.setPreviewTexture(mSurfaceTexture);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
