package com.example.dailyquote.domain.model

import com.google.gson.Gson

data class Quote(
    val author: String,
    val id: Int,
    val quote: String
) {
    companion object {
        fun fromJson(quoteJson: String): Quote {
            return Gson().fromJson(quoteJson, Quote::class.java)
        }
    }
}

fun Quote.toJson(): String {
    return Gson().toJson(this) ?: ""
}

fun Quote.fromJson(quoteJson: String): Quote {
    return Gson().fromJson(quoteJson, Quote::class.java)
}