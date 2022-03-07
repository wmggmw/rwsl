package com.wmg.rwsl.openglTest.nosurfaceview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.wmg.rwsl.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2019\6\26 0026.
 */

public class NoSurfaceViewActivity extends AppCompatActivity {
    private ImageView mIv;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_surfaceview);

        mIv = findViewById(R.id.iv);
    }

    private int mTextureId = -1;
    private SurfaceTexture mSurfaceTexture;
    private int mGLProgId = -1;
    private int mGLAttribVertex, mGLAttribTextureCoordinate;
    private int mGLUniformTexture, mGLUniformHue;
    private FloatBuffer mGLVertexBuffer, mGLTextureCoordinateBuffer;
    private GL10 mGL;
    public void startProcess(View view) {
        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lyf, null);
        }
        //初始化EGL环境
        mGL = EGLNoSurfaceUtils.initEGL(mBitmap.getWidth(), mBitmap.getHeight());
        //创建纹理id
        if (mTextureId == -1) {
            mTextureId = OpenGLUtils.loadTexture(mBitmap, mTextureId, false);
            mSurfaceTexture = new SurfaceTexture(mTextureId);
        }
        //创建着色器程序
        if (mGLProgId == -1) {
            mGLProgId = OpenGLUtils.loadProgram(OpenGLUtils.NO_FILTER_VERTEX_SHADER, OpenGLUtils.HUE_FRAGMENT_SHADER);
        }
        GLES20.glViewport(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
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
        mGLTextureCoordinateBuffer.put(OpenGLUtils.TEXTURE_NO_ROTATION).position(0);
        GLES20.glVertexAttribPointer(mGLAttribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0,
                mGLTextureCoordinateBuffer);
        GLES20.glEnableVertexAttribArray(mGLAttribTextureCoordinate);
        //设置texture值
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        GLES20.glUniform1i(mGLUniformTexture, 0);

        int i=1;
        while (i>0) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            //开始绘制
            GLES20.glUseProgram(mGLProgId);

            //设置hue值，这条设置不能够移到glUseProgram()方法之前
            GLES20.glUniform1f(mGLUniformHue, 10);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
            GLES20.glDisableVertexAttribArray(mGLAttribVertex);
            GLES20.glDisableVertexAttribArray(mGLAttribTextureCoordinate);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            mSurfaceTexture.updateTexImage();
            i--;
        }
        Bitmap hueBitmap = EGLNoSurfaceUtils.convertToBitmap(mGL, mBitmap.getWidth(), mBitmap.getHeight());
        mIv.setImageBitmap(hueBitmap);
    }
}
