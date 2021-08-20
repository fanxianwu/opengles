package com.fanfan.opengl.fbo

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.fanfan.opengl.R
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.RequestCallback
import java.lang.Exception
import java.nio.ByteBuffer


class FboActivity:AppCompatActivity(),FboRender.FrameBuffInterface {

    private  val TAG = FboActivity::class.java.simpleName
    private lateinit var selectBtn:Button
    private lateinit var glSurfaceView: GLSurfaceView
    private lateinit var imageView: ImageView
    private lateinit var fboRender:FboRender
    var picPath:String? = null
    private var bitmapWidth  = 0
    private var bitmapHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fbo)
        PermissionX.init(this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .request(object :RequestCallback{
                override fun onResult(
                    allGranted: Boolean,
                    grantedList: MutableList<String>,
                    deniedList: MutableList<String>
                ) {
                    if(allGranted) init()
                }
            })
    }

    //权限监测

    fun init(){
        selectBtn = findViewById(R.id.fbo_btn)
        glSurfaceView = findViewById(R.id.fbo_glView)
        imageView = findViewById(R.id.fbo_imgView)

        selectBtn.setOnClickListener {
            val picIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(picIntent,100)
        }

        glSurfaceView.setEGLContextClientVersion(2)
        fboRender = FboRender(this)
        fboRender.frameBuffInterface = this
        glSurfaceView.setRenderer(fboRender)
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == RESULT_OK){
            val uri = data?.data
            Log.i(TAG,"uri:${uri.toString()}")
            val projection = arrayOf(MediaStore.Images.ImageColumns.DATA)
            val curser = contentResolver.query(uri!!,projection,null,null,null)
            if(curser != null){
                curser.moveToFirst()
                val index = curser.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                 picPath = curser.getString(index)
                Log.i(TAG,"picPath:${picPath}")
                val bitmap = BitmapFactory.decodeFile(picPath)
                bitmapWidth = bitmap.width
                bitmapHeight = bitmap.height
                fboRender.bitmap = bitmap
                glSurfaceView.requestRender()
                curser.close()
            }
        }
    }

    override fun call(byteBuffer: ByteBuffer) {
        try {
            Log.i(TAG,"getbitMap")
            val bitmap = Bitmap.createBitmap(bitmapWidth,bitmapHeight,Bitmap.Config.ARGB_8888)
            bitmap.copyPixelsFromBuffer(byteBuffer)
       //     val byteArray = byteBuffer.array()
       //     val bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
           // val bitmap = BitmapFactory.decodeFile(picPath!!)
            if(bitmap != null){
                runOnUiThread{
                    imageView.setImageBitmap(bitmap)
                }
            }
        }catch (e:Exception){
            Log.e(TAG,"call error:${e.toString()}")
        }
    }
}