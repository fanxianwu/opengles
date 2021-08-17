package com.fanfan.opengl.filter

import MatrixUtils
import android.content.res.Resources
import android.opengl.GLES20
import android.util.Log
import android.util.SparseArray
import com.fanfan.opengl.utils.ShaderUtils
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.lang.Exception
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

open abstract class AFilter(mRes:Resources){

    private val TAG = "Filter"
    companion object{
        val KEY_OUT = 0x101
        val KEY_IN = 0x102
        val KEY_INDEX = 0x201
        val OM = MatrixUtils.getOriginalMatrix()
    }

    /**
     * 程序句柄
     */
    protected var mProgram:Int = 0

    /**
     * 定点坐标句柄
     */
    protected var mHPosition:Int = 0

    /**
     * 纹理坐标句柄
     */

    protected var mHCoord:Int = 0

    /**
     * 总变换矩阵句柄
     */

    protected var mHMatrix:Int = 0

    /**
     * 默认纹理贴图坐标句柄
     */
    protected var mHTexture:Int = 0

    protected lateinit var res:Resources

    /**
     * 顶点坐标buff
     */
    protected lateinit var mVerBuffer:FloatBuffer

    /**
     * 纹理坐标buff
     */
    protected lateinit var mTexBuffer:FloatBuffer

    /**
     * 索引坐标buff
     */

    protected lateinit var mindexBuffer:ShortArray

    protected var mFlat:Int = 0

    private var matrix = Arrays.copyOf(OM,16)

    protected var textrueType = 0

    protected var textureId = 0

    //顶点坐标
    private val sPos = floatArrayOf(
        -1.0f,1.0f,
        -1.0f,-1.0f,
        1.0f,1.0f,
        1.0f,-1.0f
    )
    //纹理坐标
    private val sCoord = floatArrayOf(
        0.0f,0.0f,
        0.0f,1.0f,
        1.0f,0.0f,
        1.0f,1.0f
    )

    private lateinit var mBools:SparseArray<BooleanArray>
    private lateinit var mInts:SparseArray<IntArray>
    private lateinit var mFloats:SparseArray<FloatArray>

    init {
        this.res = mRes
        initBuffer()
    }

    protected fun initBuffer(){
        var a = ByteBuffer.allocateDirect(sPos.size*4)
        a.order(ByteOrder.nativeOrder())
        mVerBuffer = a.asFloatBuffer()
        mVerBuffer.put(sPos)
        mVerBuffer.position(0)
        var b = ByteBuffer.allocateDirect(sCoord.size*4)
        b.order(ByteOrder.nativeOrder())
        mTexBuffer= b.asFloatBuffer()
        mTexBuffer.put(sCoord)
        mTexBuffer.position(0)
    }

    fun create(){
        onCreate()
    }

    fun setSize(width:Int,height:Int){
        onSizeChanged(width,height)
    }

