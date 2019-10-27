package com.example.bussitemessage;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Tools {
    public static boolean NetWork(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if(info != null){
            return info.isAvailable();
        }
        return false;
    }
}
