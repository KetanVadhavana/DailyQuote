package com.example.dailyquote.ui.all_quotes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.dailyquote.ui.daily_quote.PastQuoteItem

@Composable
fun AllQuotesPage() {

    val viewModel = hiltViewModel<AllQuotesViewModel>()
    val items = viewModel.items.collectAsLazyPagingItems()

    if (items.loadState.refresh is LoadState.Loading) {
        // Show centered loader only when first loading
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn {
            items(items.itemCount) { index ->
                val item = items[index] //Retrieve item using index
                item?.let {
                    PastQuoteItem(quote = it)
                    HorizontalDivider()
                }
            }

            // Show bottom loader when fetching next page
            if (items.loadState.append is LoadState.Loading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.padding(8.dp))
                    }
                }
            }

            // Show error message with retry button when appending fails
            if (items.loadState.append is LoadState.Error) {
                val error = items.loadState.append as LoadState.Error
                item {
                    ErrorMessage(
                        error.error.message ?: "Something went wrong",
                        onRetry = { items.retry() })
                }
            }
        }

        // Show error message with retry button when initial load fails
        if (items.loadState.refresh is LoadState.Error) {
            val error = items.loadState.refresh as LoadState.Error
            ErrorMessage(error.error.message ?: "Failed to load data", onRetry = { items.retry() })
        }
    }
}

@Composable
fun ErrorMessage(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = Color.Red, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}
