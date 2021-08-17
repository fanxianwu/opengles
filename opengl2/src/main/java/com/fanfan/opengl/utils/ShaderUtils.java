package com.fanfan.opengl.utils;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

import java.io.InputStream;

public class ShaderUtils {
    private final static String TAG = "ShaderUtils";

    private static int loadShader(int type,String shaderStr){
        int shader = GLES20.glCreateShader(type);
        if(shader != 0) {
            GLES20.glShaderSource(shader, shaderStr);
            GLES20.glCompileShader(shader);
            int compiled[] = new int[1];
            GLES20.glGetShaderiv(shader,GLES20.GL_COMPILE_STATUS,compiled,0);
            if(compiled[0] != GLES20.GL_TRUE){
                Log.e(TAG,"Could not compile shader:"+shader);
                Log.e(TAG,"GLES20 Error:"+GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }


    public static int createProgram(Resources res,String vertexName,String fragmentName){
        String vertexStr = loadFromAsset(res,vertexName);
        String fragmentStr = loadFromAsset(res,fragmentName);
        int vertex = loadShader(GLES20.GL_VERTEX_SHADER,vertexStr);
        if(vertex == 0){
            return 0;
        }
        int fragment = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentStr);
        if(fragment == 0){
            return  0;
        }
        int program = GLES20.glCreateProgram();
        if(program != 0){
            GLES20.glAttachShader(program,vertex);
            GLES20.glAttachShader(program,fragment);
            GLES20.glLinkProgram(program);
            int [] linkStatus = new int[1];
            GLES20.glGetProgramiv(program,GLES20.GL_LINK_STATUS,linkStatus,0);
            if(linkStatus[0] != GLES20.GL_TRUE){
                Log.e(TAG,"Could not link program:"+GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    private static String loadFromAsset(Resources res,String assetName){
        StringBuilder strBuild = new StringBuilder();
        try{
            InputStream input = res.getAssets().open(assetName);
            byte datas[] = new byte[1024];
            int redNum;
            while((redNum = input.read(datas))!=-1){
                strBuild.append(new String(datas,0,redNum));
            }
            input.close();
        }catch (Exception e){
            Log.i(TAG,"loadFromAsset error:"+e.toString());
        }
        return strBuild.toString().replaceAll("\\r\\n","\n");
    }
}
