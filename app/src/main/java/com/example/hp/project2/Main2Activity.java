package com.example.hp.project2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    ArrayList<String> sensorData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        sensorData.add("hai");
        sensorData.add("22");
        sensorData.add("3");
        sensorData.add("hello");

        List<String> lsensorData= Collections.synchronizedList(sensorData);
sensorData.remove(0);
        sensorData.add("hello2345");

      lsensorData= Collections.synchronizedList(sensorData);


        for(int i=0;i<4;i++){
            Toast.makeText(this, lsensorData.get(i), Toast.LENGTH_SHORT).show();
        }

    }
}
