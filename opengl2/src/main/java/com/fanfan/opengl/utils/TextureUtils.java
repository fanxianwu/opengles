package com.fanfan.opengl.utils;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class TextureUtils {

    public static int createTexture(Bitmap bitmap){
        int textureId = 0;
        if(bitmap == null || bitmap.isRecycled()){
            return textureId;
        }
        int textures[] = new int[1];
        GLES20.glGenTextures(1,textures,0);
        //绑定纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0]);
        //设置纹理取色参数
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmap,0);
       GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        return textures[0];
    }
}
