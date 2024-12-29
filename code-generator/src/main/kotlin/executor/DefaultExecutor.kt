package executor

import config.ConfigFileGenerator
import logging.Logger
import java.io.File

class DefaultExecutor(
    private val logger: Logger,
    private val configFileGenerator: ConfigFileGenerator,
    private val processBuilderProvider: (info: ProcessBuilderProviderInfo) -> ProcessBuilder
) : Executor {

    override fun generateCode(inputDirectory: String, outputDirectory: String, workingDirectory: String): Result<Unit> {
        val workingDirectory = setupWorkingDirectory(workingDirectory)

        val configFileResult = configFileGenerator
            .generateConfigFile(
                inputDirectory = inputDirectory,
                outputDirectory = outputDirectory
            )

        val codeGenerationResult = configFileResult
            .fold(
                onSuccess = { configFile ->
                    logger.info("Generating code in $outputDirectory")

                    try {
                        val info = ProcessBuilderProviderInfo(
                            command = getCommand(configFile),
                            workingDirectory = workingDirectory.absolutePath
                        )

                        processBuilderProvider(info)
                            .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                            .redirectError(ProcessBuilder.Redirect.DISCARD)
                            .redirectErrorStream(true)
                            .start()
                            .waitForResult()
                    } catch (e: Exception) {
                        logger.error("Error generating code")
                        Result.failure(e)
                    }
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )

        return codeGenerationResult
    }

    private fun setupWorkingDirectory(workingDirectory: String): File {
        logger.info("Creating working directory $workingDirectory")

        val workingDirectory = File(workingDirectory)
        workingDirectory.mkdir()
        return workingDirectory
    }

    private fun getCommand(configFile: File): String =
        "npm init -y && npm install karakum typescript -D && npx karakum --config ${configFile.absolutePath}"

    private fun Process.waitForResult(): Result<Unit> {
        val exitCode = waitFor()

        return if (exitCode == SUCCESS_EXIT_CODE) {
            logger.info("Code generated")
            Result.success(Unit)
        } else {
            val errorMessage = "Error generating code"
            logger.error(errorMessage)
            Result.failure(RuntimeException(errorMessage))
        }
    }

    data class ProcessBuilderProviderInfo(
        val command: String,
        val workingDirectory: String
    )

    private companion object {
        const val SUCCESS_EXIT_CODE = 0
    }

}