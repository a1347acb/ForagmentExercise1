package com.example.choose_one;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.choose_one.Tools.OkHttpFz;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PathMessage extends AppCompatActivity {
    private FrameLayout temperature,humidity,illumination,CO2,PM2_5,path;
    private TextView temperature_Value,humidity_Value,illumination_Value,CO2_Value,PM2_5_Value,path_Value;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_message);
        bd();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mAsynaction mAsynaction = new mAsynaction();
                mAsynaction.execute();
            }
        },0,3000);
    }
    private void bd(){
        temperature = findViewById(R.id.temperature);
        temperature_Value = findViewById(R.id.temperature_Value);
        humidity = findViewById(R.id.humidity);
        humidity_Value = findViewById(R.id.humidity_Value);
        illumination = findViewById(R.id.illumination);
        illumination_Value = findViewById(R.id.illumination_Value);
        CO2 = findViewById(R.id.CO2);
        CO2_Value = findViewById(R.id.CO2_Value);
        PM2_5 = findViewById(R.id.PM2_5);
        PM2_5_Value = findViewById(R.id.PM2_5_Value);
        path = findViewById(R.id.path);
        path_Value = findViewById(R.id.path_Value);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    class mAsynaction extends AsyncTask<String,Void, Map<String,String>>{
        @Override
        protected void onPostExecute(Map<String, String> stringStringMap) {
            super.onPostExecute(stringStringMap);
            temperature_Value.setText(stringStringMap.get("temperature"));
            if(Integer.valueOf(stringStringMap.get("temperature")) >= 15 && Integer.valueOf(stringStringMap.get("temperature")) <= 32)
                temperature.setBackground(getResources().getDrawable(R.drawable.item_parent_background_y));
            else
                temperature.setBackground(getResources().getDrawable(R.drawable.item_parent_background_n));

            humidity_Value.setText(stringStringMap.get("humidity"));
            if(Integer.valueOf(stringStringMap.get("humidity")) >= 30 && Integer.valueOf(stringStringMap.get("humidity")) <= 80)
                humidity.setBackground(getResources().getDrawable(R.drawable.item_parent_background_y));
            else
                humidity.setBackground(getResources().getDrawable(R.drawable.item_parent_background_n));

            illumination_Value.setText(stringStringMap.get("illumination"));
            if(Integer.valueOf(stringStringMap.get("illumination")) >= 1000 && Integer.valueOf(stringStringMap.get("illumination")) <= 2500)
                illumination.setBackground(getResources().getDrawable(R.drawable.item_parent_background_y));
            else
                illumination.setBackground(getResources().getDrawable(R.drawable.item_parent_background_n));

            CO2_Value.setText(stringStringMap.get("CO2"));
            if(Integer.valueOf(stringStringMap.get("CO2")) >= 0 && Integer.valueOf(stringStringMap.get("CO2")) <= 3000)
                CO2.setBackground(getResources().getDrawable(R.drawable.item_parent_background_y));
            else
                CO2.setBackground(getResources().getDrawable(R.drawable.item_parent_background_n));

            PM2_5_Value.setText(stringStringMap.get("PM2_5"));
            if(Integer.valueOf(stringStringMap.get("PM2_5")) >= 0 && Integer.valueOf(stringStringMap.get("PM2_5")) <= 260)
                PM2_5.setBackground(getResources().getDrawable(R.drawable.item_parent_background_y));
            else
                PM2_5.setBackground(getResources().getDrawable(R.drawable.item_parent_background_n));

            path_Value.setText(stringStringMap.get("path"));
            if(Integer.valueOf(stringStringMap.get("path")) >= 1 && Integer.valueOf(stringStringMap.get("path")) <= 3)
                path.setBackground(getResources().getDrawable(R.drawable.item_parent_background_y));
            else
                path.setBackground(getResources().getDrawable(R.drawable.item_parent_background_n));
        }

        @Override
        protected Map<String, String> doInBackground(String... strings) {
            Map<String,String> data = new HashMap<>();
            OkHttpFz fz = new OkHttpFz("http://47.106.75.2:8080/transportservice/action" + "/GetAllSense.do");
            String dt = fz.Tong("{\"UserName\":\"user1\"}");
            try {
                JSONObject object = new JSONObject(dt);
                if(object.getString("ERRMSG").equals("成功") && object.getString("RESULT").equals("S")){
                    data.put("temperature",object.getString("temperature"));
                    data.put("humidity",object.getString("humidity"));
                    data.put("illumination",object.getString("LightIntensity"));
                    data.put("CO2",object.getString("co2"));
                    data.put("PM2_5",object.getString("pm2.5"));
                    OkHttpFz fz1 = new OkHttpFz("http://47.106.75.2:8080/transportservice/action" + "/GetRoadStatus.do");
                    String dt1 = fz1.Tong("{\"RoadId\":1,\"UserName\":\"user1\"}");
                    JSONObject object1 = new JSONObject(dt1);
                    if(object.getString("ERRMSG").equals("成功") && object.getString("RESULT").equals("S")){
                        data.put("path",object1.getString("Status"));
                    }else{
                        data.put("path","0");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return data;
        }
    }
}
