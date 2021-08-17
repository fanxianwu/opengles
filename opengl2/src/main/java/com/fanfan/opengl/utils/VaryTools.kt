package com.fanfan.opengl.utils

import android.opengl.Matrix
import java.util.*

class VaryTools {
    private var mMatrixCamera = FloatArray(16)
    private var mMatrixProjection = FloatArray(16)
    private var mMatrixCurrent = floatArrayOf(
        1f,0f,0f,0f,
        0f,1f,0f,0f,
        0f,0f,1f,0f,
        0f,0f,0f,1f
    )

    private val mStack:Stack<FloatArray> = Stack()

    public fun pushMatrix(){
        mStack.push(Arrays.copyOf(mMatrixCurrent,16))
    }

    public fun popMatrix(){
        mMatrixCurrent = mStack.pop()
    }

    public fun clearStack(){
        mStack.clear()
    }
    fun translate(x:Float,y:Float,z:Float){
        Matrix.translateM(mMatrixCurrent,0,x,y,z)
    }

    fun rotate(angle:Float,x:Float,y:Float,z:Float){
        Matrix.rotateM(mMatrixCurrent,0,angle,x,y,z)
    }

    fun scale(x:Float,y:Float,z:Float){
        Matrix.scaleM(mMatrixCurrent,0,x,y,z)
    }

    fun setCamera(ex:Float,ey:Float,ez:Float,cx:Float,cy:Float,cz:Float
    ,ux:Float,uy:Float,uz:Float){
        Matrix.setLookAtM(mMatrixCamera,0,ex,ey,ez,
        cx,cy,cz,ux,uy,uz)
    }

    fun frustum(left:Float,right:Float,bottom:Float,top:Float,near:Float,
    far:Float){
        Matrix.frustumM(mMatrixProjection,0,left,right,bottom,top,near,far)
    }

    fun ortho(left:Float,right:Float,bottom:Float,top:Float,near:Float,far:Float){
        Matrix.orthoM(mMatrixProjection,0,left,right,bottom, top, near, far)
    }

    fun getFinalMatrix():FloatArray{
        var ans = FloatArray(16)
        Matrix.multiplyMM(ans,0,mMatrixCamera,0,mMatrixCurrent,0)
        Matrix.multiplyMM(ans,0,mMatrixProjection,0,ans,0)
        return ans
    }

}