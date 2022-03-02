package com.example.googlemap.Models;

import java.util.ArrayList;

public class ModelApi {
    public String stat;
    public Modelphoto photos;

    public String getStat() {
        return stat;
    }

    public void setPage(String page) {
        this.stat = page;
    }

    public Modelphoto getPhotos() {
        return photos;
    }

    public void setPhotos(Modelphoto photos) {
        this.photos = photos;
    }
}
