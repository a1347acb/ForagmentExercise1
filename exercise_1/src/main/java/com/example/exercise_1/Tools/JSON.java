package com.example.exercise_1.Tools;

import org.json.JSONException;
import org.json.JSONObject;

public class JSON {
    public static boolean LoginJSON(String data){
        try {
            JSONObject object = new JSONObject(data);
            if(object.getString("ERRMSG").equals("登陆成功")){
                if(object.getString("RESULT").equals("S")){
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
