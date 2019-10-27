package com.example.bussitemessage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BusSiteMessage extends AppCompatActivity {
    private TextView plateNumber,head,offal,peopleNumber;//线路信息（公交线路、首发时间、末班时间、当前承载人数）
    private TextView site_one,site_two;//站点标题
    private ListView site_Items_one,site_Items_two;//站点信息
    private Button details;//详情按钮
    private mBaseAdapter adapter_one;
    private mBaseAdapter adapter_two;
    private static int sum;
    private Timer timer;
    private Handler handler = new Handler(){
        private int PeopleNumber;
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    synchronized (this){
                        int people = 0;
                        try {
                            JSONObject object = new JSONObject((String)msg.obj);
                            if(object != null){
                                people = object.getInt("BusCapacity");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        PeopleNumber = PeopleNumber + people;
                        sum = sum -1;
                        Log.e("SUM",String.valueOf(sum));
                    }
                    if(sum == 0){
                        peopleNumber.setText(String.valueOf(PeopleNumber));
                    }
                    break;
                case -2:
                    sum = (int)msg.obj;
                    break;
            }
        }
    };
    private Handler handler2 = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    adapter_one = new mBaseAdapter(BusSiteMessage.this,(List<SiteData>)msg.obj);
                    site_Items_one.setAdapter(adapter_one);
                    break;
                case 2:
                    adapter_two = new mBaseAdapter(BusSiteMessage.this,(List<SiteData>)msg.obj);
                    site_Items_two.setAdapter(adapter_two);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_site_message);
        bd();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //开启查询头部公交信息的线程
                new TitleRequest(Values.API + "/GetBusInfo.do","{\"Line\":0,\"UserName\":\"user1\"}",handler).start();
                //循环开启两个分别查询1号站台、2号站台1、2号公交的距离、承载人数
                for(int i=1;i<=2;i++){
                    final int finalI = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message message = Message.obtain();
                            message.what = finalI;
                            message.obj = plateRequest(Values.API + "/GetBusStationInfo.do", finalI);
                            handler2.sendMessage(message);
                        }
                    }).start();
                }
            }
        },0,3000);
    }

    /**
     * 绑定控件、关联事件
     */
    private void bd(){
        plateNumber = findViewById(R.id.plateNumber);
        head = findViewById(R.id.head);
        offal =findViewById(R.id.offal);
        peopleNumber = findViewById(R.id.peopleNumber);

        //第一个下拉菜单标题点击事件
        site_one = findViewById(R.id.one_site);
        site_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(site_Items_one.getHeight() == 0){
                    LinearLayout.LayoutParams s = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    site_Items_one.setLayoutParams(s);
                }else{
                    LinearLayout.LayoutParams s = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
                    site_Items_one.setLayoutParams(s);
                }
            }
        });
        //第二个下拉菜单标题点击事件
        site_two = findViewById(R.id.two_site);
        site_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(site_Items_two.getHeight() == 0){
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    site_Items_two.setLayoutParams(params);
                }else{
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
                    site_Items_two.setLayoutParams(params);
                }
            }
        });

        site_Items_one = findViewById(R.id.site_Items_one);
        site_Items_two =findViewById(R.id.site_Items_two);

        details = findViewById(R.id.details);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
    //查询头部公交总览信息
    class TitleRequest extends Thread{
        String URL;
        String Parameter;
        Handler thandler;
        public TitleRequest(String URL,String Parameter,Handler thandler){
            this.URL = URL;
            this.Parameter = Parameter;
            this.thandler = thandler;
        }
        @Override
        public void run() {
            super.run();
            List<Integer> data = JSON(URL,Parameter);
            if(data == null)
                return;
            Message message = Message.obtain();
            message.what = -2;
            message.obj = data.size();
            thandler.sendMessage(message);
            for(int i=0;i<data.size();i++){
                PeopleNumber(i,thandler);
            }
        }

        /**
         * 解析所有公交信息，返回查询到的公交车编号
         * @return
         */
        private List<Integer> JSON(String URL,String Parameter){
            List<Integer> BusStationId = new ArrayList<>();
            OkHttpFz fz = new OkHttpFz(URL);
            String data = fz.Tong(Parameter);
            if(data == null)
                return null;
            try {
                JSONObject object = new JSONObject(data);
                Log.e("values",data);
                if(object.getString("ERRMSG").equals("成功")){
                    JSONArray array = object.getJSONArray("ROWS_DETAIL");
                    for(int i=0;i<array.length();i++){
                        JSONObject object1 = array.getJSONObject(i);
                        BusStationId.add(Integer.valueOf(object1.getString("id")));
                    }
                    return BusStationId;
                }else{
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return BusStationId;
        }
        /**
         * 根据公交编号查询公交当前承载人数
         * @param BusStationId
         * @return
         */
        private void PeopleNumber(int BusStationId,Handler handler){
            OkHttpFz fz = new OkHttpFz(Values.API + "/GetBusCapacity.do");
            fz.Yi("{\"BusId\":"+BusStationId+",\"UserName\":\"user1\"}",handler);
        }
    }


    /**
     * 查询公交承载人数
     * @param BusStationId
     * @return
     */
    private int PeopleNumber(int BusStationId){
        OkHttpFz fz = new OkHttpFz(Values.API + "/GetBusCapacity.do");
        String data = fz.Tong("{\"BusId\":"+BusStationId+",\"UserName\":\"user1\"}");
        try {
            JSONObject object = new JSONObject(data);
            if(object.getString("ERRMSG").equals("成功") && object.getString("RESULT").equals("S")){
                return object.getInt("BusCapacity");
            }else{
                Log.e("异常","获取公交车承载人数失败！");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 查询公交编号、距离指定站点距离、预计到达时间
     * @param URL
     * @param plateNumber
     * @return
     */
    private List<SiteData> plateRequest(String URL,int plateNumber){
        List<SiteData> dt = new ArrayList<>();
        OkHttpFz fz = new OkHttpFz(URL);
        String data = fz.Tong("{\"BusStationId\":"+plateNumber+",\"UserName\":\"user1\"}");
        try {
            JSONObject object = new JSONObject(data);
            if(object.getString("ERRMSG").equals("成功") && object.getString("RESULT").equals("S")){
                JSONArray array = object.getJSONArray("ROWS_DETAIL");
                for(int i=0;i<2;i++){
                    dt.add(new SiteData());
                    JSONObject object1 = array.getJSONObject(i);
                    dt.get(i).setNumber(object1.getInt("BusId"));
                    dt.get(i).setDistance(object1.getInt("Distance"));
                    dt.get(i).setMinute(object1.getInt("Distance")/(20000/60));
                    dt.get(i).setPeopleNumber(PeopleNumber(object1.getInt("BusId")));
                }
            }else{
                Log.e("异常","公交信息请求失败！");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dt;
    }
}
