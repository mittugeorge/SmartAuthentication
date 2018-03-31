package com.example.hp.project2;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;

public class LockActivity extends AppCompatActivity implements View.OnClickListener {
    EditText pin;
    Button bt_ok;
    String pn1="8632";
    DevicePolicyManager deviceManger;

      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        deviceManger = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        pin = (EditText) findViewById(R.id.pin);
        bt_ok = (Button) findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String pn=pin.getText().toString();
        if(pn.equals(pn1)) {
            Intent i = new Intent(LockActivity.this, TapEventNew.class);
            startActivity(i);
        }
        else{
            deviceManger.lockNow();
        }
    }
}
