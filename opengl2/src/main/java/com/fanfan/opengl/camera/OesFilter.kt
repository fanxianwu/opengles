package com.fanfan.opengl.camera

import android.content.res.Resources
import android.opengl.GLES10Ext
import android.opengl.GLES11Ext
import android.opengl.GLES20
import com.fanfan.opengl.filter.AFilter
import java.util.*

class OesFilter(res:Resources):AFilter(res) {
    private var mHCoordMatrix = 0
    private var mCoordMatrix = Arrays.copyOf(OM,16)

    override fun onCreate() {
        createProgram("shader/oes_base_vertex.glsl","shader/oes_base_fragment.glsl")
        mHCoordMatrix = GLES20.glGetUniformLocation(mProgram,"vCoordMatrix")
    }

    fun setCoordMatrix(matrix:FloatArray){
        this.mCoordMatrix = matrix
    }

    override fun onSetExpandData(){
        GLES20.glUniformMatrix4fv(mHCoordMatrix,1,false,mCoordMatrix,0)
    }

    override fun onBindTexture(){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0+textrueType)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,textureId)
       // GLES20.glUniform1i()
    }

    override fun onSizeChanged(width: Int, height: Int) {

    }
}