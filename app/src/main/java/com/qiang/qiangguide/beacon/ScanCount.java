package com.qiang.qiangguide.beacon;

/**
 * Created by Qiang on 2016/9/27.
 */

public class ScanCount {

    int count;
    double distance;

    public ScanCount() {
    }

    public ScanCount(int count, double distance) {
        this.count = count;
        this.distance = distance;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getDistance() {
        return this.distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

}
