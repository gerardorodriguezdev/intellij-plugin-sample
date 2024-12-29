package config

import logging.Logger
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class DefaultConfigFileGenerator(private val logger: Logger) : ConfigFileGenerator {

    override fun generateConfigFile(
        inputDirectory: String,
        outputDirectory: String
    ): Result<File> {
        val inputDirectory = File(inputDirectory)
        if (inputDirectory.doesNotExist()) {
            val errorMessage = "Invalid input directory $inputDirectory"
            logger.error(errorMessage)
            return Result.failure(IllegalStateException(errorMessage))
        }

        val outputDirectory = setupOutputDirectory(outputDirectory)

        val filesToGenerateCode = inputDirectory.getFilesToGenerateCode()

        val configFile = generateConfigFile(
            filesToGenerateCode = filesToGenerateCode,
            outputDirectory = outputDirectory
        )

        return Result.success(configFile)
    }

    private fun File.doesNotExist(): Boolean = !exists()

    private fun setupOutputDirectory(outputDirectory: String): File {
        logger.info("Preparing output directory $outputDirectory")

        val outputDirectory = File(outputDirectory)
        if (outputDirectory.exists()) {
            outputDirectory.deleteRecursively()
        }
        outputDirectory.mkdir()

        return outputDirectory
    }

    private fun File.getFilesToGenerateCode(): List<String> {
        logger.info("Analysing input directory $absolutePath")

        return walkTopDown()
            .mapNotNull { file ->
                if (file.isValidTypescriptFile()) {
                    file.absolutePath
                } else {
                    null
                }
            }
            .toList()
    }

    private fun File.isValidTypescriptFile(): Boolean =
        extension == "ts" &&
                !name.endsWith(".d.ts") &&
                !name.endsWith(".test.ts") &&
                !absolutePath.contains("node_modules")

    private fun generateConfigFile(
        filesToGenerateCode: List<String>,
        outputDirectory: File
    ): File {
        logger.info("Generating karakum config file in $outputDirectory")

        val config = Config(
            input = filesToGenerateCode,
            output = outputDirectory.absolutePath
        )

        val configJson = Json.encodeToString(config)

        val configFile = File(outputDirectory, CONFIG_FILE_NAME)
        configFile.createNewFile()
        configFile.writeText(configJson)

        return configFile
    }

    @Serializable
    private data class Config(
        val input: List<String>,
        val output: String
    )

    private companion object {
        const val CONFIG_FILE_NAME = "karakum.config.json"
    }
}