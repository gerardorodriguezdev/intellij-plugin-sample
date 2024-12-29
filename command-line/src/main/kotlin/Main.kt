import config.DefaultConfigFileGenerator
import executor.DefaultExecutor
import logging.DefaultLogger
import java.io.File

fun main(args: Array<String>) {
    val logger = DefaultLogger()
    val configFileGenerator = DefaultConfigFileGenerator(logger = logger)
    val executor = DefaultExecutor(
        logger = logger,
        configFileGenerator = configFileGenerator,
        processBuilderProvider = { info ->
            ProcessBuilder()
                .directory(File(info.workingDirectory))
                .command("/bin/bash", "-c", info.command)
        }
    )
    val parser = Parser(executor = executor)
    parser.parse(args)
}