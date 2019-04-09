package com.example.youtubeapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YoutubeSearchResult {

    @SerializedName("nextPageToken")
    private String mNextPageToken;
    @SerializedName("pageInfo")
    private PageInfo mPageInfo;
    @SerializedName("items")
    private List<Items> mItems;

    public String getNextPageToken() {
        return mNextPageToken;
    }

    public PageInfo getPageInfo() {
        return mPageInfo;
    }

    public List<Items> getItems() {
        return mItems;
    }
}
