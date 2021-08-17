package com.fanfan.opengl.camera

import android.graphics.Point
import android.graphics.SurfaceTexture
import android.hardware.Camera
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.Comparator

class KitkatCamera:ICamera {
    private lateinit var mConfig:ICamera.Config
    private lateinit var mCamera: Camera
    private lateinit var picSize:android.hardware.Camera.Size
    private lateinit var preSize:Camera.Size
    private lateinit var mPicSize:Point
    private lateinit var mPreSize:Point
    private lateinit var sizeComparator:CameraSizeComparator;

    init {
        mConfig = ICamera.Config(1.778f,720,720)
        sizeComparator = CameraSizeComparator()
    }

    override fun open(cameraId: Int): Boolean {
        mCamera = Camera.open(cameraId)
        if(mCamera != null){
            val param = mCamera.parameters
            picSize = getPropPreviewSize(param.supportedPictureSizes,mConfig.rate,
            mConfig.minPictureWidth)
            preSize = getPropPreviewSize(param.supportedPreviewSizes,mConfig.rate,
            mConfig.minPreviewWidth)
            param.setPictureSize(picSize.width,picSize.height)
            param.setPreviewSize(preSize.width,preSize.height)
            mCamera.parameters = param

            val pre = param.previewSize
            val pic = param.pictureSize
            mPicSize = Point(pic.height,pic.width)
            mPreSize = Point(pre.height,pre.width)
            return true
        }
        return false
    }

    private fun getPropPreviewSize(list:List<Camera.Size>,th:Float,minWidth:Int):Camera.Size{
        Collections.sort(list,sizeComparator)
        var i = 0
        for(s in list){
            if(s.height >= minWidth && equalRate(s,th)){
                break
            }
            i++
        }
        if(i == list.size){
            i = 0
        }
        return list.get(i)
    }

    private fun equalRate(s:Camera.Size,rate:Float):Boolean{
        val r = s.width/s.height.toFloat()
        if(Math.abs(r-rate) <= 0.03){
            return true
        }else{
            return false
        }
    }

    override fun setConfig(config: ICamera.Config) {
        this.mConfig = config
    }

    override fun preview():Boolean {
        if(mCamera != null){
            mCamera.startPreview()
        }
        return false
    }

    override fun switchTo(cameraId: Int) {
        close()
        open(cameraId)
    }

    override fun takePhoto(callback: ICamera.TakePhotoCallback) {
        TODO("Not yet implemented")
    }

    override fun close(): Boolean {
        if(mCamera != null){
            try {
                mCamera.stopPreview()
                mCamera.release()
                return true
            }catch (e:Exception){
                e.printStackTrace()
                return false
            }

        }
        return false
    }

    override fun setPreviewTexture(texture: SurfaceTexture) {
        if(mCamera != null){
            try {
                mCamera.setPreviewTexture(texture)
            }catch (e:IOException){
                e.printStackTrace()
            }
        }
    }

    override fun getPreviewSize(): Point {
        return mPreSize
    }

    override fun getPictureSize(): Point {
        return mPicSize
    }

    private class CameraSizeComparator:Comparator<Camera.Size>{
        override fun compare(lhs: Camera.Size?, rhs: Camera.Size?): Int {
            if(lhs!!.height == rhs!!.height){
                return 0
            }else if(lhs!!.height > rhs!!.height){
                return 1
            }else{
                return -1
            }
        }
    }
}