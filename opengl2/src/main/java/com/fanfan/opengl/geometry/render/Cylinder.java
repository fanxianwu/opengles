package com.fanfan.opengl.geometry.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.View;

import com.fanfan.opengl.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class Cylinder extends Shape {

    private float[] dataPos;
    private FloatBuffer vertexBuff;
    private final int N = 360;
    private final float height= 2.0f;
    private final float radius = 1.0f;

    private Oval frontOval;
    private Oval backOval;

    private int mProgram;

    private float[] mViewMatrix = new float[16];
    private float[] mCameraMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];

    public Cylinder(View view) {
        super(view);
        frontOval = new Oval(view,height);
        backOval = new Oval(view,0);
        dataPos = createPos();
        ByteBuffer bb = ByteBuffer.allocateDirect(dataPos.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuff = bb.asFloatBuffer();
        vertexBuff.put(dataPos);
        vertexBuff.position(0);

    }

    private float[] createPos(){
        float increase = 360f/N;
        List<Float> arrayPos = new ArrayList<>();
        for(float i = 0;i< 360+increase;i+=increase){
            arrayPos.add((float) (radius*Math.sin(i*Math.PI/180f)));
            arrayPos.add((float) (radius*Math.cos(i*Math.PI/180f)));
            arrayPos.add(height);

            arrayPos.add((float) (radius*Math.sin(i*Math.PI/180f)));
            arrayPos.add((float) (radius*Math.cos(i*Math.PI/180f)));
            arrayPos.add(0f);
        }
        float posData[] = new float[arrayPos.size()];
        for(int num = 0;num < posData.length;num++){
            posData[num] = arrayPos.get(num);
        }
        return posData;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //开启深度test
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        mProgram = ShaderUtils.createProgram(mView.getResources(),"vshader/Cone.glsl","fshader/Cone.glsl");
        frontOval.onSurfaceCreated(gl,config);
        backOval.onSurfaceCreated(gl,config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio = width > height?(float)width/height:(float)height/width;
        Matrix.setLookAtM(mViewMatrix,0,4.0f,4.0f,-4.0f,0,0,0,0,1,0);
        if(width > height){
            Matrix.frustumM(mProjectMatrix,0,-ratio,ratio,-1,1,3,20);
        }else{
            Matrix.frustumM(mProjectMatrix,0,-1,1,-ratio,ratio,3f,20f);
        }

        Matrix.multiplyMM(mViewMatrix,0,mProjectMatrix,0,mViewMatrix,0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgram);
        int matrixHandle = GLES20.glGetUniformLocation(mProgram,"vMatrix");
        GLES20.glUniformMatrix4fv(matrixHandle,1,false,mViewMatrix,0);
        int positionHandle = GLES20.glGetAttribLocation(mProgram,"vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle,3,GLES20.GL_FLOAT,false,0,vertexBuff);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,dataPos.length/3);
        GLES20.glDisableVertexAttribArray(positionHandle);
        frontOval.setMatrix(mViewMatrix);
        backOval.setMatrix(mViewMatrix);
        frontOval.onDrawFrame(gl);
        backOval.onDrawFrame(gl);
    }
}
