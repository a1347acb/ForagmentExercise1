package com.example.exercise_1.Tools;

import android.os.Handler;
import android.os.Message;

import com.example.exercise_1.Interfaces.LoginMessage;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpFz {
    private String url;//请求地址

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    private String parameter;//请求参数
    public OkHttpFz(String url) {
        this.url = url;
    }

    /**
     * 同步请求
     * @return
     */
    public String Together(){
        OkHttpClient client = new OkHttpClient();
        Request request;
        if(parameter == null){
            request = new Request.Builder()
                    .url(url)
                    .build();
        }else{
            RequestBody body = FormBody.create(MediaType.parse("application/json"),parameter);
            request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
        }
        try {
            Response response = client.newCall(request).execute();
            //返回请求到的数据内容（以字符串的形式）
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 异步请求
     */
    public void Together(final Handler handler){
        OkHttpClient client = new OkHttpClient();
        Request request;
        if(parameter == null){
            request = new Request.Builder()
                    .url(url)
                    .build();
        }else{
            RequestBody body = FormBody.create(MediaType.parse("application/json"),parameter);
            request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
        }
        //将结果通过接口回调到异步请求的线程（缺点：如果一个线程中存在多个异步请求，则无法判断究竟是哪个请求返回的数据）
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message(handler,2,e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message(handler,1,JSON.LoginJSON(response.body().string()));
            }
        });
    }
    //添加消息到消息队列的方法
    private static void Message(Handler handler,int what,Object obj){
        Message message = Message.obtain();
        message.what = what;
        message.obj = obj;
        handler.sendMessage(message);
    }
}
