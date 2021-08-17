package com.fanfan.opengl.vary

import android.content.res.Resources
import android.opengl.GLES20
import android.util.Log
import com.fanfan.opengl.utils.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Cube(var res:Resources) {



    private val cubePositions = arrayOf(
        -1.0f,1.0f,1.0f,
        -1.0f,-1.0f,1.0f,
        1.0f,-1.0f,1.0f,
        1.0f,1.0f,1.0f,
        -1.0f,1.0f,-1.0f,
        -1.0f,-1.0f,-1.0f,
        1.0f,-1.0f,-1.0f,
        1.0f,1.0f,-1.0f
    );

    private val index = shortArrayOf(
        6, 7, 4, 6, 4, 5,    //后面
        6, 3, 7, 6, 2, 3,    //右面
        6, 5, 1, 6, 1, 2,    //下面
        0, 3, 2, 0, 2, 1,    //正面
        0, 1, 5, 0, 5, 4,    //左面
        0, 7, 3, 0, 4, 7    //上面
    )
    private val color  = floatArrayOf(
        0f,1f,0f,1f,
        0f,1f,0f,1f,
        0f,1f,0f,1f,
        0f,1f,0f,1f,
        1f,0f,0f,1f,
        1f,0f,0f,1f,
        1f,0f,0f,1f,
        1f,0f,0f,1f
    )

    private lateinit var vertexBuf:FloatBuffer
    private lateinit var colorBuf:FloatBuffer
    private lateinit var indexBuf:ShortBuffer
      var mProgram:Int = 0
      var hVertex:Int = 0
      var hColor:Int = 0
      var hMatrix:Int = 0

    init {
        initData()
    }

    private fun initData(){
        Log.i("fanfan","cubePositions is ${cubePositions} and res is ${res}")
        val a = ByteBuffer.allocateDirect(cubePositions.size*4)
        a.order(ByteOrder.nativeOrder())
        vertexBuf = a.asFloatBuffer()
        vertexBuf.put(cubePositions.toFloatArray())
        vertexBuf.position(0)
        val b= ByteBuffer.allocateDirect(color.size*4)
        b.order(ByteOrder.nativeOrder())
        colorBuf = b.asFloatBuffer()
        colorBuf.put(color)
        colorBuf.position(0)
        val c = ByteBuffer.allocateDirect(index.size*2)
        c.order(ByteOrder.nativeOrder())
        indexBuf = c.asShortBuffer()
        indexBuf.put(index)
        indexBuf.position(0)
    }

    public fun create(){
        mProgram = ShaderUtils.createProgram(res,"vary/vertex.glsl","vary/fragment.glsl")
        hVertex = GLES20.glGetAttribLocation(mProgram,"vPosition")
        hColor = GLES20.glGetAttribLocation(mProgram,"aColor")
        hMatrix = GLES20.glGetUniformLocation(mProgram,"vMatrix")
    }

    private var matrix:FloatArray? = null
    public fun setMatrix(matrix:FloatArray){
        this.matrix = matrix
    }

    public fun drawSelf(){
        GLES20.glUseProgram(mProgram)
        if (matrix != null) GLES20.glUniformMatrix4fv(hMatrix,1,false,matrix,0)
        GLES20.glEnableVertexAttribArray(hVertex)
        GLES20.glEnableVertexAttribArray(hColor)
        GLES20.glVertexAttribPointer(hVertex,3,GLES20.GL_FLOAT,false,0,vertexBuf)
        GLES20.glVertexAttribPointer(hColor,4,GLES20.GL_FLOAT,false,0,colorBuf)
        //索引法绘制正方形
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,index.size,GLES20.GL_UNSIGNED_SHORT,indexBuf)
        GLES20.glDisableVertexAttribArray(hVertex)
        GLES20.glDisableVertexAttribArray(hColor)
    }

}