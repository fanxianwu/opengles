package com.fanfan.opengl.geometry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fanfan.opengl.ChooseActivity;
import com.fanfan.opengl.R;
import com.fanfan.opengl.geometry.render.FGLSurfaceView;

public class FGLViewActivity extends AppCompatActivity implements View.OnClickListener {

    private Button changeButton;
    private FGLSurfaceView fglSurfaceView;
    private final int REQESTCODE = 1000;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fglview);
        changeButton = findViewById(R.id.open_change);
        fglSurfaceView = findViewById(R.id.open_sv);
        changeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent choseIntent = new Intent(this, ChooseActivity.class);
        startActivityForResult(choseIntent,REQESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQESTCODE ){
            Class clazz = (Class) data.getSerializableExtra("clazzName");
            fglSurfaceView.setShape(clazz);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fglSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        fglSurfaceView.onPause();
    }
}
