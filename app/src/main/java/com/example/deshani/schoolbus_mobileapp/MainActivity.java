package com.example.deshani.schoolbus_mobileapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private double lat_value;
    private double lng_value;

    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.track_me);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps=new GPSTracker(MainActivity.this);

                if(gps.canGetLocation){
                    lat_value=gps.getLatitude();
                    lng_value=gps.getLongitude();

                    Toast.makeText(getApplicationContext(),"Your Location is: " + lat_value+ " ,"+lng_value, Toast.LENGTH_LONG).show();
                }

                else {
                    gps.showSettingsAlert();
                }

            }
        });

    }
}
