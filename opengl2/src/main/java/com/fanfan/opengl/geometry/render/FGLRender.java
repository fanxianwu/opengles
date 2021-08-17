package com.fanfan.opengl.geometry.render;

import android.opengl.GLES20;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Constructor;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class FGLRender extends Shape {

    private final String TAG = FGLRender.class.getSimpleName();
    private Shape shape;
    private Class<? extends Shape> clazz = Triangle.class;

    public FGLRender(View view){
        super(view);
    }

    public void setShape(Class shape){
            clazz = shape;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f,0.5f,0.5f,1.0f);
        try{
            Constructor constructor = clazz.getDeclaredConstructor(View.class);
            constructor.setAccessible(true);
            shape = (Shape) constructor.newInstance(mView);
        }catch (Exception e){
            Log.i(TAG,"create Exception:"+e.toString());
        }
        shape.onSurfaceCreated(gl,config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
        if(shape != null){
            shape.onSurfaceChanged(gl,width,height);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        if(shape != null){
            shape.onDrawFrame(gl);
        }
    }
}
