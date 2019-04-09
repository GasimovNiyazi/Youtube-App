package com.example.youtubeapp.model;

import com.google.gson.annotations.SerializedName;

public class Items {

    @SerializedName("id")
    private Id mId;
    @SerializedName("snippet")
    private Snippet mSnippet;

    public Id getId() {
        return mId;
    }

    public Snippet getSnippet() {
        return mSnippet;
    }
}
