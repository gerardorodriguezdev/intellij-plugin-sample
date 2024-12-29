package config

import utils.Constants
import java.io.File

interface ConfigFileGenerator {
    fun generateConfigFile(
        inputDirectory: String,
        outputDirectory: String = Constants.DEFAULT_OUTPUT_DIRECTORY
    ): Result<File>
}