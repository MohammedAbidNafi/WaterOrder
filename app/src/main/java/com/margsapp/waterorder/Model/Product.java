package com.margsapp.waterorder.Model;

public class Product {

    String PID,Title,Quantity,Price,Image,Desc;





    public Product(String pid, String title, String quantity, String price, String image,String desc) {

        PID = pid;
        Title = title;
        Quantity = quantity;
        Price = price;
        Image = image;
        Desc = desc;
    }

    public Product() {

    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }


    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
