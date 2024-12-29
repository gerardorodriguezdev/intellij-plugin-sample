package testing

fun getExpectedConfigFileContent(testPaths: TestPaths): String =
    with(testPaths) {
        """{"input":["$inputFile"],"output":"$outputDirectory"}""".trimIndent()
    }