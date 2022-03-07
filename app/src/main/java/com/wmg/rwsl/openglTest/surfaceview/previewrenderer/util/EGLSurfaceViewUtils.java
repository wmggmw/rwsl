package com.wmg.rwsl.openglTest.surfaceview.previewrenderer.util;

import static javax.microedition.khronos.egl.EGL10.EGL_ALPHA_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_BLUE_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_DEFAULT_DISPLAY;
import static javax.microedition.khronos.egl.EGL10.EGL_DEPTH_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_GREEN_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_NONE;
import static javax.microedition.khronos.egl.EGL10.EGL_NO_CONTEXT;
import static javax.microedition.khronos.egl.EGL10.EGL_RED_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_STENCIL_SIZE;

import android.util.Log;
import android.view.Surface;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2019\6\22 0022.
 */

public class EGLSurfaceViewUtils {
    private static String TAG = "wLog";
    public static EGLSurface mEGLSurface;
    public static EGLDisplay mEGLDisplay;
    public static EGLContext mEGLContext;
    public static EGL10 mEGL10;
    public static GL10 mGL10;

    public static GL10 initEGL(Surface surface) {
        int[] version = new int[2];

        // No error checking performed, minimum required code to elucidate logic
        EGL10 egl = (EGL10) EGLContext.getEGL();
        mEGL10 = egl;
        EGLDisplay eglDisplay = egl.eglGetDisplay(EGL_DEFAULT_DISPLAY);
        if (eglDisplay == null || eglDisplay == EGL10.EGL_NO_DISPLAY) {
            Log.e(TAG, "create display error");
        }
        mEGLDisplay = eglDisplay;
        egl.eglInitialize(eglDisplay, version);
        EGLConfig eglConfig = chooseConfig(mEGL10, mEGLDisplay);

        int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
        int[] contextAttribList = {
                EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL10.EGL_NONE
        };
        EGLContext eglContext = egl.eglCreateContext(eglDisplay, eglConfig, EGL_NO_CONTEXT, contextAttribList);
        if (eglContext == null || eglContext == EGL10.EGL_NO_CONTEXT) {
            Log.e(TAG, "create context error");
        }
        mEGLContext = eglContext;

        int[] surfaceAttribList = {
                EGL10.EGL_NONE
        };
        EGLSurface eglSurface = egl.eglCreateWindowSurface(eglDisplay, eglConfig, surface, surfaceAttribList);
        if (eglSurface == null || eglSurface == EGL10.EGL_NO_SURFACE) {
            Log.e(TAG, "create window error");
            final int error = mEGL10.eglGetError();
            Log.e(TAG, "create window error " + error);
            if (error == EGL10.EGL_BAD_NATIVE_WINDOW) {
                Log.e(TAG, "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
            } else if (error == EGL10.EGL_BAD_ATTRIBUTE) {
                Log.e(TAG, "createWindowSurface returned EGL_BAD_ATTRIBUTE.");
            }
        }
        mEGLSurface = eglSurface;
        egl.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext);

        GL10 gl = (GL10) eglContext.getGL();
        mGL10 = gl;
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

        return eglConfigs[0]; // Best match is probably the first configuration
    }

    public static void makeCurrent() {
        if (mEGL10 != null) {
            mEGL10.eglMakeCurrent(mEGLDisplay, mEGLSurface, mEGLSurface, mEGLContext);
        }
    }

    public static void swapBuffers() {
        if (mEGL10 != null) {
            mEGL10.eglSwapBuffers(mEGLDisplay, mEGLSurface);
        }
    }
}
