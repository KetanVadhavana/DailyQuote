package com.example.dailyquote.data.remote.util

import com.example.dailyquote.data.local.QuoteEntity
import com.example.dailyquote.data.remote.QuoteDto
import com.example.dailyquote.domain.model.Quote

fun QuoteDto.toQuote(): Quote {
    return Quote(
        quote = quote,
        id = id,
        author = author,
    )
}

fun QuoteDto.toQuoteEntity(): QuoteEntity {
    return QuoteEntity(
        quote = quote,
        id = id,
        author = author,
        modified = System.currentTimeMillis().toString()
    )
}

fun QuoteEntity.toQuote(): Quote {
    return Quote(
        quote = quote,
        id = id,
        author = author,
    )
}
