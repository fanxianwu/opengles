package com.fanfan.opengl.geometry.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TriangleColorFull  extends Shape{

    private float triangleCoords[] = {
            0.5f,0.5f,0,
            -0.5f,-0.5f,0,
            0.5f,-0.5f,0
    };

    private float colors[] = {
            1.0f,0,0,1,
            0,1.0f,0,1,
            0,0,1.0f,1
    };

    private FloatBuffer coordFuffer;
    private FloatBuffer colorBuffer;

    private final String vertextShaderCode =
            "attribute vec4 vPosition;"+
                    "uniform mat4 vMatrix;"+
                    "varying vec4 vColor;"+
                    "attribute vec4 aColor;"+
                    " void main() { gl_Position = vMatrix*vPosition;"+
                    "vColor = aColor;}";

    private final String fragmentShaderCode =
            "precision mediump float;"+
                    "varying vec4 vColor;"+
                    " void main(){ gl_FragColor = vColor;}";

    private int mProgram;

    public TriangleColorFull(View view) {
        super(view);

         ByteBuffer dd = ByteBuffer.allocateDirect(triangleCoords.length*4);
         dd.order(ByteOrder.nativeOrder());
         coordFuffer = dd.asFloatBuffer();
         coordFuffer.put(triangleCoords);
         coordFuffer.position(0);

         ByteBuffer cc = ByteBuffer.allocateDirect(colors.length * 4);
         cc.order(ByteOrder.nativeOrder());
         colorBuffer = cc.asFloatBuffer();
         colorBuffer.put(colors);
         colorBuffer.position(0);

        mProgram = GLES20.glCreateProgram();
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertextShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);
        GLES20.glAttachShader(mProgram,vertexShader);
        GLES20.glAttachShader(mProgram,fragmentShader);

        GLES20.glLinkProgram(mProgram);



    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    private float[] mProjectMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio = (float)width /height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix,0,-ratio,ratio,-1,1,3,7);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix,0,0,0,7.0f,0f,0f,0f,0f,1.0f,0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);
    }

    private int mMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgram);

        mMatrixHandle = GLES20.glGetUniformLocation(mProgram,"vMatrix");
        GLES20.glUniformMatrix4fv(mMatrixHandle,1,false,mMVPMatrix,0);

        mColorHandle = GLES20.glGetAttribLocation(mProgram,"aColor");
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(mColorHandle,4,GLES20.GL_FLOAT,false,0,colorBuffer);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram,"vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle,3,GLES20.GL_FLOAT,false,0,coordFuffer);

        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,3);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
