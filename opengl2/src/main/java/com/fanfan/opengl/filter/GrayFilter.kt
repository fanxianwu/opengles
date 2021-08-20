package com.fanfan.opengl.filter

import android.content.res.Resources

class GrayFilter(res:Resources) :AFilter(res) {

    override fun onCreate() {
        createProgram("vshader/filter_base_vertex.glsl","fshader/color_gray.glsl")
    }

    override fun onSizeChanged(width: Int, height: Int) {

    }
}