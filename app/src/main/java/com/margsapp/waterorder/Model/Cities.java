package com.margsapp.waterorder.Model;

public class Cities {

    String name,image;

    public Cities(String name, String image) {
        this.name = name;
        this.image = image;
    }


    public Cities() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
