package com.example.dailyquote.ui.daily_quote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.dailyquote.R
import com.example.dailyquote.data.local.SharedPreferences
import com.example.dailyquote.data.remote.util.Resource
import com.example.dailyquote.domain.repository.QuoteRepository
import com.example.dailyquote.domain.worker.SyncDailyQuoteWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class DailyQuoteViewModel @Inject constructor(
    private val repository: QuoteRepository,
    private val workManager: WorkManager,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {

    companion object {
        const val SYNC_DAILY_QUOTE = "SYNC_DAILY_QUOTE"
        const val PERIODIC_DAILY_QUOTE_ID = "daily_quote"
    }

    private val _uiState = MutableStateFlow(
        DailyQuoteState(
            autoSync = sharedPreferences.getBoolean(SYNC_DAILY_QUOTE)
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<DailyQuoteEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {

        viewModelScope.launch {
            repository.getRecentQuotes().collect { quotes ->

                val todayQuote = quotes.firstOrNull()

                if (todayQuote == null) {
                    // no quotes are synced. So get the first quote
                    refreshDailyQuote()
                    return@collect
                }

                val pastQuotes =
                    if (quotes.size > 1) quotes.subList(1, quotes.size) else emptyList()

                _uiState.update {
                    uiState.value.copy(
                        todayQuote = todayQuote,
                        pastQuotes = pastQuotes
                    )
                }
            }
        }
    }

    fun onEvent(event: DailyQuoteEvent) {

        when (event) {
            is DailyQuoteEvent.AutoSyncDailyQuote -> {

                //Update UI state for auto sync switch
                _uiState.update { _uiState.value.copy(autoSync = event.sync) }

                //Schedule on/off periodic work request
                setPeriodicSync(event.sync)

                //Save auto sync stat to user preference
                sharedPreferences.putBoolean(SYNC_DAILY_QUOTE, event.sync)
            }

            is DailyQuoteEvent.RefreshDailyQuote -> refreshDailyQuote()
            is DailyQuoteEvent.ShowError -> {}
        }

    }


    private fun refreshDailyQuote() {

        viewModelScope.launch(Dispatchers.IO) {

            repository.getRandomQuote().collect { resource ->

                if (resource is Resource.Loading) {
                    _uiState.update { uiState.value.copy(isRefreshing = resource.isLoading) }
                } else if (resource is Resource.Error) {
                    resource.message?.let {

                        //Send error event to UI
                        _uiEvent.send(DailyQuoteEvent.ShowError(R.string.error_msg_no_network, it))

                        //Disable loading event
                        _uiState.update { uiState.value.copy(isRefreshing = false) }
                    }
                }
            }
        }
    }

    private fun setPeriodicSync(start: Boolean) {

        if (start) {

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val dailyQuoteWork =
                PeriodicWorkRequest.Builder(SyncDailyQuoteWorker::class.java, 15, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .setBackoffCriteria(BackoffPolicy.LINEAR, 5, TimeUnit.MINUTES)
                    .setInitialDelay(1, TimeUnit.MINUTES)
                    .build()

            workManager.enqueueUniquePeriodicWork(
                PERIODIC_DAILY_QUOTE_ID,
                ExistingPeriodicWorkPolicy.UPDATE,
                dailyQuoteWork
            )

        } else {
            workManager.cancelUniqueWork(PERIODIC_DAILY_QUOTE_ID)
        }

    }

}