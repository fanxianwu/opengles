package com.fanfan.opengl.geometry.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Oval extends Shape{

    private float height;
    private final int N = 360;
    private final int RADIUS = 1;
    private FloatBuffer vertexBuff;
    private final int BYTEPERVERTEX = 4;

    private final String vertexShader = "attribute vec4 vPosition;"+
            "uniform mat4 vMatrix;"+
            "void main() {"+
            " gl_Position = vMatrix*vPosition;"
            +" }";

    private final String fragmentShader = "precision mediump float;"+
            "uniform vec4 vColor;"+
            "void main() {"+
            " gl_FragColor = vColor;"
            +" }";

    private int vertexHandle;
    private int fragmentHandle;
    private int program;

    private int mMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;

    private float mProjectMatrix[] = new float[16];
    private float mCamerialMatrix[] = new float[16];
    private float mViewMatrix[] = new float[16];

    private float dataPos[];

    private float color[] = {1.0f,1.0f,1.0f,1.0f};

    public Oval(View view){
        this(view,0);
    }

    public Oval(View view,float height) {
        super(view);
        this.height = height;

        dataPos = createPos();
        ByteBuffer bb = ByteBuffer.allocateDirect(dataPos.length*BYTEPERVERTEX);
        bb.order(ByteOrder.nativeOrder());
        vertexBuff = bb.asFloatBuffer();
        vertexBuff.put(dataPos);
        vertexBuff.position(0);

        vertexHandle = loadShader(GLES20.GL_VERTEX_SHADER,vertexShader);
        fragmentHandle = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShader);
        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program,vertexHandle);
        GLES20.glAttachShader(program,fragmentHandle);
        GLES20.glLinkProgram(program);
    }

    private float[] createPos(){
        List<Float> pos = new ArrayList();
        pos.add(0.0f);
        pos.add(0.0f);
        pos.add(height);
        float angle = 360f/N;
        for(float i = 0;i < 360+angle;i+=angle){
            pos.add((float) (Math.sin(i*Math.PI/180f)*RADIUS));
            pos.add((float) (Math.cos(i*Math.PI/180f)*RADIUS));
            pos.add(height);
        }
        float posData[] = new float[pos.size()];
        for(int num = 0;num < pos.size();num++){
            posData[num] = (float) pos.get(num);
        }
        return posData;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio = width > height?(float)width/height:(float)height/width;
        Matrix.setLookAtM(mViewMatrix,0,1.0f,-10.0f,-4.0f,0,0,0,0,1,0);
        if(width > height){
            Matrix.frustumM(mProjectMatrix,0,-ratio,ratio,-1,1,3,20);
        }else{
            Matrix.frustumM(mProjectMatrix,0,-1,1,-ratio,ratio,3,20);
        }
        Matrix.multiplyMM(mViewMatrix,0,mProjectMatrix,0,mCamerialMatrix,0);
    }

    public void setMatrix(float[] matrix){
        this.mViewMatrix=matrix;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(program);
        mMatrixHandle = GLES20.glGetUniformLocation(program,"vMatrix");
        GLES20.glUniformMatrix4fv(mMatrixHandle,1,false,mViewMatrix,0);
        mPositionHandle = GLES20.glGetAttribLocation(program,"vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle,3,GLES20.GL_FLOAT,false,0,vertexBuff);
       // GLES20.glVertexAttrib4fv(mPositionHandle,vertexBuff);
        mColorHandle = GLES20.glGetUniformLocation(program,"vColor");
        GLES20.glUniform4fv(mColorHandle,1,color,0);
        //开始画三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,dataPos.length/3);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
