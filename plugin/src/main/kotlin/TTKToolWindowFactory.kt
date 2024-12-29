import androidx.compose.runtime.SideEffect
import androidx.compose.ui.awt.ComposePanel
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import config.DefaultConfigFileGenerator
import executor.DefaultExecutor
import executor.Executor
import logging.EmptyLogger
import presentation.Presenter
import ui.screens.Screen
import ui.theme.TTKColors
import utils.Constants
import utils.JvmDispatchersProvider

class TTKToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val presenter =
            Presenter(
                dispatchersProvider = JvmDispatchersProvider(),
                executor = DefaultExecutor(
                    logger = EmptyLogger,
                    configFileGenerator = DefaultConfigFileGenerator(
                        logger = EmptyLogger
                    ),
                    processBuilderProvider = { info ->
                        val generalCommandLine = GeneralCommandLine("/bin/bash", "-c", info.command)
                        generalCommandLine.setWorkDirectory(info.workingDirectory)
                        generalCommandLine.toProcessBuilder()
                    }
                ),
                initialOutputDirectory = "${project.basePath}/${Constants.DEFAULT_OUTPUT_DIRECTORY}",
                initialWorkingDirectory = "${project.basePath}/${Executor.DEFAULT_WORKING_DIRECTORY}",
                fileChooser = { selectFileDirectory(project) }
            )

        val component = ComposePanel().apply {
            setBounds(0, 0, 800, 600)
            setContent {
                TTKColors {
                    Screen(
                        state = presenter.state.value,
                        actionDispatcher = presenter::dispatch
                    )
                }

                if (presenter.state.value.successMessage != null) {
                    SideEffect {
                        reloadFiles()
                    }
                }
            }
        }

        val content = ContentFactory.getInstance().createContent(
            component,
            null,
            false
        )

        Disposer.register(toolWindow.contentManager, presenter)

        toolWindow.contentManager.addContent(content)
    }

    private fun selectFileDirectory(project: Project): String? {
        val fileDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor()
        val selectedDirectory = FileChooser.chooseFile(
            fileDescriptor,
            project,
            null
        )
        return selectedDirectory?.path
    }

    private fun reloadFiles() {
        LocalFileSystem.getInstance().refreshWithoutFileWatcher(true)
    }
}