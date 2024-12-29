package testing

import java.io.File

fun getFileContent(filePath: String): String = File(filePath).listFiles()?.first()?.readText() ?: ""

fun Any.getResourceContent(resourcePath: String): String =
    javaClass.classLoader.getResource(resourcePath)?.readText() ?: ""

fun TestPaths.getCommandLineArguments(): Array<String> = arrayOf(
    "generate",
    "-i",
    inputDirectory,
    "-o",
    outputDirectory,
    "-w",
    workingDirectory
)