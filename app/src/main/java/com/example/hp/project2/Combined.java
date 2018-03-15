//under development

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
    private double maxX1, maxY1, maxZ1;
    private double maxX2, maxY2, maxZ2;
    private double maxX3, maxY3, maxZ3;
    private long t_start;
    private long t_endX1, t_endY1, t_endZ1;
    private long t_endX2, t_endY2, t_endZ2;
    private long t_endX3, t_endY3, t_endZ3;
    private long avg100msBefore, avg100msAfter, diff;;
    long currentTime, t_after_center;
    long t_max_tapX1, t_max_tapY1, t_max_tapZ1;
    long t_max_tapX2, t_max_tapY2, t_max_tapZ2;
    long t_max_tapX3, t_max_tapY3, t_max_tapZ3;
    private long t = 1;
    private double meanX1, meanY1, meanZ1;
    private double meanX2, meanY2, meanZ2;
    private double meanX3, meanY3, meanZ3;
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
    ArrayList<SensorData> hundredMilliDataAheadX1;
    ArrayList<SensorData> hundredMilliDataAheadX2;
    ArrayList<SensorData> hundredMilliDataAheadX3;
    ArrayList<SensorData> hundredMilliDataAheadY1;
    ArrayList<SensorData> hundredMilliDataAheadY2;
    ArrayList<SensorData> hundredMilliDataAheadY3;
    ArrayList<SensorData> hundredMilliDataAheadZ1;
    ArrayList<SensorData> hundredMilliDataAheadZ2;
    ArrayList<SensorData> hundredMilliDataAheadZ3;
    List<SensorData> hundredMilliDataBehind;
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
        hundredMilliDataAheadX1 = new ArrayList<>();
        hundredMilliDataAheadX2 = new ArrayList<>();
        hundredMilliDataAheadX3 = new ArrayList<>();
        hundredMilliDataAheadY1 = new ArrayList<>();
        hundredMilliDataAheadY2 = new ArrayList<>();
        hundredMilliDataAheadY3 = new ArrayList<>();
        hundredMilliDataAheadZ1 = new ArrayList<>();
        hundredMilliDataAheadZ2 = new ArrayList<>();
        hundredMilliDataAheadZ3 = new ArrayList<>();
        DataAhead1 = new ArrayList<>();
        DataAhead2 = new ArrayList<>();
        DataAhead3 = new ArrayList<>();
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
                    }
                    t_endX1=t_max_tapX1;
                    t_endY1=t_max_tapY1;
                    t_endZ1=t_max_tapZ1;

                    maxX2=DataAhead2.get(0).getX();
                    maxY2=DataAhead2.get(0).getY();
                    maxZ2=DataAhead2.get(0).getZ();
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
                    }
                    t_endX2=t_max_tapX2;
                    t_endY2=t_max_tapY2;
                    t_endZ2=t_max_tapZ2;

                    maxX3=DataAhead3.get(0).getX();
                    maxY3=DataAhead3.get(0).getY();
                    maxZ3=DataAhead3.get(0).getZ();
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
                    }
                    t_endX3=t_max_tapX3;
                    t_endY3=t_max_tapY3;
                    t_endZ3=t_max_tapZ3;

                    for (int k = 0; k<=DataAhead1.get((int) t_endX1).getTimestamp(); k++){
                        tapDataX1.add(DataAhead1.get(k));
                    }
                    for (int k = 0; k<=DataAhead1.get((int) t_endY1).getTimestamp(); k++){
                        tapDataY1.add(DataAhead1.get(k));
                    }
                    for (int k = 0; k<=DataAhead1.get((int) t_endZ1).getTimestamp(); k++){
                        tapDataZ1.add(DataAhead1.get(k));
                    }

                    for (int k = 0; k<=DataAhead2.get((int) t_endX2).getTimestamp(); k++){
                        tapDataX2.add(DataAhead2.get(k));
                    }
                    for (int k = 0; k<=DataAhead2.get((int) t_endY2).getTimestamp(); k++){
                        tapDataY2.add(DataAhead2.get(k));
                    }
                    for (int k = 0; k<=DataAhead2.get((int) t_endZ2).getTimestamp(); k++){
                        tapDataZ2.add(DataAhead2.get(k));
                    }

                    for (int k = 0; k<=DataAhead3.get((int) t_endX3).getTimestamp(); k++){
                        tapDataX3.add(DataAhead3.get(k));
                    }
                    for (int k = 0; k<=DataAhead3.get((int) t_endY3).getTimestamp(); k++){
                        tapDataY3.add(DataAhead3.get(k));
                    }
                    for (int k = 0; k<=DataAhead3.get((int) t_endZ3).getTimestamp(); k++){
                        tapDataZ3.add(DataAhead3.get(k));
                    }
