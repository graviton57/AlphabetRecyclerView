package com.havrylyuk.alphabetrecyclerviewdemo.service;


import com.havrylyuk.alphabetrecyclerviewdemo.model.Countries;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Igor Havrylyuk on 08.03.2017.
 */

public interface ApiService {

    @GET("countryInfoJSON")
    Call<Countries> getCountries(
            @Query("lang") String lang,
            @Query("username") String userName,
            @Query("style") String style);
}
