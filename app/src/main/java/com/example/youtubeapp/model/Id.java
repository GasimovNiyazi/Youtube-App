package com.example.youtubeapp.model;

import com.google.gson.annotations.SerializedName;

public class Id {


    @SerializedName("videoId")
    private String mVideoId;

    public String getVideoId() {
        return mVideoId;
    }
}
