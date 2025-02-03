package com.example.dailyquote.ui.daily_quote

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.dailyquote.domain.model.Quote
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun DailyQuoteViewPage() {

    val viewModel = hiltViewModel<DailyQuoteViewModel>()
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.uiEvent) { event ->
        if (event is DailyQuoteEvent.ShowError) {

            event.code?.let {
                Toast.makeText(
                    context, context.resources.getString(it), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    DailyQuoteView(
        state = state,
        onEvent = viewModel::onEvent
    )


}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DailyQuoteView(
    state: DailyQuoteState,
    onEvent: (DailyQuoteEvent) -> Unit,
) {

    val context = LocalContext.current

    val permissionState = rememberPermissionState(
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    Column {

        LazyColumn {
            item {
                DailyQuoteCardView(
                    quote = state.todayQuote,
                    isRefreshing = state.isRefreshing,
                    autoSync = state.autoSync,
                    permissionState = permissionState,
                    onRefresh = { onEvent(DailyQuoteEvent.RefreshDailyQuote) },
                    onAllowPermission = {

                        if (permissionState.status.shouldShowRationale) {
                            permissionState.launchPermissionRequest()
                        } else {

                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", context.packageName, null)
                            ).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }.also {
                                context.startActivity(it)
                            }
                        }
                    },
                    onAutoRefresh = { autoSyncOn ->
                        onEvent(DailyQuoteEvent.AutoSyncDailyQuote(autoSyncOn))
                        if (autoSyncOn)
                            permissionState.launchPermissionRequest()
                    }
                )
            }

            val count = state.pastQuotes.size - 1

            itemsIndexed(items = state.pastQuotes) { index, quote ->
                PastQuoteItem(quote)

                if (index < count) {
                    HorizontalDivider(
                        color = Color.LightGray,
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(horizontal = 15.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun <T> ObserveAsEvents(
    event: Flow<T>, key1: Any? = null, key2: Any? = null, onEvent: (T) -> Unit
) {

    val lifeCycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifeCycleOwner, key1, key2) {
        lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                event.collect(onEvent)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DailyQuoteCardView(
    quote: Quote?,
    isRefreshing: Boolean,
    autoSync: Boolean,
    permissionState: PermissionState,
    onRefresh: () -> Unit,
    onAutoRefresh: (Boolean) -> Unit,
    onAllowPermission: () -> Unit,
) {

    Column(modifier = Modifier.padding(15.dp)) {
        Card(elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp)
                ) {

                    Text(
                        "Quote of the day",
                        fontSize = 30.sp,
                        fontFamily = FontFamily.Cursive,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.primary
                    )

                    if (isRefreshing) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            modifier = Modifier
                                .padding(15.dp)
                                .then(Modifier.size(18.dp))
                        )
                    } else {

                        IconButton(
                            onClick = onRefresh,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }


                if (quote != null) {

                    Text(
                        quote.quote,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        quote.author,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .align(Alignment.End),
                        color = Color.Gray
                    )

                } else {
                    Text("Your today's Quote", fontSize = 20.sp)
                }

                HorizontalDivider(
                    Modifier
                        .padding(top = 15.dp, bottom = 15.dp)
                        .height(0.1.dp),
                    color = Color.Gray
                )

                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

                    Text("Auto Sync")
                    Spacer(Modifier.weight(1f))
                    Switch(
                        checked = autoSync, onCheckedChange = onAutoRefresh
                    )

                }

                if (autoSync && !permissionState.status.isGranted) {

                    Row(
                        modifier = Modifier.padding(top = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.Info, contentDescription = null, tint = Color.Gray)
                        Text(
                            "Allow permission to receive daily quote notification",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .weight(1F)
                                .padding(horizontal = 15.dp)
                        )
                        TextButton(
                            onClick = onAllowPermission,
                        ) {
                            Text("Allow")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PastQuoteItem(quote: Quote) {

    Column(Modifier.padding(vertical = 15.dp, horizontal = 20.dp)) {
        Text(quote.quote)
        Text("~ ${quote.author}", fontSize = 13.sp, color = Color.Gray)
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Preview(showBackground = true, name = "Daily Quote Card")
@Composable
fun DailyQuoteCardViewPreview() {

    val postNotificationPermissionState = rememberPermissionState(
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    DailyQuoteCardView(quote = Quote(
        id = 0,
        author = "Martin Luther King, Jr.",
        quote = "Everything That We See Is A Shadow Cast By That Which We Do Not See."
    ),
        false,
        autoSync = true,
        permissionState = postNotificationPermissionState,
        onRefresh = {},
        onAllowPermission = {},
        onAutoRefresh = {})
}

@Preview(showBackground = true, name = "Daily Quote Page")
@Composable
fun DailyQuoteViewPagePreview() {
    DailyQuoteView(
        state = DailyQuoteState(),
        onEvent = { }
    )
}