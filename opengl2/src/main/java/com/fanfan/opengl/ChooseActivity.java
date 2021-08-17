package com.fanfan.opengl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fanfan.opengl.geometry.render.Ball;
import com.fanfan.opengl.geometry.render.Cone;
import com.fanfan.opengl.geometry.render.Cube;
import com.fanfan.opengl.geometry.render.Cylinder;
import com.fanfan.opengl.geometry.render.Square;
import com.fanfan.opengl.geometry.render.Triangle;
import com.fanfan.opengl.geometry.render.TriangleColorFull;
import com.fanfan.opengl.geometry.render.TriangleWithCamera;

import java.util.ArrayList;


public class ChooseActivity extends AppCompatActivity {

    private ListView mList;
    private ArrayList<Data> mData;


    private class Data{
        String showName;
        Class<?> clazz;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mList = findViewById(R.id.mList);
        initData();
    }

    private void initData(){
        if(mData == null){
            mData = new ArrayList<>();
        }
        addData("三角形",Triangle.class);
        addData("正三角形", TriangleWithCamera.class);
        addData("彩色三角形", TriangleColorFull.class);
        addData("正方形", Square.class);
        addData("正方体", Cube.class);
        addData("圆锥体", Cone.class);
        addData("圆柱体", Cylinder.class);
        addData("球体", Ball.class);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent reIntent = new Intent();
                reIntent.putExtra("clazzName",mData.get(position).clazz);
                setResult(RESULT_OK,reIntent);
                finish();
            }
        });

        mList.setAdapter(new DataAdapter());
    }

    private void addData(String showName,Class clazz){
        Data tData = new Data();
        tData.showName = showName;
        tData.clazz = clazz;

        mData.add(tData);
    }

    private class DataAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.item_choose,parent,false);
            }
            TextView viewButton = (TextView) convertView.getTag();
            if(viewButton == null){
                viewButton = convertView.findViewById(R.id.mName);
                viewButton.setTag(viewButton);
            }
            viewButton.setText(mData.get(position).showName);
            return convertView;
        }
    }
}
