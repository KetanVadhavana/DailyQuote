package com.example.dailyquote.ui.daily_quote

sealed class DailyQuoteEvent {

    data object RefreshDailyQuote : DailyQuoteEvent()
    data class AutoSyncDailyQuote(val sync: Boolean) : DailyQuoteEvent()
    data class ShowError(val code: Int?, val error: String?) : DailyQuoteEvent()

}