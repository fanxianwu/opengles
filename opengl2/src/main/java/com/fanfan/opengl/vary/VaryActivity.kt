package com.fanfan.opengl.vary

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.fanfan.opengl.R

class VaryActivity : AppCompatActivity() {

    private lateinit var  mGlView:GLSurfaceView
    private lateinit var render:VaryRender

    fun initGL(){
        mGlView = findViewById(R.id.mGLView)
        Log.i("VaryActivity","mGLView is:${mGlView}")
        render = VaryRender(resources)
        mGlView.apply {
            setEGLContextClientVersion(2)
            setRenderer(render)
            renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_opengl)
        initGL()
    }

}