package com.example.hp.project2;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.attr.x;
import static android.R.attr.y;

public class SensorActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor acc,gyr,mag;
    //float x_event[], y_event[], z_event[];
    ArrayList<Float> x_event = new ArrayList<Float>();
    ArrayList<Float> y_event = new ArrayList<Float>();
    ArrayList<Float> z_event = new ArrayList<Float>();

    TextView tv_ax,tv_ay,tv_az;
    TextView tv_gx,tv_gy,tv_gz;
    TextView tv_mx,tv_my,tv_mz;
    TextView tv_acc,tv_gyr,tv_mag;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyr = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mag = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        tv_ax=(TextView)findViewById(R.id.tv_ax);
        tv_ay=(TextView)findViewById(R.id.tv_ay);
        tv_az=(TextView)findViewById(R.id.tv_az);
        tv_gx=(TextView)findViewById(R.id.tv_gx);
        tv_gy=(TextView)findViewById(R.id.tv_gy);
        tv_gz=(TextView)findViewById(R.id.tv_gz);
        tv_mx=(TextView)findViewById(R.id.tv_mx);
        tv_my=(TextView)findViewById(R.id.tv_my);
        tv_mz=(TextView)findViewById(R.id.tv_mz);
        tv_acc=(TextView)findViewById(R.id.tv_acc);
        tv_gyr=(TextView)findViewById(R.id.tv_gyr);
        tv_mag=(TextView)findViewById(R.id.tv_mag);

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.

            if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
                float lux = event.values[0];
                float luy = event.values[1];
                float luz = event.values[2];
                tv_ax.setText(lux + "");
                tv_ay.setText(luy + "");
                tv_az.setText(luz + "");

                x_event.add(event.values[0]);
                y_event.add(event.values[0]);
                z_event.add(event.values[0]);
            }
            else if (event.sensor.getType()==Sensor.TYPE_GYROSCOPE) {
                float gyx = event.values[0];
                float gyy = event.values[1];
                float gyz = event.values[2];
                tv_gx.setText(gyx + "");
                tv_gy.setText(gyy + "");
                tv_gz.setText(gyz + "");
            }
            else if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD) {
                float max = event.values[0];
                float may = event.values[1];
                float maz = event.values[2];
                tv_mx.setText(max + "");
                tv_my.setText(may + "");
                tv_mz.setText(maz + "");
            }

        // Do something with this sensor value.
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}

