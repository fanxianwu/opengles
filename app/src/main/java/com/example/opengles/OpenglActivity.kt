package com.example.opengles

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class OpenglActivity:AppCompatActivity(),ViewTreeObserver.OnGlobalLayoutListener {

    private lateinit var  myGLSurfaceView:MyGlSurfaceView
    private var mySurfaceRender = MySurfaceRender()
    private lateinit var rootView:RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opengl)
        rootView = findViewById(R.id.rootView)
        rootView.viewTreeObserver.addOnGlobalLayoutListener(this)
        mySurfaceRender.init()

    }

    override fun onGlobalLayout() {
        rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
        var layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT)
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        myGLSurfaceView = MyGlSurfaceView(this)
        myGLSurfaceView.setRenderer(mySurfaceRender)
        myGLSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        rootView.addView(myGLSurfaceView,layoutParams)
    }

    override fun onDestroy() {
        super.onDestroy()
        mySurfaceRender.unInit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_change_setting){

        }
        return true
    }

    fun showGlSampleDialog(){
        var alertDialog = AlertDialog.Builder(this)
        var inflater = LayoutInflater.from(this)
        val rootView = inflater.inflate()
    }


}