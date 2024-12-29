package config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import testing.FakeLogger
import testing.TestPaths
import testing.getExpectedConfigFileContent
import java.io.File

class DefaultConfigFileGeneratorTest {

    @TempDir
    lateinit var tempDir: File

    private val logger = FakeLogger()
    private val defaultConfigFileGenerator = DefaultConfigFileGenerator(logger = logger)

    @Test
    fun `given valid input directory when generateConfigFile then returns success result`() {
        val testPaths = TestPaths.createTestPaths(tempDir)
        testPaths.setupDirectories()
        val expectedConfigFileContent = getExpectedConfigFileContent(testPaths)

        val result = defaultConfigFileGenerator.generateConfigFile(
            inputDirectory = testPaths.inputDirectory,
            outputDirectory = testPaths.outputDirectory
        )
        val actualConfigFileContent = result.getOrNull()?.readText()

        assertEquals(expectedConfigFileContent, actualConfigFileContent)
        assertTrue(logger.info.isNotEmpty())
        assertTrue(logger.error.isEmpty())
    }

    @Test
    fun `given invalid input directory when generateConfigFile then returns failure result`() {
        val result = defaultConfigFileGenerator.generateConfigFile(
            inputDirectory = "invalidDirectory",
            outputDirectory = "anyOutputDirectory"
        )

        assertTrue(result.isFailure)
        assertTrue(logger.info.isEmpty())
        assertTrue(logger.error.isNotEmpty())
    }
}