    protected fun onClear(){
        GLES20.glClearColor(1.0f,1.0f,1.0f,1.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

    }

    protected fun onUserProgram(){
        GLES20.glUseProgram(mProgram)
    }

    open protected fun onSetExpandData(){
        GLES20.glUniformMatrix4fv(mHMatrix,1,false,matrix,0)
    }

    open protected fun onBindTexture(){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 +textrueType)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId)
        GLES20.glUniform1i(mHTexture,textrueType)
    }

    protected fun onDraw(){
        GLES20.glEnableVertexAttribArray(mHPosition)
        GLES20.glVertexAttribPointer(mHPosition,2,GLES20.GL_FLOAT,false,0,mVerBuffer)
        GLES20.glEnableVertexAttribArray(mHCoord)
        GLES20.glVertexAttribPointer(mHCoord,2,GLES20.GL_FLOAT,false,0,mTexBuffer)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4)
        GLES20.glDisableVertexAttribArray(mHCoord)
        GLES20.glDisableVertexAttribArray(mHPosition)
    }

    fun draw(){
        onClear()
        onUserProgram()
        onSetExpandData()
        onBindTexture()
        onDraw()
    }

    fun setFloat(type:Int,params:FloatArray){
        if(mFloats == null){
            mFloats = SparseArray()
        }
        mFloats.put(type,params)
    }

    fun setInt(type:Int,params:IntArray){
        if(mInts == null){
            mInts = SparseArray()
        }
        mInts.put(type,params)
    }

    fun setBool(type:Int,params:BooleanArray){
        if(mBools == null){
            mBools = SparseArray()
        }
        mBools.put(type,params)
    }

    fun getBool(type:Int,index:Int):Boolean{
        if(mBools == null){
            return false
        }
        val bArray = mBools.get(type)
        return !(bArray == null || bArray.size <= index) && bArray.get(index)
    }

    fun getInt(type:Int,index:Int):Int{
        if(mInts == null){
            return 0
        }
        val bArray = mInts.get(type)
        if(bArray == null || bArray.size <= index){
            return 0
        }
        return bArray.get(index)
    }



    protected fun createProgram(vertex:String,fragment:String){
        mProgram =  uCreateGlProgram(vertex,fragment)
        if(mProgram != null){
            mHPosition = GLES20.glGetAttribLocation(mProgram,"vPosition")
            mHCoord = GLES20.glGetAttribLocation(mProgram,"vCoord")
            mHMatrix = GLES20.glGetUniformLocation(mProgram,"vMatrix")
            mHTexture = GLES20.glGetUniformLocation(mProgram,"vTexture")
        }
    }

    private fun uCreateGlProgram(vertex:String,fragment:String):Int{
        val vertexStr = loadShaderFromAsset(vertex)
        val fragmentStr = loadShaderFromAsset(fragment)
        if(vertexStr != null && fragmentStr != null){
            val vertexShader = uCreateShader(GLES20.GL_VERTEX_SHADER,vertexStr)
            val fragmentShader = uCreateShader(GLES20.GL_FRAGMENT_SHADER,fragmentStr)
            if(vertexShader != null && fragmentShader != null){
                var tmpProgram = GLES20.glCreateProgram()
                if(tmpProgram !=0){
                    GLES20.glAttachShader(tmpProgram,vertexShader)
                    GLES20.glAttachShader(tmpProgram,fragmentShader)
                    GLES20.glLinkProgram(tmpProgram)
                    var status = IntArray(1)
                    GLES20.glGetProgramiv(tmpProgram,GLES20.GL_COMPILE_STATUS,
                    status,0)
                    if(status[0] != GLES20.GL_TRUE){
                        glError(1,"Could not link program:${GLES20.glGetProgramInfoLog(tmpProgram)}")
                        tmpProgram = 0
                    }
                }
                return tmpProgram
            }
        }
        return 0
    }

    fun glError(code:Int,index:Any){
        if(code != 0){
            Log.e(TAG,"glError:$code-----$index")
        }
    }

    private fun loadShaderFromAsset(assetName:String):String?{
        var inPutStream:InputStream? = null
        var bytes = ByteArray(1024)
        var arrayBuff = ByteArrayOutputStream()
        if(res != null){
            try {
                inPutStream = res.assets.open(assetName)
                var readNum = inPutStream.read(bytes)
                while(readNum != -1) {
                    arrayBuff.write(bytes, 0, readNum)
                    readNum = inPutStream.read(bytes)
                }
                arrayBuff.toString().replace("\r\n","\n")
            }catch (e:Exception){
                Log.e(TAG,"read assetFile error:${e.toString()}")
            }finally {
                if(inPutStream !=null){
                    inPutStream.close()
                }
            }
        }
        return null
    }

    private fun uCreateShader(type:Int,shaderStr:String):Int{
        var shader = GLES20.glCreateShader(type)
        if(shader != 0){
            GLES20.glShaderSource(shader,shaderStr)
            GLES20.glCompileShader(shader)
            var compiled = IntArray(1)
            GLES20.glGetShaderiv(shader,GLES20.GL_COMPILE_STATUS,compiled,0)
            if(compiled[0] == 0){
                glError(1,"Could not compiled shader:${type}")
                glError(1,"GLES20 Error:${GLES20.glGetShaderInfoLog(shader)}")
                GLES20.glDeleteShader(shader)
                shader = 0
            }
        }
        return shader
    }


    protected abstract fun onCreate()
    protected abstract fun onSizeChanged(width:Int,height:Int)

}