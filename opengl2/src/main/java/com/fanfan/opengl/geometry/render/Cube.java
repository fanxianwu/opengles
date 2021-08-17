package com.fanfan.opengl.geometry.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Cube extends Shape {

    private FloatBuffer vertexBuff;
    private FloatBuffer colorBuff;
    private ShortBuffer indexBuff;

    private float cubeCooders[] = {
        -1.0f,1.0f,1.0f, //正面左上角
        -1.0f,-1.0f,1.0f,
        1.0f,-1.0f,1.0f,
        1.0f,1.0f,1.0f,
        -1.0f,1.0f,-1.0f,
        -1.0f,-1.0f,-1.0f,
        1.0f,-1.0f,-1.0f,
        1.0f,1.0f,-1.0f
    };

    private float colors[] = {
      1.0f,0f,0f,1.0f,
            1.0f,0f,0f,1.0f,
            1.0f,0f,0f,1.0f,
            1.0f,0f,0f,1.0f,
            0.0f,1.0f,0f,1.0f,
            0.0f,1.0f,0f,1.0f,
            0.0f,1.0f,0f,1.0f,
            0.0f,1.0f,0f,1.0f
    };

    private short index[] = {
        0,3,2,0,2,1,
        0,1,5,0,5,4,
        0,7,3,0,4,7,
        6,7,4,6,5,4,
        1,5,6,1,2,6,
        3,2,6,3,7,6
    };


    private final String vertexCode =
            "attribute vec4 vPosition;"+
             "uniform mat4 mMatrix;"+
              "varying vec4 vColor;"+
              "attribute vec4 aColor;"+
            " void main(){"+
            "gl_Position = mMatrix*vPosition;"+
                    "vColor = aColor;}";

    private final String fragmentCode =
            "precision mediump float;"+
             "varying vec4 vColor;"+
            "void main(){"+
            " gl_FragColor = vColor;}";

    private int mProgram;

    public Cube(View view) {
        super(view);
        //顶点坐标数据
        ByteBuffer dd = ByteBuffer.allocateDirect(cubeCooders.length *4);
        dd.order(ByteOrder.nativeOrder());
        vertexBuff = dd.asFloatBuffer();
        vertexBuff.put(cubeCooders);
        vertexBuff.position(0);
        // draw element 数据
        ByteBuffer inbuf = ByteBuffer.allocateDirect(index.length*2);
        inbuf.order(ByteOrder.nativeOrder());
        indexBuff = inbuf.asShortBuffer();
        indexBuff.put(index);
        indexBuff.position(0);
        //颜色数据
        ByteBuffer coBuf = ByteBuffer.allocateDirect(colors.length*4);
        coBuf.order(ByteOrder.nativeOrder());
        colorBuff = coBuf.asFloatBuffer();
        colorBuff.put(colors);
        colorBuff.position(0);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram,vertexShader);
        GLES20.glAttachShader(mProgram,fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    private float[] mProjectMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //开启深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio = (float) width/height;
        Matrix.frustumM(mProjectMatrix,0,-ratio,ratio,-1,1,3,20);
        Matrix.setLookAtM(mViewMatrix,0,-5.0f,5.0f,10.0f,0f,0f,0f,0f,1.0f,0f);
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);
    }


    private int mVertexHandle;
    private int mColorHandle;
    private int mMatrixHandle;

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgram);

        mVertexHandle = GLES20.glGetAttribLocation(mProgram,"vPosition");
        GLES20.glEnableVertexAttribArray(mVertexHandle);
        GLES20.glVertexAttribPointer(mVertexHandle,3,GLES20.GL_FLOAT,false,0,vertexBuff);

        mColorHandle = GLES20.glGetAttribLocation(mProgram,"aColor");
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(mColorHandle,4,GLES20.GL_FLOAT,false,0,colorBuff);

        mMatrixHandle = GLES20.glGetUniformLocation(mProgram,"mMatrix");
        GLES20.glUniformMatrix4fv(mMatrixHandle,1,false,mMVPMatrix,0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES,index.length,GLES20.GL_UNSIGNED_SHORT,indexBuff);
        GLES20.glDisableVertexAttribArray(mVertexHandle);
    }
}
