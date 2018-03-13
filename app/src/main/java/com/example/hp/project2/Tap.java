package com.example.hp.project2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Tap extends AppCompatActivity implements View.OnClickListener, SensorEventListener, View.OnTouchListener {

    private SensorManager mSensorManager;
    private Sensor acc, gyr, mag;
    private boolean started = false;
    private double x, y, z, M;
    private long t_start, t_end;
    long currentTime;
    private long t = 1;
    //private ArrayList accData;
    ArrayList<String> accData;
    ArrayList<SensorData> outData;
    ArrayList<String> gyrData;
    ArrayList<String> magData;
    ArrayList<String> timeStamp;
    ArrayList<Long> ourTimeStamp;

    ArrayList<SensorData> intermediateData;

    ArrayList<ArrayList<SensorData>> all_clicked_sensorData;


    Button bt_one, bt_two, bt_three, bt_four, bt_five, bt_six, bt_seven, bt_eight, bt_nine, bt_zero, bt_submit;
    TextView tv_enter;//, tv_captcha;


    private long TappedCurrentTimeStamp_test;

    /*private View.OnClickListener mCorkyListener = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked
        }
    };*/
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
        //tv_captcha=(TextView)findViewById(R.id.tv_captcha);
        bt_one.setOnClickListener(this);
        // bt_two.setOnClickListener(this);
        bt_three.setOnClickListener(this);
        //bt_four.setOnClickListener(this);
        //bt_five.setOnClickListener(this);
        //bt_six.setOnClickListener(this);
        bt_seven.setOnClickListener(this);


        bt_one.setOnTouchListener(this);
        //bt_eight.setOnClickListener(this);
        //bt_nine.setOnClickListener(this);
        //bt_zero.setOnClickListener(this);*/
        bt_submit.setOnClickListener(this);
        timeStamp = new ArrayList<>();
        accData = new ArrayList<>();
        gyrData = new ArrayList<>();
        magData = new ArrayList<>();
        outData = new ArrayList<>();
        intermediateData = new ArrayList<>();
        ourTimeStamp = new ArrayList<>();

        all_clicked_sensorData  = new ArrayList<ArrayList<SensorData>>();


        sensorStarting();
    }

    public void sensorStarting() {
        Log.d("Basil", "calling starting functions");
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
        if (started == true) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_one:
                for (int i = 0; i < accData.size(); i++) {
                    Log.d("time", timeStamp.get(i));
                    Log.d("accelerometer data", accData.get(i));
                    //Log.d("gyroscope data",gyrData.get(i));
                    //Log.d("magnetometer data",magData.get(i));
                }
                //Log.d("t_start", String.valueOf(t_start));
                //Log.d("t_end", String.valueOf(t_end));
                //Log.d("t", String.valueOf(t));
/*
                //basil test
                Log.d("hiii",".........................."+y);
                for(int i =0 ; i < outData.size() ; i++)
                {
                    Log.d("basil","inside for loop"+i);
                    try
                    {
                        SensorData sensorData = outData.get(i);
                        if(sensorData.getY() == y)
                        {
                            long tappedTimeStamp = ourTimeStamp.get(i);

                            long hundredMilli = 100;

                            double substractedTimeStamp  = tappedTimeStamp - hundredMilli;

                            long addedTimeStamp = tappedTimeStamp + hundredMilli ;

                            Log.d("mariam",tappedTimeStamp+".............."+substractedTimeStamp+"><><><><>><><><><"+addedTimeStamp);


                            int firstIndex  = ourTimeStamp.indexOf(substractedTimeStamp);
                            int lastIndex  = ourTimeStamp.indexOf(addedTimeStamp);

                            Log.d("killer",""+firstIndex+">>>>>>>"+lastIndex);

                            Log.d("basil", "asdasdasdasdasdsad");
                        }
                       /* for(i=199;timeStamp.get(i)>tStart;i--){
                            if(timeStamp.get(i)<=tStart){

                            }
                        }*/
  /*                  }
                    catch (Exception exp)
                    {
                        Log.wtf("error","asdasdasdsasError");
                    }
                }

                //basil test ends

*/
                // save prev data if available
                started = true;
                mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                acc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mSensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_FASTEST);
                //gyr = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
                //mSensorManager.registerListener(this, gyr, SensorManager.SENSOR_DELAY_FASTEST);
                //mag = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                //mSensorManager.registerListener(this, mag, SensorManager.SENSOR_DELAY_FASTEST);
                break;
            case R.id.bt_submit:
                started = false;
                mSensorManager.unregisterListener(this);
                /*Intent i=new Intent(Tap.this,SensorActivity.class);
                startActivity(i);*/
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (started) {

            //if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            M = Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
             currentTime = System.currentTimeMillis();
            SensorData data = new SensorData(currentTime, x, y, z, M);
            if (timeStamp.size() < 200) {
               // accData.add("" + data);
                outData.add(data);
                ourTimeStamp.add(currentTime);
               // timeStamp.add("" + currentTime);
            } else {
                //List lsdata=Collections.synchronizedList(accData);
               // accData.remove(0);
                //timeStamp.remove(0);
                outData.remove(0);
                ourTimeStamp.remove(0);
                //accData.add("" + data);
                outData.add(data);
                ourTimeStamp.add(currentTime);
                //timeStamp.add("" + currentTime);
            }
            //}
           /* else if (event.sensor.getType()==Sensor.TYPE_GYROSCOPE) {
                double x = event.values[0];
                double y = event.values[1];
                double z = event.values[2];
                long currentTime = System.currentTimeMillis();
                SensorData data = new SensorData(/*currentTime, *///x, y, z);
               /* if(timeStamp.size()<200){
                    accData.add(""+data);
                    timeStamp.add(""+currentTime);
                }
                else{
                    //List lsdata=Collections.synchronizedList(gyrData);
                    gyrData.remove(0);
                    timeStamp.remove(0);
                    gyrData.add(""+data);
                    timeStamp.add(""+currentTime);
                }
            }
            else if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD) {
                double x = event.values[0];
                double y = event.values[1];
                double z = event.values[2];
                long currentTime = System.currentTimeMillis();
                SensorData data = new SensorData(/*currentTime, *///x, y, z);
               /* if(timeStamp.size()<200){
                    magData.add(""+data);
                    timeStamp.add(""+currentTime);
                }
                else{
                    //List lsdata=Collections.synchronizedList(magData);
                    magData.remove(0);
                    timeStamp.remove(0);
                    magData.add(""+data);
                    timeStamp.add(""+currentTime);
                }
            }*/
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {


            TappedCurrentTimeStamp_test = currentTime;

            //t_start=motionEvent.getDownTime();
            //t_start=motionEvent.getEventTime();
            Calendar d = Calendar.getInstance();
            t_start = d.getTimeInMillis();


            for (int i = 0; i < outData.size(); i++) {

                try {
                    SensorData sensorData = outData.get(i);
                    if (sensorData.getTimestamp() == TappedCurrentTimeStamp_test)
                    {
                        long tappedTimeStamp = ourTimeStamp.get(i);
                        long hundredMilli = 100;

                        long substractedTimeStamp = tappedTimeStamp - hundredMilli;
                        long additionTimeStamp = tappedTimeStamp + hundredMilli;

                        int temp = 0;
                        long temp2 = 0;

                        for (int j = i; j > 0; j--)
                        {
                            temp2 = substractedTimeStamp;
                            if (ourTimeStamp.get(j) <= temp2) {
                                temp = j;
                                break;
                            }
                        }

                        int startPostion = temp;
                        Log.d("Starting position basil", "" + startPostion);
                        int endTemp = 0;
                        long endtemp2 = 0;
                        for(int z = i; z < ourTimeStamp.size();z++ )
                        {
                            endtemp2 = additionTimeStamp;
                            if(ourTimeStamp.get(z) >= endtemp2)
                            {
                                endTemp = z;
                                break;
                            }
                        }

                        int endPosition = endTemp;

                        for (int s = startPostion; s < endPosition; s++)
                        {
                            intermediateData.add(outData.get(s));
                        }

                        all_clicked_sensorData.add(intermediateData);

                        Log.d("zerbra_count",""+intermediateData.size());

                        for(int q = 0 ; q < all_clicked_sensorData.get(0).size();q++)
                        {
                            Log.d("zebra",""+all_clicked_sensorData.get(0).get(q).getTimestamp());
                        }

                        Log.d("zebra_endsssss",""+tappedTimeStamp);


                    }

                } catch (Exception exp) {
                    Log.wtf("error", "asdasdasdsasError");
                }
            }
            //basil test ends
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            Calendar d = Calendar.getInstance();
            t_end = d.getTimeInMillis();
            Log.d("t_end.....", String.valueOf(t_end));
        }

        return true;
    }
}


