package com.example.dailyquote

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.dailyquote.ui.MainActivity
import com.example.dailyquote.ui.home.HomeTabs
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenUITest {

//    @get:Rule
//    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun clicking_tab_should_update_UI() {

//        composeTestRule.setContent {
//            HomeScreen() // Replace with your actual UI
//        }

        composeTestRule.onNodeWithTag(HomeTabs.AllQuotes.name).performClick()

        composeTestRule.onNodeWithTag(HomeTabs.AllQuotes.name).assert(isSelected())
        composeTestRule.onNodeWithTag("AppbarTitle")
            .assertTextEquals("All Quotes")

        composeTestRule.onNodeWithTag(HomeTabs.DailyQuote.name).performClick()
        composeTestRule.onNodeWithTag(HomeTabs.DailyQuote.name).assert(isSelected())
        composeTestRule.onNodeWithTag("AppbarTitle")
            .assertTextEquals("Daily Quote")

    }
}