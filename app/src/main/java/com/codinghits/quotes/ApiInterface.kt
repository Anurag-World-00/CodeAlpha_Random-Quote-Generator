package com.codinghits.quotes

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("random")
    fun geData() : Call<List<QuoteData>>
}