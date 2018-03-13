package com.example.hp.project2;

/**
 * Created by HP on 1/20/2018.
 */

public class SensorData {
    private long timestamp;
    private double x;
    private double y;
    private double z;
    private double magnitude;

    public SensorData(long timestamp, double x, double y, double z, double magnitude) {
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.z = z;
        this.magnitude = magnitude;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getZ() {
        return z;
    }
    public void setZ(double z) {
        this.z = z;
    }
    public double getMagnitude() {
        return magnitude;
    }
    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }


    public String toString()
    {
        return "t="+timestamp+", x="+x+", y="+y+", z="+z+", magnitude="+magnitude+"";
        //return "x="+x+", y="+y+", z="+z+"";
    }
}
