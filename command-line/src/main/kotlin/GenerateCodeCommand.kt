@file:OptIn(ExperimentalCli::class)

import executor.Executor
import kotlinx.cli.*
import utils.Constants

class GenerateCodeCommand(
    private val executor: Executor
) : Subcommand(
    name = "generate",
    actionDescription = "Generates code"
) {
    private val inputDirectory by option(
        type = ArgType.String,
        shortName = "i",
        description = "Sets input directory",
    ).required()

    private val workingDirectory by option(
        type = ArgType.String,
        shortName = "w",
        description = "Sets working directory",
    ).default(Executor.DEFAULT_WORKING_DIRECTORY)

    private val outputDirectory by option(
        type = ArgType.String,
        shortName = "o",
        description = "Sets output directory",
    ).default(Constants.DEFAULT_OUTPUT_DIRECTORY)

    override fun execute() {
        executor.generateCode(
            inputDirectory = inputDirectory,
            workingDirectory = workingDirectory,
            outputDirectory = outputDirectory
        )
    }
}