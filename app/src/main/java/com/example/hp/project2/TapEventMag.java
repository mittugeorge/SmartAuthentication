package com.example.hp.project2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TOMU on 16-03-2018.
 */
public class TapEventMag extends AppCompatActivity implements View.OnClickListener, SensorEventListener, View.OnTouchListener {

    private SensorManager mSensorManager;
    private Sensor mag;
    private boolean started = false, secondStart = false;
    private double x, y, z, M, maxX, maxY, maxZ, maxM;
    private long t_start, t_endX, t_endY, t_endZ, t_endM,
            avg100msBefore, avg100msAfterX,avg100msAfterY,avg100msAfterZ,avg100msAfterM, diffX, diffY, diffZ, diffM;
    long currentTime, t_after_centerX, t_after_centerY,t_after_centerZ, t_after_centerM, t_max_tapX, t_max_tapY, t_max_tapZ, t_max_tapM;
    private long t = 1;
    private double meanX, meanY, meanZ, meanM;
    private double sdX,sdY,sdZ,sdM;
    private double ncX,ncY,ncZ,ncM;
    private double mcX,mcY,mcZ,mcM;
    private double durX, durY, durZ, durM;
    private double max2avgX, max2avgY, max2avgZ, max2avgM;
    ArrayList<SensorData> tapData;
    ArrayList<SensorData> hundredMilliDataAheadX3;
    ArrayList<SensorData> hundredMilliDataAheadY3;
    ArrayList<SensorData> hundredMilliDataAheadZ3;
    ArrayList<SensorData> hundredMilliDataAheadM3;
    List<SensorData> hundredMilliDataBehind;
    ArrayList<SensorData> magData;
    ArrayList<String> timeStamp;
    ArrayList<Long> magTimeStamp;
    ArrayList<SensorData> DataAhead3;
    ArrayList<SensorData> limitSensorData;
    ArrayList<ArrayList<SensorData>> all_clicked_sensorData;
    Button bt_one, bt_two, bt_three, bt_four, bt_five, bt_six, bt_seven, bt_eight, bt_nine, bt_zero, bt_submit,bt_cancel;
    private long TappedCurrentTimeStamp_test;
    ArrayList<SensorData> tapDataX3;
    ArrayList<SensorData> tapDataY3;
    ArrayList<SensorData> tapDataZ3;
    ArrayList<SensorData> tapDataM3;


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
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        bt_cancel.setOnTouchListener(this);
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
        timeStamp = new ArrayList<>();
        magData = new ArrayList<>();
        tapData = new ArrayList<>();
        limitSensorData = new ArrayList<>();
        magTimeStamp = new ArrayList<>();
        hundredMilliDataAheadX3 = new ArrayList<>();
        hundredMilliDataAheadY3 = new ArrayList<>();
        hundredMilliDataAheadZ3 = new ArrayList<>();
        hundredMilliDataAheadM3 = new ArrayList<>();
        hundredMilliDataBehind = new ArrayList<>();
        all_clicked_sensorData = new ArrayList<ArrayList<SensorData>>();
        DataAhead3 = new ArrayList<>();
        tapDataX3 = new ArrayList<>();
        tapDataY3 = new ArrayList<>();
        tapDataZ3 = new ArrayList<>();
        tapDataM3 = new ArrayList<>();
        sensorStarting();
    }

    public void sensorStarting() {
        Log.d("Basil_new", "calling starting functions");
        started = true;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
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
                magData.add(data);
                magTimeStamp.add(currentTime);
            } else {
                magData.remove(0);
                magTimeStamp.remove(0);
                magData.add(data);
                magTimeStamp.add(currentTime);
            }
        } else if (secondStart) {
            Log.d("PerfectMan", "else if");
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            M = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
            currentTime = System.currentTimeMillis();
            SensorData data = new SensorData(currentTime, x, y, z, M);
            DataAhead3.add(data);
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
                    maxX = DataAhead3.get(0).getX();
                    maxY = DataAhead3.get(0).getY();
                    maxZ = DataAhead3.get(0).getZ();
                    maxM = DataAhead3.get(0).getMagnitude();
                    for (int i = 0; i < DataAhead3.size(); i++) {
                        if (DataAhead3.get(i).getX() > maxX) {
                            maxX = DataAhead3.get(i).getX();
                            t_max_tapX = DataAhead3.get(i).getTimestamp();
                        }
                        if (DataAhead3.get(i).getY() > maxY) {
                            maxY = DataAhead3.get(i).getY();
                            t_max_tapY = DataAhead3.get(i).getTimestamp();
                        }
                        if (DataAhead3.get(i).getZ() > maxZ) {
                            maxZ = DataAhead3.get(i).getZ();
                            t_max_tapZ = DataAhead3.get(i).getTimestamp();
                        }
                        if (DataAhead3.get(i).getMagnitude() > maxM) {
                            maxM = DataAhead3.get(i).getMagnitude();
                            t_max_tapM = DataAhead3.get(i).getTimestamp();
                        }
                    }

                    if(t_max_tapX==0) {
                        t_endX=DataAhead3.get(DataAhead3.indexOf(t_max_tapX)+1).getTimestamp();
                    }
                    else t_endX = t_max_tapX;
                    if(t_max_tapY==0) {
                        t_endY=DataAhead3.get(DataAhead3.indexOf(t_max_tapY)+1).getTimestamp();
                    }
                    else t_endY=t_max_tapY;
                    if(t_max_tapZ==0) {
                        t_endZ=DataAhead3.get(DataAhead3.indexOf(t_max_tapZ)+1).getTimestamp();
                    }
                    else t_endZ=t_max_tapZ;
                    if(t_max_tapM==0) {
                        t_endM=DataAhead3.get(DataAhead3.indexOf(t_max_tapM)+1).getTimestamp();
                    }
                    else t_endM=t_max_tapM;

//                    int k1=DataAhead3.indexOf(t_endX);
//                    int k2=DataAhead3.indexOf(t_endY);
//                    int k3=DataAhead3.indexOf(t_endZ);
//                    int k4=DataAhead3.indexOf(t_endM);

                    int k1=0,k2=0,k3=0,k4=0;
                    for (int i = 0; i <  DataAhead3.size(); i++) {
                        if(DataAhead3.get(i).getTimestamp() == t_endX){
                            k1=i;
                        }
                        if(DataAhead3.get(i).getTimestamp() == t_endY){
                            k2=i;
                        }
                        if(DataAhead3.get(i).getTimestamp() == t_endZ){
                            k3=i;
                        }
                        if(DataAhead3.get(i).getTimestamp() == t_endM){
                            k4=i;
                        }
                    }

                    for (int k = 0; k <= k1; k++) {
                        tapDataX3.add(DataAhead3.get(k));
                    }
                    for (int k = 0; k <= k2; k++) {
                        tapDataY3.add(DataAhead3.get(k));
                    }
                    for (int k = 0; k <= k3; k++) {
                        tapDataZ3.add(DataAhead3.get(k));
                    }
                    for (int k = 0; k <= k4; k++) {
                        tapDataM3.add(DataAhead3.get(k));
                    }

//                    long tmp1=t_endX+100;
//                    long tmp2=t_endY+100;
//                    long tmp3=t_endZ+100;
//                    long tmp4=t_endM+100;
//                    int t1 = 0,t2=0,t3=0,t4=0;
//
//                    for(int k = 0;k < DataAhead3.size();k--)
//                    {
//                        if (DataAhead3.get(k).getTimestamp() >= tmp1) {
//                            t1 = k;
//                            break;
//                        }
//                    }
//                    for(int k = 0;k < DataAhead3.size();k--)
//                    {
//                        if (DataAhead3.get(k).getTimestamp() >= tmp2) {
//                            t2 = k;
//                            break;
//                        }
//                    }
//                    for(int k = 0;k < DataAhead3.size();k--)
//                    {
//                        if (DataAhead3.get(k).getTimestamp() >= tmp3) {
//                            t3 = k;
//                            break;
//                        }
//                    }
//                    for(int k = 0;k < DataAhead3.size();k--)
//                    {
//                        if (DataAhead3.get(k).getTimestamp() >= tmp4) {
//                            t4 = k;
//                            break;
//                        }
//                    }

                    for(int k = 0; k < DataAhead3.size();k++)
                    {
                        hundredMilliDataAheadX3.add(DataAhead3.get(k));
                        Log.d("100msDataAhead..X..",""+hundredMilliDataAheadX3.get(hundredMilliDataAheadX3.indexOf(DataAhead3.get(k))).getTimestamp());
                    }
                    for(int k = 0; k < DataAhead3.size();k++)
                    {
                        hundredMilliDataAheadY3.add(DataAhead3.get(k));
                        Log.d("100msDataAhead..Y..",""+hundredMilliDataAheadY3.get(hundredMilliDataAheadX3.indexOf(DataAhead3.get(k))).getTimestamp());
                    }
                    for(int k = 0; k < DataAhead3.size();k++)
                    {
                        hundredMilliDataAheadZ3.add(DataAhead3.get(k));
                        Log.d("100msDataAhead..Z..",""+hundredMilliDataAheadZ3.get(hundredMilliDataAheadX3.indexOf(DataAhead3.get(k))).getTimestamp());
                    }
                    for(int k = 0; k < DataAhead3.size();k++)
                    {
                        hundredMilliDataAheadM3.add(DataAhead3.get(k));
                        Log.d("100msDataAhead..M..",""+hundredMilliDataAheadM3.get(hundredMilliDataAheadX3.indexOf(DataAhead3.get(k))).getTimestamp());
                    }

                    long substractedTimeStamp = TappedCurrentTimeStamp_test - 100;
                    long temp2 = 0;
                    int temp = 0;

                    for (int k = magData.size() - 1; k > 0; k--) {
                        temp2 = substractedTimeStamp;
                        if (magData.get(k).getTimestamp() <= temp2) {
                            temp = k;
                            break;
                        }
                    }

                    hundredMilliDataBehind = magData.subList(temp, magData.size());

                    for (int k = 0; k < hundredMilliDataBehind.size(); k++) {
                        Log.d("100msDataBehind", "" + hundredMilliDataBehind.get(k).getTimestamp());
                    }

                    mean_calculation();
                    standard_deviation_calc();
                    avg_calculation();
                    net_change();
                    max_change();
                    //     diff_time();
                    duration();
                    max_to_avg();

                    MagRead m=new MagRead();
                    m.execute(Double.toString(meanX),Double.toString(meanY),Double.toString(meanZ),Double.toString(meanM),
                            Double.toString(sdX),Double.toString(sdY),Double.toString(sdZ),Double.toString(sdM),
                            Double.toString(diffX),Double.toString(diffY),Double.toString(diffZ),Double.toString(diffM),
                            Double.toString(ncX),Double.toString(ncY),Double.toString(ncZ),Double.toString(ncM),
                            Double.toString(mcX),Double.toString(mcY),Double.toString(mcZ),Double.toString(mcM),
                            Double.toString(durX),Double.toString(durY),Double.toString(durZ),Double.toString(durM),
                            Double.toString(max2avgX),Double.toString(max2avgY),Double.toString(max2avgZ),Double.toString(max2avgM));
                }
            }, 100);
        }


        return true;
    }

    public void mean_calculation() {
        double sumX = 0, sumY = 0, sumZ = 0, sumM = 0;

        for (int i = 0; i < tapDataX3.size(); i++) {
            sumX = sumX + tapDataX3.get(i).getX();
        }
        for (int i = 0; i < tapDataY3.size(); i++) {
            sumY = sumY + tapDataY3.get(i).getY();
        }
        for (int i = 0; i < tapDataZ3.size(); i++) {
            sumZ = sumZ + tapDataZ3.get(i).getZ();
        }
        for (int i = 0; i < tapDataM3.size(); i++) {
            sumM = sumM + tapDataM3.get(i).getMagnitude();
        }
        meanX = sumX / tapDataX3.size();
        meanY = sumY / tapDataY3.size();
        meanZ = sumZ / tapDataZ3.size();
        meanM = sumM / tapDataM3.size();
        Log.d("mean...X..", "" + meanX);
        Log.d("mean...Y..", "" + meanY);
        Log.d("mean...Z..", "" + meanZ);
        Log.d("mean...M..", "" + meanM);
    }

    public void standard_deviation_calc() {
        double sqX = 0, sqY = 0, sqZ = 0, sqM = 0;
        for (int i = 0; i < tapDataX3.size(); i++) {
            sqX = sqX + Math.pow((tapDataX3.get(i).getX() - meanX), 2);
        }
        for (int i = 0; i < tapDataY3.size(); i++) {
            sqY = sqY + Math.pow((tapDataY3.get(i).getY() - meanY), 2);
        }
        for (int i = 0; i < tapDataZ3.size(); i++) {
            sqZ = sqZ + Math.pow((tapDataZ3.get(i).getZ() - meanZ), 2);
        }
        for (int i = 0; i < tapDataM3.size(); i++) {
            sqM = sqM + Math.pow((tapDataM3.get(i).getMagnitude() - meanM), 2);
        }
        sdX = Math.sqrt(sqX / (tapDataX3.size() - 1));
        sdY = Math.sqrt(sqY / (tapDataY3.size() - 1));
        sdZ = Math.sqrt(sqZ / (tapDataZ3.size() - 1));
        sdM = Math.sqrt(sqM / (tapDataM3.size() - 1));
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
        for (int i = 0; i < hundredMilliDataAheadX3.size(); i++) {
            t1 = t1 + hundredMilliDataAheadX3.get(i).getTimestamp();
        }
        for (int i = 0; i < hundredMilliDataAheadY3.size(); i++) {
            t1 = t1 + hundredMilliDataAheadY3.get(i).getTimestamp();
        }
        for (int i = 0; i < hundredMilliDataAheadZ3.size(); i++) {
            t1 = t1 + hundredMilliDataAheadZ3.get(i).getTimestamp();
        }
        for (int i = 0; i < hundredMilliDataAheadM3.size(); i++) {
            t1 = t1 + hundredMilliDataAheadM3.get(i).getTimestamp();
        }
        avg100msAfterX = t1 / hundredMilliDataAheadX3.size();
        avg100msAfterY = t1 / hundredMilliDataAheadY3.size();
        avg100msAfterZ = t1 / hundredMilliDataAheadZ3.size();
        avg100msAfterM = t1 / hundredMilliDataAheadM3.size();
        //Log.d("avg100msAfter......", "" + avg100msAfter);
        diffX = avg100msAfterX - avg100msBefore;
        diffY = avg100msAfterY - avg100msBefore;
        diffZ = avg100msAfterZ - avg100msBefore;
        diffM = avg100msAfterM - avg100msBefore;
        Log.d("Difference..X..", "" + diffX);
        Log.d("Difference..Y..", "" + diffY);
        Log.d("Difference..Z..", "" + diffZ);
        Log.d("Difference..M..", "" + diffM);
    }

    public void net_change() {
        double avgX, avgY, avgZ, avgM, sX = 0, sY = 0, sZ = 0, sM = 0;
        for (int i = 0; i < tapDataX3.size(); i++) {
            sX = sX + tapDataX3.get(i).getX();
        }
        for (int i = 0; i < tapDataY3.size(); i++) {
            sY = sY + tapDataY3.get(i).getY();
        }
        for (int i = 0; i < tapDataZ3.size(); i++) {
            sZ = sZ + tapDataZ3.get(i).getZ();
        }
        for (int i = 0; i < tapDataM3.size(); i++) {
            sM = sM + tapDataM3.get(i).getMagnitude();
        }
        avgX = sX / tapDataX3.size();
        avgY = sY / tapDataY3.size();
        avgZ = sZ / tapDataZ3.size();
        avgM = sM / tapDataM3.size();
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
        long t_before_center;
        t_after_centerX = hundredMilliDataAheadX3.get(hundredMilliDataAheadX3.size() / 2).getTimestamp();
        t_after_centerY = hundredMilliDataAheadY3.get(hundredMilliDataAheadY3.size() / 2).getTimestamp();
        t_after_centerZ = hundredMilliDataAheadZ3.get(hundredMilliDataAheadZ3.size() / 2).getTimestamp();
        t_after_centerM = hundredMilliDataAheadM3.get(hundredMilliDataAheadM3.size() / 2).getTimestamp();
        t_before_center = hundredMilliDataBehind.get(hundredMilliDataBehind.size() / 2).getTimestamp();
        durX= ((double)(t_after_centerX-t_before_center)/(double)diffX);
        durY= ((double)(t_after_centerY-t_before_center)/(double)diffY);
        durZ= ((double)(t_after_centerZ-t_before_center)/(double)diffZ);
        durM= ((double)(t_after_centerM-t_before_center)/(double)diffM);
        Log.d("Normalized time dur..X", "" + durX);
        Log.d("Normalized time dur..Y", "" + durY);
        Log.d("Normalized time dur..Z", "" + durZ);
        Log.d("Normalized time dur..M", "" + durM);
    }

    public void max_to_avg() {
        max2avgX= (double) ((t_after_centerX-t_max_tapX)/(avg100msAfterX-maxX));
        max2avgY= (double) ((t_after_centerY-t_max_tapY)/(avg100msAfterY-maxY));
        max2avgZ= (double) ((t_after_centerZ-t_max_tapZ)/(avg100msAfterZ-maxZ));
        max2avgM= (double) ((t_after_centerM-t_max_tapM)/(avg100msAfterM-maxM));
        Log.d("Max_to_avg..X", "" + max2avgX);
        Log.d("Max_to_avg..Y", "" + max2avgY);
        Log.d("Max_to_avg..Z", "" + max2avgZ);
        Log.d("Max_to_avg..M", "" + max2avgM);
    }

    private class MagRead extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wc=new WebServiceCaller();
            wc.setSoapObject("Magnet");
            wc.addProperty("f1x",strings[0]);
            wc.addProperty("f1y",strings[1]);
            wc.addProperty("f1z",strings[2]);
            wc.addProperty("f1m",strings[3]);
            wc.addProperty("f2x",strings[4]);
            wc.addProperty("f2y",strings[5]);
            wc.addProperty("f2z",strings[6]);
            wc.addProperty("f2m",strings[7]);
            wc.addProperty("f3x",strings[8]);
            wc.addProperty("f3y",strings[9]);
            wc.addProperty("f3z",strings[10]);
            wc.addProperty("f3m",strings[11]);
            wc.addProperty("f4x",strings[12]);
            wc.addProperty("f4y",strings[13]);
            wc.addProperty("f4z",strings[14]);
            wc.addProperty("f4m",strings[15]);
            wc.addProperty("f5x",strings[16]);
            wc.addProperty("f5y",strings[17]);
            wc.addProperty("f5z",strings[18]);
            wc.addProperty("f5m",strings[19]);
            wc.addProperty("f6x",strings[20]);
            wc.addProperty("f6y",strings[21]);
            wc.addProperty("f6z",strings[22]);
            wc.addProperty("f6m",strings[23]);
            wc.addProperty("f7x",strings[24]);
            wc.addProperty("f7y",strings[25]);
            wc.addProperty("f7z",strings[26]);
            wc.addProperty("f7m",strings[27]);
            wc.callWebService();

            return wc.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(TapEventMag.this, s, Toast.LENGTH_SHORT).show();
        }
    }
}