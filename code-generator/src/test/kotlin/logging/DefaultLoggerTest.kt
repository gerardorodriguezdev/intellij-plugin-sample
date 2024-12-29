package logging

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

class DefaultLoggerTest {
    private val originalOutputStream = System.out
    private val originalErrorStream = System.err

    private val outputStream = ByteArrayOutputStream()
    private val errorStream = ByteArrayOutputStream()

    private val logger = DefaultLogger()

    @BeforeEach
    fun setup() {
        System.setOut(PrintStream(outputStream))
        System.setErr(PrintStream(errorStream))
    }

    @AfterEach
    fun tearDown() {
        System.setOut(originalOutputStream)
        System.setErr(originalErrorStream)
    }

    @Test
    fun info() {
        logger.info("Info")

        assertEquals("Info\n", outputStream.toString())
    }

    @Test
    fun error() {
        logger.error("Error")

        assertEquals("Error\n", errorStream.toString())
    }
}