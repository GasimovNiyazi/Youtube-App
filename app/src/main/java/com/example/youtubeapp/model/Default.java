package com.example.youtubeapp.model;

import com.google.gson.annotations.SerializedName;

public class Default {

    @SerializedName("url")
    private String mUrl;
    @SerializedName("width")
    private Integer mWidth;
    @SerializedName("height")
    private Integer mHeight;

    public String getUrl() {
        return mUrl;
    }

    public Integer getWidth() {
        return mWidth;
    }

    public Integer getHeight() {
        return mHeight;
    }
}
