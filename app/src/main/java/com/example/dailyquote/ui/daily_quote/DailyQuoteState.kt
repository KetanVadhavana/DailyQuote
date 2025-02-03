package com.example.dailyquote.ui.daily_quote

import com.example.dailyquote.domain.model.Quote

data class DailyQuoteState(
    val isRefreshing: Boolean = false,
    val autoSync: Boolean = false,
    val todayQuote: Quote? = null,
    val pastQuotes: List<Quote> = emptyList(),
)
