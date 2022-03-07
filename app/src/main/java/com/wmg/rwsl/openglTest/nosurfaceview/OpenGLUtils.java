package com.wmg.rwsl.openglTest.nosurfaceview;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * Created by Administrator on 2019\6\22 0022.
 */

public class OpenGLUtils {
    public static final float CUBE[] = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f,
    };

    public static final float TEXTURE_NO_ROTATION[] = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
    };

    public static final String NO_FILTER_VERTEX_SHADER = "" +
            "attribute vec4 position;\n" +
            "attribute vec4 inputTextureCoordinate;\n" +
            " \n" +
            "varying vec2 textureCoordinate;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = position;\n" +
            "    textureCoordinate = inputTextureCoordinate.xy;\n" +
            "}";

    public static final String HUE_FRAGMENT_SHADER = "" +
            "precision highp float;\n" +
            "varying highp vec2 textureCoordinate;\n" +
            "\n" +
            "uniform sampler2D inputImageTexture;\n" +
            "uniform mediump float hueAdjust;\n" +
            "const highp vec4 kRGBToYPrime = vec4 (0.299, 0.587, 0.114, 0.0);\n" +
            "const highp vec4 kRGBToI = vec4 (0.595716, -0.274453, -0.321263, 0.0);\n" +
            "const highp vec4 kRGBToQ = vec4 (0.211456, -0.522591, 0.31135, 0.0);\n" +
            "\n" +
            "const highp vec4 kYIQToR = vec4 (1.0, 0.9563, 0.6210, 0.0);\n" +
            "const highp vec4 kYIQToG = vec4 (1.0, -0.2721, -0.6474, 0.0);\n" +
            "const highp vec4 kYIQToB = vec4 (1.0, -1.1070, 1.7046, 0.0);\n" +
            "\n" +
            "void main ()\n" +
            "{\n" +
            "    // Sample the input pixel\n" +
            "    highp vec4 color = texture2D(inputImageTexture, textureCoordinate);\n" +
            "\n" +
            "    // Convert to YIQ\n" +
            "    highp float YPrime = dot (color, kRGBToYPrime);\n" +
            "    highp float I = dot (color, kRGBToI);\n" +
            "    highp float Q = dot (color, kRGBToQ);\n" +
            "\n" +
            "    // Calculate the hue and chroma\n" +
            "    highp float hue = atan (Q, I);\n" +
            "    highp float chroma = sqrt (I * I + Q * Q);\n" +
            "\n" +
            "    // Make the user's adjustments\n" +
            "    hue += (-hueAdjust); //why negative rotation?\n" +
            "\n" +
            "    // Convert back to YIQ\n" +
            "    Q = chroma * sin (hue);\n" +
            "    I = chroma * cos (hue);\n" +
            "\n" +
            "    // Convert back to RGB\n" +
            "    highp vec4 yIQ = vec4 (YPrime, I, Q, 0.0);\n" +
            "    color.r = dot (yIQ, kYIQToR);\n" +
            "    color.g = dot (yIQ, kYIQToG);\n" +
            "    color.b = dot (yIQ, kYIQToB);\n" +
            "\n" +
            "    // Save the result\n" +
            "    gl_FragColor = color;\n" +
            "}\n";

    public static int loadTexture(final Bitmap img, final int usedTexId, final boolean recycle) {
        int textures[] = new int[1];
        if (usedTexId == -1) {
            GLES20.glGenTextures(1, textures, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, img, 0);
        } else {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, usedTexId);
            GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, img);
            textures[0] = usedTexId;
        }
        if (recycle) {
            img.recycle();
        }
        return textures[0];
    }

    public static int loadProgram(final String strVSource, final String strFSource) {
        int iVShader;
        int iFShader;
        int iProgId;
        int[] link = new int[1];
        iVShader = loadShader(strVSource, GLES20.GL_VERTEX_SHADER);
        if (iVShader == 0) {
            Log.d("Load Program", "Vertex Shader Failed");
            return 0;
        }
        iFShader = loadShader(strFSource, GLES20.GL_FRAGMENT_SHADER);
        if (iFShader == 0) {
            Log.d("Load Program", "Fragment Shader Failed");
            return 0;
        }

        iProgId = GLES20.glCreateProgram();

        GLES20.glAttachShader(iProgId, iVShader);
        GLES20.glAttachShader(iProgId, iFShader);

        GLES20.glLinkProgram(iProgId);

        GLES20.glGetProgramiv(iProgId, GLES20.GL_LINK_STATUS, link, 0);
        if (link[0] <= 0) {
            Log.d("Load Program", "Linking Failed");
            return 0;
        }
        GLES20.glDeleteShader(iVShader);
        GLES20.glDeleteShader(iFShader);
        return iProgId;
    }

    private static int loadShader(final String strSource, final int iType) {
        int[] compiled = new int[1];
        int iShader = GLES20.glCreateShader(iType);
        GLES20.glShaderSource(iShader, strSource);
        GLES20.glCompileShader(iShader);
        GLES20.glGetShaderiv(iShader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Log.d("Load Shader Failed", "Compilation\n" + GLES20.glGetShaderInfoLog(iShader));
            return 0;
        }
        return iShader;
    }
}
