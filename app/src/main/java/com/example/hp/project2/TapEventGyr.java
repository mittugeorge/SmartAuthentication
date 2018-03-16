package com.example.hp.project2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TOMU on 16-03-2018.
 */
public class TapEventGyr extends AppCompatActivity implements View.OnClickListener, SensorEventListener, View.OnTouchListener {

    private SensorManager mSensorManager;
    private Sensor gyr;
    private boolean started = false, secondStart = false;
    private double x, y, z, M, maxX, maxY, maxZ, maxM;
    private long t_start, t_endX, t_endY, t_endZ, t_endM,
            avg100msBefore, avg100msAfter, diff;
    long currentTime, t_after_center, t_max_tapX, t_max_tapY, t_max_tapZ, t_max_tapM;
    private long t = 1;
    private double meanX, meanY, meanZ, meanM;
    ArrayList<SensorData> tapData;
    ArrayList<SensorData> hundredMilliDataAhead;
    List<SensorData> hundredMilliDataBehind;
    ArrayList<SensorData> gyrData;
    ArrayList<String> timeStamp;
    ArrayList<Long> gyrTimeStamp;
    ArrayList<SensorData> DataAhead2;
    ArrayList<SensorData> limitSensorData;
    ArrayList<ArrayList<SensorData>> all_clicked_sensorData;
    Button bt_one, bt_two, bt_three, bt_four, bt_five, bt_six, bt_seven, bt_eight, bt_nine, bt_zero, bt_submit;
    TextView tv_enter;//, tv_captcha;
    private long TappedCurrentTimeStamp_test;
    ArrayList<SensorData> tapDataX2;
    ArrayList<SensorData> tapDataY2;
    ArrayList<SensorData> tapDataZ2;
    ArrayList<SensorData> tapDataM2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        gyrData = new ArrayList<>();
        tapData = new ArrayList<>();
        limitSensorData = new ArrayList<>();
        gyrTimeStamp = new ArrayList<>();
        hundredMilliDataAhead = new ArrayList<>();
        hundredMilliDataBehind = new ArrayList<>();
        all_clicked_sensorData = new ArrayList<ArrayList<SensorData>>();
        DataAhead2 = new ArrayList<>();
        tapDataX2 = new ArrayList<>();
        tapDataY2 = new ArrayList<>();
        tapDataZ2 = new ArrayList<>();
        tapDataM2 = new ArrayList<>();
        sensorStarting();
    }

    public void sensorStarting() {
        Log.d("Basil_new", "calling starting functions");
        started = true;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyr = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(this, gyr, SensorManager.SENSOR_DELAY_FASTEST);
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
        if (started == true) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (started) {
            Log.d("PerfectMan", "if");
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            M = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
            currentTime = System.currentTimeMillis();
            SensorData data = new SensorData(currentTime, x, y, z, M);
            if (timeStamp.size() < 200) {
                gyrData.add(data);
                gyrTimeStamp.add(currentTime);
            } else {
                gyrData.remove(0);
                gyrTimeStamp.remove(0);
                gyrData.add(data);
                gyrTimeStamp.add(currentTime);
            }
        } else if (secondStart) {
            Log.d("PerfectMan", "else if");
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            M = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
            currentTime = System.currentTimeMillis();
            SensorData data = new SensorData(currentTime, x, y, z, M);
            DataAhead2.add(data);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            TappedCurrentTimeStamp_test = currentTime;
            t_start = TappedCurrentTimeStamp_test;
            Log.d("Tapped..t_start", "" + TappedCurrentTimeStamp_test);
            started = false;
            secondStart = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    secondStart = false;
                    maxX = DataAhead2.get(0).getX();
                    maxY = DataAhead2.get(0).getY();
                    maxZ = DataAhead2.get(0).getZ();
                    maxM = DataAhead2.get(0).getMagnitude();
                    for (int i = 0; i < DataAhead2.size(); i++) {
                        if (DataAhead2.get(i).getX() > maxX) {
                            maxX = DataAhead2.get(i).getX();
                            t_max_tapX = DataAhead2.get(i).getTimestamp();
                        }
                        if (DataAhead2.get(i).getY() > maxY) {
                            maxY = DataAhead2.get(i).getY();
                            t_max_tapY = DataAhead2.get(i).getTimestamp();
                        }
                        if (DataAhead2.get(i).getZ() > maxZ) {
                            maxZ = DataAhead2.get(i).getZ();
                            t_max_tapZ = DataAhead2.get(i).getTimestamp();
                        }
                        if (DataAhead2.get(i).getMagnitude() > maxM) {
                            maxM = DataAhead2.get(i).getMagnitude();
                            t_max_tapM = DataAhead2.get(i).getTimestamp();
                        }
                    }
                    t_endX = t_max_tapX;
                    t_endY = t_max_tapY;
                    t_endZ = t_max_tapZ;
                    t_endM = t_max_tapM;

                    for (int k = 0; k <= DataAhead2.get((int) t_endX).getTimestamp(); k++) {
                        tapDataX2.add(DataAhead2.get(k));
                    }
                    for (int k = 0; k <= DataAhead2.get((int) t_endY).getTimestamp(); k++) {
                        tapDataY2.add(DataAhead2.get(k));
                    }
                    for (int k = 0; k <= DataAhead2.get((int) t_endZ).getTimestamp(); k++) {
                        tapDataZ2.add(DataAhead2.get(k));
                    }
                    for (int k = 0; k <= DataAhead2.get((int) t_endM).getTimestamp(); k++) {
                        tapDataM2.add(DataAhead2.get(k));
                    }

                    for (int k = 0; k < DataAhead2.size(); k++) {
                        hundredMilliDataAhead.add(DataAhead2.get(k));
                        Log.d("100msDataAhead", "" + hundredMilliDataAhead.get(k).getTimestamp());
                    }

                    long substractedTimeStamp = TappedCurrentTimeStamp_test - 100;
                    long temp2 = 0;
                    int temp = 0;

                    for (int k = gyrData.size() - 1; k > 0; k--) {
                        temp2 = substractedTimeStamp;
                        if (gyrData.get(k).getTimestamp() <= temp2) {
                            temp = k;
                            break;
                        }
                    }

                    hundredMilliDataBehind = gyrData.subList(temp, gyrData.size());

                    for (int k = 0; k < hundredMilliDataBehind.size(); k++) {
                        Log.d("100msDataBehind", "" + hundredMilliDataBehind.get(k).getTimestamp());
                    }

                    limitSensorData.addAll(hundredMilliDataBehind);

                    for (int i = 0; i < hundredMilliDataAhead.size(); i++) {
                        limitSensorData.add(hundredMilliDataAhead.get(i));
                    }

                    mean_calculation();
                    standard_deviation_calc();
                    avg_calculation();
                    net_change();
                    max_change();
                    //     diff_time();
                    duration();
                    max_to_avg();
                }
            }, 100);
        }


        return true;
    }

    public void mean_calculation() {
        double sumX = 0, sumY = 0, sumZ = 0, sumM = 0;

        for (int i = 0; i < tapDataX2.size(); i++) {
            sumX = sumX + tapDataX2.get(i).getX();
        }
        for (int i = 0; i < tapDataY2.size(); i++) {
            sumY = sumY + tapDataY2.get(i).getY();
        }
        for (int i = 0; i < tapDataZ2.size(); i++) {
            sumZ = sumZ + tapDataZ2.get(i).getZ();
        }
        for (int i = 0; i < tapDataM2.size(); i++) {
            sumM = sumM + tapDataM2.get(i).getMagnitude();
        }
        meanX = sumX / tapDataX2.size();
        meanY = sumY / tapDataY2.size();
        meanZ = sumZ / tapDataZ2.size();
        meanM = sumM / tapDataM2.size();
        Log.d("mean...X..", "" + meanX);
        Log.d("mean...Y..", "" + meanY);
        Log.d("mean...Z..", "" + meanZ);
        Log.d("mean...M..", "" + meanM);
    }

    public void standard_deviation_calc() {
        double sqX = 0, sqY = 0, sqZ = 0, sqM = 0;
        double sdX, sdY, sdZ, sdM;
        for (int i = 0; i < tapDataX2.size(); i++) {
            sqX = sqX + Math.pow((tapDataX2.get(i).getX() - meanX), 2);
        }
        for (int i = 0; i < tapDataY2.size(); i++) {
            sqY = sqY + Math.pow((tapDataY2.get(i).getY() - meanY), 2);
        }
        for (int i = 0; i < tapDataZ2.size(); i++) {
            sqZ = sqZ + Math.pow((tapDataZ2.get(i).getZ() - meanZ), 2);
        }
        for (int i = 0; i < tapDataM2.size(); i++) {
            sqM = sqM + Math.pow((tapDataM2.get(i).getMagnitude() - meanM), 2);
        }
        sdX = Math.sqrt(sqX / (tapDataX2.size() - 1));
        sdY = Math.sqrt(sqY / (tapDataY2.size() - 1));
        sdZ = Math.sqrt(sqZ / (tapDataZ2.size() - 1));
        sdM = Math.sqrt(sqM / (tapDataM2.size() - 1));
        Log.d("standard deviation..X", "" + sdX);
        Log.d("standard deviation..Y", "" + sdY);
        Log.d("standard deviation..Z", "" + sdZ);
        Log.d("standard deviation..M", "" + sdM);
    }

    public void avg_calculation() {
        long t = 0, t1 = 0;
        for (int i = 0; i < hundredMilliDataBehind.size(); i++) {
            t = t + hundredMilliDataBehind.get(i).getTimestamp();
        }
        avg100msBefore = t / hundredMilliDataBehind.size();
        Log.d("avg100msBefore......", "" + avg100msBefore);
        for (int i = 0; i < hundredMilliDataAhead.size(); i++) {
            t1 = t1 + hundredMilliDataAhead.get(i).getTimestamp();
        }
        avg100msAfter = t1 / hundredMilliDataAhead.size();
        Log.d("avg100msAfter......", "" + avg100msAfter);
        diff = avg100msAfter - avg100msBefore;
        Log.d("Difference....", "" + diff);
    }

    public void net_change() {
        double avgX, avgY, avgZ, avgM, sX = 0, sY = 0, sZ = 0, sM = 0, ncX, ncY, ncZ, ncM;
        for (int i = 0; i < tapDataX2.size(); i++) {
            sX = sX + tapDataX2.get(i).getX();
        }
        for (int i = 0; i < tapDataY2.size(); i++) {
            sY = sY + tapDataY2.get(i).getY();
        }
        for (int i = 0; i < tapDataZ2.size(); i++) {
            sZ = sZ + tapDataZ2.get(i).getZ();
        }
        for (int i = 0; i < tapDataM2.size(); i++) {
            sM = sM + tapDataM2.get(i).getMagnitude();
        }
        avgX = sX / tapDataX2.size();
        avgY = sY / tapDataY2.size();
        avgZ = sZ / tapDataZ2.size();
        avgM = sM / tapDataM2.size();
        ncX = avgX - avg100msBefore;
        ncY = avgY - avg100msBefore;
        ncZ = avgZ - avg100msBefore;
        ncM = avgM - avg100msBefore;
        Log.d("Net change..X...", "" + ncX);
        Log.d("Net change..Y...", "" + ncY);
        Log.d("Net change..Z...", "" + ncZ);
        Log.d("Net change..M...", "" + ncM);
    }

    public void max_change() {
        double mcX, mcY, mcZ, mcM;
        mcX = maxX - avg100msBefore;
        mcY = maxY - avg100msBefore;
        mcZ = maxZ - avg100msBefore;
        mcM = maxM - avg100msBefore;
        Log.d("Maximum Change in X", "" + mcX);
        Log.d("Maximum Change in Y", "" + mcY);
        Log.d("Maximum Change in Z", "" + mcZ);
        Log.d("Maximum Change in M", "" + mcM);
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

    public void duration() {
        long dur, t_before_center;
        t_after_center = hundredMilliDataAhead.get(hundredMilliDataAhead.size() / 2).getTimestamp();
        t_before_center = hundredMilliDataBehind.get(hundredMilliDataBehind.size() / 2).getTimestamp();
        dur = (t_after_center - t_before_center) / diff;
        Log.d("Normalized time duratio", "" + dur);
    }

    public void max_to_avg() {
        long max2avgX, max2avgY, max2avgZ, max2avgM;
        max2avgX = (long) ((t_after_center - t_max_tapX) / (avg100msAfter - maxX));
        max2avgY = (long) ((t_after_center - t_max_tapY) / (avg100msAfter - maxY));
        max2avgZ = (long) ((t_after_center - t_max_tapZ) / (avg100msAfter - maxZ));
        max2avgM = (long) ((t_after_center - t_max_tapM) / (avg100msAfter - maxM));
        Log.d("Max_to_avg..X", "" + max2avgX);
        Log.d("Max_to_avg..Y", "" + max2avgY);
        Log.d("Max_to_avg..Z", "" + max2avgZ);
        Log.d("Max_to_avg..M", "" + max2avgM);
    }
}