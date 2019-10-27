package com.example.bussitemessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class mBaseAdapter extends BaseAdapter {
    private List<SiteData> data;
    private View mView;
    private Context context;
    private TextView busNumber,mPeoperNumber,minute,distance;

    public mBaseAdapter(Context context, List<SiteData> data){
        this.context = context;
        //进行距离升序排序
        Collections.sort(data);
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        mView = LayoutInflater.from(context).inflate(R.layout.site_items,viewGroup,false);
        bd();
        busNumber.setText(String.valueOf(data.get(i).getBusNumber()));
        mPeoperNumber.setText(String.valueOf(data.get(i).getPeopleNumber()));
        minute.setText(String.valueOf(data.get(i).getMinute()));
        distance.setText(String.valueOf(data.get(i).getDistance()));
        return mView;
    }
    private void bd(){
        busNumber = mView.findViewById(R.id.busNumber);
        mPeoperNumber = mView.findViewById(R.id.mPeopleNumber);
        minute = mView.findViewById(R.id.minute);
        distance = mView.findViewById(R.id.distance);
    }
}
