package com.example.hp.project2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class FlipCoin extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    public static final Random RANDOM = new Random();
    private ImageView coin;
    private Button btn,bt_next,bt_exit;
    private SensorManager mSensorManager;
    private Sensor acc;
    ArrayList<String> accData;
    ArrayList<String> timeStamp;
    private boolean started = false;
    private double x,y,z,M;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin);
        coin = (ImageView) findViewById(R.id.coin);
        btn = (Button) findViewById(R.id.btn);
        timeStamp = new ArrayList<>();
        accData = new ArrayList<>();
        bt_exit = (Button) findViewById(R.id.bt_exit);
        bt_next = (Button) findViewById(R.id.bt_next);
        bt_next.setOnClickListener(this);
        bt_exit.setOnClickListener(this);
        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipCoin();
            }
        });*/
        btn.setOnClickListener(this);
    }

    private void flipCoin() {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(1000);
        fadeOut.setFillAfter(true);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                coin.setImageResource(RANDOM.nextFloat() > 0.5f ? R.drawable.tails : R.drawable.heads);

                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator());
                fadeIn.setDuration(3000);
                fadeIn.setFillAfter(true);

                coin.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        coin.startAnimation(fadeOut);
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
            case R.id.btn:
                flipCoin();
                for (int i = 0; i < accData.size(); i++) {
                    Log.d("time", timeStamp.get(i));
                    Log.d("accelerometer data", accData.get(i));
                }
                // save prev data if available
                started = true;
                mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                acc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mSensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_FASTEST);
                Toast.makeText(this, "Wait", Toast.LENGTH_SHORT).show();

                gpstrack gps = new gpstrack(FlipCoin.this);


                if(gps.canGetLocation()){


                    double longitude = gps.getLongitude();
                    double latitude = gps.getLatitude();


                     /*------- To get city name from coordinates -------- */
                    String cityName = null;
                    Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                    List<Address> addresses;
                    try {

                        addresses = gcd.getFromLocation(latitude,
                                longitude, 1);
                        if (addresses.size() > 0) {
                            System.out.println(addresses.get(0).getLocality());
                            cityName = addresses.get(0).getLocality();
                        }
                    }

                    catch (IOException e) {
                        e.printStackTrace();
                    }
//            Gps g=new Gps();
//            g.execute(cityName);

                    Toast.makeText(getApplicationContext(),"Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude)+"\nCity:"+cityName,Toast.LENGTH_SHORT).show();
                }
                else
                {

                    gps.showSettingsAlert();
                }

                break;
            case R.id.bt_next:
                /*Intent i=new Intent(Tap.this,SensorActivity.class);
                startActivity(i);*/
                Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_exit:
                started = false;
                mSensorManager.unregisterListener(this);
                Toast.makeText(this, "Closing", Toast.LENGTH_SHORT).show();
                break;
            default:
                started = false;
                mSensorManager.unregisterListener(this);
                break;
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (started) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            M = Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
            long currentTime = System.currentTimeMillis();
            SensorData data = new SensorData(currentTime, x, y, z, M);
            if(timeStamp.size()<200){
                accData.add(""+data);
                timeStamp.add(""+currentTime);
            }
            else{
                accData.remove(0);
                timeStamp.remove(0);
                accData.add(""+data);
                timeStamp.add(""+currentTime);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
