package com.example.bussitemessage;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpFz {
    private static String URL;
    private OkHttpClient client;
    public OkHttpFz(String URL){
        this.URL = URL;
        client =  new OkHttpClient();
        Log.e("URL",URL);
    }

    /**
     * 同步Post请求
     * @param parameter
     * @return
     */
    public String Tong(String parameter){
        Log.e("同步参数",parameter);
        try {
            URL url = new URL(URL);
            Request request = new Request.Builder()
                    .url(url)
                    .post(FormBody.create(MediaType.parse("application/json"),parameter))
                    .build();
            return client.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 异步Post请求
     * @param parameter
     * @param handler
     */
    public void Yi(String parameter, final Handler handler){
        Log.e("异步请求参数", parameter );
        Request request = new Request.Builder()
                .url(URL)
                .post(FormBody.create(MediaType.parse("application/json"),parameter))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message(-1,e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message(1,response.body().string());
            }
            private void Message(int what,Object obj){
                Message message = Message.obtain();
                message.what = what;
                message.obj = obj;
                handler.sendMessage(message);
            }
        });
    }
}
