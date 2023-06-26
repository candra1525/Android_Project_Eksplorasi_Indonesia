package com.candra.eksplorindonesia.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServer
{
    private static final String BASE_URL = "https://candracandra1525.000webhostapp.com/";

    private static Retrofit retrofit;

    public static Retrofit connectionRetrofit()
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
