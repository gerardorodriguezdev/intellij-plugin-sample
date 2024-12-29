package testing

import java.io.File

data class TestPaths(
    val inputDirectory: String,
    val inputFile: String,
    val outputDirectory: String,
    val workingDirectory: String
) {
    fun setupDirectories(inputFileContent: String = "") {
        val inputDirectory = File(inputDirectory)
        inputDirectory.mkdir()

        val inputFile = File(inputFile)
        inputFile.createNewFile()
        inputFile.writeText(inputFileContent)

        val workingDirectory = File(workingDirectory)
        workingDirectory.mkdir()
    }

    companion object {
        fun createTestPaths(tempDir: File): TestPaths {
            val tempDir = tempDir.absolutePath
            val inputDirectory = "$tempDir/inputDirectory"
            val inputFile = "$inputDirectory/inputFile.ts"
            val outputDirectory = "$tempDir/outputDirectory"
            val workingDirectory = "$tempDir/workingDirectory"

            return TestPaths(
                inputDirectory = inputDirectory,
                inputFile = inputFile,
                outputDirectory = outputDirectory,
                workingDirectory = workingDirectory
            )
        }
    }
}