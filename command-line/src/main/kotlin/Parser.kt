import executor.Executor
import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli

@OptIn(ExperimentalCli::class)
class Parser(
    private val executor: Executor,
) {

    fun parse(args: Array<String>) {
        val parser = ArgParser("ttk")
        parser.registerSubcommands()
        parser.parse(args)
    }

    private fun ArgParser.registerSubcommands() {
        val generateCodeCommand = GenerateCodeCommand(executor)
        subcommands(generateCodeCommand)
    }

}