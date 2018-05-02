package com.example.hp.project2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button bt_learn,bt_execute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        bt_learn=(Button)findViewById(R.id.bt_learn);
        bt_execute=(Button)findViewById(R.id.bt_execute);
        bt_learn.setOnClickListener(this);
        bt_execute.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_learn:
                Intent i = new Intent(HomeActivity.this, LearningActivity.class);
                startActivity(i);
            case R.id.bt_execute:
                Intent i1 = new Intent(HomeActivity.this, ExecutionActivity.class);
                startActivity(i1);
        }
    }
}
