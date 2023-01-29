package com.margsapp.waterorder.Model;

import java.util.ArrayList;

public class Order {

    String order_id;
    int order_value;
    String order_status;
    ArrayList<String> item_list;
    String d_t;
    String name;
    String PhoneNumber;
    String Address;


    public Order() {
    }

    public Order(String order_id, int order_value, String order_status, ArrayList<String> item_list, String d_t, String name, String phoneNumber, String address) {
        this.order_id = order_id;
        this.order_value = order_value;
        this.order_status = order_status;
        this.item_list = item_list;
        this.d_t = d_t;
        this.name = name;
        this.PhoneNumber = phoneNumber;
        this.Address = address;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getOrder_value() {
        return order_value;
    }

    public void setOrder_value(int order_value) {
        this.order_value = order_value;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public ArrayList<String> getItem_list() {
        return item_list;
    }

    public void setItem_list(ArrayList<String> item_list) {
        this.item_list = item_list;
    }

    public String getD_t() {
        return d_t;
    }

    public void setD_t(String d_t) {
        this.d_t = d_t;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
