package com.margsapp.waterorder.Model;

import android.location.Geocoder;

import java.sql.Timestamp;
import java.util.Date;

public class Users {

    private String Username, Gender,Address_STR;

    private java.util.Date DOB;

    private Geocoder location;

    public Users(String gender,String username, java.util.Date DOB,String address_STR) {
        Gender = gender;
        Username = username;
        this.DOB = DOB;
        Address_STR = address_STR;

    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getAddress_STR() {
        return Address_STR;
    }

    public void setAddress_STR(String address_STR) {
        Address_STR = address_STR;
    }

    public java.util.Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public Geocoder getLocation() {
        return location;
    }

    public void setLocation(Geocoder location) {
        this.location = location;
    }
}
