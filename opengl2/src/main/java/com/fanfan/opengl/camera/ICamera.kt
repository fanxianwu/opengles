package com.fanfan.opengl.camera

import android.graphics.Point
import android.graphics.SurfaceTexture

interface ICamera {
    fun open(cameraId:Int):Boolean
    fun setConfig(config:Config)
    fun preview():Boolean
    fun switchTo(cameraId:Int)
    fun takePhoto(callback:TakePhotoCallback)
    fun close():Boolean
    fun setPreviewTexture(texture:SurfaceTexture)

    fun getPreviewSize():Point
    fun getPictureSize():Point


    data class Config(var rate:Float,var minPreviewWidth:Int,var minPictureWidth:Int)

    interface TakePhotoCallback{
        fun onTakePhoto(bytes:ByteArray,width:Int,height:Int)
    }

    interface PreviewFrameCallback{
        fun onPreviewFrame(bytes:ByteArray,width:Int,height:Int)
    }

}