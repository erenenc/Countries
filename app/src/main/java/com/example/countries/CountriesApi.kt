package com.example.countries

import com.example.countries.model.Country
import com.example.countries.model.CountryDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface CountriesApi {

    @GET("v1/geo/countries?limit=10")
    suspend fun getCountries(
        @Header("X-RapidAPI-Key") access_key: String,
        @Header("X-RapidAPI-Host") api_host: String
    ): Response<Country>

    @GET("/v1/geo/countries/{code}")
    suspend fun getSpecificCountry(
        @Path("code") code: String,
        @Header("X-RapidAPI-Key") access_key: String,
        @Header("X-RapidAPI-Host") api_host: String
    ) : Response<CountryDetail>

}