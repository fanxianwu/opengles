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
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Ball extends Shape {

    private int mProgram;
    private FloatBuffer vertexBuff;
    private float[] posData;
    private float step = 1.0f;

    private float[] mViewMatrix = new float[16];
    private float[] mCameraMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];

    public Ball(View view) {
        super(view);
        posData = createPosData();
        ByteBuffer bb  = ByteBuffer.allocateDirect(posData.length* 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuff = bb.asFloatBuffer();
        vertexBuff.put(posData);
        vertexBuff.position(0);
    }

    private float[] createPosData(){
        List<Float> posArray = new ArrayList<>();
        //遍历维度
        for(float i = -90;i<90+step;i+=step){
            float temp = (float) Math.cos((Math.PI * i)/180f);
            float z = (float) Math.sin((Math.PI*i)/180f);

            //遍历经度
            float step2=step*2;
            for(float j = 0;j< 360+step;j+=step){
                float x = (float) (temp * Math.cos(j*Math.PI/180f));
                float y = (float) (temp * Math.sin(j*Math.PI/180f));
                posArray.add(x);
                posArray.add(y);
                posArray.add(z);
            }
        }
        float[] posData = new float[posArray.size()];
        for(int num = 0;num < posData.length;num++){
            posData[num] = posArray.get(num);
        }
        return posData;
    }

    private float[] createBallPos(){
        //球以(0,0,0)为中心，以R为半径，则球上任意一点的坐标为
        // ( R * cos(a) * sin(b),y0 = R * sin(a),R * cos(a) * cos(b))
        // 其中，a为圆心到点的线段与xz平面的夹角，b为圆心到点的线段在xz平面的投影与z轴的夹角
        ArrayList<Float> data=new ArrayList<>();
        float r1,r2;
        float h1,h2;
        float sin,cos;
        for(float i=-90;i<90+step;i+=step){
            r1 = (float)Math.cos(i * Math.PI / 180.0);
            r2 = (float)Math.cos((i + step) * Math.PI / 180.0);
            h1 = (float)Math.sin(i * Math.PI / 180.0);
            h2 = (float)Math.sin((i + step) * Math.PI / 180.0);
            // 固定纬度, 360 度旋转遍历一条纬线
            float step2=step*2;
            for (float j = 0.0f; j <360.0f+step; j +=step2 ) {
                cos = (float) Math.cos(j * Math.PI / 180.0);
                sin = -(float) Math.sin(j * Math.PI / 180.0);

                data.add(r1 *cos);
                data.add(r1*sin);
                data.add(h1);
//                data.add(r2*cos);
//                data.add(r2*sin);
//                data.add(h2);

//                data.add(r2 * cos);
//                data.add(h2);
//                data.add(r2 * sin);
//                data.add(r1 * cos);
//                data.add(h1);
//                data.add(r1 * sin);
            }
        }
        float[] f=new float[data.size()];
        for(int i=0;i<f.length;i++){
            f[i]=data.get(i);
        }
        return f;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        mProgram = ShaderUtils.createProgram(mView.getResources(),"vshader/Ball.glsl","fshader/Cone.glsl");

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Matrix.setLookAtM(mCameraMatrix,0,4.0f,4.0f,4.0f,0f,0f,0f,0f,1f,0f);
      //  Matrix.setLookAtM(mCameraMatrix, 0, 1.0f, -10.0f, -4.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        float ratio = width > height ?(float)width/height:(float)height/width;
        if(width > height){
            Matrix.frustumM(mProjectMatrix,0,-ratio,ratio,-1,1,3,20);
        }else{
            Matrix.frustumM(mProjectMatrix,0,-1,1,-ratio,ratio,3,20);
        }
        Matrix.multiplyMM(mViewMatrix,0,mProjectMatrix,0,mCameraMatrix,0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.i("Ball","onDrawFrame");
        GLES20.glUseProgram(mProgram);
        int matrixHandle = GLES20.glGetUniformLocation(mProgram,"vMatrix");
        GLES20.glUniformMatrix4fv(matrixHandle,1,false,mViewMatrix,0);
        int positionHandle = GLES20.glGetAttribLocation(mProgram,"vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle,3,GLES20.GL_FLOAT,false,0,vertexBuff);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,posData.length/3);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
