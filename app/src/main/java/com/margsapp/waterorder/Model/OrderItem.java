package com.margsapp.waterorder.Model;

public class OrderItem {

    int price;
    String orderID,title,Quantity,No,Image;


    public OrderItem(int price, String orderID, String title, String quantity, String no, String image) {
        this.price = price;
        this.orderID = orderID;
        this.title = title;
        Quantity = quantity;
        No = no;
        Image = image;
    }

    public OrderItem() {
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
