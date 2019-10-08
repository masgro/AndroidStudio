package com.example.helloworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.provider.Settings;
import android.view.View;
import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private LocationManager locationManager;
    private LocationListener locationListener;

    public Float acc_x = 0.0f;
    public Float acc_y = 0.0f;
    public Float acc_z = 0.0f;

    public Double latitud = 0.0;
    public Double longitud = 0.0;

    private Button simpleButton1, simpleButton2, simpleButton3, simpleButton4;
    private Sensor accelerometer;
    private SensorManager Sm;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = Sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        simpleButton1 = (Button) findViewById(R.id.GetX);
        simpleButton2 = (Button) findViewById(R.id.GetY);
        simpleButton3 = (Button) findViewById(R.id.GetZ);
        simpleButton4 = (Button) findViewById(R.id.GetLocation);
        textView = (TextView) findViewById(R.id.textView);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                textView.setText("");
                textView.append("\n" + location.getLatitude() + " " + location.getLongitude() + " " + location.getAltitude());
                //Toast.makeText(getApplicationContext(), Double.toString(location.getLongitude()), Toast.LENGTH_LONG).show();
                //latitud = location.getLatitude();
                //longitud = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET
                },10 );
                return;
            }
        }else {
            configureButton();
        }


        simpleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), Float.toString(acc_x), Toast.LENGTH_LONG).show();
            }
        });

        simpleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), Float.toString(acc_y), Toast.LENGTH_LONG).show();
            }
        });

        simpleButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), Float.toString(acc_z), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }

    private void configureButton() {
        simpleButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.requestLocationUpdates("gps", 5, 1, locationListener);
            }
        });

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        acc_x = event.values[0];
        acc_y = event.values[1];
        acc_z = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}



