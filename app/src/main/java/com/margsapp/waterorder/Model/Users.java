package com.margsapp.waterorder.Model;

import android.location.Geocoder;

import java.sql.Timestamp;
import java.util.Date;

public class Users {

    private String Username, Gender,PhoneNumber,Address_STR;



    private Geocoder location;

    public Users(String gender,String username, String phoneNumber,String address_STR) {
        Gender = gender;
        Username = username;
        PhoneNumber = phoneNumber;
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

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public Geocoder getLocation() {
        return location;
    }

    public void setLocation(Geocoder location) {
        this.location = location;
    }
}
