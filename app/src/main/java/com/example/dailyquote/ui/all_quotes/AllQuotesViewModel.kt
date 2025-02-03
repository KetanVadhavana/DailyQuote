package com.example.dailyquote.ui.all_quotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.dailyquote.domain.repository.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllQuotesViewModel @Inject constructor(
    repository: QuoteRepository
) : ViewModel() {

    val items = repository.getQuotes().flow.cachedIn(viewModelScope)

}