package com.example.dailyquote.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface QuoteApiService {

    @GET("/quotes/random")
    suspend fun getRandomQuote(): QuoteDto

    @GET("/quotes")
    suspend fun getQuotes(
        @Query("skip") page: Int,
        @Query("limit") pageSize: Int
    ): QuoteDtoList

}