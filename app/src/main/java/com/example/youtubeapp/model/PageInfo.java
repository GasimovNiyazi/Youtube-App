package com.example.youtubeapp.model;

import com.google.gson.annotations.SerializedName;

public class PageInfo {

    @SerializedName("totalResults")
    private Integer mTotalresults;

    @SerializedName("resultsPerPage")
    private Integer mResultsPerPage;

    public Integer getTotalresults() {
        return mTotalresults;
    }

    public Integer getResultsPerPage() {
        return mResultsPerPage;
    }
}
