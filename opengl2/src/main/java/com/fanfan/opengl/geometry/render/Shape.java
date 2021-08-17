package com.fanfan.opengl.geometry.render;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.View;

public abstract class Shape implements GLSurfaceView.Renderer {
    protected View mView;
    private final String TAG  = Shape.class.getSimpleName();
    public Shape(View view){
        this.mView = view;
    }

    public int loadShader(int type,String shaderCode){
        //根据type创建着色器
        int shader = GLES20.glCreateShader(type);
        //将资源加入着色器并且编译
        GLES20.glShaderSource(shader,shaderCode);
        GLES20.glCompileShader(shader);
        //判断是否有编译错误
        int compileStatus[] = new int[1];
        GLES20.glGetShaderiv(shader,GLES20.GL_COMPILE_STATUS,compileStatus,0);
        if(compileStatus[0] == 0){
            //获取编译错误信息
            String shaderCompileError = GLES20.glGetShaderInfoLog(shader);
            Log.i(TAG,"shader compile error:"+shaderCompileError);
            return  0;
        }
        return shader;
    }
}
