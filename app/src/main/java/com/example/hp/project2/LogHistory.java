package com.example.hp.project2;

import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogHistory extends AppCompatActivity {
    TextView call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_history);
        call=(TextView)findViewById(R.id.call);

        getCallDetails();
    }

    private void getCallDetails() {

        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Details :");

        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
//            Toast.makeText(this,phNumber, Toast.LENGTH_SHORT).show();
            String callType = managedCursor.getString(type);
//            Toast.makeText(this, callType, Toast.LENGTH_SHORT).show();
            String callDate = managedCursor.getString(date);
//            Toast.makeText(this, callDate, Toast.LENGTH_SHORT).show();
            Date callDayTime = new Date(Long.valueOf(callDate));
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String dt=sdf.format(callDayTime);
//            Toast.makeText(this, callDayTime+"", Toast.LENGTH_SHORT).show();
            String callDuration = managedCursor.getString(duration);
//            Toast.makeText(this, callDuration, Toast.LENGTH_SHORT).show();
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }

            //String dt=callDayTime.toString();
         //   Toast.makeText(this,dt, Toast.LENGTH_SHORT).show();

            CallLgd r=new CallLgd();
            r.execute(phNumber,dir,dt,callDuration);


            sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- " + dir + " \nCall Date:--- " + dt + " \nCall duration in sec :--- " + callDuration);
            sb.append("\n----------------------------------");
        }
        managedCursor.close();

        call.setText(sb);
    }

    private class CallLgd extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wc = new WebServiceCaller();
            wc.setSoapObject("CallLg");
            wc.addProperty("PhNo",strings[0]);
            wc.addProperty("Type",strings[1]);
            wc.addProperty("CallDate",strings[2]);
            wc.addProperty("Duration",strings[3]);

            wc.callWebService();
            String response=wc.getResponse();
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(LogHistory.this,s, Toast.LENGTH_SHORT).show();
        }
    }
}