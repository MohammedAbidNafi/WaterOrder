package com.margsapp.waterorder.Model;

public class Price {

    int TotalPrice;


    public Price(int totalprice) {
        TotalPrice = totalprice;
    }

    public Price() {
    }
    

    public int getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        TotalPrice = totalPrice;
    }
}
