package com.margsapp.waterorder.Model;

public class Cart {


    String No,PID,Price,Quantity,Title,Image;

    public Cart(String no, String PID, String price, String quantity, String title, String image) {
        No = no;
        this.PID = PID;
        Price = price;
        Quantity = quantity;
        Title = title;
        Image = image;
    }

    public Cart() {
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
