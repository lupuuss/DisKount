package com.github.lupuuss.diskount.android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.lupuuss.diskount.AppState
import com.github.lupuuss.diskount.android.theme.AppTheme
import com.daftmobile.redukt.test.tools.TestStore
import org.junit.Rule

@Suppress("TestFunctionName")
abstract class ComposeTest {
    @get:Rule
    val rule = createComposeRule()
    val store = TestStore(AppState())

    @Composable
    abstract fun Content()

    fun withRule(clearStoreActions: Boolean = true, block: ComposeContentTestRule.() -> Unit) {
        rule.setContent {
            AppTheme {
                CompositionLocalProvider(LocalStore provides store) {
                    Content()
                }
            }
        }
        if (clearStoreActions) store.test { clearActionsHistory() }
        rule.block()
    }
}