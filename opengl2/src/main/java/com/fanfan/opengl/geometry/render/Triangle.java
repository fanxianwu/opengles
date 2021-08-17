package com.fanfan.opengl.geometry.render;

import android.opengl.GLES20;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Triangle extends Shape {

    //三角形定点数据
    private FloatBuffer vertexBuffer;

    private int mProgram;

    private int mPositionHandle;
    private int mColorHandle;

    private float[] mViewMatrix = new float[16];

    //定点个数
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    //定点之间的偏移量
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    float color[] = {1.0f,1.0f,1.0f,1.0f };

    //定点着色器code
    private final String vertexShaderCode=
            "attribute vec4 vPosition;"+
            "void main(){"+
            " gl_Position = vPosition;"+
            "}";
    //片元着色器code
    private final String fragmentShaderCode=
            "precision mediump float;"+
                    "uniform vec4 vColor;"+
                    "void main() {"+
                    " gl_FragColor = vColor;"+
                    "}";

    static final int COORDS_PER_VERTEX = 3;

    static float triangleCoords[]={
      0.5f,0.5f,0.0f, //top
      -0.5f,-0.5f,0.0f, //bottom left
      0.5f,-0.5f,0.0f //bottom right
    };

    public Triangle(View view){
        super(view);
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length*4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        //加载定点着色器
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        //加载片元着色器
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);

        //创建一个空的OpenGLES 程序
        mProgram = GLES20.glCreateProgram();
        //将定点着色器加入到程序
        GLES20.glAttachShader(mProgram,vertexShader);
        //将片元着色器加入到程序
        GLES20.glAttachShader(mProgram,fragmentShader);
        //连接到着色器程序
        GLES20.glLinkProgram(mProgram);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //将程序加入到OpenGLES2.0 环境
        GLES20.glUseProgram(mProgram);

        //获取定点着色器的vPosition 成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram,"vPosition");
        //启动三角形定点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle,COORDS_PER_VERTEX,GLES20.GL_FLOAT,false,vertexStride,vertexBuffer);

        //获取片元着色器的vColor成员的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram,"vColor");
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle,1,color,0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertexCount);
        //禁止定点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }
}
