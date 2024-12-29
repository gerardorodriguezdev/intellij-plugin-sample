package executor

import config.DefaultConfigFileGenerator
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import testing.*
import java.io.File
import kotlin.test.assertEquals

class DefaultExecutorTest {

    @TempDir
    lateinit var tempDir: File

    private val logger = FakeLogger()
    private val configFileGenerator = DefaultConfigFileGenerator(logger = logger)
    private val executor = DefaultExecutor(
        logger = logger,
        configFileGenerator = configFileGenerator,
        processBuilderProvider = { info ->
            ProcessBuilder()
                .directory(File(info.workingDirectory))
                .command("/bin/bash", "-c", info.command)
        }
    )

    @Test
    fun `given invalid input directory returns error result`() {
        val testPaths = TestPaths.createTestPaths(tempDir)
        testPaths.setupDirectories()

        val result = executor.generateCode(
            inputDirectory = "",
            outputDirectory = testPaths.outputDirectory,
            workingDirectory = testPaths.workingDirectory
        )

        assertTrue(result.isFailure)
        assertTrue(logger.error.isNotEmpty())
    }

    @Tag(TestTags.END_TO_END)
    @Test
    fun `given valid directories generates files`() {
        val inputFileContent = getResourceContent("Input.ts")
        val testPaths = TestPaths.createTestPaths(tempDir)
        testPaths.setupDirectories(inputFileContent = inputFileContent)
        val expectedOutputFileContent = getResourceContent("Output.kt")

        val result = executor.generateCode(
            inputDirectory = testPaths.inputDirectory,
            outputDirectory = testPaths.outputDirectory,
            workingDirectory = testPaths.workingDirectory
        )
        result.fold(
            onSuccess = {},
            onFailure = {
                println(it)
            }
        )
        val actualOutputFileContent = getFileContent(filePath = testPaths.outputDirectory)

        assertEquals(expectedOutputFileContent, actualOutputFileContent)
        assertEquals(Result.success(Unit), result)
        assertTrue(logger.info.isNotEmpty())
        assertTrue(logger.error.isEmpty())
    }
}