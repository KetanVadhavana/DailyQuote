package com.example.dailyquote.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.dailyquote.data.remote.QuoteApiService
import com.example.dailyquote.data.remote.util.toQuote
import com.example.dailyquote.domain.model.Quote

class QuotePagingSource(private val apiService: QuoteApiService) : PagingSource<Int, Quote>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Quote> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize
            val response = apiService.getQuotes(page, pageSize)

            LoadResult.Page(
                data = response.quotes.map { it.toQuote() },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.quotes.isEmpty()) null else page + 1
            )


        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Quote>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}