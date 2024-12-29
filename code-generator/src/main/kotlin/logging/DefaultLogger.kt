package logging

class DefaultLogger: Logger {
    override fun info(message: String?) {
        println(message)
    }

    override fun error(message: String?) {
        System.err.println(message)
    }
}