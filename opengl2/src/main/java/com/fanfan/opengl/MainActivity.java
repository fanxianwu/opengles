package com.fanfan.opengl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fanfan.opengl.chartlet.ChartletActivity;
import com.fanfan.opengl.fbo.FboActivity;
import com.fanfan.opengl.geometry.activity.FGLViewActivity;
import com.fanfan.opengl.vary.VaryActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecycleView;
    private ArrayList<MenuBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecycleView = findViewById(R.id.open_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(linearLayoutManager);
        addData("绘制形体", FGLViewActivity.class);
        addData("图形变换", VaryActivity.class);
        addData("纹理贴图", ChartletActivity.class);
        addData("FBO", FboActivity.class);
        mRecycleView.setAdapter(new MenuAdapter());
    }

    private void addData(String name,Class rClass){
        MenuBean menuBean = new MenuBean();
        menuBean.name = name;
        menuBean.clazz = rClass;
        data.add(menuBean);
    }



    class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder>{

        @NonNull
        @Override
        public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_view,parent,false);
            return new MenuViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {

                holder.setPostion(position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }


    class MenuViewHolder extends RecyclerView.ViewHolder{
        private Button mButton;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            mButton = itemView.findViewById(R.id.open_bt);
            ColorDrawable colorDrawable = new ColorDrawable(R.color.white);
            mButton.setBackground(colorDrawable);
          //  mButton.setBackgroundColor();
        }
        public void setPostion(int position){
            mButton.setText(data.get(position).name);
            mButton.setOnClickListener(MainActivity.this);
            mButton.setTag(position);
        }
    }



    private class MenuBean{
        String name;
        Class<?> clazz;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        MenuBean menuBean = data.get(position);
        Intent sIntent = new Intent(this,menuBean.clazz);
        startActivity(sIntent);
    }
}