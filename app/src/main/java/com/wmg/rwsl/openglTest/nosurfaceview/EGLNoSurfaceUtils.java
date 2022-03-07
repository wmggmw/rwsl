package com.wmg.rwsl.openglTest.nosurfaceview;

import static javax.microedition.khronos.egl.EGL10.EGL_ALPHA_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_BLUE_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_DEFAULT_DISPLAY;
import static javax.microedition.khronos.egl.EGL10.EGL_DEPTH_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_GREEN_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_HEIGHT;
import static javax.microedition.khronos.egl.EGL10.EGL_NONE;
import static javax.microedition.khronos.egl.EGL10.EGL_NO_CONTEXT;
import static javax.microedition.khronos.egl.EGL10.EGL_RED_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_STENCIL_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_WIDTH;
import static javax.microedition.khronos.opengles.GL10.GL_RGBA;
import static javax.microedition.khronos.opengles.GL10.GL_UNSIGNED_BYTE;

import android.graphics.Bitmap;

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2019\6\22 0022.
 */

public class EGLNoSurfaceUtils {

    public static GL10 initEGL(int width, int height) {
        int[] version = new int[2];
        int[] attribList = new int[] {
                EGL_WIDTH, width,
                EGL_HEIGHT, height,
                EGL_NONE
        };

        // No error checking performed, minimum required code to elucidate logic
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay eglDisplay = egl.eglGetDisplay(EGL_DEFAULT_DISPLAY);
        egl.eglInitialize(eglDisplay, version);
        EGLConfig eglConfig = chooseConfig(egl, eglDisplay); // Choosing a config is a little more
        // complicated

        // mEGLContext = mEGL.eglCreateContext(mEGLDisplay, mEGLConfig,
        // EGL_NO_CONTEXT, null);
        int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
        int[] attrib_list = {
                EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL10.EGL_NONE
        };
        EGLContext eglContext = egl.eglCreateContext(eglDisplay, eglConfig, EGL_NO_CONTEXT, attrib_list);

        EGLSurface eglSurface = egl.eglCreatePbufferSurface(eglDisplay, eglConfig, attribList);
        egl.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext);

        GL10 gl = (GL10) eglContext.getGL();
        return gl;
    }

    private static EGLConfig chooseConfig(EGL10 egl, EGLDisplay eglDisplay) {
        int[] attribList = new int[] {
                EGL_DEPTH_SIZE, 0,
                EGL_STENCIL_SIZE, 0,
                EGL_RED_SIZE, 8,
                EGL_GREEN_SIZE, 8,
                EGL_BLUE_SIZE, 8,
                EGL_ALPHA_SIZE, 8,
                EGL10.EGL_RENDERABLE_TYPE, 4,
                EGL_NONE
        };

        // No error checking performed, minimum required code to elucidate logic
        // Expand on this logic to be more selective in choosing a configuration
        int[] numConfig = new int[1];
        egl.eglChooseConfig(eglDisplay, attribList, null, 0, numConfig);
        int configSize = numConfig[0];
        EGLConfig[] eglConfigs = new EGLConfig[configSize];
        egl.eglChooseConfig(eglDisplay, attribList, eglConfigs, configSize, numConfig);

//        if (LIST_CONFIGS) {
//            listConfig();
//        }

        return eglConfigs[0]; // Best match is probably the first configuration
    }

    public static Bitmap convertToBitmap(GL10 gl, int width, int height) {
        int[] iat = new int[width * height];
        IntBuffer ib = IntBuffer.allocate(width * height);
        gl.glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, ib);
        int[] ia = ib.array();

        //Stupid !
        // Convert upside down mirror-reversed image to right-side up normal
        // image.
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                iat[(height - i - 1) * width + j] = ia[i * width + j];
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(iat));
        return bitmap;
    }
}
