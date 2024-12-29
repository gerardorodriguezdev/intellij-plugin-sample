import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import testing.*
import java.io.File

class MainTest {

    @TempDir
    lateinit var tempDir: File

    @Tag(TestTags.END_TO_END)
    @Test
    fun main() {
        val inputFileContent = getResourceContent("Input.ts")
        val testPaths = TestPaths.createTestPaths(tempDir)
        testPaths.setupDirectories(inputFileContent = inputFileContent)
        val expectedOutputFileContent = getResourceContent("Output.kt")
        val arguments = testPaths.getCommandLineArguments()

        main(arguments)

        val actualOutputFileContent = getFileContent(filePath = testPaths.outputDirectory)
        assertEquals(expectedOutputFileContent, actualOutputFileContent)
    }
}