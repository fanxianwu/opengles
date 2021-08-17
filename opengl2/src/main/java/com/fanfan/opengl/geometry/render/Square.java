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

public class Square extends Shape {

    private float squareCoords[] = {
        -0.5f,0.5f,0,
        -0.5f,-0.5f,0,
        0.5f,-0.5f,0,
        0.5f,0.5f,0
    };

    private FloatBuffer squareBuff;
    private ShortBuffer indexBuff;
    private int mProgram;
    private final String vertexShaderCode =
            "attribute vec4 vPosition;"+
            "uniform mat4 vMatrix;"+
            "void main(){"+
            " gl_Position = vMatrix * vPosition;"+
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;"+
                    "uniform vec4 vColor;"+
                    "void main(){"+
                    "gl_FragColor = vColor;}";


    public Square(View view) {
        super(view);

        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        squareBuff = bb.asFloatBuffer();
        squareBuff.put(squareCoords);
        squareBuff.position(0);

        ByteBuffer inBuf = ByteBuffer.allocateDirect(index.length*2);
        inBuf.order(ByteOrder.nativeOrder());
        indexBuff = inBuf.asShortBuffer();
        indexBuff.put(index);
        indexBuff.position(0);

        mProgram = GLES20.glCreateProgram();
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
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
        float ratio = (float) width /height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix,0,-ratio,ratio,-1,1,3,7);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix,0,0,0,7.0f,0f,0f,0f,0f,1.0f,0.0f);

        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);

    }

    private int mMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;

    private float colors[] = {
            1.0f,1.0f,1.0f,1.0f
    };

    private short index[] = {
        0,1,2,0,2,3
    };

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgram);

        mMatrixHandle = GLES20.glGetUniformLocation(mProgram,"vMatrix");
        GLES20.glUniformMatrix4fv(mMatrixHandle,1,false,mMVPMatrix,0);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram,"vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle,3,GLES20.GL_FLOAT,false,3*4,squareBuff);

        mColorHandle = GLES20.glGetUniformLocation(mProgram,"vColor");
        GLES20.glUniform4fv(mColorHandle,1,colors,0);

       // GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
       // GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,4);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,index.length,GLES20.GL_UNSIGNED_SHORT,indexBuff);

        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }
}
