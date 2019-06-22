package com.example.gridpic.catAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CatService {
    @GET("/v1/images/search")
    Call<List<CatResponce> > get(@Query("format") String format);
}
