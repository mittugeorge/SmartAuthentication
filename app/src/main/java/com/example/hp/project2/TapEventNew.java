package com.example.hp.project2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by HP on 2/10/2018.
 */

public class TapEventNew extends AppCompatActivity implements View.OnClickListener, SensorEventListener, View.OnTouchListener {

    private SensorManager mSensorManager;
    private Sensor acc, gyr, mag;
    private boolean started = false,secondStart = false;
    private double x, y, z, M, maxX, maxY, maxZ;
    private long t_start, t_end, avg100msBefore, avg100msAfter, diff;;
    long currentTime, t_after_center, t_max_tapX, t_max_tapY, t_max_tapZ;
    private long t = 1;
    private double meanX, meanY, meanZ;
    //private ArrayList accData;
    ArrayList<String> accData;
    ArrayList<SensorData> outData;
    ArrayList<SensorData> tapData;
    ArrayList<SensorData> hundredMilliDataAhead;
    List<SensorData> hundredMilliDataBehind;
    ArrayList<String> gyrData;
    ArrayList<String> magData;
    ArrayList<String> timeStamp;
    ArrayList<Long> ourTimeStamp;
    ArrayList<SensorData> limitSensorData;
    ArrayList<ArrayList<SensorData>> all_clicked_sensorData;
    Button bt_one, bt_two, bt_three, bt_four, bt_five, bt_six, bt_seven, bt_eight, bt_nine, bt_zero, bt_submit;
    TextView tv_enter;//, tv_captcha;
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
        tv_enter = (TextView) findViewById(R.id.tv_enter);
        bt_one.setOnClickListener(this);
        bt_three.setOnClickListener(this);
        bt_seven.setOnClickListener(this);

        bt_one.setOnTouchListener(this);
        bt_submit.setOnClickListener(this);
        timeStamp = new ArrayList<>();
        accData = new ArrayList<>();
        gyrData = new ArrayList<>();
        magData = new ArrayList<>();
        outData = new ArrayList<>();
        tapData = new ArrayList<>();
        limitSensorData = new ArrayList<>();
        ourTimeStamp = new ArrayList<>();
        hundredMilliDataAhead = new ArrayList<>();
        hundredMilliDataBehind = new ArrayList<>();

        all_clicked_sensorData = new ArrayList<ArrayList<SensorData>>();


