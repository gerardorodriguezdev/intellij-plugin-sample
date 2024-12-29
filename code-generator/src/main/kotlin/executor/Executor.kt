package executor

interface Executor {
    fun generateCode(
        inputDirectory: String,
        outputDirectory: String,
        workingDirectory: String = DEFAULT_WORKING_DIRECTORY
    ): Result<Unit>

    companion object {
        const val DEFAULT_WORKING_DIRECTORY = "TTKWorkingDirectory"
    }
}