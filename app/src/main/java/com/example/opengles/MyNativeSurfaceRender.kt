package com.example.opengles

class MyNativeSurfaceRender {

    init {
        System.loadLibrary("native-render")
    }

    external fun native_init()
    external fun native_uninit()
    external fun native_onSurfaceCreate()
    external fun native_onSurfaceChange(width:Int,height:Int)
    external fun native_onDrawFrame()

}