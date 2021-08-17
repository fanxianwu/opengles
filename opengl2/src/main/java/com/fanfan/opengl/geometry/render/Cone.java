package com.fanfan.opengl.geometry.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.View;

import com.fanfan.opengl.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Cone extends Shape {

    private FloatBuffer vertexBuff;
    private int program;
    private Oval oval;

    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    private int n = 360;
    private float height = 2.0f;
    private float radius = 1.0f;

    private float[] colors = {1.0f,1.0f,1.0f,1.0f};

    private int vSize;

    private int mMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;

    public Cone(View view) {
        super(view);
        oval = new Oval(mView);
        ArrayList<Float> pos = new ArrayList<>();
        pos.add(0.0f);
        pos.add(0.0f);
        pos.add(height);
        float angDegSpan = 360f / n;
        for(float i = 0;i< 360+angDegSpan;i+=angDegSpan){
            pos.add((float) (radius*Math.cos(i*Math.PI/180f)));
            pos.add((float) (radius*Math.sin(i*Math.PI/180f)));
            pos.add(0.0f);
        }
        float[] d = new float[pos.size()];
        for(int i = 0;i < d.length;i++){
            d[i] = pos.get(i);
        }
        vSize = d.length / 3;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(d.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuff = byteBuffer.asFloatBuffer();
        vertexBuff.put(d);
        vertexBuff.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.i("Cone","onSurfaceCreated");
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        program = ShaderUtils.createProgram(mView.getResources(),"vshader/Cone.glsl","fshader/Cone.glsl");
        oval.onSurfaceCreated(gl,config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        //计算宽高比
//        float ratio=(float)width/height;
//        //设置透视投影
//        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
//        //设置相机位置
//        Matrix.setLookAtM(mViewMatrix, 0, 1.0f, -10.0f, -4.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
//        //计算变换矩阵
//        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);

       // oval.onSurfaceChanged(gl,width,height);
        float ratio = width > height ? (float)width/height:(float)height/width;
        Matrix.setLookAtM(mViewMatrix,0,4.0f,4.0f,-4.0f,0,0,0,0,1,0);
        if(width > height){
            Matrix.frustumM(mProjectMatrix,0,-ratio,ratio,-1,1,3,20);
        }else{
            Matrix.frustumM(mProjectMatrix,0,-1,1,-ratio,ratio,3,20);
        }
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(program);
        mMatrixHandle = GLES20.glGetUniformLocation(program,"vMatrix");
        GLES20.glUniformMatrix4fv(mMatrixHandle,1,false,mMVPMatrix,0);
        mPositionHandle = GLES20.glGetAttribLocation(program,"vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle,3,GLES20.GL_FLOAT,false,0,vertexBuff);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,vSize);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        oval.setMatrix(mMVPMatrix);
        oval.onDrawFrame(gl);
    }
}
