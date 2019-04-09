package com.example.youtubeapp.api;

import com.example.youtubeapp.model.YoutubeSearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeSearchApi {


    @GET("v3/search")
    Call<YoutubeSearchResult> getYoutubeSearchResult(@Query("part") String part,
                                                     @Query("maxResults") Integer maxResults,
                                                     @Query("chart") String chart,
                                                     @Query("q") String searchKey,
                                                     @Query("regionCode") String regionCode,
                                                     @Query("type") String type,
                                                     @Query("pageToken") String pageToken,
                                                     @Query("key") String apiKey);


}
