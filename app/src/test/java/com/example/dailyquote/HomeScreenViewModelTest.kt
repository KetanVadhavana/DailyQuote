package com.example.dailyquote

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.dailyquote.ui.home.HomeScreenViewModel
import com.example.dailyquote.ui.home.HomeTabs
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

class HomeScreenViewModelTest {

    @Test
    fun `navigateTo should update currentTab`() {
        val viewModel = HomeScreenViewModel()

        // Initial state should be DailyQuote
        assertEquals(HomeTabs.DailyQuote, viewModel.currentTab.value)

        // Navigate to AllQuotes
        viewModel.navigateTo(HomeTabs.AllQuotes)
        assertEquals(HomeTabs.AllQuotes, viewModel.currentTab.value)

        // Navigate back to DailyQuote
        viewModel.navigateTo(HomeTabs.DailyQuote)
        assertEquals(HomeTabs.DailyQuote, viewModel.currentTab.value)
    }
}
