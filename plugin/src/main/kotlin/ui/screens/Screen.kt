package ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ui.theme.DarkColors
import ui.theme.LightColors
import ui.theme.Theme

@Composable
fun Screen(
    state: State,
    actionDispatcher: (action: Action) -> Unit
) {
    Theme {
        val scaffoldState = rememberScaffoldState()

        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = { snackbarHostState ->
                SnackbarHost(
                    hostState = snackbarHostState,
                    snackbar = { snackbarData ->
                        Snackbar(
                            snackbarData = snackbarData,
                            backgroundColor = if (state.errorMessage != null) MaterialTheme.colors.error else SnackbarDefaults.backgroundColor,
                            contentColor = if (state.errorMessage != null) MaterialTheme.colors.onError else MaterialTheme.colors.surface,
                            modifier = Modifier.testTag(TestTags.SNACKBAR)
                        )
                    },
                )
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DirectorySelector(
                    label = "Input directory:",
                    directory = state.inputDirectory,
                    onDirectoryChanged = { inputDirectory ->
                        actionDispatcher(Action.InputDirectoryChanged(inputDirectory))
                    },
                    onChooseDirectoryClick = {
                        actionDispatcher(Action.ChooseInputDirectoryClick)
                    },
                    textFieldTag = TestTags.INPUT_DIRECTORY_SELECTOR,
                    iconTag = TestTags.INPUT_DIRECTORY_SELECTOR_ICON,
                )

                DirectorySelector(
                    label = "Output directory:",
                    directory = state.outputDirectory,
                    onDirectoryChanged = { outputDirectory ->
                        actionDispatcher(Action.OutputDirectoryChanged(outputDirectory))
                    },
                    onChooseDirectoryClick = {
                        actionDispatcher(Action.ChooseOutputDirectoryClick)
                    },
                    textFieldTag = TestTags.OUTPUT_DIRECTORY_SELECTOR,
                    iconTag = TestTags.OUTPUT_DIRECTORY_SELECTOR_ICON,
                )

                DirectorySelector(
                    label = "Working directory:",
                    directory = state.workingDirectory,
                    onDirectoryChanged = { workingDirectory ->
                        actionDispatcher(Action.WorkingDirectoryChanged(workingDirectory))
                    },
                    onChooseDirectoryClick = {
                        actionDispatcher(Action.ChooseWorkingDirectoryClick)
                    },
                    textFieldTag = TestTags.WORKING_DIRECTORY_SELECTOR,
                    iconTag = TestTags.WORKING_DIRECTORY_SELECTOR_ICON,
                )

                Button(
                    onClick = {
                        actionDispatcher(Action.GenerateCodeClick)
                    },
                    enabled = !state.isLoading,
                    modifier = Modifier.testTag(TestTags.GENERATE_CODE_BUTTON)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.animateContentSize()
                    ) {
                        Text(text = "Generate code")
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.testTag(TestTags.CIRCULAR_PROGRESS_INDICATOR)
                            )
                        }
                    }
                }
            }
        }

        LaunchedEffect(state) {
            val snackbarMessage = state.errorMessage ?: state.successMessage
            if (snackbarMessage != null) {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = snackbarMessage
                )
                actionDispatcher(Action.SnackbarShown)
            }
        }
    }
}

@Composable
private fun DirectorySelector(
    label: String,
    directory: String,
    onDirectoryChanged: (value: String) -> Unit,
    onChooseDirectoryClick: () -> Unit,
    textFieldTag: String,
    iconTag: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = directory,
            onValueChange = onDirectoryChanged,
            enabled = true,
            trailingIcon = {
                IconButton(
                    onClick = onChooseDirectoryClick,
                    modifier = Modifier.testTag(iconTag)
                ) {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = label,
                    )
                }
            },
            label = {
                Text(text = label)
            },
            modifier = Modifier.testTag(textFieldTag)
        )
    }
}

data class State(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val inputDirectory: String = "",
    val outputDirectory: String = "",
    val workingDirectory: String = "",
    val successMessage: String? = null
)

sealed interface Action {
    data class InputDirectoryChanged(val inputDirectory: String) : Action
    data class OutputDirectoryChanged(val outputDirectory: String) : Action
    data class WorkingDirectoryChanged(val workingDirectory: String) : Action
    data object ChooseInputDirectoryClick : Action
    data object ChooseOutputDirectoryClick : Action
    data object ChooseWorkingDirectoryClick : Action
    data object GenerateCodeClick : Action
    data object SnackbarShown : Action
}

object TestTags {
    const val SNACKBAR = "Snackbar"

    const val INPUT_DIRECTORY_SELECTOR = "InputDirectorySelector"
    const val INPUT_DIRECTORY_SELECTOR_ICON = "InputDirectorySelectorIcon"

    const val OUTPUT_DIRECTORY_SELECTOR = "OutputDirectorySelector"
    const val OUTPUT_DIRECTORY_SELECTOR_ICON = "OutputDirectorySelectorIcon"

    const val WORKING_DIRECTORY_SELECTOR = "WorkingDirectorySelector"
    const val WORKING_DIRECTORY_SELECTOR_ICON = "WorkingDirectorySelectorIcon"

    const val GENERATE_CODE_BUTTON = "GenerateCodeButton"
    const val CIRCULAR_PROGRESS_INDICATOR = "CircularProgressIndicator"
}

@Preview
@Composable
private fun ScreenFullPreview() {
    LightColors {
        Screen(
            state = State(
                inputDirectory = "/inputDirectory",
                outputDirectory = "/outputDirectory",
                workingDirectory = "/workingDirectory"
            ),
            actionDispatcher = {}
        )
    }
}

@Preview
@Composable
private fun ScreenLoadingPreview() {
    LightColors {
        Screen(
            state = State(
                isLoading = true
            ),
            actionDispatcher = {}
        )
    }
}

@Preview
@Composable
private fun ScreenFullDarkPreview() {
    DarkColors {
        Screen(
            state = State(
                inputDirectory = "/inputDirectory",
                outputDirectory = "/outputDirectory",
                workingDirectory = "/workingDirectory"
            ),
            actionDispatcher = {}
        )
    }
}

@Preview
@Composable
private fun ScreenLoadingDarkPreview() {
    DarkColors {
        Screen(
            state = State(
                isLoading = true
            ),
            actionDispatcher = {}
        )
    }
}