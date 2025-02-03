package com.example.dailyquote.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.dailyquote.data.local.QuoteDao
import com.example.dailyquote.data.remote.QuoteApiService
import com.example.dailyquote.data.remote.QuoteDto
import com.example.dailyquote.data.remote.util.Resource
import com.example.dailyquote.data.remote.util.toQuote
import com.example.dailyquote.data.remote.util.toQuoteEntity
import com.example.dailyquote.domain.model.Quote
import com.example.dailyquote.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException

class QuoteRepositoryImpl(
    private val quoteApi: QuoteApiService,
    private val quoteDao: QuoteDao
) : QuoteRepository {

    override fun getRandomQuote(): Flow<Resource<Quote>> {

        return flow {
            emit(Resource.Loading(true))

            val quoteDto: QuoteDto? = try {
                quoteApi.getRandomQuote()
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "Network error"))
                null
            }

            quoteDto?.let {

                //Save quote into local db
                quoteDao.insertQuote(it.toQuoteEntity())

                // emit for notification if needed
                emit(Resource.Success(it.toQuote()))
            }

            emit(Resource.Loading(false))

        }
    }

    override fun getRecentQuotes(): Flow<List<Quote>> {
        return quoteDao.getQuotes().map { r -> r.map { it.toQuote() } }
    }

    override fun getQuotes(): Pager<Int, Quote> {
        return Pager(
            config = PagingConfig(pageSize = 20, initialLoadSize = 20, prefetchDistance = 5),
            pagingSourceFactory = { QuotePagingSource(quoteApi) }
        )
    }


}