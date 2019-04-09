package com.example.youtubeapp.model;


import com.google.gson.annotations.SerializedName;


public class Snippet {

    @SerializedName("publishedAt")
    private String mPublishDate;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("thumbnails")
    private Thumbnails mThumbnails;
    @SerializedName("channelTitle")
    private String mChannelTitle;


    public String getTitle() {
        return mTitle.replace("&#39;", "\'").replace("&quot;", "\"");
    }

    public Thumbnails getThumbnails() {
        return mThumbnails;
    }

    public String getChannelTitle() {
        return mChannelTitle;
    }
}
