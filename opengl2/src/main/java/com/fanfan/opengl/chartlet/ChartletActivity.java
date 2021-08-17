package com.fanfan.opengl.chartlet;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fanfan.opengl.R;

public class ChartletActivity extends AppCompatActivity {

    private ChartletRender chartletRender;
    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chartlet);
        init();
    }

    private void init(){
        glSurfaceView = findViewById(R.id.gl_chartlet);
        glSurfaceView.setEGLContextClientVersion(2);
        chartletRender = new ChartletRender(this);
        glSurfaceView.setRenderer(chartletRender);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        glSurfaceView.requestRender();
//        glSurfaceView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                glSurfaceView.requestRender();
//            }
//        },5000);
    }
}