//
                    //
                    //
                    //
                    //
                    //
                    //
                    //



                    for(int k = 0; k < hundredMilliDataAheadX1.size();k++)
                    {
                        Log.d("100msDataAhead",""+hundredMilliDataAheadX1.get(k).getTimestamp());
                    }

                    long substractedTimeStamp = TappedCurrentTimeStamp_test - 100;
                    long temp2 = 0;
                    int temp = 0;

                    for(int k = accData.size()-1;k >0;k--)
                    {
                        temp2 = substractedTimeStamp;
                        if (accData.get(k).getTimestamp() <= temp2) {
                            temp = k;
                            break;
                        }
                    }

                    hundredMilliDataBehind =  accData.subList(temp,accData.size());

                    for(int k = 0; k< hundredMilliDataBehind.size();k++)
                    {
                        Log.d("100msDataBehind",""+hundredMilliDataBehind.get(k).getTimestamp());
                    }

                    limitSensorData.addAll(hundredMilliDataBehind);

                    for(int i =0 ; i< hundredMilliDataAheadX1.size();i++)
                    {
                        limitSensorData.add(hundredMilliDataAheadX1.get(i));
                    }


                    for(int i=0;i<hundredMilliDataAheadX1.size()/2;i++){
                        tapDataX1.add(hundredMilliDataAheadX1.get(i));
                        Log.d("Data...",""+tapDataX1.get(i).getTimestamp());
                    }
                    //t_end1=tapData.get(tapData.size()-1).getTimestamp();
                    mean_calculation();
                    standard_deviation_calc();
                    avg_calculation();
                    net_change();
                    max_change();
                    //     diff_time();
                    duration();
                    max_to_avg();
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
        double sumX=0,sumY=0,sumZ=0;

        for(int i=0;i<limitSensorData.size();i++){
            sumX = sumX + limitSensorData.get(i).getX();
            sumY = sumY + limitSensorData.get(i).getY();
            sumZ = sumZ + limitSensorData.get(i).getZ();
        }
        meanX1=sumX/limitSensorData.size();
        meanY1=sumY/limitSensorData.size();
        meanZ1=sumZ/limitSensorData.size();
        Log.d("mean...X..",""+meanX1);
        Log.d("mean...Y..",""+meanY1);
        Log.d("mean...Z..",""+meanZ1);
    }

    public void standard_deviation_calc(){
        double sqX=0,sqY=0,sqZ=0;
        double sdX,sdY,sdZ;
        for(int i=0;i<limitSensorData.size();i++){
            sqX = sqX + Math.pow((limitSensorData.get(i).getX()-meanX1),2);
            sqY = sqY + Math.pow((limitSensorData.get(i).getY()-meanY1),2);
            sqZ = sqZ + Math.pow((limitSensorData.get(i).getZ()-meanZ1),2);
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
        for(int i=0;i<hundredMilliDataAheadX1.size();i++){
            t1 = t1 + hundredMilliDataAheadX1.get(i).getTimestamp();
        }
        avg100msAfter=t1/hundredMilliDataAheadX1.size();
        Log.d("avg100msAfter......",""+avg100msAfter);
        diff=avg100msAfter-avg100msBefore;
        Log.d("Difference....",""+diff);
    }

    public void net_change(){
        double avgX,avgY,avgZ,sX=0,sY=0,sZ=0,ncX,ncY,ncZ;
        for(int i=0;i<tapDataX1.size();i++){
            sX = sX + tapDataX1.get(i).getX();
            sY = sY + tapDataY1.get(i).getY();
            sZ = sZ + tapDataZ1.get(i).getZ();
        }
        avgX=sX/tapDataX1.size();
        avgY=sY/tapDataY1.size();
        avgZ=sZ/tapDataZ1.size();
        ncX=avgX-avg100msBefore;
        ncY=avgY-avg100msBefore;
        ncZ=avgZ-avg100msBefore;
        Log.d("Net change..X...",""+ncX);
        Log.d("Net change..Y...",""+ncY);
        Log.d("Net change..Z...",""+ncZ);
    }
    public void max_change(){
        double mcX,mcY,mcZ;
        maxX1=tapDataX1.get(0).getX();
        maxY1=tapDataY1.get(0).getY();
        maxZ1=tapDataZ1.get(0).getZ();
        for(int i=0; i<tapDataX1.size(); i++){
            if(tapDataX1.get(i).getX() > maxX1){
                maxX1 = tapDataX1.get(i).getX();
                t_max_tapX1=tapDataX1.get(i).getTimestamp();
            }
            if(tapDataY1.get(i).getY() > maxY1){
                maxY1 = tapDataY1.get(i).getY();
                t_max_tapY1=tapDataY1.get(i).getTimestamp();
            }
            if(tapDataZ1.get(i).getZ() > maxZ1){
                maxZ1 = tapDataZ1.get(i).getZ();
                t_max_tapZ1=tapDataZ1.get(i).getTimestamp();
            }
        }
        mcX=maxX1-avg100msBefore;
        mcY=maxY1-avg100msBefore;
        mcZ=maxZ1-avg100msBefore;
        Log.d("Maximum Change in X",""+mcX);
        Log.d("Maximum Change in Y",""+mcY);
        Log.d("Maximum Change in Z",""+mcZ);
    }

   /* public void diff_time(){
        long t_min;
        for(int i=0;i<hundredMilliDataAhead1.size();i++){
            for (j=0;j<hundredMilliDataAhead1.size();j++){
                mod=
            }
            avgDiffs(i)=mod/(hundredMilliDataAhead1.size()-i+1);
        }
    }*/

    public void duration(){
        long dur, t_before_center;
        t_after_center=hundredMilliDataAheadX1.get(hundredMilliDataAheadX1.size()/2).getTimestamp();
        t_before_center=hundredMilliDataBehind.get(hundredMilliDataBehind.size()/2).getTimestamp();
        dur=(t_after_center-t_before_center)/diff;
        Log.d("Normalized time duratio",""+dur);
    }

    public void max_to_avg(){
        long max2avgX1, max2avgY1, max2avgZ1;
        long max2avgX2, max2avgY2, max2avgZ2;
        long max2avgX3, max2avgY3, max2avgZ3;
        max2avgX1= (long) ((t_after_center-t_max_tapX1)/(avg100msAfter-maxX1));
        max2avgY1= (long) ((t_after_center-t_max_tapY1)/(avg100msAfter-maxY1));
        max2avgZ1= (long) ((t_after_center-t_max_tapZ1)/(avg100msAfter-maxZ1));
        Log.d("Max_to_avg..X",""+max2avgX1);
        Log.d("Max_to_avg..Y",""+max2avgY1);
        Log.d("Max_to_avg..Z",""+max2avgZ1);
    }
}

