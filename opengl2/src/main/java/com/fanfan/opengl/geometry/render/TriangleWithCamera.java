package com.fanfan.opengl.geometry.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TriangleWithCamera extends Shape {

    private float triangleCoords[] = {
            0.5f,0.5f,0,
            -0.5f,-0.5f,0,
            0.5f,-0.5f,0
    };

    private int mMatrixHandle;
    private int mPositonHandle;

    private int mColorHandle;

    private final String vertexShaderCode =
            "attribute vec4 vPosition;"+
                    "uniform mat4 vMatrix;"+
                    "void main() {"+
                    " gl_Position = vMatrix*vPosition;"+
                    " }";

    private final String fragmentShaderCode =
            "precision mediump float;"+
                    "uniform vec4 vColor;"+
                    "void main() {"+
                    " gl_FragColor = vColor;"+
                    "}";

    private FloatBuffer floatBuffer;
    private int mProgram;

    private float mProjectMatrix[] = new float[16];
    private float [] mViewMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    static int COORDS_PRE_VERTEX = 3;

    private final int vertexCount = triangleCoords.length / COORDS_PRE_VERTEX;

    private final int vertexStride = COORDS_PRE_VERTEX * 4;

    float color[] = {1.0f,1.0f,1.0f,1.0f};


    public TriangleWithCamera(View view) {
        super(view);
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length*4);
        bb.order(ByteOrder.nativeOrder());
        floatBuffer = bb.asFloatBuffer();
        floatBuffer.put(triangleCoords);
        floatBuffer.position(0);

        //加载定点着色器
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);

        //创建一个空的OpenGl程序
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram,vertexShader);
        GLES20.glAttachShader(mProgram,fragmentShader);

        GLES20.glLinkProgram(mProgram);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio = (float)width /height;
        //设置透视投影
       // Matrix.frustumM(mProjectMatrix,0,-ratio,ratio,-1,1,3,7);
        Matrix.perspectiveM(mProjectMatrix,0,130,ratio,0.1f,1000);
        //正交投影
      //  Matrix.orthoM(mProjectMatrix,0,-1,1,-ratio,ratio,0.1f,7);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix,0,0,0,1f,0f,0f,0f,0.0f,1.0f,0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgram);

        //获取变换矩阵vMatrix 成员句柄
        mMatrixHandle = GLES20.glGetUniformLocation(mProgram,"vMatrix");
        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandle,1,false,mMVPMatrix,0);
        //回去定点着色器的vPostion成员句柄
        mPositonHandle = GLES20.glGetAttribLocation(mProgram,"vPosition");
        GLES20.glEnableVertexAttribArray(mPositonHandle);
        GLES20.glVertexAttribPointer(mPositonHandle,COORDS_PRE_VERTEX,GLES20.GL_FLOAT,false,vertexStride,floatBuffer);

        mColorHandle = GLES20.glGetUniformLocation(mProgram,"vColor");
        GLES20.glUniform4fv(mColorHandle,1,color,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertexCount);
        GLES20.glDisableVertexAttribArray(mPositonHandle);

    }
}
