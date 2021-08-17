package com.fanfan.opengl.geometry.render;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class FGLSurfaceView extends GLSurfaceView {

    private FGLRender render;

    public FGLSurfaceView(Context context) {
        this(context,null);
    }

    public FGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        //设置openGLES的版本号
        setEGLContextClientVersion(2);
        render = new FGLRender(this);
        setRenderer(render);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void setShape(Class shape){
        render.setShape(shape);
    }
}
