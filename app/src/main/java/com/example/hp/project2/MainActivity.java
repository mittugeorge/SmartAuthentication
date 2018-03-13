package com.example.hp.project2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button bt_reg;
    TextView tv_name, tv_addr, tv_mail, tv_user, tv_pwd, tv_head;
    EditText et_name, et_addr, et_mail, et_user, et_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_reg = (Button) findViewById(R.id.bt_reg);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_addr = (TextView) findViewById(R.id.tv_addr);
        tv_mail = (TextView) findViewById(R.id.tv_mail);
        tv_user = (TextView) findViewById(R.id.tv_user);
        tv_pwd = (TextView) findViewById(R.id.tv_pwd);
        et_name = (EditText) findViewById(R.id.et_name);
        et_addr = (EditText) findViewById(R.id.et_addr);
        et_mail = (EditText) findViewById(R.id.et_mail);
        et_user = (EditText) findViewById(R.id.et_user);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        bt_reg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final String name = et_name.getText().toString();
        final String addr = et_addr.getText().toString();
        final String mail = et_mail.getText().toString();
        final String user = et_user.getText().toString();
        final String pwd = et_pwd.getText().toString();

        Reg r=new Reg();
        r.execute(name,addr,mail,user,pwd);
//        Toast.makeText(this,name, Toast.LENGTH_SHORT).show();


    }

    public class Reg extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wc=new WebServiceCaller();
            wc.setSoapObject("Registration");
            wc.addProperty("Name",strings[0]);
            wc.addProperty("Address",strings[1]);
            wc.addProperty("Email",strings[2]);
            wc.addProperty("Username",strings[3]);
            wc.addProperty("Password",strings[4]);
            wc.callWebService();
            String response=wc.getResponse();
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Toast.makeText(MainActivity.this,s, Toast.LENGTH_SHORT).show();
        }
    }
}
