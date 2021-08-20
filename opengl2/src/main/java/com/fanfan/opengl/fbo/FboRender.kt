package com.fanfan.opengl.fbo

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.util.Log
import android.util.MutableInt
import androidx.core.content.contentValuesOf
import com.fanfan.opengl.filter.GrayFilter
import java.nio.ByteBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class FboRender(val mContext:Context):GLSurfaceView.Renderer {

     var frameBuffInterface:FrameBuffInterface? = null
     var bitmap:Bitmap? = null
     val grayFilter:GrayFilter = GrayFilter(mContext.resources)
    private var frameBuffId = intArrayOf(0)
    private var renderBuffId = intArrayOf(0)
    private var textures = intArrayOf(0,0)
    private var byteBuffer:ByteBuffer? = null

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        grayFilter.create()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {

    }

    override fun onDrawFrame(gl: GL10?) {
        Log.i("FboRender","bitmap:${bitmap}")

        if(bitmap != null && !bitmap!!.isRecycled) {
            GLES20.glViewport(0, 0, bitmap!!.width, bitmap!!.height)
            createBuff()
            grayFilter.textureId = textures[0]
            grayFilter.draw()
            //从framebuff 中读取数据
            GLES20.glReadPixels(0, 0, bitmap!!.width, bitmap!!.height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, byteBuffer)
            if (frameBuffInterface != null) {
                frameBuffInterface!!.call(byteBuffer!!)
            }
            deleteBuff()
            bitmap!!.recycle()
        }
    }

    fun createBuff(){
        GLES20.glGenFramebuffers(1,frameBuffId,0)

        GLES20.glGenRenderbuffers(1,renderBuffId,0)
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER,renderBuffId[0])
        //设置renderbuff 大小
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER,GLES20.GL_DEPTH_COMPONENT16,bitmap!!.width,bitmap!!.height)

     //   GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER,0)
        //创建纹理
        GLES20.glGenTextures(2,textures,0)
        for((num,texture) in textures.withIndex()){
            //绑定纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE)
            if(num == 0){
                //设置数据为bitmap
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGBA,bitmap!!,0)
            }else{
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGBA,bitmap!!.width,bitmap!!.height,0,GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE,null)
            }
        }
        byteBuffer = ByteBuffer.allocate(bitmap!!.width*bitmap!!.height*4)
       // byteBuffer!!.position(0)

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,frameBuffId[0])

        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,GLES20.GL_COLOR_ATTACHMENT0,GLES20.GL_TEXTURE_2D,textures[1],0)
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER,GLES20.GL_DEPTH_ATTACHMENT,GLES20.GL_RENDERBUFFER,renderBuffId[0])
    }

    fun deleteBuff(){
        GLES20.glDeleteTextures(2,textures,0)
        GLES20.glDeleteRenderbuffers(1,renderBuffId,0)
        GLES20.glDeleteFramebuffers(1,frameBuffId,0)
    }

    interface FrameBuffInterface{
        fun call(byteBuffer: ByteBuffer)
    }
}