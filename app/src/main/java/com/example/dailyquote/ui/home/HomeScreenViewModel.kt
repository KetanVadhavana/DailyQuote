package com.example.dailyquote.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.example.dailyquote.R

class HomeScreenViewModel : ViewModel() {

    private val _currentTab = mutableStateOf(HomeTabs.DailyQuote)
    val currentTab = _currentTab

    fun navigateTo(tab: HomeTabs) {
        _currentTab.value = tab
    }

}

enum class HomeTabs(val strResourceId: Int, val icon: ImageVector) {

    DailyQuote(
        strResourceId = R.string.home_tab_daily_quote,
        icon = Icons.Filled.Home
    ),

    AllQuotes(
        strResourceId = R.string.home_tab_all_quotes,
        icon = Icons.AutoMirrored.Filled.List
    )

}