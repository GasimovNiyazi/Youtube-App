package com.example.youtubeapp.model;

import com.google.gson.annotations.SerializedName;

public class Thumbnails {

    @SerializedName("default")
    private Default mDefault;

    public Default getDefault() {
        return mDefault;
    }
}
