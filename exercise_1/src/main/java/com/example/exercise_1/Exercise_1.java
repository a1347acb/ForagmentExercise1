package com.example.exercise_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exercise_1.Interfaces.LoginMessage;
import com.example.exercise_1.Tools.JSON;
import com.example.exercise_1.Tools.OkHttpFz;
import com.example.exercise_1.Tools.Tool;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Exercise_1 extends AppCompatActivity{
    private String url = "http://47.106.75.2:8080/transportservice/action/user_login.do";
    private TextView uid;//账号输入框
    private TextView pwd;//密码输入框
    private Button Login;//登录按钮
    private static ProgressDialog dialog;
    public static Context mContext;
    private static TextView LoginMessage;
    private static Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 1:
                    if((Boolean)msg.obj){
                        LoginMessage.setText("登录成功！");
                    }else{
                        LoginMessage.setText("登录失败！");
                    }
                    break;
                case 2:
                    Toast.makeText(Exercise_1.mContext,"请求异常："+msg.obj,Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_1);
        mContext = getApplicationContext();

        bd();//控件初始化、事件绑定

        dialog = new ProgressDialog(Exercise_1.this);
        dialog.setMessage("加载中...");

    }
    private void bd(){
        uid = findViewById(R.id.uid);
        pwd = findViewById(R.id.pwd);
        Login = findViewById(R.id.loginButton);
        LoginMessage = findViewById(R.id.LoginMessage);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String parameter = String.format("{\"UserName\":\"%s\",\"UserPwd\":\"%s\"}",uid.getText().toString(),pwd.getText().toString());

//                mAsynctask task = new mAsynctask();
//                task.execute(url,parameter);

                OkHttpFz fz = new OkHttpFz(url);
                fz.setParameter(parameter);
                fz.Together(handler);
            }
        });
    }
//    static class mAsynctask extends AsyncTask<String,Void,Boolean>{
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            dialog.show();
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//            dialog.dismiss();
//            if(aBoolean){
//                Toast.makeText(Exercise_1.mContext,"登录成功！",Toast.LENGTH_SHORT).show();
//            }else{
//                Toast.makeText(Exercise_1.mContext,"登录失败！",Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        protected Boolean doInBackground(String... strings) {
//            Tool.Loge("URL",strings[0]);
//            Tool.Loge("paragmeter",strings[1]);
//            OkHttpFz post = new OkHttpFz(strings[0]);
//            post.setParameter(strings[1]);
//            return JSON.LoginJSON(post.Together());
//        }
//    }
}
