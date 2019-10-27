package com.example.bussitemessage;

import java.util.Comparator;

public class SiteData implements Comparable<SiteData> {
    private int busNumber;
    private int peopleNumber;
    private int minute;
    private int distance;

    public int getDistance() {
        return distance;
    }

    public int getBusNumber() {
        return busNumber;
    }

    public int getMinute() {
        return minute;
    }

    public int getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(int peopleNumber) {
        this.peopleNumber = peopleNumber;
    }

    public void setNumber(int number) {
        this.busNumber = number;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
    @Override
    public int compareTo(SiteData siteData) {
        if(distance > siteData.getDistance()){
            return 1;
        }else if(distance < siteData.getDistance()){
            return -1;
        }else{
            return 0;
        }
    }
}
