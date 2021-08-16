package com.test.test3app.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * created by zhaoyuntao
 * on 2020-01-13
 * description:
 */
public class GLView extends GLSurfaceView {
    private Renderer mRenderer;

    public GLView(Context context) {
        this(context,null);
    }

    public GLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);//设置OpenGL ES 2.0 context
        mRenderer = new GLRenderer2();
        setRenderer(mRenderer);//设置渲染器
    }
}
