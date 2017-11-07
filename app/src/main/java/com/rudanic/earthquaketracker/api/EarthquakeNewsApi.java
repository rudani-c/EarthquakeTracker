package com.rudanic.earthquaketracker.api;

import com.rudanic.earthquaketracker.model.EarthquakeNews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EarthquakeNewsApi {
    String BASE_URL = "http://earthquake.usgs.gov";

    @GET("/fdsnws/event/1/query")
    Call<EarthquakeNews> getEarthquakeNews(@Query("format") String format,
                                           @Query("limit") String limit,
                                           @Query("minmag") String minMagnitude,
                                           @Query("orderby") String orderBy);

    @GET("/fdsnws/event/1/query")
    Call<EarthquakeNews> getFilteredEarthquakes(@Query("format") String format,
                                                @Query("limit") String limit,
                                                @Query("latitude") String latitude,
                                                @Query("longitude") String longitude,
                                                @Query("maxradius") String maxRadius);
}
