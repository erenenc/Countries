package com.example.countries

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api : CountriesApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://wft-geo-db.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountriesApi::class.java)
    }

}