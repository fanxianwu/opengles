package com.fanfan.opengl.chartlet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.fanfan.opengl.utils.ShaderUtils;
import com.fanfan.opengl.utils.TextureUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ChartletRender implements GLSurfaceView.Renderer {

    private final String TAG = ChartletRender.class.getSimpleName();
    private Bitmap bitmap;
    private Context mContext;

    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMvpMatrix = new float[16];

    private int mvpMatrixHandle;

    private int vertexNum;
    private int coordinateNum;

    private FloatBuffer vertexBuff;

    private int mProgram;

    private int mTextureId;

    private int mVertexHandle;
    private int mCoordinateHandle;
    private int samplerHandle;

    private float[] vertexData = {
        -1.0f,-1.0f,0f,
        1.0f,-1.0f,0f,
        -1.0f,1.0f,0f,
        1.0f,1.0f,0f
    };

    private FloatBuffer coordinateBuff;

    private float[] coordinateData = {
        0f,1f,
        1f,1f,
        0f,0f,
        1f,0f
    };

    public ChartletRender(Context context){
        mContext = context;
        ByteBuffer bb = ByteBuffer.allocateDirect(vertexData.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuff = bb.asFloatBuffer();
        vertexBuff.put(vertexData);
        vertexBuff.position(0);
        vertexNum = vertexData.length / 3;

        ByteBuffer dd = ByteBuffer.allocateDirect(coordinateData.length * 4);
        dd.order(ByteOrder.nativeOrder());
        coordinateBuff = dd.asFloatBuffer();
        coordinateBuff.put(coordinateData);
        coordinateBuff.position(0);
        coordinateNum = coordinateData.length / 2;

        bitmap = decodeBitMap();
    }

    private Bitmap decodeBitMap(){
        InputStream inputStream = null;
        try{
          inputStream =  mContext.getAssets().open("picture/mv.jpg");
          return BitmapFactory.decodeStream(inputStream);
        }catch (IOException e){
            Log.i(TAG,"decode bitmap error:"+e.toString());
        }finally {
            if(inputStream != null){
                try{
                    inputStream.close();
                }catch (IOException e){

                }

            }
        }
        return null;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f,1.0f,1.0f,1.0f);
        mProgram = ShaderUtils.createProgram(mContext.getResources(),"vshader/vchartlet.glsl", "fshader/fchartlet.glsl");
        mVertexHandle = GLES20.glGetAttribLocation(mProgram,"vPosition");
        mCoordinateHandle = GLES20.glGetAttribLocation(mProgram,"vCoordinate");
        mvpMatrixHandle = GLES20.glGetUniformLocation(mProgram,"mvpProject");
        samplerHandle =   GLES20.glGetUniformLocation(mProgram,"vTexture");
        mTextureId = TextureUtils.createTexture(bitmap);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置变换矩阵
        float imgRadio = bitmap.getWidth() / (float)bitmap.getHeight();
        float screenRadio = width / (float)height;
        //判断还是不很完善,例子可以有效果
        if(width > height){
            //横屏
            if(imgRadio > screenRadio){
                Matrix.orthoM(mProjectMatrix,0,-imgRadio*screenRadio,imgRadio*screenRadio,-1,1,3,8);
            }else{
                Matrix.orthoM(mProjectMatrix,0,-screenRadio/imgRadio,screenRadio/imgRadio,-1,1,3,8);
            }
        }else{
            if(imgRadio > screenRadio && imgRadio > 1.0){
                //竖屏但是图片是横屏
                Matrix.orthoM(mProjectMatrix,0,-1,1,-imgRadio/(screenRadio),imgRadio/screenRadio,3,8);
            }else if(imgRadio > screenRadio && imgRadio <=1.0){
                //屏幕和图片都是竖屏
                Matrix.orthoM(mProjectMatrix,0,-1,1,-(screenRadio*imgRadio),1/(screenRadio*imgRadio),3,8);
            }else{
                Matrix.orthoM(mProjectMatrix,0,-1,1,-imgRadio/screenRadio,imgRadio/screenRadio,3,8);
            }
        }
        Matrix.setLookAtM(mViewMatrix,0,0f,0f,7f,0f,0f,0f,0f,1f,0f);
        Matrix.multiplyMM(mMvpMatrix,0,mProjectMatrix,0,mViewMatrix,0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        Log.i(TAG,"onDraw mprogram:"+mProgram);

        GLES20.glEnableVertexAttribArray(mVertexHandle);
        GLES20.glEnableVertexAttribArray(mCoordinateHandle);
        GLES20.glVertexAttribPointer(mVertexHandle,3,GLES20.GL_FLOAT,false,0,vertexBuff);

        GLES20.glVertexAttribPointer(mCoordinateHandle,2,GLES20.GL_FLOAT,false,0,coordinateBuff);
        GLES20.glUniformMatrix4fv(mvpMatrixHandle,1,false,mMvpMatrix,0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,mTextureId);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform1i(samplerHandle,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,vertexNum);
        GLES20.glDisableVertexAttribArray(mVertexHandle);
        GLES20.glDisableVertexAttribArray(mCoordinateHandle);
    }
}
