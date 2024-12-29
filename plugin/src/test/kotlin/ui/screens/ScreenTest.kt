package ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import testing.ScreenRobot
import kotlin.test.assertEquals

class ScreenTest {
    @get:Rule
    val composeRule = createComposeRule()
    private val actions = mutableListOf<Action>()

    @Test
    fun `given initial state then renders empty`() {
        onScreen(state = empty) {
            inputDirectorySelector.textField.assertIsDisplayed()
            inputDirectorySelector.icon.assertIsDisplayed()

            outputDirectorySelector.textField.assertIsDisplayed()
            outputDirectorySelector.icon.assertIsDisplayed()

            workingDirectorySelector.textField.assertIsDisplayed()
            workingDirectorySelector.icon.assertIsDisplayed()

            generateCodeButton.assertIsDisplayed()
            circularProgressIndicator.assertDoesNotExist()

            assertSnackbarDoesNotExist()
        }
    }

    @Test
    fun `given loading state then renders loading`() {
        onScreen(state = loading) {
            generateCodeButton.assertIsDisplayed()
        }
    }

    @Test
    fun `given input and output and working directories present then renders full`() {
        onScreen(state = full) {
            inputDirectorySelector.textField.assertIsDisplayed()
            inputDirectorySelector.textField.assertTextEquals(INPUT_DIRECTORY)
            inputDirectorySelector.icon.assertIsDisplayed()

            outputDirectorySelector.textField.assertIsDisplayed()
            outputDirectorySelector.textField.assertTextEquals(OUTPUT_DIRECTORY)
            outputDirectorySelector.icon.assertIsDisplayed()

            workingDirectorySelector.textField.assertIsDisplayed()
            workingDirectorySelector.textField.assertTextEquals(WORKING_DIRECTORY)
            workingDirectorySelector.icon.assertIsDisplayed()
        }
    }

    @Test
    fun `given success message present then renders success message`() {
        composeRule.mainClock.autoAdvance = false

        onScreen(state = success) {
            assertSnackbarIsDisplayed()
            assertSnackbarText(SUCCESS_MESSAGE)
        }
        composeRule.mainClock.advanceTimeBy(SNACKBAR_DURATION)

        assertEquals(Action.SnackbarShown, actions.last())
    }

    @Test
    fun `given error message present then renders error message`() {
        composeRule.mainClock.autoAdvance = false

        onScreen(state = error) {
            assertSnackbarIsDisplayed()
            assertSnackbarText(ERROR_MESSAGE)
        }
        composeRule.mainClock.advanceTimeBy(SNACKBAR_DURATION)

        assertEquals(Action.SnackbarShown, actions.last())
    }

    @Test
    fun `given input and output and working directories present when selecting directories then choose directories click action dispatched`() {
        onScreen(state = empty) {
            inputDirectorySelector.icon.performClick()
            outputDirectorySelector.icon.performClick()
            workingDirectorySelector.icon.performClick()

            assertEquals(
                listOf(
                    Action.ChooseInputDirectoryClick,
                    Action.ChooseOutputDirectoryClick,
                    Action.ChooseWorkingDirectoryClick
                ),
                actions
            )
        }
    }

    @Test
    fun `given input and output and working directories present when clicking generate code then generate code action dispatched`() {
        onScreen(state = empty) {
            generateCodeButton.performClick()
            assertEquals(Action.GenerateCodeClick, actions.last())
        }
    }

    private fun onScreen(state: State, block: ScreenRobot.() -> Unit) {
        ScreenRobot(
            composeRule = composeRule,
            state = state,
            actions = actions
        ).block()
    }

    private companion object {
        const val SUCCESS_MESSAGE = "SuccessMessage"
        const val ERROR_MESSAGE = "ErrorMessage"
        const val INPUT_DIRECTORY = "InputDirectory"
        const val OUTPUT_DIRECTORY = "OutputDirectory"
        const val WORKING_DIRECTORY = "WorkingDirectory"
        const val SNACKBAR_DURATION = 4_000L

        val empty = State()

        val loading = State(
            isLoading = true
        )

        val full = State(
            inputDirectory = INPUT_DIRECTORY,
            outputDirectory = OUTPUT_DIRECTORY,
            workingDirectory = WORKING_DIRECTORY
        )

        val success = State(
            successMessage = SUCCESS_MESSAGE
        )

        val error = State(
            errorMessage = ERROR_MESSAGE
        )
    }

}