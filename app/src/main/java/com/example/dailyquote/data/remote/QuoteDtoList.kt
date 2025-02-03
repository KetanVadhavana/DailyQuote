package com.example.dailyquote.data.remote

data class QuoteDtoList(
    val limit: Int,
    val quotes: List<QuoteDto>,
    val skip: Int,
    val total: Int
)