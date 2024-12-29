import executor.Executor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import testing.TestPaths
import testing.getCommandLineArguments
import java.io.File

class ParserTest {
    @TempDir
    lateinit var tempDir: File

    private val executor: Executor = mockk()

    @Test
    fun `given no arguments when generate code then calls executor`() {
        val testPaths = TestPaths.createTestPaths(tempDir)
        every {
            executor.generateCode(
                inputDirectory = testPaths.inputDirectory,
                outputDirectory = testPaths.outputDirectory,
                workingDirectory = testPaths.workingDirectory
            )
        } returns Result.success(Unit)
        val parser = Parser(executor)
        val arguments = testPaths.getCommandLineArguments()

        parser.parse(arguments)

        verify {
            executor.generateCode(
                inputDirectory = testPaths.inputDirectory,
                outputDirectory = testPaths.outputDirectory,
                workingDirectory = testPaths.workingDirectory
            )
        }
    }

}