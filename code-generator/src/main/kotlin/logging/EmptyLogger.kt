package logging

object EmptyLogger: Logger {
    override fun info(message: String?) {}
    override fun error(message: String?) {}
}