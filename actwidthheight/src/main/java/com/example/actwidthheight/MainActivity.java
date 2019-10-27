package com.example.actwidthheight;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button widen,narrowDown,horizontally_center,vertical_center,low_light;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bd();//调用关联控件的方法
    }

    /**
     * 控件关联、点击事件关联
     */
    private void bd(){
        text = findViewById(R.id.text1);

        widen = findViewById(R.id.widen);
        widen.setOnClickListener(this);
        narrowDown = findViewById(R.id.narrowDown);
        narrowDown.setOnClickListener(this);
        horizontally_center = findViewById(R.id.horizontally_center);
        horizontally_center.setOnClickListener(this);
        vertical_center = findViewById(R.id.vertical_center);
        vertical_center.setOnClickListener(this);
        low_light = findViewById(R.id.low_light);
        low_light.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.widen:

                //设置text width为match_parent

                //创建一个LayoutParams对象，传入宽度、高度
                RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //重新加载布局
                text.setLayoutParams(params1);
                break;
            case R.id.narrowDown:

                //设置text layout_width为wrap_content

                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //重新加载布局
                text.setLayoutParams(params2);
                break;
            case R.id.horizontally_center:

                //设置text水平居中

                RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params3.addRule(RelativeLayout.CENTER_HORIZONTAL);
                //重新加载布局
                text.setLayoutParams(params3);
                break;
            case R.id.vertical_center:

                //设置text垂直居中

                RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT|RelativeLayout.CENTER_HORIZONTAL);
                //重新加载布局
                text.setLayoutParams(params4);
                break;
            case R.id.low_light:

                //设置text控件右下对齐

                RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params5.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                params5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                //重新加载布局
                text.setLayoutParams(params5);
                break;
            default:
                break;
        }
    }
}
