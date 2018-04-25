//under development

package com.example.hp.project2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by HP on 3/2/2018.
 */


public class Combined extends AppCompatActivity implements View.OnClickListener, SensorEventListener, View.OnTouchListener {

    private SensorManager mSensorManager;
    private Sensor acc, gyr, mag;
    private boolean started = false,secondStart = false;
    private double x, y, z, M;
    private double maxX1, maxY1, maxZ1, maxM1;
    private double maxX2, maxY2, maxZ2, maxM2;
    private double maxX3, maxY3, maxZ3, maxM3;
    private long t_start;
    private long t_endX1, t_endY1, t_endZ1, t_endM1;
    private long t_endX2, t_endY2, t_endZ2, t_endM2;
    private long t_endX3, t_endY3, t_endZ3, t_endM3;
    private long avg100msBefore1,avg100msBefore2,avg100msBefore3;
    private long t_after_center1,t_after_center2,t_after_center3;
    private long avg100msAfter1, diff1;
    private long avg100msAfter2, diff2;
    private long avg100msAfter3, diff3;
    long currentTime, t_after_center;
    long t_max_tapX1, t_max_tapY1, t_max_tapZ1, t_max_tapM1;
    long t_max_tapX2, t_max_tapY2, t_max_tapZ2, t_max_tapM2;
    long t_max_tapX3, t_max_tapY3, t_max_tapZ3, t_max_tapM3;
    private long t = 1;
    private double meanX1, meanY1, meanZ1, meanM1;
    private double meanX2, meanY2, meanZ2, meanM2;
    private double meanX3, meanY3, meanZ3, meanM3;
    private double sdX1,sdY1,sdZ1,sdM1,sdX2,sdY2,sdZ2,sdM2,sdX3,sdY3,sdZ3,sdM3;
    private double ncX1,ncY1,ncZ1,ncM1,ncX2,ncY2,ncZ2,ncM2,ncX3,ncY3,ncZ3,ncM3;
    private double mcX1,mcY1,mcZ1,mcM1,mcX2,mcY2,mcZ2,mcM2,mcX3,mcY3,mcZ3,mcM3;
    private double dur1, dur2, dur3;
    private double max2avgX1, max2avgY1, max2avgZ1, max2avgM1;
    private double max2avgX2, max2avgY2, max2avgZ2, max2avgM2;
    private double max2avgX3, max2avgY3, max2avgZ3, max2avgM3;
    ArrayList<SensorData> accData;
    ArrayList<SensorData> tapDataX1;
    ArrayList<SensorData> tapDataX2;
    ArrayList<SensorData> tapDataX3;
    ArrayList<SensorData> tapDataY1;
    ArrayList<SensorData> tapDataY2;
    ArrayList<SensorData> tapDataY3;
    ArrayList<SensorData> tapDataZ1;
    ArrayList<SensorData> tapDataZ2;
    ArrayList<SensorData> tapDataZ3;
    ArrayList<SensorData> tapDataM1;
    ArrayList<SensorData> tapDataM2;
    ArrayList<SensorData> tapDataM3;
    List<SensorData> hundredMilliDataBehind1;
    List<SensorData> hundredMilliDataBehind2;
    List<SensorData> hundredMilliDataBehind3;
    ArrayList<SensorData> DataAhead1;
    ArrayList<SensorData> DataAhead2;
    ArrayList<SensorData> DataAhead3;
    ArrayList<SensorData> gyrData;
    ArrayList<SensorData> magData;
    ArrayList<String> timeStamp1;
    ArrayList<String> timeStamp2;
    ArrayList<String> timeStamp3;
    ArrayList<Long> accTimeStamp;
    ArrayList<Long> gyrTimeStamp;
    ArrayList<Long> magTimeStamp;
    ArrayList<SensorData> limitSensorData;
    ArrayList<ArrayList<SensorData>> all_clicked_sensorData;
    Button bt_one, bt_two, bt_three, bt_four, bt_five, bt_six, bt_seven, bt_eight, bt_nine, bt_zero, bt_submit,bt_cancel;
    private long TappedCurrentTimeStamp_test;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap);
        bt_one = (Button) findViewById(R.id.bt_one);
        bt_two = (Button) findViewById(R.id.bt_two);
        bt_three = (Button) findViewById(R.id.bt_three);
        bt_four = (Button) findViewById(R.id.bt_four);
        bt_five = (Button) findViewById(R.id.bt_five);
        bt_six = (Button) findViewById(R.id.bt_six);
        bt_seven = (Button) findViewById(R.id.bt_seven);
        bt_eight = (Button) findViewById(R.id.bt_eight);
        bt_nine = (Button) findViewById(R.id.bt_nine);
        bt_zero = (Button) findViewById(R.id.bt_zero);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Combined.this, "user", Toast.LENGTH_SHORT).show();
            }
        });
        //bt_cancel.setOnTouchListener(this);
        bt_one.setOnTouchListener(this);
        bt_two.setOnTouchListener(this);
        bt_three.setOnTouchListener(this);
        bt_four.setOnTouchListener(this);
        bt_five.setOnTouchListener(this);
        bt_six.setOnTouchListener(this);
        bt_seven.setOnTouchListener(this);
        bt_eight.setOnTouchListener(this);
        bt_nine.setOnTouchListener(this);
        bt_zero.setOnTouchListener(this);
        bt_submit.setOnTouchListener(this);
        timeStamp1 = new ArrayList<>();
        accData = new ArrayList<>();
        gyrData = new ArrayList<>();
        magData = new ArrayList<>();
        accData = new ArrayList<>();
        tapDataX1 = new ArrayList<>();
        tapDataX2 = new ArrayList<>();
        tapDataX3 = new ArrayList<>();
        tapDataY1 = new ArrayList<>();
        tapDataY2 = new ArrayList<>();
        tapDataY3 = new ArrayList<>();
        tapDataZ1 = new ArrayList<>();
        tapDataZ2 = new ArrayList<>();
        tapDataZ3 = new ArrayList<>();
        limitSensorData = new ArrayList<>();
        accTimeStamp = new ArrayList<>();
        DataAhead1 = new ArrayList<>();
        DataAhead2 = new ArrayList<>();
        DataAhead3 = new ArrayList<>();
        hundredMilliDataBehind1 = new ArrayList<>();
        hundredMilliDataBehind2 = new ArrayList<>();
        hundredMilliDataBehind3 = new ArrayList<>();

        all_clicked_sensorData = new ArrayList<ArrayList<SensorData>>();

        sensorStarting();
    }

    public void sensorStarting()
    {
        Log.d("App", "calling starting functions");
        started = true;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_FASTEST);
        gyr = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(this, gyr, SensorManager.SENSOR_DELAY_FASTEST);
        mag = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(this, mag, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onDestroy() {
        started = false;
        mSensorManager.unregisterListener(this);
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (started == true)
        {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (started)
        {
            Log.d("PerfectMan", "if");
            if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
                M = Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
                currentTime = System.currentTimeMillis();
                SensorData data = new SensorData(currentTime, x, y, z, M);
                if (timeStamp1.size() < 200) {
                    accData.add(data);
                    accTimeStamp.add(currentTime);
                } else {
                    accData.remove(0);
                    accTimeStamp.remove(0);
                    accData.add(data);
                    accTimeStamp.add(currentTime);

                }
            }
            else if (event.sensor.getType()==Sensor.TYPE_GYROSCOPE) {
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
                M = Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
                currentTime = System.currentTimeMillis();
                SensorData data = new SensorData(currentTime, x, y, z, M);
                if (timeStamp2.size() < 200) {
                    gyrData.add(data);
                    gyrTimeStamp.add(currentTime);
                } else {
                    gyrData.remove(0);
                    gyrTimeStamp.remove(0);
                    gyrData.add(data);
                    gyrTimeStamp.add(currentTime);

                }
            }
            else if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD) {
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
                M = Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
                currentTime = System.currentTimeMillis();
                SensorData data = new SensorData(currentTime, x, y, z, M);
                if (timeStamp3.size() < 200) {
                    magData.add(data);
                    magTimeStamp.add(currentTime);
                } else {
                    magData.remove(0);
                    magTimeStamp.remove(0);
                    magData.add(data);
                    magTimeStamp.add(currentTime);

                }
            }
        }
        else if(secondStart)
        {
            Log.d("PerfectMan","else if");
            if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
                M = Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
                currentTime = System.currentTimeMillis();
                SensorData data = new SensorData(currentTime, x, y, z, M);
                DataAhead1.add(data);
            }
            else if (event.sensor.getType()==Sensor.TYPE_GYROSCOPE) {
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
                M = Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
                currentTime = System.currentTimeMillis();
                SensorData data = new SensorData(currentTime, x, y, z, M);
                DataAhead2.add(data);
            }
            else if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD) {
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
                M = Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
                currentTime = System.currentTimeMillis();
                SensorData data = new SensorData(currentTime, x, y, z, M);
                DataAhead3.add(data);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    public void onClick(View view)
    {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
        {
            TappedCurrentTimeStamp_test =  currentTime;
            t_start=TappedCurrentTimeStamp_test;
            Log.d("Tapped..t_start",""+TappedCurrentTimeStamp_test);
            started =  false;
            secondStart = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    secondStart = false;

                    maxX1=DataAhead1.get(0).getX();
                    maxY1=DataAhead1.get(0).getY();
                    maxZ1=DataAhead1.get(0).getZ();
                    maxM1=DataAhead1.get(0).getMagnitude();
                    for (int i=0;i<DataAhead1.size();i++){
                        if(DataAhead1.get(i).getX() > maxX1){
                            maxX1 = DataAhead1.get(i).getX();
                            t_max_tapX1=DataAhead1.get(i).getTimestamp();
                        }
                        if(DataAhead1.get(i).getY() > maxY1){
                            maxY1 = DataAhead1.get(i).getY();
                            t_max_tapY1=DataAhead1.get(i).getTimestamp();
                        }
                        if(DataAhead1.get(i).getZ() > maxZ1){
                            maxZ1 = DataAhead1.get(i).getZ();
                            t_max_tapZ1=DataAhead1.get(i).getTimestamp();
                        }
                        if(DataAhead1.get(i).getMagnitude() > maxM1){
                            maxM1 = DataAhead1.get(i).getMagnitude();
                            t_max_tapM1=DataAhead1.get(i).getTimestamp();
                        }
                    }

                    if(t_max_tapX1==0) {
                        t_endX1=DataAhead1.get(DataAhead1.indexOf(t_max_tapX1)+1).getTimestamp();
                    }
                    else t_endX1 = t_max_tapX1;
                    if(t_max_tapY1==0) {
                        t_endY1=DataAhead1.get(DataAhead1.indexOf(t_max_tapY1)+1).getTimestamp();
                    }
                    else t_endY1=t_max_tapY1;
                    if(t_max_tapZ1==0) {
                        t_endZ1=DataAhead1.get(DataAhead1.indexOf(t_max_tapZ1)+1).getTimestamp();
                    }
                    else t_endZ1=t_max_tapZ1;
                    if(t_max_tapM1==0) {
                        t_endM1=DataAhead1.get(DataAhead1.indexOf(t_max_tapM1)+1).getTimestamp();
                    }
                    else t_endM1=t_max_tapM1;

                    maxX2=DataAhead2.get(0).getX();
                    maxY2=DataAhead2.get(0).getY();
                    maxZ2=DataAhead2.get(0).getZ();
                    maxM2=DataAhead2.get(0).getMagnitude();
                    for (int i=0;i<DataAhead2.size();i++){
                        if(DataAhead2.get(i).getX() > maxX2){
                            maxX2 = DataAhead2.get(i).getX();
                            t_max_tapX2=DataAhead2.get(i).getTimestamp();
                        }
                        if(DataAhead2.get(i).getY() > maxY2){
                            maxY2 = DataAhead2.get(i).getY();
                            t_max_tapY2=DataAhead2.get(i).getTimestamp();
                        }
                        if(DataAhead2.get(i).getZ() > maxZ2){
                            maxZ2 = DataAhead2.get(i).getZ();
                            t_max_tapZ2=DataAhead2.get(i).getTimestamp();
                        }
                        if(DataAhead2.get(i).getMagnitude() > maxM2){
                            maxM2 = DataAhead2.get(i).getMagnitude();
                            t_max_tapM2=DataAhead2.get(i).getTimestamp();
                        }
                    }

                    if(t_max_tapX2==0) {
                        t_endX2=DataAhead2.get(DataAhead2.indexOf(t_max_tapX2)+1).getTimestamp();
                    }
                    else t_endX2 = t_max_tapX2;
                    if(t_max_tapY2==0) {
                        t_endY2=DataAhead2.get(DataAhead2.indexOf(t_max_tapY2)+1).getTimestamp();
                    }
                    else t_endY2=t_max_tapY2;
                    if(t_max_tapZ2==0) {
                        t_endZ2=DataAhead2.get(DataAhead2.indexOf(t_max_tapZ2)+1).getTimestamp();
                    }
                    else t_endZ2=t_max_tapZ2;
                    if(t_max_tapM2==0) {
                        t_endM2=DataAhead2.get(DataAhead2.indexOf(t_max_tapM2)+1).getTimestamp();
                    }
                    else t_endM2=t_max_tapM2;

                    maxX3=DataAhead3.get(0).getX();
                    maxY3=DataAhead3.get(0).getY();
                    maxZ3=DataAhead3.get(0).getZ();
                    maxM3=DataAhead1.get(0).getMagnitude();
                    for (int i=0;i<DataAhead3.size();i++){
                        if(DataAhead3.get(i).getX() > maxX3){
                            maxX3 = DataAhead3.get(i).getX();
                            t_max_tapX3=DataAhead3.get(i).getTimestamp();
                        }
                        if(DataAhead3.get(i).getY() > maxY3){
                            maxY3 = DataAhead3.get(i).getY();
                            t_max_tapY3=DataAhead3.get(i).getTimestamp();
                        }
                        if(DataAhead3.get(i).getZ() > maxZ3){
                            maxZ3 = DataAhead3.get(i).getZ();
                            t_max_tapZ3=DataAhead3.get(i).getTimestamp();
                        }
                        if(DataAhead3.get(i).getMagnitude() > maxM3){
                            maxM3 = DataAhead3.get(i).getMagnitude();
                            t_max_tapM3=DataAhead3.get(i).getTimestamp();
                        }
                    }

                    if(t_max_tapX3==0) {
                        t_endX3=DataAhead3.get(DataAhead3.indexOf(t_max_tapX3)+1).getTimestamp();
                    }
                    else t_endX3 = t_max_tapX3;
                    if(t_max_tapY3==0) {
                        t_endY3=DataAhead3.get(DataAhead3.indexOf(t_max_tapY3)+1).getTimestamp();
                    }
                    else t_endY3=t_max_tapY3;
                    if(t_max_tapZ3==0) {
                        t_endZ3=DataAhead3.get(DataAhead3.indexOf(t_max_tapZ3)+1).getTimestamp();
                    }
                    else t_endZ3=t_max_tapZ3;
                    if(t_max_tapM3==0) {
                        t_endM3=DataAhead3.get(DataAhead3.indexOf(t_max_tapM3)+1).getTimestamp();
                    }
                    else t_endM3=t_max_tapM3;

                    int kx1=0,kx2=0,kx3=0,ky1=0,ky2=0,ky3=0,kz1=0,kz2=0,kz3=0,km1=0,km2=0,km3=0;
                    for (int i = 0; i <  DataAhead1.size(); i++) {
                        if(DataAhead1.get(i).getTimestamp() == t_endX1){
                            kx1=i;
                        }
                        if(DataAhead1.get(i).getTimestamp() == t_endY1){
                            ky1=i;
                        }
                        if(DataAhead1.get(i).getTimestamp() == t_endZ1){
                            kz1=i;
                        }
                        if(DataAhead1.get(i).getTimestamp() == t_endM1){
                            km1=i;
                        }
                    }
                    for (int i = 0; i <  DataAhead2.size(); i++) {
                        if(DataAhead2.get(i).getTimestamp() == t_endX2){
                            kx2=i;
                        }
                        if(DataAhead2.get(i).getTimestamp() == t_endY2){
                            ky2=i;
                        }
                        if(DataAhead2.get(i).getTimestamp() == t_endZ2){
                            kz2=i;
                        }
                        if(DataAhead2.get(i).getTimestamp() == t_endM2){
                            km2=i;
                        }
                    }
                    for (int i = 0; i <  DataAhead3.size(); i++) {
                        if(DataAhead3.get(i).getTimestamp() == t_endX3){
                            kx3=i;
                        }
                        if(DataAhead3.get(i).getTimestamp() == t_endY3){
                            ky3=i;
                        }
                        if(DataAhead3.get(i).getTimestamp() == t_endZ3){
                            kz3=i;
                        }
                        if(DataAhead3.get(i).getTimestamp() == t_endM3){
                            km3=i;
                        }
                    }

                    for (int k = 0; k<=kx1; k++){
                        tapDataX1.add(DataAhead1.get(k));
                    }
                    for (int k = 0; k<=ky1; k++){
                        tapDataY1.add(DataAhead1.get(k));
                    }
                    for (int k = 0; k<=kz1; k++){
                        tapDataZ1.add(DataAhead1.get(k));
                    }
                    for (int k = 0; k<=km1; k++){
                        tapDataM1.add(DataAhead1.get(k));
                    }

                    for (int k = 0; k<=kx2; k++){
                        tapDataX2.add(DataAhead2.get(k));
                    }
                    for (int k = 0; k<=ky2; k++){
                        tapDataY2.add(DataAhead2.get(k));
                    }
                    for (int k = 0; k<=kz2; k++){
                        tapDataZ2.add(DataAhead2.get(k));
                    }
                    for (int k = 0; k<=km2; k++){
                        tapDataM2.add(DataAhead2.get(k));
                    }

                    for (int k = 0; k<=kx3; k++){
                        tapDataX3.add(DataAhead3.get(k));
                    }
                    for (int k = 0; k<=ky3; k++){
                        tapDataY3.add(DataAhead3.get(k));
                    }
                    for (int k = 0; k<=kz3; k++){
                        tapDataZ3.add(DataAhead3.get(k));
                    }
                    for (int k = 0; k<=km3; k++){
                        tapDataM3.add(DataAhead3.get(k));
                    }

                    long substractedTimeStamp = TappedCurrentTimeStamp_test - 100;
                    long temp = 0;
                    int temp1 = 0,temp2 = 0,temp3 = 0;

                    for(int k = accData.size()-1;k >0;k--)
                    {
                        temp = substractedTimeStamp;
                        if (accData.get(k).getTimestamp() <= temp) {
                            temp1 = k;
                            break;
                        }
                    }
                    for(int k = gyrData.size()-1;k >0;k--)
                    {
                        temp = substractedTimeStamp;
                        if (gyrData.get(k).getTimestamp() <= temp) {
                            temp2 = k;
                            break;
                        }
                    }
                    for(int k = magData.size()-1;k >0;k--)
                    {
                        temp = substractedTimeStamp;
                        if (magData.get(k).getTimestamp() <= temp) {
                            temp3 = k;
                            break;
                        }
                    }

                    hundredMilliDataBehind1 =  accData.subList(temp1,accData.size());
                    hundredMilliDataBehind2 =  gyrData.subList(temp2,gyrData.size());
                    hundredMilliDataBehind3 =  magData.subList(temp3,magData.size());

                    mean_calculation();
                    standard_deviation_calc();
                    avg_calculation();
                    net_change();
                    max_change();
                    duration();
                    max_to_avg();

                    AccRead a=new AccRead();
                    a.execute(Double.toString(meanX1),Double.toString(meanY1),Double.toString(meanZ1),Double.toString(meanM1),
                            Double.toString(sdX1),Double.toString(sdY1),Double.toString(sdZ1),Double.toString(sdM1),
                            Double.toString(diff1),Double.toString(diff1),Double.toString(diff1),Double.toString(diff1),
                            Double.toString(ncX1),Double.toString(ncY1),Double.toString(ncZ1),Double.toString(ncM1),
                            Double.toString(mcX1),Double.toString(mcY1),Double.toString(mcZ1),Double.toString(mcM1),
                            Double.toString(dur1),Double.toString(dur1),Double.toString(dur1),Double.toString(dur1),
                            Double.toString(max2avgX1),Double.toString(max2avgY1),Double.toString(max2avgZ1),Double.toString(max2avgM1),
                            Double.toString(meanX2),Double.toString(meanY2),Double.toString(meanZ2),Double.toString(meanM2),
                            Double.toString(sdX2),Double.toString(sdY2),Double.toString(sdZ2),Double.toString(sdM2),
                            Double.toString(diff2),Double.toString(diff2),Double.toString(diff2),Double.toString(diff2),
                            Double.toString(ncX2),Double.toString(ncY2),Double.toString(ncZ2),Double.toString(ncM2),
                            Double.toString(mcX2),Double.toString(mcY2),Double.toString(mcZ2),Double.toString(mcM2),
                            Double.toString(dur2),Double.toString(dur2),Double.toString(dur2),Double.toString(dur2),
                            Double.toString(max2avgX2),Double.toString(max2avgY2),Double.toString(max2avgZ2),Double.toString(max2avgM2),
                            Double.toString(meanX3),Double.toString(meanY3),Double.toString(meanZ3),Double.toString(meanM3),
                            Double.toString(sdX3),Double.toString(sdY3),Double.toString(sdZ3),Double.toString(sdM3),
                            Double.toString(diff3),Double.toString(diff3),Double.toString(diff3),Double.toString(diff3),
                            Double.toString(ncX3),Double.toString(ncY3),Double.toString(ncZ3),Double.toString(ncM3),
                            Double.toString(mcX3),Double.toString(mcY3),Double.toString(mcZ3),Double.toString(mcM3),
                            Double.toString(dur3),Double.toString(dur3),Double.toString(dur3),Double.toString(dur3),
                            Double.toString(max2avgX3),Double.toString(max2avgY3),Double.toString(max2avgZ3),Double.toString(max2avgM3));

                }
            },300);
        }

        gpstrack gps = new gpstrack(Combined.this);
        if(gps.canGetLocation()){
            double longitude = gps.getLongitude();
            double latitude = gps.getLatitude();
                     /*------- To get city name from coordinates -------- */
            String cityName = null;
            String address="";
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {

                addresses = gcd.getFromLocation(latitude, longitude, 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    address=addresses.get(0).getAddressLine(0);
                    Toast.makeText(Combined.this,address,Toast.LENGTH_SHORT).show();
                    cityName = addresses.get(0).getLocality();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(),"Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude)+"\nCity:"+cityName,Toast.LENGTH_SHORT).show();
        }
        else
        {
            gps.showSettingsAlert();
        }

        return true;
    }

    public void mean_calculation()
    {
        double sumX1=0,sumY1=0,sumZ1=0,sumM1=0,sumX2=0,sumY2=0,sumZ2=0,sumM2=0,sumX3=0,sumY3=0,sumZ3=0,sumM3=0;

        for(int i=0;i<tapDataX1.size();i++) {
            sumX1 = sumX1 + tapDataX1.get(i).getX();
        }
        for(int i=0;i<tapDataY1.size();i++) {
            sumY1 = sumY1 + tapDataY1.get(i).getY();
        }
        for(int i=0;i<tapDataZ1.size();i++) {
            sumZ1 = sumZ1 + tapDataZ1.get(i).getZ();
        }
        for(int i=0;i<tapDataM1.size();i++) {
            sumM1 = sumM1 + tapDataM1.get(i).getMagnitude();
        }
        for(int i=0;i<tapDataX2.size();i++) {
            sumX2 = sumX2 + tapDataX2.get(i).getX();
        }
        for(int i=0;i<tapDataY2.size();i++) {
            sumY2 = sumY2 + tapDataY2.get(i).getY();
        }
        for(int i=0;i<tapDataZ2.size();i++) {
            sumZ2 = sumZ2 + tapDataZ2.get(i).getZ();
        }
        for(int i=0;i<tapDataM2.size();i++) {
            sumM2 = sumM2 + tapDataM2.get(i).getMagnitude();
        }
        for(int i=0;i<tapDataX3.size();i++) {
            sumX3 = sumX3 + tapDataX3.get(i).getX();
        }
        for(int i=0;i<tapDataY3.size();i++) {
            sumY3 = sumY3 + tapDataY3.get(i).getY();
        }
        for(int i=0;i<tapDataZ3.size();i++) {
            sumZ3 = sumZ3 + tapDataZ3.get(i).getZ();
        }
        for(int i=0;i<tapDataM3.size();i++) {
            sumM3 = sumM3 + tapDataM3.get(i).getMagnitude();
        }
        meanX1=sumX1/tapDataX1.size();
        meanY1=sumY1/tapDataY1.size();
        meanZ1=sumZ1/tapDataZ1.size();
        meanM1=sumM1/tapDataM1.size();
        meanX2=sumX2/tapDataX2.size();
        meanY2=sumY2/tapDataY2.size();
        meanZ2=sumZ2/tapDataZ2.size();
        meanM2=sumM2/tapDataM2.size();
        meanX3=sumX3/tapDataX3.size();
        meanY3=sumY3/tapDataY3.size();
        meanZ3=sumZ3/tapDataZ3.size();
        meanM3=sumM3/tapDataM3.size();
    }

    public void standard_deviation_calc(){
        double sqX1=0,sqY1=0,sqZ1=0,sqM1=0,sqX2=0,sqY2=0,sqZ2=0,sqM2=0,sqX3=0,sqY3=0,sqZ3=0,sqM3=0;

        for(int i=0;i<tapDataX1.size();i++) {
            sqX1 = sqX1 + Math.pow((tapDataX1.get(i).getX()-meanX1),2);
        }
        for(int i=0;i<tapDataY1.size();i++) {
            sqY1 = sqY1 + Math.pow((tapDataY1.get(i).getY()-meanY1),2);
        }
        for(int i=0;i<tapDataZ1.size();i++) {
            sqZ1 = sqZ1 + Math.pow((tapDataZ1.get(i).getZ()-meanZ1),2);
        }
        for(int i=0;i<tapDataM1.size();i++) {
            sqM1 = sqM1 + Math.pow((tapDataM1.get(i).getMagnitude()-meanM1),2);
        }
        for(int i=0;i<tapDataX2.size();i++) {
            sqX2 = sqX2 + Math.pow((tapDataX2.get(i).getX()-meanX2),2);
        }
        for(int i=0;i<tapDataY2.size();i++) {
            sqY2 = sqY2 + Math.pow((tapDataY2.get(i).getY()-meanY2),2);
        }
        for(int i=0;i<tapDataZ2.size();i++) {
            sqZ2 = sqZ2 + Math.pow((tapDataZ2.get(i).getZ()-meanZ2),2);
        }
        for(int i=0;i<tapDataM2.size();i++) {
            sqM2 = sqM2 + Math.pow((tapDataM2.get(i).getMagnitude()-meanM2),2);
        }
        for(int i=0;i<tapDataX3.size();i++) {
            sqX3 = sqX3 + Math.pow((tapDataX3.get(i).getX()-meanX3),2);
        }
        for(int i=0;i<tapDataY3.size();i++) {
            sqY3 = sqY3 + Math.pow((tapDataY3.get(i).getY()-meanY3),2);
        }
        for(int i=0;i<tapDataZ3.size();i++) {
            sqZ3 = sqZ3 + Math.pow((tapDataZ3.get(i).getZ()-meanZ3),2);
        }
        for(int i=0;i<tapDataM3.size();i++) {
            sqM3 = sqM3 + Math.pow((tapDataM3.get(i).getMagnitude()-meanM3),2);
        }
        sdX1=Math.sqrt(sqX1/(tapDataX1.size()-1));
        sdY1=Math.sqrt(sqY1/(tapDataY1.size()-1));
        sdZ1=Math.sqrt(sqZ1/(tapDataZ1.size()-1));
        sdM1=Math.sqrt(sqM1/(tapDataM1.size()-1));
        sdX2=Math.sqrt(sqX2/(tapDataX2.size()-1));
        sdY2=Math.sqrt(sqY2/(tapDataY2.size()-1));
        sdZ2=Math.sqrt(sqZ2/(tapDataZ2.size()-1));
        sdM2=Math.sqrt(sqM2/(tapDataM2.size()-1));
        sdX3=Math.sqrt(sqX3/(tapDataX3.size()-1));
        sdY3=Math.sqrt(sqY3/(tapDataY3.size()-1));
        sdZ3=Math.sqrt(sqZ3/(tapDataZ3.size()-1));
        sdM3=Math.sqrt(sqM3/(tapDataM3.size()-1));
    }

    public void avg_calculation(){
        long t1=0,t2=0,t3=0,t4=0,t5=0,t6=0;
        for(int i=0;i<hundredMilliDataBehind1.size();i++){
            t1 = t1 + hundredMilliDataBehind1.get(i).getTimestamp();
        }
        for(int i=0;i<hundredMilliDataBehind2.size();i++){
            t2 = t2 + hundredMilliDataBehind2.get(i).getTimestamp();
        }
        for(int i=0;i<hundredMilliDataBehind3.size();i++){
            t3 = t3 + hundredMilliDataBehind3.get(i).getTimestamp();
        }
        avg100msBefore1=t1/hundredMilliDataBehind1.size();
        avg100msBefore2=t2/hundredMilliDataBehind2.size();
        avg100msBefore3=t3/hundredMilliDataBehind3.size();

        for(int i=0;i<DataAhead1.size();i++){
            t4 = t4 + DataAhead1.get(i).getTimestamp();
        }
        for(int i=0;i<DataAhead2.size();i++){
            t5 = t5 + DataAhead2.get(i).getTimestamp();
        }
        for(int i=0;i<DataAhead3.size();i++){
            t6 = t6 + DataAhead3.get(i).getTimestamp();
        }

        avg100msAfter1=t4/DataAhead1.size();
        avg100msAfter2=t5/DataAhead2.size();
        avg100msAfter3=t6/DataAhead3.size();
        diff1=avg100msAfter1-avg100msBefore1;
        diff2=avg100msAfter2-avg100msBefore2;
        diff3=avg100msAfter3-avg100msBefore3;
    }

    public void net_change(){
        double avgX1,avgY1,avgZ1,avgM1,sX1=0,sY1=0,sZ1=0,sM1=0;
        double avgX2,avgY2,avgZ2,avgM2,sX2=0,sY2=0,sZ2=0,sM2=0;
        double avgX3,avgY3,avgZ3,avgM3,sX3=0,sY3=0,sZ3=0,sM3=0;
        for(int i=0;i<tapDataX1.size();i++) {
            sX1 = sX1 + tapDataX1.get(i).getX();
        }
        for(int i=0;i<tapDataY1.size();i++) {
            sY1 = sY1 + tapDataY1.get(i).getY();
        }
        for(int i=0;i<tapDataZ1.size();i++) {
            sZ1 = sZ1 + tapDataZ1.get(i).getZ();
        }
        for(int i=0;i<tapDataM1.size();i++) {
            sM1 = sM1 + tapDataM1.get(i).getMagnitude();
        }
        avgX1=sX1/tapDataX1.size();
        avgY1=sY1/tapDataY1.size();
        avgZ1=sZ1/tapDataZ1.size();
        avgM1=sM1/tapDataM1.size();
        ncX1=avgX1-avg100msBefore1;
        ncY1=avgY1-avg100msBefore1;
        ncZ1=avgZ1-avg100msBefore1;
        ncM1=avgM1-avg100msBefore1;
        for(int i=0;i<tapDataX2.size();i++) {
            sX2 = sX2 + tapDataX2.get(i).getX();
        }
        for(int i=0;i<tapDataY2.size();i++) {
            sY2 = sY2 + tapDataY2.get(i).getY();
        }
        for(int i=0;i<tapDataZ2.size();i++) {
            sZ2 = sZ2 + tapDataZ2.get(i).getZ();
        }
        for(int i=0;i<tapDataM2.size();i++) {
            sM2 = sM2 + tapDataM2.get(i).getMagnitude();
        }
        avgX2=sX2/tapDataX2.size();
        avgY2=sY2/tapDataY2.size();
        avgZ2=sZ2/tapDataZ2.size();
        avgM2=sM2/tapDataM2.size();
        ncX2=avgX2-avg100msBefore2;
        ncY2=avgY2-avg100msBefore2;
        ncZ2=avgZ2-avg100msBefore2;
        ncM2=avgM2-avg100msBefore2;
        for(int i=0;i<tapDataX3.size();i++) {
            sX3 = sX3 + tapDataX3.get(i).getX();
        }
        for(int i=0;i<tapDataY3.size();i++) {
            sY3 = sY3 + tapDataY3.get(i).getY();
        }
        for(int i=0;i<tapDataZ3.size();i++) {
            sZ3 = sZ3 + tapDataZ3.get(i).getZ();
        }
        for(int i=0;i<tapDataM3.size();i++) {
            sM3 = sM3 + tapDataM3.get(i).getMagnitude();
        }
        avgX3=sX3/tapDataX3.size();
        avgY3=sY3/tapDataY3.size();
        avgZ3=sZ3/tapDataZ3.size();
        avgM3=sM3/tapDataM3.size();
        ncX3=avgX3-avg100msBefore3;
        ncY3=avgY3-avg100msBefore3;
        ncZ3=avgZ3-avg100msBefore3;
        ncM3=avgM3-avg100msBefore3;
    }

    public void max_change(){
        mcX1=maxX1-avg100msBefore1;
        mcY1=maxY1-avg100msBefore1;
        mcZ1=maxZ1-avg100msBefore1;
        mcM1=maxM1-avg100msBefore1;
        mcX2=maxX2-avg100msBefore2;
        mcY2=maxY2-avg100msBefore2;
        mcZ2=maxZ2-avg100msBefore2;
        mcM2=maxM2-avg100msBefore2;
        mcX3=maxX3-avg100msBefore3;
        mcY3=maxY3-avg100msBefore3;
        mcZ3=maxZ3-avg100msBefore3;
        mcM3=maxM3-avg100msBefore3;
    }

    public void duration(){
        long t_before_center1,t_before_center2,t_before_center3;
        t_after_center1=DataAhead1.get(DataAhead1.size()/2).getTimestamp();
        t_after_center2=DataAhead2.get(DataAhead2.size()/2).getTimestamp();
        t_after_center3=DataAhead3.get(DataAhead3.size()/2).getTimestamp();
        t_before_center1=hundredMilliDataBehind1.get(hundredMilliDataBehind1.size()/2).getTimestamp();
        t_before_center2=hundredMilliDataBehind2.get(hundredMilliDataBehind2.size()/2).getTimestamp();
        t_before_center3=hundredMilliDataBehind3.get(hundredMilliDataBehind3.size()/2).getTimestamp();
        dur1= ((double)(t_after_center1-t_before_center1)/(double)diff1);
        dur2= ((double)(t_after_center2-t_before_center2)/(double)diff2);
        dur3= ((double)(t_after_center3-t_before_center3)/(double)diff3);
    }

    public void max_to_avg(){
        max2avgX1= (double) ((t_after_center1-t_max_tapX1)/(avg100msAfter1-maxX1));
        max2avgY1= (double) ((t_after_center1-t_max_tapY1)/(avg100msAfter1-maxY1));
        max2avgZ1= (double) ((t_after_center1-t_max_tapZ1)/(avg100msAfter1-maxZ1));
        max2avgM1= (double) ((t_after_center1-t_max_tapM1)/(avg100msAfter1-maxM1));
        max2avgX2= (double) ((t_after_center2-t_max_tapX2)/(avg100msAfter2-maxX2));
        max2avgY2= (double) ((t_after_center2-t_max_tapY2)/(avg100msAfter2-maxY2));
        max2avgZ2= (double) ((t_after_center2-t_max_tapZ2)/(avg100msAfter2-maxZ2));
        max2avgM2= (double) ((t_after_center2-t_max_tapM2)/(avg100msAfter2-maxM2));
        max2avgX3= (double) ((t_after_center3-t_max_tapX3)/(avg100msAfter3-maxX3));
        max2avgY3= (double) ((t_after_center3-t_max_tapY3)/(avg100msAfter3-maxY3));
        max2avgZ3= (double) ((t_after_center3-t_max_tapZ3)/(avg100msAfter3-maxZ3));
        max2avgM3= (double) ((t_after_center3-t_max_tapM3)/(avg100msAfter3-maxM3));
    }

    private class AccRead extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wc=new WebServiceCaller();
            wc.setSoapObject("Accel");
            wc.addProperty("f1x1",strings[0]);
            wc.addProperty("f1y1",strings[1]);
            wc.addProperty("f1z1",strings[2]);
            wc.addProperty("f1m1",strings[3]);
            wc.addProperty("f2x1",strings[4]);
            wc.addProperty("f2y1",strings[5]);
            wc.addProperty("f2z1",strings[6]);
            wc.addProperty("f2m1",strings[7]);
            wc.addProperty("f3x1",strings[8]);
            wc.addProperty("f3y1",strings[9]);
            wc.addProperty("f3z1",strings[10]);
            wc.addProperty("f3m1",strings[11]);
            wc.addProperty("f4x1",strings[12]);
            wc.addProperty("f4y1",strings[13]);
            wc.addProperty("f4z1",strings[14]);
            wc.addProperty("f4m1",strings[15]);
            wc.addProperty("f5x1",strings[16]);
            wc.addProperty("f5y1",strings[17]);
            wc.addProperty("f5z1",strings[18]);
            wc.addProperty("f5m1",strings[19]);
            wc.addProperty("f6x1",strings[20]);
            wc.addProperty("f6y1",strings[21]);
            wc.addProperty("f6z1",strings[22]);
            wc.addProperty("f6m1",strings[23]);
            wc.addProperty("f7x1",strings[24]);
            wc.addProperty("f7y1",strings[25]);
            wc.addProperty("f7z1",strings[26]);
            wc.addProperty("f7m1",strings[27]);
            wc.addProperty("f1x2",strings[28]);
            wc.addProperty("f1y2",strings[29]);
            wc.addProperty("f1z2",strings[30]);
            wc.addProperty("f1m2",strings[31]);
            wc.addProperty("f2x2",strings[32]);
            wc.addProperty("f2y2",strings[33]);
            wc.addProperty("f2z2",strings[34]);
            wc.addProperty("f2m2",strings[35]);
            wc.addProperty("f3x2",strings[36]);
            wc.addProperty("f3y2",strings[37]);
            wc.addProperty("f3z2",strings[38]);
            wc.addProperty("f3m2",strings[39]);
            wc.addProperty("f4x2",strings[40]);
            wc.addProperty("f4y2",strings[41]);
            wc.addProperty("f4z2",strings[42]);
            wc.addProperty("f4m2",strings[43]);
            wc.addProperty("f5x2",strings[44]);
            wc.addProperty("f5y2",strings[45]);
            wc.addProperty("f5z2",strings[46]);
            wc.addProperty("f5m2",strings[47]);
            wc.addProperty("f6x2",strings[48]);
            wc.addProperty("f6y2",strings[49]);
            wc.addProperty("f6z2",strings[50]);
            wc.addProperty("f6m2",strings[51]);
            wc.addProperty("f7x2",strings[52]);
            wc.addProperty("f7y2",strings[53]);
            wc.addProperty("f7z2",strings[54]);
            wc.addProperty("f7m2",strings[55]);
            wc.addProperty("f1x3",strings[56]);
            wc.addProperty("f1y3",strings[57]);
            wc.addProperty("f1z3",strings[58]);
            wc.addProperty("f1m3",strings[59]);
            wc.addProperty("f2x3",strings[60]);
            wc.addProperty("f2y3",strings[61]);
            wc.addProperty("f2z3",strings[62]);
            wc.addProperty("f2m3",strings[63]);
            wc.addProperty("f3x3",strings[64]);
            wc.addProperty("f3y3",strings[65]);
            wc.addProperty("f3z3",strings[66]);
            wc.addProperty("f3m3",strings[67]);
            wc.addProperty("f4x3",strings[68]);
            wc.addProperty("f4y3",strings[69]);
            wc.addProperty("f4z3",strings[70]);
            wc.addProperty("f4m3",strings[71]);
            wc.addProperty("f5x3",strings[72]);
            wc.addProperty("f5y3",strings[73]);
            wc.addProperty("f5z3",strings[74]);
            wc.addProperty("f5m3",strings[75]);
            wc.addProperty("f6x3",strings[76]);
            wc.addProperty("f6y3",strings[77]);
            wc.addProperty("f6z3",strings[78]);
            wc.addProperty("f6m3",strings[79]);
            wc.addProperty("f7x3",strings[80]);
            wc.addProperty("f7y3",strings[81]);
            wc.addProperty("f7z3",strings[82]);
            wc.addProperty("f7m3",strings[83]);
            wc.callWebService();

            return wc.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(Combined.this, s, Toast.LENGTH_SHORT).show();
            if (s.equals("intruder")){
                Intent i = new Intent(Combined.this, LockActivity.class);
                startActivity(i);
            }
        }
    }
}

