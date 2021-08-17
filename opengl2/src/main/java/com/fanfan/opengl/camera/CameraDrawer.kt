package com.fanfan.opengl.camera

import android.content.res.Resources
import android.graphics.SurfaceTexture
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CameraDrawer(res:Resources):GLSurfaceView.Renderer {

    private val matrix = FloatArray(16)
    private var surfaceTexture:SurfaceTexture? = null
    private var width:Int = 0
    private var height:Int = 0
    private var dataWidth:Int = 0
    private var dataHeight:Int = 0


    override fun onDrawFrame(gl: GL10?) {

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {

    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

    }
}