package presentation

import executor.Executor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import testing.FakeDispatchersProvider
import ui.screens.Action
import ui.screens.State
import kotlin.test.assertEquals

class PresenterTest {
    private val initialOutputDirectory = "InitialOutputDirectory"
    private val initialWorkingDirectory = "InitialWorkingDirectory"
    private val choosenDirectory = "choosenDirectory"
    private val executor: Executor = mockk()
    private val presenter = Presenter(
        initialOutputDirectory = initialOutputDirectory,
        initialWorkingDirectory = initialWorkingDirectory,
        dispatchersProvider = FakeDispatchersProvider(),
        executor = executor,
        fileChooser = { choosenDirectory }
    )

    @Test
    fun `when inputDirectoryChanged then updates inputDirectory`() {
        val newDirectory = "newDirectory"
        val expectedState = State(
            inputDirectory = newDirectory,
            outputDirectory = initialOutputDirectory,
            workingDirectory = initialWorkingDirectory
        )

        presenter.dispatch(Action.InputDirectoryChanged(inputDirectory = newDirectory))

        assertEquals(expectedState, presenter.state.value)
    }

    @Test
    fun `when outputDirectoryChange then updates outputDirectory`() {
        val newDirectory = "newDirectory"
        val expectedState = State(
            outputDirectory = newDirectory,
            workingDirectory = initialWorkingDirectory
        )

        presenter.dispatch(Action.OutputDirectoryChanged(outputDirectory = newDirectory))

        assertEquals(expectedState, presenter.state.value)
    }

    @Test
    fun `when workingDirectoryChange then updates workingDirectory`() {
        val newDirectory = "newDirectory"
        val expectedState = State(
            outputDirectory = initialOutputDirectory,
            workingDirectory = newDirectory,
        )

        presenter.dispatch(Action.WorkingDirectoryChanged(workingDirectory = newDirectory))

        assertEquals(expectedState, presenter.state.value)
    }

    @Test
    fun `when chooseInputDirectoryClick file then updates inputDirectory`() {
        val expectedState = State(
            inputDirectory = choosenDirectory,
            outputDirectory = initialOutputDirectory,
            workingDirectory = initialWorkingDirectory
        )

        presenter.dispatch(Action.ChooseInputDirectoryClick)

        assertEquals(expectedState, presenter.state.value)
    }

    @Test
    fun `when chooseOutputDirectoryClick file then updates outputDirectory`() {
        val expectedState = State(
            outputDirectory = choosenDirectory,
            workingDirectory = initialWorkingDirectory
        )

        presenter.dispatch(Action.ChooseOutputDirectoryClick)

        assertEquals(expectedState, presenter.state.value)
    }

    @Test
    fun `when chooseWorkingDirectoryClick file then updates workingDirectory`() {
        val expectedState = State(
            outputDirectory = initialOutputDirectory,
            workingDirectory = choosenDirectory
        )

        presenter.dispatch(Action.ChooseWorkingDirectoryClick)

        assertEquals(expectedState, presenter.state.value)
    }

    @Test
    fun `when generateCodeClick with valid files then updates success message`() {
        val inputDirectory = "newDirectory"
        val expectedState = State(
            inputDirectory = inputDirectory,
            outputDirectory = initialOutputDirectory,
            workingDirectory = initialWorkingDirectory,
            successMessage = Presenter.SUCCESS_MESSAGE
        )
        every {
            executor.generateCode(
                inputDirectory = inputDirectory,
                outputDirectory = initialOutputDirectory,
                workingDirectory = initialWorkingDirectory
            )
        } returns Result.success(Unit)

        presenter.dispatch(Action.InputDirectoryChanged(inputDirectory = inputDirectory))
        presenter.dispatch(Action.GenerateCodeClick)

        assertEquals(expectedState, presenter.state.value)
        verify {
            executor.generateCode(
                inputDirectory = inputDirectory,
                outputDirectory = initialOutputDirectory,
                workingDirectory = initialWorkingDirectory
            )
        }
    }

    @Test
    fun `when generateCodeClick with invalid files then updates error message`() {
        val inputDirectory = "newDirectory"
        val expectedState = State(
            inputDirectory = inputDirectory,
            outputDirectory = initialOutputDirectory,
            workingDirectory = initialWorkingDirectory,
            errorMessage = Presenter.ERROR_MESSAGE
        )
        every {
            executor.generateCode(
                inputDirectory = inputDirectory,
                outputDirectory = initialOutputDirectory,
                workingDirectory = initialWorkingDirectory
            )
        } returns Result.failure(IllegalStateException("Error"))

        presenter.dispatch(Action.InputDirectoryChanged(inputDirectory = inputDirectory))
        presenter.dispatch(Action.GenerateCodeClick)

        assertEquals(expectedState, presenter.state.value)
        verify {
            executor.generateCode(
                inputDirectory = inputDirectory,
                outputDirectory = initialOutputDirectory,
                workingDirectory = initialWorkingDirectory
            )
        }
    }

    @Test
    fun `when snackbarShown with then resets messages`() {
        val inputDirectory = "newDirectory"
        val expectedState = State(
            inputDirectory = inputDirectory,
            outputDirectory = initialOutputDirectory,
            workingDirectory = initialWorkingDirectory
        )
        every {
            executor.generateCode(
                inputDirectory = inputDirectory,
                outputDirectory = initialOutputDirectory,
                workingDirectory = initialWorkingDirectory
            )
        } returns Result.success(Unit)

        presenter.dispatch(Action.InputDirectoryChanged(inputDirectory = inputDirectory))
        presenter.dispatch(Action.GenerateCodeClick)
        presenter.dispatch(Action.SnackbarShown)

        assertEquals(expectedState, presenter.state.value)
    }

}