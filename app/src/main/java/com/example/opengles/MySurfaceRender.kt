package com.example.opengles

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MySurfaceRender():GLSurfaceView.Renderer {

    private var nativeSurfaceRender = MyNativeSurfaceRender()


    fun init(){
        nativeSurfaceRender.native_init()
    }

    fun unInit(){
        nativeSurfaceRender.native_uninit()
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        nativeSurfaceRender.native_onSurfaceCreate()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        nativeSurfaceRender.native_onSurfaceChange(width,height)
    }

    override fun onDrawFrame(gl: GL10?) {
        nativeSurfaceRender.native_onDrawFrame()
    }
}