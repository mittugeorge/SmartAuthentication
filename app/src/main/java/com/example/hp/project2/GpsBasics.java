package com.example.hp.project2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by MARIYAM on 2/10/2018.
 */

public class GpsBasics extends AppCompatActivity implements View.OnClickListener {
    private LocationManager locationManager;
    Button bt_gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsbasics);
       bt_gps=(Button)findViewById(R.id.bt_gps);
       bt_gps.setOnClickListener(this);


//        /********** get Gps location service LocationManager object ***********/
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//                /* CAL METHOD requestLocationUpdates */
//
//        // Parameters :
//        //   First(provider)    :  the name of the provider with which to register
//        //   Second(minTime)    :  the minimum time interval for notifications,
//        //                         in milliseconds. This field is only used as a hint
//        //                         to conserve power, and actual time between location
//        //                         updates may be greater or lesser than this value.
//        //   Third(minDistance) :  the minimum distance interval for notifications, in meters
//        //   Fourth(listener)   :  a {#link LocationListener} whose onLocationChanged(Location)
//        //                         method will be called for each location update
//
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, this);
//
//        /********* After registration onLocationChanged method  ********/
//        /********* called periodically after each 3 sec ***********/
//    }
//
//    /************* Called after each 3 sec **********/
//    @Override
//    public void onLocationChanged(Location location) {
//
//        String str = "Latitude: "+location.getLatitude()+" Longitude: "+location.getLongitude();
//
//        Toast.makeText(getBaseContext(), str, Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//        /******** Called when User off Gps *********/
//
//        Toast.makeText(getBaseContext(), "Gps turned off ", Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//        /******** Called when User on Gps  *********/
//
//        Toast.makeText(getBaseContext(), "Gps turned on ", Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//        // TODO Auto-generated method stub
//
//    }
    }

    @Override
    public void onClick(View view) {
        gpstrack gps = new gpstrack(GpsBasics.this);


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

            location1 lc = new location1();
            lc.execute(Double.toString(longitude),Double.toString(latitude),cityName);

        }
        else
        {

            gps.showSettingsAlert();
        }
    }

    private class location1 extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller wc=new WebServiceCaller();
            wc.setSoapObject("Location");
            wc.addProperty("longitude",strings[0]);
            wc.addProperty("latitude",strings[1]);
            wc.addProperty("city",strings[2]);
            wc.callWebService();

            return wc.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(GpsBasics.this, s, Toast.LENGTH_SHORT).show();
        }
    }
}

