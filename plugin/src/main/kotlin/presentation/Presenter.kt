package presentation

import androidx.compose.runtime.mutableStateOf
import com.intellij.openapi.Disposable
import executor.Executor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ui.screens.Action
import ui.screens.State
import utils.DispatchersProvider
import androidx.compose.runtime.State as ImmutableState

class Presenter(
    initialOutputDirectory: String,
    initialWorkingDirectory: String,
    private val dispatchersProvider: DispatchersProvider,
    private val executor: Executor,
    private val fileChooser: () -> String?
) : Disposable {
    private val scope = CoroutineScope(dispatchersProvider.io())
    private val _state = mutableStateOf(
        State(
            outputDirectory = initialOutputDirectory,
            workingDirectory = initialWorkingDirectory
        )
    )
    val state: ImmutableState<State> = _state

    fun dispatch(action: Action) {
        when (action) {
            is Action.InputDirectoryChanged -> handleInputDirectoryChanged(action)
            is Action.OutputDirectoryChanged -> handleOutputDirectoryChanged(action)
            is Action.WorkingDirectoryChanged -> handleWorkingDirectoryChanged(action)
            is Action.ChooseInputDirectoryClick -> handleChooseInputDirectoryClick()
            is Action.ChooseOutputDirectoryClick -> handleChooseOutputDirectoryClick()
            is Action.ChooseWorkingDirectoryClick -> handleChooseWorkingDirectoryClick()
            is Action.GenerateCodeClick -> handleGenerateCode()
            is Action.SnackbarShown -> handleSnackbarShown()
        }
    }

    override fun dispose() {
        scope.cancel()
    }

    private fun handleInputDirectoryChanged(action: Action.InputDirectoryChanged) {
        _state.value = _state.value.copy(inputDirectory = action.inputDirectory)
    }

    private fun handleOutputDirectoryChanged(action: Action.OutputDirectoryChanged) {
        _state.value = _state.value.copy(outputDirectory = action.outputDirectory)
    }

    private fun handleWorkingDirectoryChanged(action: Action.WorkingDirectoryChanged) {
        _state.value = _state.value.copy(workingDirectory = action.workingDirectory)
    }

    private fun handleChooseInputDirectoryClick() {
        val directory = fileChooser()
        if (directory != null) _state.value = _state.value.copy(inputDirectory = directory)
    }

    private fun handleChooseOutputDirectoryClick() {
        val directory = fileChooser()
        if (directory != null) _state.value = _state.value.copy(outputDirectory = directory)
    }

    private fun handleChooseWorkingDirectoryClick() {
        val directory = fileChooser()
        if (directory != null) _state.value = _state.value.copy(workingDirectory = directory)
    }

    private fun handleGenerateCode() {
        if (_state.value.isLoading) return

        _state.value = _state.value.copy(isLoading = true)

        scope.launch {
            val result = executor.generateCode(
                inputDirectory = _state.value.inputDirectory,
                outputDirectory = _state.value.outputDirectory,
                workingDirectory = _state.value.workingDirectory
            )

            withContext(dispatchersProvider.main()) {
                result.fold(
                    onSuccess = {
                        handleCodeGenerated()
                    },
                    onFailure = {
                        handleCodeGenerationError()
                    }
                )
            }
        }
    }

    private fun handleCodeGenerated() {
        _state.value = _state.value.copy(
            successMessage = SUCCESS_MESSAGE,
            isLoading = false
        )
    }

    private fun handleCodeGenerationError() {
        _state.value = _state.value.copy(
            errorMessage = ERROR_MESSAGE,
            isLoading = false
        )
    }

    private fun handleSnackbarShown() {
        _state.value = _state.value.copy(
            errorMessage = null,
            successMessage = null,
            isLoading = false
        )
    }

    companion object {
        const val SUCCESS_MESSAGE = "Code generated successfully"
        const val ERROR_MESSAGE = "Error generating code"
    }
}