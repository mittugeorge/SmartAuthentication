package com.example.hp.project2;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LockScreenActivity extends AppCompatActivity {
    Button btn_lock,btn_enable;
    static  final  int RESULT_ENABLE=1;
    DevicePolicyManager devicePolicyManager;
    ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        btn_enable=(Button)findViewById(R.id.btn_enable);
        btn_lock=(Button)findViewById(R.id.btn_lock);

        devicePolicyManager=(DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName=new ComponentName(LockScreenActivity.this, Controller.class);
        boolean active=devicePolicyManager.isAdminActive(componentName);

        if(active){
            btn_enable.setText("Disable");
            btn_lock.setVisibility(View.VISIBLE);
        }else {
            btn_enable.setText("Enable");
            btn_lock.setVisibility(View.GONE);
        }
        btn_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean active=devicePolicyManager.isAdminActive(componentName);
                if(active){
                    devicePolicyManager.removeActiveAdmin(componentName);
                    btn_enable.setText("Enable");
                    btn_lock.setVisibility(View.GONE);
                }else{
                    Intent intent=new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,componentName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"You Should Enable the app!");
                    startActivityForResult(intent,RESULT_ENABLE);
                }

            }
        });

        btn_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devicePolicyManager.lockNow();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RESULT_ENABLE:
                if(resultCode== Activity.RESULT_OK ){
                    btn_enable.setText("Disable");
                    btn_lock.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Disabled", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                }
                return;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}


