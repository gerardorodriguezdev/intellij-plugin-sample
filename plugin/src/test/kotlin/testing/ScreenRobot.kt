package testing

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import ui.screens.Action
import ui.screens.Screen
import ui.screens.State
import ui.screens.TestTags

class ScreenRobot(composeRule: ComposeContentTestRule, state: State, actions: MutableList<Action>) {

    init {
        composeRule.setContent {
            Screen(
                state = state,
                actionDispatcher = { action ->
                    actions.add(action)
                }
            )
        }
    }

    private val snackbar = composeRule.onNodeWithTag(TestTags.SNACKBAR, useUnmergedTree = true)
    val inputDirectorySelector = DirectorySelector(
        composeRule = composeRule,
        textFieldTag = TestTags.INPUT_DIRECTORY_SELECTOR,
        iconTag = TestTags.INPUT_DIRECTORY_SELECTOR_ICON
    )
    val outputDirectorySelector = DirectorySelector(
        composeRule = composeRule,
        textFieldTag = TestTags.OUTPUT_DIRECTORY_SELECTOR,
        iconTag = TestTags.OUTPUT_DIRECTORY_SELECTOR_ICON
    )
    val workingDirectorySelector = DirectorySelector(
        composeRule = composeRule,
        textFieldTag = TestTags.WORKING_DIRECTORY_SELECTOR,
        iconTag = TestTags.WORKING_DIRECTORY_SELECTOR_ICON
    )
    val generateCodeButton = composeRule.onNodeWithTag(TestTags.GENERATE_CODE_BUTTON)
    val circularProgressIndicator = composeRule.onNodeWithTag(TestTags.CIRCULAR_PROGRESS_INDICATOR)

    fun assertSnackbarIsDisplayed() {
        snackbar.assertIsDisplayed()
    }

    fun assertSnackbarDoesNotExist() {
        snackbar.assertDoesNotExist()
    }

    fun assertSnackbarText(text: String) {
        snackbar.onChildren().filterToOne(hasText(text)).assertIsDisplayed()
    }

    class DirectorySelector(
        composeRule: ComposeContentTestRule,
        textFieldTag: String,
        iconTag: String
    ) {
        val textField = composeRule.onNodeWithTag(textFieldTag, useUnmergedTree = true)
        val icon = composeRule.onNodeWithTag(iconTag, useUnmergedTree = true)
    }
}