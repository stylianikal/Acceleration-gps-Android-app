package com.example.gpsapp.User;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class User implements Serializable {

    private String username;
    private String password;
    private String timestamp;
    private double acceleration,last_speed, longitude, lattitude;

    public User(){
        // Will be used only when reading user object from db.
    }

    public User( String username, String password, String timestamp, double acceleration, double last_speed, double longitude, double lattitude){
        this.username = username;
        this.password = password;
        this.timestamp = timestamp;
        this.acceleration = acceleration;
        this.last_speed = last_speed;
        this.longitude = longitude;
        this.lattitude = lattitude;
    }
    @Exclude
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public double getLast_speed() {
        return last_speed;
    }

    public void setLast_speed(double last_speed) {
        this.last_speed = last_speed;
    }


    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }
}
