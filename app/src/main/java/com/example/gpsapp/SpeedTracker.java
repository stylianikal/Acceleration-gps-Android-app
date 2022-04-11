package com.example.gpsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.gpsapp.User.User;

import java.sql.Timestamp;

public class SpeedTracker extends AppCompatActivity implements LocationListener {
    private User user;
    private LocationManager locationManager;
    private TextView textView1, textView2;
    static final int REQ_LOG_CODE = 23;

    long Time_new;
    double Speed_new ;
    long Time_old ;
    double Speed_old = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_tracker);

        Intent speedtracker = getIntent();
        user = (User) speedtracker.getSerializableExtra("User");
        Time_old = System.currentTimeMillis();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
    }


    public void startTracker(View v){
        if (ActivityCompat.checkSelfPermission(SpeedTracker.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(SpeedTracker.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQ_LOG_CODE);
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, SpeedTracker.this);
        }
    }

    //energopiite otan den dinete permission to dangerous permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_LOG_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) return;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, SpeedTracker.this);
        }
    }

    public void gotomap(View view){ startActivity(new Intent(this,MapsActivity.class));}

    @Override
    public void onLocationChanged(@NonNull Location location) {


        textView1.setText(String.valueOf(location.getLatitude()+ " , " + String.valueOf(location.getLongitude())));
        //textView2.setText(String.valueOf(location.getSpeed()));

        // covert speed to km/h
        Speed_new = location.getSpeed() * 3.6;
        Time_new = System.currentTimeMillis();

        double acceleration = acc((Speed_new - Speed_old), dt(Time_new, Time_old));

        textView2.setText(String.valueOf(acceleration));
        user.setAcceleration(acceleration);
        user.setLast_speed(Speed_new);
        user.setTimestamp(new Timestamp(Time_new).toString());
        user.setLattitude(location.getLatitude());
        user.setLongitude(location.getLongitude());


        assert user != null;
        new Database().saveData(user);

    }
    private double dt (long new_time, long old_time){
        double dt = new_time- old_time;
        Time_old = Time_new;

        // converting dt from ms to h. 60 mins per hour multiply by 60 seconds per min.
        return dt / 3600;
    }
    private double acc(double DU, double DT){

        if ( Speed_new == Speed_old && DT == 0){
            return 0.0;
        }

        Speed_old = Speed_new;
        return DU/DT;
    }
    public void map(View v){
        Intent mapactivity = new Intent(this, MapsActivity.class);
        mapactivity.putExtra("User", user);
        startActivity(mapactivity);
    }
}