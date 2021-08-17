package com.fanfan.opengl.camera

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CameraView(context: Context?):GLSurfaceView(context),GLSurfaceView.Renderer {

    constructor(context: Context?, attrs: AttributeSet?) : this(context)

    private lateinit var mCamera2:KitkatCamera
    private lateinit var mCameraDrawer:CameraDrawer
    private var cameraId = 1
    private lateinit var mRunnable:Runnable

    init {
        initData()
    }

    fun initData(){
        setEGLContextClientVersion(2)
        setRenderer(this)
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        mCamera2 = KitkatCamera()
       // mCameraDrawer = CameraDrawer()
    }



    override fun onDrawFrame(gl: GL10?) {

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {

    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

    }
}