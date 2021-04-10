package com.example.samrtcarduser.helper;

public class HomeHelper {

    private String name;
    private String imageView;

    public HomeHelper(String name, String imageView) {
        this.name = name;
        this.imageView = imageView;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageView() {
        return imageView;
    }

    public void setImageView(String imageView) {
        this.imageView = imageView;
    }
}
