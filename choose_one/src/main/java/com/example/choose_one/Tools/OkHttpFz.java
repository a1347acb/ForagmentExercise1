package com.example.choose_one.Tools;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpFz {
    private String URL;
    private OkHttpClient client = new OkHttpClient();
    public OkHttpFz(String URL){
        this.URL = URL;
    }
    public String Tong(String parameter){
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),parameter);
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .build();
        try {
            return client.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String Tong(){
        Request request = new Request.Builder()
                .url(URL)
                .build();
        try {
            return client.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