        sensorStarting();
    }

    public void sensorStarting()
    {
        Log.d("Basil_new", "calling starting functions");
        started = true;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_FASTEST);
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
            Log.d("PerfectMan","if");
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            M = Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
            currentTime = System.currentTimeMillis();
            SensorData data = new SensorData(currentTime, x, y, z, M);
            if (timeStamp.size() < 200)
            {
                outData.add(data);
                ourTimeStamp.add(currentTime);
            }
            else
            {
                outData.remove(0);
                ourTimeStamp.remove(0);
                outData.add(data);
                ourTimeStamp.add(currentTime);

            }
        }
        else if(secondStart)
        {
            Log.d("PerfectMan","else if");
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            M = Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
            currentTime = System.currentTimeMillis();
            SensorData data = new SensorData(currentTime, x, y, z, M);
            hundredMilliDataAhead.add(data);

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
                    for(int k = 0; k < hundredMilliDataAhead.size();k++)
                    {
                        Log.d("100msDataAhead",""+hundredMilliDataAhead.get(k).getTimestamp());
                    }

                    long substractedTimeStamp = TappedCurrentTimeStamp_test - 100;
                    long temp2 = 0;
                    int temp = 0;

                    for(int k = outData.size()-1;k >0;k--)
                    {
                        temp2 = substractedTimeStamp;
                        if (outData.get(k).getTimestamp() <= temp2) {
                            temp = k;
                            break;
                        }
                    }

                    hundredMilliDataBehind =  outData.subList(temp,outData.size());

                    for(int k = 0; k< hundredMilliDataBehind.size();k++)
                    {
                        Log.d("100msDataBehind",""+hundredMilliDataBehind.get(k).getTimestamp());
                    }

                    limitSensorData.addAll(hundredMilliDataBehind);

                    for(int i =0 ; i< hundredMilliDataAhead.size();i++)
                    {
                        limitSensorData.add(hundredMilliDataAhead.get(i));
                    }


                    for(int i=0;i<hundredMilliDataAhead.size()/2;i++){
                        tapData.add(hundredMilliDataAhead.get(i));
                        Log.d("Data...",""+tapData.get(i).getTimestamp());
                    }
                    t_end=tapData.get(tapData.size()-1).getTimestamp();
                    mean_calculation();
                    standard_deviation_calc();
                    avg_calculation();
                    net_change();
                    max_change();
               //     diff_time();
                    duration();
                    max_to_avg();
                }
            },100);
        }

        return true;
    }


    public void mean_calculation()
    {
        double sumX=0,sumY=0,sumZ=0;

        for(int i=0;i<limitSensorData.size();i++){
            sumX = sumX + limitSensorData.get(i).getX();
            sumY = sumY + limitSensorData.get(i).getY();
            sumZ = sumZ + limitSensorData.get(i).getZ();
        }
        meanX=sumX/limitSensorData.size();
        meanY=sumY/limitSensorData.size();
        meanZ=sumZ/limitSensorData.size();
        Log.d("mean...X..",""+meanX);
        Log.d("mean...Y..",""+meanY);
        Log.d("mean...Z..",""+meanZ);
    }

    public void standard_deviation_calc(){
        double sqX=0,sqY=0,sqZ=0;
        double sdX,sdY,sdZ;
        for(int i=0;i<limitSensorData.size();i++){
            sqX = sqX + Math.pow((limitSensorData.get(i).getX()-meanX),2);
            sqY = sqY + Math.pow((limitSensorData.get(i).getY()-meanY),2);
            sqZ = sqZ + Math.pow((limitSensorData.get(i).getZ()-meanZ),2);
        }
        sdX=Math.sqrt(sqX/(limitSensorData.size()-1));
        sdY=Math.sqrt(sqY/(limitSensorData.size()-1));
        sdZ=Math.sqrt(sqZ/(limitSensorData.size()-1));
        Log.d("standard deviation..X",""+sdX);
        Log.d("standard deviation..Y",""+sdY);
        Log.d("standard deviation..Z",""+sdZ);
    }

    public void avg_calculation(){
        long t=0,t1=0;
        for(int i=0;i<hundredMilliDataBehind.size();i++){
            t = t + hundredMilliDataBehind.get(i).getTimestamp();
        }
        avg100msBefore=t/hundredMilliDataBehind.size();
        Log.d("avg100msBefore......",""+avg100msBefore);
        for(int i=0;i<hundredMilliDataAhead.size();i++){
            t1 = t1 + hundredMilliDataAhead.get(i).getTimestamp();
        }
        avg100msAfter=t1/hundredMilliDataAhead.size();
        Log.d("avg100msAfter......",""+avg100msAfter);
        diff=avg100msAfter-avg100msBefore;
        Log.d("Difference....",""+diff);
    }

    public void net_change(){
        double avgX,avgY,avgZ,sX=0,sY=0,sZ=0,ncX,ncY,ncZ;
        for(int i=0;i<tapData.size();i++){
            sX = sX + tapData.get(i).getX();
            sY = sY + tapData.get(i).getY();
            sZ = sZ + tapData.get(i).getZ();
        }
        avgX=sX/tapData.size();
        avgY=sY/tapData.size();
        avgZ=sZ/tapData.size();
        ncX=avgX-avg100msBefore;
        ncY=avgY-avg100msBefore;
        ncZ=avgZ-avg100msBefore;
        Log.d("Net change..X...",""+ncX);
        Log.d("Net change..Y...",""+ncY);
        Log.d("Net change..Z...",""+ncZ);
    }
    public void max_change(){
        double mcX,mcY,mcZ;
        maxX=tapData.get(0).getX();
        maxY=tapData.get(0).getY();
        maxZ=tapData.get(0).getZ();
        for(int i=0; i<tapData.size(); i++){
            if(tapData.get(i).getX() > maxX){
                maxX = tapData.get(i).getX();
                t_max_tapX=tapData.get(i).getTimestamp();
            }
            if(tapData.get(i).getY() > maxY){
                maxY = tapData.get(i).getY();
                t_max_tapY=tapData.get(i).getTimestamp();
            }
            if(tapData.get(i).getZ() > maxZ){
                maxZ = tapData.get(i).getZ();
                t_max_tapZ=tapData.get(i).getTimestamp();
            }
        }
        mcX=maxX-avg100msBefore;
        mcY=maxY-avg100msBefore;
        mcZ=maxZ-avg100msBefore;
        Log.d("Maximum Change in X",""+mcX);
        Log.d("Maximum Change in Y",""+mcY);
        Log.d("Maximum Change in Z",""+mcZ);
    }

   /* public void diff_time(){
        long t_min;
        for(int i=0;i<hundredMilliDataAhead.size();i++){
            for (j=0;j<hundredMilliDataAhead.size();j++){
                mod=
            }
            avgDiffs(i)=mod/(hundredMilliDataAhead.size()-i+1);
        }
    }*/

    public void duration(){
        long dur, t_before_center;
        t_after_center=hundredMilliDataAhead.get(hundredMilliDataAhead.size()/2).getTimestamp();
        t_before_center=hundredMilliDataBehind.get(hundredMilliDataBehind.size()/2).getTimestamp();
        dur=(t_after_center-t_before_center)/diff;
        Log.d("Normalized time duratio",""+dur);
    }

    public void max_to_avg(){
        long max2avgX, max2avgY, max2avgZ;
        max2avgX= (long) ((t_after_center-t_max_tapX)/(avg100msAfter-maxX));
        max2avgY= (long) ((t_after_center-t_max_tapY)/(avg100msAfter-maxY));
        max2avgZ= (long) ((t_after_center-t_max_tapZ)/(avg100msAfter-maxZ));
        Log.d("Max_to_avg..X",""+max2avgX);
        Log.d("Max_to_avg..Y",""+max2avgY);
        Log.d("Max_to_avg..Z",""+max2avgZ);
    }
}

