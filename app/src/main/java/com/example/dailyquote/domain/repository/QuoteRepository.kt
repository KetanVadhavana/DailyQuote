package com.example.dailyquote.domain.repository

import androidx.paging.Pager
import com.example.dailyquote.data.remote.util.Resource
import com.example.dailyquote.domain.model.Quote
import kotlinx.coroutines.flow.Flow


interface QuoteRepository {

    fun getRandomQuote(): Flow<Resource<Quote>>
    fun getRecentQuotes(): Flow<List<Quote>>
    fun getQuotes(): Pager<Int, Quote>

}