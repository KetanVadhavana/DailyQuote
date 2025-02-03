package com.example.dailyquote.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dailyquote.ui.all_quotes.AllQuotesPage
import com.example.dailyquote.ui.daily_quote.DailyQuoteViewPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = viewModel()) {

    val selectedTab: HomeTabs by viewModel.currentTab

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Text(
                    text = stringResource(selectedTab.strResourceId),
                    Modifier.testTag("AppbarTitle")
                )
            })
        },
        bottomBar = {
            Column {
                HorizontalDivider()
                NavigationBar {
                    HomeTabs.entries.forEach { tab ->
                        NavigationBarItem(
                            icon = { Icon(tab.icon, contentDescription = tab.name) },
                            label = { Text(stringResource(tab.strResourceId)) },
                            selected = selectedTab == tab,
                            onClick = { viewModel.navigateTo(tab) },
                            alwaysShowLabel = true,
                            modifier = Modifier.testTag(tab.name)
                        )
                    }
                }
            }

        }) { innerPadding ->

        Box(modifier = Modifier.padding(innerPadding)) {

            when (selectedTab) {
                HomeTabs.DailyQuote -> DailyQuoteViewPage()
                HomeTabs.AllQuotes -> AllQuotesPage()
            }
        }
    }
}

